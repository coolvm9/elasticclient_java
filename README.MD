#
# Elastic Kibana Docker Images

# clean up all the packages
pip freeze > to_delete.txt
pip uninstall -y -r to_delete.txt
## commands
Create network

% docker network create elastic

## Version 8.9.0
docker run \
    --name elasticsearch \
    --net elastic \
    -p 9200:9200 \
    -e discovery.type=single-node \
    -e ES_JAVA_OPTS="-Xms4g -Xmx4g"\
    -e xpack.security.enabled=false \
    -it \
    docker.elastic.co/elasticsearch/elasticsearch:8.9.0

docker run \
    --name kibana \
    --net elastic \
    -p 5601:5601 \
    docker.elastic.co/kibana/kibana:8.9.0

## Version 8.10.2
docker run \
    --name elasticsearch \
    --net elastic \
    -p 9200:9200 \
    -e discovery.type=single-node \
    -e ES_JAVA_OPTS="-Xms4g -Xmx4g"\
    -e xpack.security.enabled=false \
    -it \
    docker.elastic.co/elasticsearch/elasticsearch:8.10.2
docker run \
    --name kibana \
    --net elastic \
    -p 5601:5601 \
    docker.elastic.co/kibana/kibana:8.10.2

## Version 8.11.0
docker run \
    --name elasticsearch \
    --net elastic \
    -p 9200:9200 \
    -e discovery.type=single-node \
    -e ES_JAVA_OPTS="-Xms4g -Xmx4g"\
    -e xpack.security.enabled=false \
    -it \
    docker.elastic.co/elasticsearch/elasticsearch:8.11.0
docker run \
    --name kibana \
    --net elastic \
    -p 5601:5601 \
    docker.elastic.co/kibana/kibana:8.11.0


## Version 8.12.2
docker run \
    --name elasticsearch \
    --net elastic \
    -p 9200:9200 \
    -e discovery.type=single-node \
    -e ES_JAVA_OPTS="-Xms4g -Xmx4g"\
    -e xpack.security.enabled=false \
    -it \
    docker.elastic.co/elasticsearch/elasticsearch:8.12.2
docker run \
    --name kibana \
    --net elastic \
    -p 5601:5601 \
    docker.elastic.co/kibana/kibana:8.12.2


## Version 8.12.2
docker run \
    --name elasticsearch \
    --net elastic \
    -p 9200:9200 \
    -e "discovery.type=single-node" \
    -e "ES_JAVA_OPTS=-Xms4g -Xmx4g" \
    -e "xpack.ml.enabled=true" \
    -e "node.roles=master,data,ml" \
    -e "xpack.security.enabled=false" \
    -it \
    docker.elastic.co/elasticsearch/elasticsearch:8.15.2




docker run \
    --name kibana \
    --net elastic \
    -p 5601:5601 \
    docker.elastic.co/kibana/kibana:8.15.2

docker run \
    --name elasticsearch \
    --net elastic \
    -p 9200:9200 \
    -e discovery.type=single-node \
    -e ES_JAVA_OPTS="-Xms4g -Xmx4g" \
    -e xpack.ml.enabled=true \   # Enable ML here
    -e node.roles=ml \           # Set this node to ML role
    -e xpack.security.enabled=false \
    -it \
    docker.elastic.co/elasticsearch/elasticsearch:8.15.2

localhost:9200
http://localhost:5601/app/dev_tools#/console

## Deploy eland
# create a new env in a directory
mkdir eland
cd eland
python3 -m venv eland
source eland/bin/activate  
pip3 install --upgrade pip
python -m pip install 'eland[pytorch]'
python -m pip install eland
https://www.elastic.co/guide/en/machine-learning/current/ml-nlp-deploy-model.html
https://www.elastic.co/guide/en/machine-learning/current/ml-nlp-ner-example.html
eland_import_hub_model \
    --url http://localhost:9200/ \
    --hub-model-id sentence-transformers/msmarco-MiniLM-L-12-v3 \
    --task-type text_embedding \
    --start
eland_import_hub_model \
    --url http://localhost:9200/ \
    --hub-model-id elastic/distilbert-base-uncased-finetuned-conll03-english \
    --task-type ner
