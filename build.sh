#!/bin/bash

function show_help {
cat << EOF
    -b  build all the modules
    -r  run all the modules
    -h  this help text
EOF
}

build=0
run=0
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
         *)
            shift
            break
            ;;
     esac
done

echo "build=$build run=$run"
if [ "$build" -eq "1" ]; then
    mvn clean install && \
    cp thrift-server/target/thrift-server-1.0-SNAPSHOT-jar-with-dependencies.jar bin/ && \
    cp thrift-client/target/thrift-client-1.0-SNAPSHOT-jar-with-dependencies.jar bin/ && \
    cp kafka-consumer/target/kafka-consumer-1.0-SNAPSHOT-jar-with-dependencies.jar bin/ && \
    cp common/target/common-1.0-SNAPSHOT.jar bin/
fi

if [ "$run" -eq "1" ]; then
    scala -classpath bin/thrift-server-1.0-SNAPSHOT-jar-with-dependencies.jar com.interview.thriftserver.ThriftServerApp &> logs/thrift-server.log &
    echo 'Sleeping for 5 seconds' && sleep 5
    scala -classpath bin/kafka-consumer-1.0-SNAPSHOT-jar-with-dependencies.jar com.interview.kafkaconsumer.KafkaConsumerApp &> logs/kafka-consumer.log &
    echo 'Sleeping for 5 seconds' && sleep 5
    scala -classpath bin/thrift-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.interview.thriftclient.ThriftClientApp &> logs/thrift-client.log &
fi
