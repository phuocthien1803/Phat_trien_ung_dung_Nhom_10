package control;

import entity.NhanVien;


public class SessionManager {

    // volatile đảm bảo instance được ghi/đọc đúng thứ tự trên multi-thread
    private static volatile SessionManager instance;
    private NhanVien currentNhanVien;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public NhanVien getCurrentNhanVien() {
        return currentNhanVien;
    }

    public void setCurrentNhanVien(NhanVien nv) {
        this.currentNhanVien = nv;
    }

    /**
     * Xóa dữ liệu phiên làm việc hiện tại.
     * PHẢI gọi phương thức này khi người dùng đăng xuất.
     */
    public void clear() {
        this.currentNhanVien = null;
    }
}