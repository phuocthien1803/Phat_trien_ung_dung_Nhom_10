package entity;

public enum TrangThaiHoaDon {
	BI_HUY("Bị hủy"),
	CHUA_THANH_TOAN("Chưa thanh toán"),
	DA_THANH_TOAN("Đã thanh toán");

	private String displayName;

	TrangThaiHoaDon(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
