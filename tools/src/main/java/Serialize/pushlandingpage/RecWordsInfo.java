package Serialize.pushlandingpage;

import com.alibaba.fastjson.JSON;

public class RecWordsInfo {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static void main(String[] args) {
        RecWordsInfo info = new RecWordsInfo();
        info.setContent("南京住过很多酒店");
        System.out.println(JSON.toJSONString(info));
    }
}
