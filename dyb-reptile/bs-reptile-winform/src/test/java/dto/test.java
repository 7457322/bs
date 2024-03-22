package dto;

public enum test {
    PRICE("有"), NOPRICE("无");

    private String desc;

    private test(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
