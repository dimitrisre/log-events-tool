namespace java com.interview.common.model.logevent
enum OperationType {
    INSERT,
    DELETE
}

struct MyLogEvent {
1: required i16 v = 1
2: required i64 time
3: required string m
}

struct Operation {
1: required OperationType operationType
2: required MyLogEvent payload
}