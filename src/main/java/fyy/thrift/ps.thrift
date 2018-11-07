#!/usr/local/bin/thrift --gen py --gen cpp -r -o .

namespace cpp mlx.thrift
namespace py mlx.thrift
namespace java mlx.thrift

struct ModelInfo
{
    1: string model_name;
    2: string group_name;
    3: i64 model_index;
}

service Ps
{
    list<string> create(1:string name, 2:list<binary> pb_defs, 3:bool pb_binary=false),

    void drop(1:string name),

    void move(1:string from_name, 2:string to_name, 3:string backup_name),

    binary model_def(1:string name, 2:bool pb_binary=false),

    list<binary> pull(1:list<string> names, 2:list<binary> reqs, 3:string val_type, 4:i64 log_id),

    oneway void push(1:list<string> names, 2:list<binary> reqs, 3:string val_type, 4:i32 epoch=0, 5:i64 log_id),

    // type: WEIGHTS, PARAMS, GRADS; format: PROTO, PBTXT
    void dump(1:list<string> names, 2:string path, 3:string val_type='WEIGHTS', 4:string format='PROTO', 5:bool dump_zero=false),

    void dump_all(1:string val_type='WEIGHTS', 2:string format='PROTO', 3:bool dump_zero=false),

    void dump_online(1:string name, 2:string val_type='WEIGHTS', 3:string format='PROTO', 4:bool dump_zero=false),
    
    void restore_models(),

    void load(1:string name, 2:string path),

    void set_streaming_update(1:list<string> names, 2:bool streaming_update),

    list<binary> get_updated(1:list<string> names),

    list<string> stat(1:list<string> names),

    list<string> exec_shell(1:list<string> cmds),

    i32 shard_num(),

    list<ModelInfo> list_models(),

    string serving_type(),
}

