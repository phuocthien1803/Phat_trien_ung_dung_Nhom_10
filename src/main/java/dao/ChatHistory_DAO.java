package dao;

import connectDB.ConnectDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO quản lý lịch sử chat trong SQL Server.
 *
 * DDL cần chạy một lần:
 * ─────────────────────────────────────────────────────────────────────────
 * CREATE TABLE ChatSession (
 *     sessionId   INT IDENTITY(1,1) PRIMARY KEY,
 *     tenPhien    NVARCHAR(200)  NOT NULL DEFAULT N'Cuộc trò chuyện mới',
 *     thoiGianTao DATETIME       NOT NULL DEFAULT GETDATE(),
 *     thoiGianSua DATETIME       NOT NULL DEFAULT GETDATE()
 * );
 *
 * CREATE TABLE ChatMessage (
 *     messageId   INT IDENTITY(1,1) PRIMARY KEY,
 *     sessionId   INT           NOT NULL REFERENCES ChatSession(sessionId) ON DELETE CASCADE,
 *     noiDung     NVARCHAR(MAX) NOT NULL,
 *     isUser      BIT           NOT NULL,   -- 1 = người dùng, 0 = bot
 *     thoiGian    NVARCHAR(5)   NOT NULL,   -- "HH:mm"
 *     thuTu       INT           NOT NULL    -- thứ tự tin nhắn trong phiên
 * );
 * ─────────────────────────────────────────────────────────────────────────
 */
public class ChatHistory_DAO {

    // ══════════════════════════════════════════════════════════════════════════
    // MODEL CLASSES
    // ══════════════════════════════════════════════════════════════════════════

    public static class ChatSession {
        public int    sessionId;
        public String tenPhien;
        public String thoiGianTao;   // formatted for display
        public String thoiGianSua;

        public ChatSession(int sessionId, String tenPhien,
                           String thoiGianTao, String thoiGianSua) {
            this.sessionId   = sessionId;
            this.tenPhien    = tenPhien;
            this.thoiGianTao = thoiGianTao;
            this.thoiGianSua = thoiGianSua;
        }

        @Override public String toString() { return tenPhien; }
    }

    public static class ChatMessage {
        public int    messageId;
        public int    sessionId;
        public String noiDung;
        public boolean isUser;
        public String thoiGian;
        public int    thuTu;

        public ChatMessage(int messageId, int sessionId, String noiDung,
                           boolean isUser, String thoiGian, int thuTu) {
            this.messageId = messageId;
            this.sessionId = sessionId;
            this.noiDung   = noiDung;
            this.isUser    = isUser;
            this.thoiGian  = thoiGian;
            this.thuTu     = thuTu;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SESSION CRUD
    // ══════════════════════════════════════════════════════════════════════════

    /** Tạo phiên mới, trả về sessionId. */
    public static int taoPhienMoi(String tenPhien) throws SQLException {
        String sql = "INSERT INTO ChatSession (tenPhien, thoiGianTao, thoiGianSua) "
                   + "OUTPUT INSERTED.sessionId VALUES (?, GETDATE(), GETDATE())";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, tenPhien);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("Không thể tạo phiên mới.");
        }
    }

