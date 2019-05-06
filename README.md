----------TOOLS USED----------
kafka 2.2.0 for Scala 2.11--> https://www.apache.org/dyn/closer.cgi?path=/kafka/2.2.0/kafka_2.11-2.2.0.tgz
Cassandra 3.11.4 --> http://cassandra.apache.org/download/
cqlsh 5.0.1
maven 3.6.0 --> https://maven.apache.org/install.html
java 1.8.0_212 - java-8-openjdk-amd64
Thrift version 0.9.1  --> https://thrift.apache.org/docs/install/
Scala 2.11.8


-----------INSTRUCTIONS---------
To install apache kafka follow these instructions : https://www.digitalocean.com/community/tutorials/how-to-install-apache-kafka-on-ubuntu-18-04
To install apache Cassandra follow the first section of instructions for Debian systems: http://cassandra.apache.org/download/
To install maven3 follow these instructions https://maven.apache.org/install.html
To install thrift follow these instructions https://thrift.apache.org/docs/install/ (In a Debian environment just sudo apt install thrift-compiler)

PROS: 1. you have to have a running instance of cassandra database
      2. you have to have running instances of zookeeper and kafka 

1. Clone the git repository from https://github.com/dimitrisre/log-events-tool.git
2. Change directory to log-events-tool
3. run build.sh -b to build the project
4. run build.sh -r to run the project
5. run build.sh -s to stop modules 

You can check the logs inside the logs directory with:
tail -f kafka-consumer.log 
tail -f thrift-server.log
tail -f thrift-client.log

To generate more log messages you can run 
touch generate_random_logs.tmp for bash 
while the project modules are runnning

-----TIME SPENT-----
Around 12 Hours of pure coding