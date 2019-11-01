package Serialize.pushlandingpage;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class PoiInfo {
    private String poi_id;

    public String getPoi_id() {
        return poi_id;
    }

    public void setPoi_id(String poi_id) {
        this.poi_id = poi_id;
    }

    public static void main(String[] args) {
        HashMap<Object, Object> objectObjectHashMap = Maps.newHashMap();
        String json = "{\"poi_id\":187937054,\"poi_name\":\"CAFE24\"7\",\"score\":0.07292152941226959}";
        String jsonFormatted = json.split(",")[0] + "}";
        Gson gson = new Gson();
        PoiInfo poiInfo = gson.fromJson(jsonFormatted, PoiInfo.class);
        System.out.println(poiInfo.getPoi_id());

    }
}
