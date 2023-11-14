package sample.elasticclient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    static Logger logger = Logger.getLogger(Main.class);
    static String passageUniqueData = "/Users/satyaanumolu/POCs/ElasticClient/src/main/resources/msmarco-passagetest2019-unique.tsv";
    static String passageTop1000Data = "/Users/satyaanumolu/POCs/ElasticClient/src/main/resources/msmarco-passagetest2019-top1000.tsv";
    static String reIndexCollectionsEmbeddingsJson = "/Users/satyaanumolu/POCs/ElasticClient/src/main/resources/reIndexCollectionsEmbeddings.json";
    static String reIndexTestDataEmbeddingsJson = "/Users/satyaanumolu/POCs/ElasticClient/src/main/resources/reIndexTestDataEmbeddings.json";
    static String reIndexPostUrl = "_reindex?wait_for_completion=false";
    static String reIndexPostBody = "";
    static String es_server = "http://localhost:9200";
    static String collectionIndex = "collection";
    static String testDataIndex = "test-data";


    static RestClient restClient;
    static ElasticsearchTransport transport;
    static ElasticsearchClient client;


    public static void main(String[] args) throws IOException {
        // load csv file
        try {
            initESConnection();
//            dropCollection();
            reIndexPostBody = new String(Files.readAllBytes(Paths.get(reIndexTestDataEmbeddingsJson)));
            // Test Data
//            int statusCode = loadDataToIndexAndInfer(passageTop1000Data, testDataIndex ,reIndexPostUrl, reIndexPostBody, true);
            // Collections Data
//            reIndexPostBody = new String(Files.readAllBytes(Paths.get(reIndexCollectionsEmbeddingsJson)));
            int statusCode = loadDataToIndexAndInfer(passageUniqueData, collectionIndex,reIndexPostUrl, reIndexPostBody, true);
            logger.info(statusCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            logger.info("DONE");
            try {
                closeESConnection();
            } catch (Exception e) {
            }
        }
    }

    private static int dropIndex(String indexName) throws IOException {
        int statusCode = 0;
        try {
            statusCode = requestByGetPost("DELETE", indexName);
        } catch (Exception e) {
        }
        if (statusCode == 200)
            logger.info("Collection Dropped");
        return statusCode;
    }

    private static int loadDataToIndexAndInfer(String fileName, String indexName,  String inferPostURL, String inferPostBody, boolean infer) throws IOException {
        int statusCode = 0;
        List<DataElement> list = loadDataToIndex(fileName, '\t');
        loadDataToIndex(list, indexName);
        if (infer)
            statusCode = requestByGetPost("POST", inferPostURL, inferPostBody);
        return statusCode;
    }

    private static int requestByGetPost(String... args) throws IOException {
        Request request;
        Response response;
        int statusCode = 0;

        request = new Request(
                args[0],
                args[1]);
        if (args.length > 2 && !args[2].isBlank()) {
            request.setEntity(new NStringEntity(
                    "{\"json\":\"text\"}",
                    ContentType.APPLICATION_JSON));
            request.setJsonEntity(args[2]);
        }
        response = restClient.performRequest(request);
        statusCode = response.getStatusLine().getStatusCode();
        return statusCode;


    }

    private static void initESConnection() throws IOException {
        restClient = RestClient
                .builder(HttpHost.create(es_server))
                .build();
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new ElasticsearchClient(transport);
    }

    private static void closeESConnection() throws IOException {
        restClient.close();
        transport.close();
    }

    private static void loadDataToIndex(List<DataElement> list, String indexName) throws IOException {

        BulkRequest.Builder br = new BulkRequest.Builder();
        for (DataElement data : list) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(data.getId())
                            .document(data)
                    )
            );
        }
        BulkResponse result = client.bulk(br.build());
        for (BulkResponseItem item : result.items()) {
            logger.info(item.id() + " Operation Type " + item.operationType());
            if (item.error() != null) {
                logger.error(item.error().reason());
            }
        }
        if (result.errors()) {
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    logger.error(item.error().reason());
                }
            }
        }


    }

    private static void printList(List<DataElement> list) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (DataElement data : list) {
            logger.info(mapper.writeValueAsString(data));
        }
    }

    public static List<DataElement> loadDataToIndex(String filePath, char sep) throws IOException {
        Reader reader = new BufferedReader(new FileReader(filePath));
        CsvToBean<DataElement> csvToBean = new CsvToBeanBuilder<DataElement>(reader)
                .withType(DataElement.class)
                .withSeparator(sep)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();
        return csvToBean.parse();
    }


}