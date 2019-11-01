package Serialize.pushlandingpage;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTest {

    private static String buildVerticalInfo() {
        VerticalInfo info = new VerticalInfo();
        info.setFootInfo("XXX");
        info.setSubtopicName("XXX");
        info.setTopicName("XXX");
        info.setImageUrl("http://p1.meituan.net/scarlett/07fdd78e5de9411571b42eb0bcad01fa343929.jpg");
        info.setUrl("URL");
        return JSON.toJSON(info).toString();
    }

    private static String buildBubbleInfo() {
        BubbleInfo bubbleJson = new BubbleInfo();
//        bubbleJson.setTitle("你还感兴趣哪些");
//        BubbleInfo.TopicNameAndUrl info = new BubbleInfo.TopicNameAndUrl();
//        info.setName("舒适");
//        info.setUrl("shushi_url");
//        BubbleInfo.TopicNameAndUrl info2 = new BubbleInfo.TopicNameAndUrl();
//        info2.setName("连锁");
//        info2.setUrl("liansuo_url");
//
//        List<BubbleInfo.TopicNameAndUrl> list = Lists.newArrayList(info, info);
//
//        bubbleJson.setInfo(list);

        return JSON.toJSON(bubbleJson).toString();
    }

    private static String buildHorizonInfo() {
        HorizonInfo info = new HorizonInfo();
        info.setSubtopicName("XXX");
        info.setTopicName("XXX");
        info.setUrl("URL");
        info.setImageUrl("http://p1.meituan.net/scarlett/07fdd78e5de9411571b42eb0bcad01fa343929.jpg");
        info.setDoc("XXX");
        return JSON.toJSON(info).toString();
    }

    private static String buildRecWordsInfo() {
        RecWordsInfo info = new RecWordsInfo();
        info.setContent("南京住过很多酒店");

        return JSON.toJSON(info).toString();
    }

    private static String buildMoreUrlInfo() {
        MoreUrlInfo info = new MoreUrlInfo();
        info.setUrl("XXX");
        return JSON.toJSON(info).toString();
    }

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        map.put("vertical_combine", buildVerticalInfo());
        map.put("bubble_combine", buildBubbleInfo());
        map.put("horizontal_combine", buildHorizonInfo());
        map.put("recwords", buildRecWordsInfo());
        map.put("moreurl", buildMoreUrlInfo());

        String jsoned = JSON.toJSONString(map);
//        System.out.println("json: " + jsoned);

        String x = "triggerpoi_a_City_107_6000001";
        String split = x.split("_", 2)[1];
        System.out.println(split);

//        Map after = JSON.parseObject(jsoned, Map.class);
//        System.out.println("map: " + map.toString());

//        String str = "{\"recwords\":\"{\\\"content\\\":\\\"南京住过很多酒店\\\"}\",\"vertical_combine\":\"{\\\"footInfo\\\":\\\"XXX\\\",\\\"imageUrl\\\":\\\"IMAGE_URL\\\",\\\"subtopicName\\\":\\\"XXX\\\",\\\"topicName\\\":\\\"XXX\\\",\\\"url\\\":\\\"URL\\\"}\",\"horizontal_combine\":\"{\\\"imageUrl\\\":\\\"IMAGE_URL\\\",\\\"doc\\\":\\\"XXX\\\",\\\"subtopicName\\\":\\\"XXX\\\",\\\"topicName\\\":\\\"XXX\\\",\\\"url\\\":\\\"URL\\\"}\",\"bubble_combine\":\"{\\\"title\\\":\\\"你还感兴趣哪些\\\",\\\"info\\\":[{\\\"name\\\":\\\"舒适\\\",\\\"url\\\":\\\"shushi_url\\\"},{\\\"name\\\":\\\"舒适\\\",\\\"url\\\":\\\"shushi_url\\\"}]}\"}";
//        String str = "{\"recwords\":\"{\\\"content\\\":\\\"南京住过很多酒店\\\"}\",\"vertical_combine\":\"{\\\"footInfo\\\":\\\"XXX\\\",\\\"imageUrl\\\":\\\"http://p1.meituan.net/scarlett/07fdd78e5de9411571b42eb0bcad01fa343929.jpg\\\",\\\"subtopicName\\\":\\\"XXX\\\",\\\"topicName\\\":\\\"XXX\\\",\\\"url\\\":\\\"URL\\\"}\",\"horizontal_combine\":\"{\\\"imageUrl\\\":\\\"http://p1.meituan.net/scarlett/07fdd78e5de9411571b42eb0bcad01fa343929.jpg\\\",\\\"doc\\\":\\\"XXX\\\",\\\"subtopicName\\\":\\\"XXX\\\",\\\"topicName\\\":\\\"XXX\\\",\\\"url\\\":\\\"URL\\\"}\",\"moreurl\":\"{\\\"url\\\":\\\"XXX\\\"}\",\"bubble_combine\":\"{\\\"title\\\":\\\"你还感兴趣哪些\\\",\\\"info\\\":[{\\\"name\\\":\\\"舒适\\\",\\\"url\\\":\\\"shushi_url\\\"},{\\\"name\\\":\\\"舒适\\\",\\\"url\\\":\\\"shushi_url\\\"}]}\"}";
//        Map sdf = JSON.parseObject(str, Map.class);
//        System.out.println("after:" + sdf.toString());
    }
}
