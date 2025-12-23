package test;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

public class Record {
    protected String reqId = null;
    protected long recordTimestamp;
    protected String data = null;
    /**
     * 存放原始流中的kv数据
     */
    protected Map<String, Object> mapData = null;
    /**
     * 用于上下文传递临时变量
     */
    protected Map<String, Object> extra = Maps.newHashMap();


    public long getRecordTimestamp() {
        return recordTimestamp;
    }

    public void setRecordTimestamp(long recordTimestamp) {
        this.recordTimestamp = recordTimestamp;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, java.lang.Object> getMapData() {
        return mapData;
    }

    public void setMapData(Map<String, java.lang.Object> mapData) {
        this.mapData = mapData;
    }

    public boolean setKVData(String key, Object value) {
        extra.put(key, value);
        return true;
    }

    public Object getKVData(String key) {
        return extra.get(key);
    }

    public void removeKVData(String key) {
        extra.remove(key);
    }

    @Override
    public String toString() {
        return "Record{" +
                "reqId='" + reqId + '\'' +
                ", recordTimestamp=" + recordTimestamp +
                ", data='" + data + '\'' +
                ", mapData=" + mapData +
                ", extra=" + extra +
                '}';
    }

    //    @Override
//    public String toString() {
//        Map<String, Object> datas = Maps.newHashMap();
//        datas.put("req_id", reqId);
//        datas.put("timestamp", recordTimestamp);
//        if (data != null) {
//            datas.put("data", data);
//        }
//        if (mapData != null) {
//            datas.put("map_data", mapData);
//        }
//        if (!extra.isEmpty()) {
//            datas.putAll(extra);
//        }
////        return JSON.toJSONString(datas);
//        return ToStringBuilder.reflectionToString(datas);
//    }

    public static void main(String[] args) {
        Record record = new Record();
        record.setData("data");
        record.setKVData("k1", "v1");
        record.setKVData("k2", "v2");
        record.setRecordTimestamp(1234567L);
        record.setReqId("reqId");
        Map<String, Object> map = Maps.newHashMap();
        map.put("mapkey", "mapvalue");
        record.setMapData(map);
        System.out.println(record);
    }
}
