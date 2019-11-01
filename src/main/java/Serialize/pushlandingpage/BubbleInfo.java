package Serialize.pushlandingpage;

import java.util.List;

/**
 * 气泡
 * {"key":"XXX","title":"XXX","RecWordsInfo":[{"name":"XXX","url":"XXX"},{"name":"XXX","url":"XXX"}]}
 */
public class BubbleInfo {
    private String title;
    private List<TopicNameAndUrl> info;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<TopicNameAndUrl> getInfo() {
        return info;
    }

    public void setInfo(List<TopicNameAndUrl> info) {
        this.info = info;
    }

    public static class TopicNameAndUrl {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