eland_import_hub_model \
    --url http://localhost:9200/ \
    --hub-model-id deepset/minilm-uncased-squad2 \
    --task-type question_answering

 





# create a new env in a directory
mkdir eland
cd eland
python -m venv eland
source eland/bin/activate  
pip3 install --upgrade pip
python -m pip install 'eland[pytorch]'
python -m pip install eland
https://www.elastic.co/guide/en/machine-learning/current/ml-nlp-deploy-model.html
https://www.elastic.co/guide/en/machine-learning/current/ml-nlp-ner-example.html
eland_import_hub_model \
--url http://localhost:9200/ \
--hub-model-id sentence-transformers/msmarco-MiniLM-L-12-v3 \
--task-type text_embedding \
--start

https://www.elastic.co/guide/en/machine-learning/current/ml-nlp-ner-example.html
eland_import_hub_model \
--url http://localhost:9200/ \
--hub-model-id elastic/distilbert-base-uncased-finetuned-conll03-english \
--task-type ner \
--start

eland_import_hub_model \
--url http://localhost:9200/ \
--hub-model-id dslim/bert-base-NER \
--task-type ner \
--start --insecure

https://www.elastic.co/blog/implement-image-similarity-search-elastic
eland_import_hub_model
--url http://localhost:9200/ \
--hub-model-id sentence-transformers/clip-ViT-B-32-multilingual-v1 \
--task-type text_embedding \
--start


eland_import_hub_model \
--url https://<user>:<password>@localhost:9200/ \
--hub-model-id sentence-transformers/msmarco-MiniLM-L-12-v3 \
--task-type text_embedding \
--start

docker run -it --rm --network elastic elastic/eland.

eland_import_hub_model \
--url https://<user>:<password>@localhost:9200/ \
--hub-model-id sentence-transformers/msmarco-MiniLM-L-12-v3 \
--task-type text_embedding \
--start

eland_import_hub_model \
--url http://172.18.0.2:9200/ \
--hub-model-id elastic/distilbert-base-cased-finetuned-conll03-english \
--task-type ner \
--start


eland_import_hub_model \
--url http://172.18.0.2:9200/ \
-u elastic -p password \
--clear-previous \
--hub-model-id sentence-transformers/msmarco-MiniLM-L-12-v3 \
--task-type text_embedding \
--start

eland_import_hub_model \
--url http://172.18.0.2:9200/ \
--clear-previous \
--hub-model-id sentence-transformers/msmarco-MiniLM-L-12-v3 \
--task-type text_embedding \
--start



eland_import_hub_model \
--url http://172.18.0.2:9200/ -u elastic -p password \
--hub-model-id sentence-transformers/msmarco-MiniLM-L-12-v3 \
--task-type text_embedding \
--start


eland_import_hub_model \
--url https://<user>:<password>@localhost:9200/ \
--hub-model-id sentence-transformers/msmarco-MiniLM-L-12-v3 \
--task-type text_embedding \
--start
POST _ml/trained_models/sentence-transformers__msmarco-minilm-l-12-v3/deployment/_start

https://www.elastic.co/blog/how-to-deploy-nlp-sentiment-analysis-example


eland_import_hub_model \
--url http://172.18.0.2:9200/ \
--hub-model-id distilbert-base-uncased-finetuned-sst-2-english \
--task-type text_classification \
--start
### Notes

### References
https://levelup.gitconnected.com/how-to-run-elasticsearch-8-on-docker-for-local-development-401fd3fff829
https://mohaamer5.medium.com/logstash-for-synchronize-elasticsearch-with-dbs-e5dda7cea930

Since the --start option is used at the end of the Eland import command, Elasticsearch deploys the model ready to use. If you have multiple models and want to select which model to deploy, you can use the Machine Learning > Model Management user interface in Kibana to manage the starting and stopping of models.

Go to the Machine Learning > Trained Models page and synchronize your trained models. A warning message is displayed at the top of the page that says "ML job and trained model synchronization required". Follow the link to "Synchronize your jobs and trained models." Then click Synchronize. You can also wait for the automatic synchronization that occurs in every hour, or use the sync machine learning objects API.

