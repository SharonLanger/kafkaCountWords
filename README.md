# kafkaCountWords
bigID count names in a file with two topics and multiple consumers


### Run zookeeper:
	docker run --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 -p 2181:2181 -p 2888:2888 -p 3888:3888 confluentinc/cp-zookeeper:latest

### Fetch the zookeeper's container IP
	Zookeeper_Server_IP=$(docker inspect zookeeper --format='{{ .NetworkSettings.IPAddress }}')
	
### Start the Kafka server
	docker run --name=kafka -e KAFKA_ZOOKEEPER_CONNECT=${Zookeeper_Server_IP}:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -p 9092:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1  confluentinc/cp-kafka:latest

##### if you ever mess up or you want to start fresh just stop and remove the containers and rerun the command 
	docker stop zookeeper kafka 
	docker rm zookeeper kafka 
