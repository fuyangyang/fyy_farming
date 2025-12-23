package utils;

import com.github.houbb.opencc4j.util.ZhConverterUtil;

public class Jian2fan {
    public static void main(String[] args) {
        String simplified = "我爱编程，Java是世界上最好的语言！国内的自制剧上架以后，需要运营去手动翻译成繁体，然后上传到海外进行投放。这样效率效低";

        // 简体转繁体（默认规则）
        String traditional = ZhConverterUtil.toTraditional(simplified);
        System.out.println("繁体：" + traditional);

        // 繁体转简体（反向转换）
        String simplified2 = ZhConverterUtil.toSimple(traditional);
        System.out.println("简体（反向）：" + simplified2);
    }
}
