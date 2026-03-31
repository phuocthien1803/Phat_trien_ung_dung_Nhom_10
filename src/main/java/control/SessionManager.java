package control;

import entity.NhanVien;

public class SessionManager {
    private static SessionManager instance;
    private NhanVien currentNhanVien;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentNhanVien(NhanVien nhanVien) {
        this.currentNhanVien = nhanVien;
    }

    public NhanVien getCurrentNhanVien() {
        return currentNhanVien;
    }
}

