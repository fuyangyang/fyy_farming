package Serialize.pushlandingpage;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PoilistInfo  {
    private String trigger_poilist;

    public String getTrigger_poilist() {
        return trigger_poilist;
    }

    public void setTrigger_poilist(String trigger_poilist) {
        this.trigger_poilist = trigger_poilist;
    }

    public static void main(String[] args) {
        /*PoilistInfo info = new PoilistInfo();
        info.setTrigger_poilist("a_City_107_6000001");
        String json = JSON.toJSONString(info);
        System.out.println(json);

        try {
            System.out.println("after: " + URLEncoder.encode(json, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


    }
}
