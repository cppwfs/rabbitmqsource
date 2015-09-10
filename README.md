# RabbitMQSource

Allows a user to push a specified # of 100 byte messages to a local rabbitmq instance. 

## Usage

java -jar rabbitmqsource-0.1.0.jar --batchSize=100 --messageCount=1000000

* BatchSize the number of messages to include in a batch to be dispatched to the rabbitmq.  Default is 10.
* messageCount the number of messages to dispatch to the rabbitmq. Default is 1000000

## Build

mvn clean package
