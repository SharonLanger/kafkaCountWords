# kafkaCountWords
Count names(words) in a file asynchronously with two topics and multiple consumers

### How to run:
- Run the app and trigger via REST API www.localhost:8080/main/count
- Run the method via the main test "KafkaCountWordsApplicationTests.mainRunnerTest"

#### Note: You need to define the kafka properties before you run:
- words: List of names
- input.file: file name
- reader.buffer: Number of rows in every msg
- topic.reader.partitions: Number of parallel 'Matcher's 
 
 
#### Run zookeeper:
    docker run --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 -p 2181:2181 -p 2888:2888 -p 3888:3888 confluentinc/cp-zookeeper:latest

#### Fetch the zookeeper's container IP:
	Zookeeper_Server_IP=$(docker inspect zookeeper --format='{{ .NetworkSettings.IPAddress }}')
	
#### Start the Kafka server:
	docker run --name=kafka -e KAFKA_ZOOKEEPER_CONNECT=${Zookeeper_Server_IP}:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -p 9092:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1  confluentinc/cp-kafka:latest

###### Stop and remove the containers before rerunning the commands to set up kafka: 
	docker stop zookeeper kafka 
	docker rm zookeeper kafka 
