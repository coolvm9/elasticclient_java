package sample.elasticclient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataServiceImpl implements DataService {

    private final String index;
    private final ElasticsearchClient client;

    public DataServiceImpl(String index, ElasticsearchClient client) {
        this.index = index;
        this.client = client;
    }

    @Override
    public DataElement findById(String id) throws IOException {
        final GetResponse<DataElement> getResponse = client.get(builder -> builder.index(index).id(id), DataElement.class);
        DataElement DataElement = getResponse.source();
        DataElement.setId(id);
        return DataElement;
    }

    @Override
    public DataPage<DataElement> search(String input) throws IOException {
        return createPage(createSearchRequest(input, 0, 10), input);
    }

    @Override
    public DataPage<DataElement> next(DataPage page) throws IOException {
        int from = page.getFrom() + page.getSize();
        final SearchRequest request = createSearchRequest(page.getInput(), from, page.getSize());
        return createPage(request, page.getInput());
    }

    private DataPage<DataElement> createPage(SearchRequest searchRequest, String input) throws IOException {
        final SearchResponse<DataElement> response = client.search(searchRequest, DataElement.class);
        if (response.hits().total().value() == 0) {
            return DataPage.EMPTY;
        }
        if (response.hits().hits().isEmpty()) {
            return DataPage.EMPTY;
        }

        response.hits().hits().forEach(hit -> hit.source().setId(hit.id()));
        final List<DataElement> DataElements = response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        return new DataPage(DataElements, input, searchRequest.from(), searchRequest.size());
    }

    private SearchRequest createSearchRequest(String input, int from, int size) {
        return new SearchRequest.Builder()
                .from(from)
                .size(size)
                .query(qb -> qb.multiMatch(mmqb -> mmqb.query(input).fields("name", "description")))
                .build();
    }

    @Override
    public void save(DataElement DataElement) throws IOException {
        save(Collections.singletonList(DataElement));
    }

    public void save(List<DataElement> DataElements) throws IOException {
        final BulkResponse response = client.bulk(builder -> {
            for (DataElement DataElement : DataElements) {
                builder.index(index)
                        .operations(ob -> {
                            if (DataElement.getId() != null) {
                                ob.index(ib -> ib.document(DataElement).id(DataElement.getId()));
                            } else {
                                ob.index(ib -> ib.document(DataElement));
                            }
                            return ob;
                        });
            }
            return builder;
        });

        final int size = DataElements.size();
        for (int i = 0; i < size; i++) {
            DataElements.get(i).setId(response.items().get(i).id());
        }
    }
}