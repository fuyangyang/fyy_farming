package com.chris.dnn.forward;

message RLNetwork {
  repeated RLLayer layers = 1;
}

message RLLayer {
  optional bytes layer_name = 1;
  optional uint32 layer_index = 2;
  optional bytes layer_type = 3;
  optional uint32 input_cnt = 4;
  optional uint32 output_cnt = 5;
  repeated RLWeight weights = 6;
}

message RLWeight {
  repeated float data = 1;
  optional float bias = 2;
}