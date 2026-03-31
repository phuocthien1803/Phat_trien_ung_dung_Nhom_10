package entity;

public enum HinhThuc {
    DAT_BAN("Đặt bàn"),
    DAT_BAN_TRUOC("Đặt bàn trước");

    private String displayName;

    HinhThuc(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