    /** Đổi tên phiên. */
    public static void doiTenPhien(int sessionId, String tenMoi) throws SQLException {
        String sql = "UPDATE ChatSession SET tenPhien=?, thoiGianSua=GETDATE() WHERE sessionId=?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, tenMoi);
            ps.setInt(2, sessionId);
            ps.executeUpdate();
        }
    }

    /** Xóa một phiên (cascade xóa luôn messages). */
    public static void xoaPhien(int sessionId) throws SQLException {
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM ChatSession WHERE sessionId=?")) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        }
    }

    /** Xóa tất cả phiên. */
    public static void xoaTatCaPhien() throws SQLException {
        try (Connection conn = ConnectDB.connect();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM ChatSession");
        }
    }

    /**
     * Lấy danh sách phiên, sắp xếp theo thoiGianSua DESC (mới nhất lên đầu).
     * Nhóm theo ngày giống Gemini: Hôm nay / Hôm qua / 7 ngày trước / Cũ hơn.
     */
    public static List<ChatSession> layDanhSachPhien() throws SQLException {
        String sql = """
            SELECT sessionId, tenPhien,
                   FORMAT(thoiGianTao, 'dd/MM/yyyy HH:mm') AS thoiGianTao,
                   FORMAT(thoiGianSua, 'dd/MM/yyyy HH:mm') AS thoiGianSua
            FROM ChatSession
            ORDER BY thoiGianSua DESC
            """;
        List<ChatSession> list = new ArrayList<>();
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ChatSession(
                    rs.getInt("sessionId"),
                    rs.getNString("tenPhien"),
                    rs.getString("thoiGianTao"),
                    rs.getString("thoiGianSua")
                ));
            }
        }
        return list;
    }

    /** Lấy danh sách phiên theo từ khóa tìm kiếm. */
    public static List<ChatSession> timKiemPhien(String keyword) throws SQLException {
        String sql = """
            SELECT sessionId, tenPhien,
                   FORMAT(thoiGianTao, 'dd/MM/yyyy HH:mm') AS thoiGianTao,
                   FORMAT(thoiGianSua, 'dd/MM/yyyy HH:mm') AS thoiGianSua
            FROM ChatSession
            WHERE tenPhien LIKE ?
            ORDER BY thoiGianSua DESC
            """;
        List<ChatSession> list = new ArrayList<>();
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChatSession(
                    rs.getInt("sessionId"),
                    rs.getNString("tenPhien"),
                    rs.getString("thoiGianTao"),
                    rs.getString("thoiGianSua")
                ));
            }
        }
        return list;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MESSAGE CRUD
    // ══════════════════════════════════════════════════════════════════════════

    /** Lưu một tin nhắn vào phiên. */
    public static void luuTinNhan(int sessionId, String noiDung,
                                   boolean isUser, String thoiGian, int thuTu)
            throws SQLException {
        String sql = "INSERT INTO ChatMessage (sessionId, noiDung, isUser, thoiGian, thuTu) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.setNString(2, noiDung);
            ps.setBoolean(3, isUser);
            ps.setString(4, thoiGian);
            ps.setInt(5, thuTu);
            ps.executeUpdate();
        }
        // Cập nhật thoiGianSua của session
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE ChatSession SET thoiGianSua=GETDATE() WHERE sessionId=?")) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        }
    }

    /** Lưu toàn bộ danh sách tin nhắn của một phiên (batch insert). */
    public static void luuTatCaTinNhan(int sessionId, List<String[]> messages)
            throws SQLException {
        // Xóa messages cũ của session trước
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM ChatMessage WHERE sessionId=?")) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        }
        String sql = "INSERT INTO ChatMessage (sessionId, noiDung, isUser, thoiGian, thuTu) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < messages.size(); i++) {
                String[] m = messages.get(i);
                ps.setInt(1, sessionId);
                ps.setNString(2, m[0]);
                ps.setBoolean(3, Boolean.parseBoolean(m[1]));
                ps.setString(4, m.length > 2 ? m[2] : "");
                ps.setInt(5, i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
        // Update session timestamp
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE ChatSession SET thoiGianSua=GETDATE() WHERE sessionId=?")) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        }
    }

    /** Lấy tất cả tin nhắn của một phiên theo thứ tự. */
    public static List<ChatMessage> layTinNhanTheoPhien(int sessionId) throws SQLException {
        String sql = "SELECT messageId, sessionId, noiDung, isUser, thoiGian, thuTu "
                   + "FROM ChatMessage WHERE sessionId=? ORDER BY thuTu ASC";
        List<ChatMessage> list = new ArrayList<>();
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChatMessage(
                    rs.getInt("messageId"),
                    rs.getInt("sessionId"),
                    rs.getNString("noiDung"),
                    rs.getBoolean("isUser"),
                    rs.getString("thoiGian"),
                    rs.getInt("thuTu")
                ));
            }
        }
        return list;
    }

    /** Đếm số tin nhắn trong một phiên. */
    public static int demTinNhan(int sessionId) throws SQLException {
        try (Connection conn = ConnectDB.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT COUNT(*) FROM ChatMessage WHERE sessionId=?")) {
            ps.setInt(1, sessionId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DDL AUTO-CREATE (tự tạo bảng nếu chưa có)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Gọi 1 lần khi khởi động app để đảm bảo bảng tồn tại.
     */
    public static void ensureTablesExist() {
        try (Connection conn = ConnectDB.connect();
             Statement st = conn.createStatement()) {

            st.executeUpdate("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ChatSession' AND xtype='U')
                CREATE TABLE ChatSession (
                    sessionId   INT IDENTITY(1,1) PRIMARY KEY,
                    tenPhien    NVARCHAR(200)  NOT NULL DEFAULT N'Cuộc trò chuyện mới',
                    thoiGianTao DATETIME       NOT NULL DEFAULT GETDATE(),
                    thoiGianSua DATETIME       NOT NULL DEFAULT GETDATE()
                )
                """);

            st.executeUpdate("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ChatMessage' AND xtype='U')
                CREATE TABLE ChatMessage (
                    messageId   INT IDENTITY(1,1) PRIMARY KEY,
                    sessionId   INT           NOT NULL
                                REFERENCES ChatSession(sessionId) ON DELETE CASCADE,
                    noiDung     NVARCHAR(MAX) NOT NULL,
                    isUser      BIT           NOT NULL,
                    thoiGian    NVARCHAR(5)   NOT NULL DEFAULT '',
                    thuTu       INT           NOT NULL DEFAULT 0
                )
                """);

            System.out.println("[ChatHistory_DAO] Tables ready.");
        } catch (Exception e) {
            System.err.println("[ChatHistory_DAO] ensureTablesExist error: " + e.getMessage());
        }
    }
}
