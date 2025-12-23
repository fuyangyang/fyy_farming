package basic.switch_enum;

public enum Code {
    SUCCESS(10000, "操作成功"),
    FAIL(10001, "操作失败");


    private int code;
    private String msg;

    //为了更好的返回代号和说明，必须呀重写构造方法
    private Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    // 根据value返回枚举类型,主要在switch中使用
    public static Code getByValue(int value) {
        for (Code code : values()) {
            if (code.getCode() == value) {
                return code;
            }
        }
        return null;
    }

    public static void main(String[] args) {
//获取代码
        int code=Code.SUCCESS.getCode();
//获取代码对应的信息
        String msg=Code.SUCCESS.getMsg();

//在switch中使用通常需要先获取枚举类型才判断，因为case中是常量或者int、byte、short、char，写其他代码编译是不通过的


        switch (Code.getByValue(code)) {

            case SUCCESS:
                System.out.println("SUCCESS");
                break;

            case FAIL:
                System.out.println("FAIL");
                break;

        }

    }
}
