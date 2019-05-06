#!/bin/bash

KAFKA_HOME="/home/kafka/kafka"

function show_help {
cclienkafkaconsumerEOF
cat << EOF
    -b  build all the modules
    -r  run all the modules
    -s  to stop all modules
    -h  this help text

if you have install kafka in a different location than $KAFKA_HOME please pass an env variable before this script eg. KAFKA_HOME=/opt/kafka ./build.sh -r
EOF
}

build=0
run=0
stopApps=0
while true; do
    case "$1" in
        -h)
            show_help
            exit 0
            ;;
         -b)
            build=1
            shift
            ;;
         -r)
            run=1
            shift
            ;;
         -s)
			stopApps=1
			shift
			;;
         *)
            shift
            break
            ;;
     esac
done

echo "build=$build run=$run stopApps=$stopApps KAFKA_HOME=$KAFKA_HOME"

if [ "$build" -eq "1" ]; then
    mvn clean install && \
    cp thrift-server/target/thrift-server-1.0-SNAPSHOT-jar-with-dependencies.jar bin/ && \
    cp thrift-client/target/thrift-client-1.0-SNAPSHOT-jar-with-dependencies.jar bin/ && \
    cp kafka-consumer/target/kafka-consumer-1.0-SNAPSHOT-jar-with-dependencies.jar bin/ && \
    cp common/target/common-1.0-SNAPSHOT.jar bin/
fi

if [ "$run" -eq "1" ]; then
	touch ~/generate_random_logs.tmp
	$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic LogEventsTopic
	cqlsh -f common/src/main/resources/schema.cql
	echo "Sleeping for 5 seconds" && sleep 5
    java -classpath bin/thrift-server-1.0-SNAPSHOT-jar-with-dependencies.jar com.interview.thriftserver.ThriftServerApp &> logs/thrift-server.log &
    echo "$!" > thriftserver.pid
    echo 'Sleeping for 5 seconds' && sleep 5
    java -classpath bin/kafka-consumer-1.0-SNAPSHOT-jar-with-dependencies.jar com.interview.kafkaconsumer.KafkaConsumerApp &> logs/kafka-consumer.log &
    echo "$!" > kafkaconsumer.pid
    echo 'Sleeping for 5 seconds' && sleep 5
    java -classpath bin/thrift-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.interview.thriftclient.ThriftClientApp &> logs/thrift-client.log &
    echo "$!" > thriftclient.pid
fi

if [ "$stopApps" -eq "1" ]; then
	kill $(cat thriftserver.pid) && echo "thriftserver stopped with (pid: $(cat thriftserver.pid))"
	kill $(cat thriftclient.pid) && echo "thriftclient stopped with (pid: $(cat thriftclient.pid))"
	kill $(cat kafkaconsumer.pid) && echo "kafkaconsumer stopped with (pid: $(cat kafkaconsumer.pid))"
	rm ~/generate_random_logs.tmp
fi
