export SRC_DIR=$PWD/src/main/proto
//export DST_DIR=$PWD/src/main/java/protobuf/bean
export DST_DIR=$SRC_DIR
protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/SearchRequest.proto