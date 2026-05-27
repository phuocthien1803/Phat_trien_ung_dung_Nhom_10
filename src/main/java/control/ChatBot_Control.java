package control;

import connectDB.ConnectDB;
import dao.ChatHistory_DAO;
import dao.ChatHistory_DAO.ChatSession;
import dao.ChatHistory_DAO.ChatMessage;
import dao.ThongKe_DAO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatBot_Control implements Initializable {

    // ── FXML refs ──────────────────────────────────────────────────────────────
    @FXML private AnchorPane rootPane;
    @FXML private VBox       sidebar;
    @FXML private VBox       sessionList;
    @FXML private TextField  txtSearchHistory;
    @FXML private HBox       headerBar;
    @FXML private HBox       quickActionsBar;
    @FXML private HBox       inputBar;
    @FXML private VBox       chatBox;
    @FXML private TextField  txtInput;
    @FXML private ScrollPane scrollPane;
    @FXML private Button     btnToggleTheme;
    @FXML private Button     btnClear;
    @FXML private Button     btnSend;
    @FXML private Button     btnOpenSidebar;
    @FXML private Button     btnCloseSidebar;
    @FXML private Button     btnNewChat;
    @FXML private Button     btnSaveSession;
    @FXML private Button     btnDeleteAllSessions;
    @FXML private Label      lblTitle;
    @FXML private Label      lblSessionName;
    @FXML private Button     chip1, chip2, chip3, chip4, chip5;

    // ── API ────────────────────────────────────────────────────────────────────
    private static final String API_KEY = "AIzaSyCJW0AmE8bpG5BOIl16G2MnSJPTisP8cg4";
    private static final String API_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    // ── Session state ──────────────────────────────────────────────────────────
    /** [text, isUser, HH:mm] */
    private static final List<String[]>   chatHistory   = new ArrayList<>();
    private static final List<JSONObject> geminiHistory = new ArrayList<>();
    private static int     currentSessionId   = -1;   // -1 = chưa lưu
    private static String  currentSessionName = "Cuộc trò chuyện mới";
    private static boolean sidebarOpen        = true;
    private static boolean isDarkMode         = true;

    // ── Theme ──────────────────────────────────────────────────────────────────
    private static final String D_ROOT="#1A1A1A", D_HEADER="#111111", D_QUICK="#161616",
                                D_CHAT="#1A1A1A", D_INPUT="#111111",
                                D_SIDE="#111111", D_TITLE="#D4AF37";
    private static final String L_ROOT="#F5F0E8", L_HEADER="#FFFFFF", L_QUICK="#FAF5EC",
                                L_CHAT="#F5F0E8", L_INPUT="#FFFFFF",
                                L_SIDE="#F0EBE0", L_TITLE="#8B0000";

    // ── Typing animation ───────────────────────────────────────────────────────
    private static final String[] DOTS = {"●○○","○●○","○○●","●●●"};
    private int      dotFrame = 0;
    private Timeline typingTL;

    // ══════════════════════════════════════════════════════════════════════════
    // INITIALIZE
    // ══════════════════════════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ensure DB tables exist
        ChatHistory_DAO.ensureTablesExist();

        // Auto-scroll
        chatBox.heightProperty().addListener((o, ov, nv) -> scrollPane.setVvalue(1.0));

        // Search box live filter
        txtSearchHistory.textProperty().addListener((o, ov, nv) -> loadSessionList(nv));

        // Apply theme & sidebar
        applyTheme(isDarkMode);
        applySidebarState();

        // Load sessions into sidebar
        loadSessionList("");

        // Restore or welcome
        if (chatHistory.isEmpty()) {
            addBotMessage("Xin chào! Tôi là **Trợ lý AI Tân Hải Vân** 🍽\n"
                + "Hãy hỏi tôi về trạng thái bàn, doanh thu hôm nay, thống kê tháng,\n"
                + "hoặc nhấn một trong các nút hỏi nhanh bên trên.");
        } else {
            for (String[] m : chatHistory)
                renderMessage(m[0], Boolean.parseBoolean(m[1]), m.length > 2 ? m[2] : "");
        }
        updateSessionNameLabel();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SIDEBAR HANDLERS
    // ══════════════════════════════════════════════════════════════════════════

    @FXML public void handleToggleSidebar() {
        sidebarOpen = !sidebarOpen;
        applySidebarState();
    }

    private void applySidebarState() {
        if (sidebarOpen) {
            sidebar.setPrefWidth(280);
            sidebar.setMinWidth(280);
            sidebar.setMaxWidth(280);
            sidebar.setVisible(true);
            sidebar.setManaged(true);
        } else {
            sidebar.setVisible(false);
            sidebar.setManaged(false);
        }
        btnOpenSidebar.setText(sidebarOpen ? "✕" : "☰");
    }

    /** Load danh sách phiên vào sidebar. */
    private void loadSessionList(String keyword) {
        sessionList.getChildren().clear();
        try {
            List<ChatSession> sessions = keyword.isBlank()
                ? ChatHistory_DAO.layDanhSachPhien()
                : ChatHistory_DAO.timKiemPhien(keyword);

            if (sessions.isEmpty()) {
                Label empty = new Label("Chưa có lịch sử");
                empty.setStyle("-fx-text-fill: #555; -fx-font-size: 13; -fx-padding: 20 0 0 8;");
                sessionList.getChildren().add(empty);
                return;
            }

            // Nhóm theo ngày (Gemini-style)
            String lastGroup = "";
            for (ChatSession s : sessions) {
                String group = getDateGroup(s.thoiGianSua);
                if (!group.equals(lastGroup)) {
                    Label groupLabel = new Label(group);
                    groupLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 11; "
                        + "-fx-font-weight: bold; -fx-padding: 10 6 4 6; -fx-text-transform: uppercase;");
                    sessionList.getChildren().add(groupLabel);
                    lastGroup = group;
                }
                sessionList.getChildren().add(buildSessionCard(s));
            }
        } catch (Exception e) {
            System.err.println("[Sidebar] loadSessionList error: " + e.getMessage());
        }
    }

    /** Xác định nhóm ngày giống Gemini. */
    private String getDateGroup(String dateStr) {
        // dateStr format: "dd/MM/yyyy HH:mm"
        try {
            String[] parts = dateStr.split(" ")[0].split("/");
            int d = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            LocalDate date = LocalDate.of(y, m, d);
            LocalDate today = LocalDate.now();
            long diff = today.toEpochDay() - date.toEpochDay();
            if (diff == 0) return "Hôm nay";
            if (diff == 1) return "Hôm qua";
            if (diff <= 7) return "7 ngày trước";
            if (diff <= 30) return "30 ngày trước";
            return "Cũ hơn";
        } catch (Exception e) { return "Cũ hơn"; }
    }

    /** Tạo card cho một phiên trong sidebar. */
    private HBox buildSessionCard(ChatSession s) {
        boolean isActive = (s.sessionId == currentSessionId);

        // Title label
        Label titleLbl = new Label(s.tenPhien);
        titleLbl.setMaxWidth(160);
        titleLbl.setStyle("-fx-font-size: 13; -fx-text-fill: "
            + (isActive ? "#FFFFFF" : (isDarkMode ? "#CCCCCC" : "#333333"))
            + "; -fx-font-weight: " + (isActive ? "bold" : "normal") + ";");
        titleLbl.setEllipsisString("...");
        titleLbl.setWrapText(false);

        // Time label
        Label timeLbl = new Label(s.thoiGianSua.length() > 5
            ? s.thoiGianSua.substring(11) : s.thoiGianSua);
        timeLbl.setStyle("-fx-font-size: 10; -fx-text-fill: #666;");

        VBox textCol = new VBox(2, titleLbl, timeLbl);
        textCol.setMaxWidth(170);
        HBox.setHgrow(textCol, Priority.ALWAYS);

        // Delete button (hidden, shows on hover)
        Button btnDel = new Button("✕");
        btnDel.setStyle("-fx-background-color: transparent; -fx-text-fill: #666; "
            + "-fx-font-size: 12; -fx-cursor: hand; -fx-padding: 0 4;");
        btnDel.setVisible(false);
        btnDel.setOnAction(e -> {
            e.consume();
            confirmAndDeleteSession(s.sessionId);
        });

        HBox card = new HBox(8, textCol, btnDel);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(8, 10, 8, 10));
        card.setPrefHeight(50);
        card.setStyle(buildCardStyle(isActive));

        // Hover effects
        card.setOnMouseEntered(e -> {
            btnDel.setVisible(true);
            if (!isActive)
                card.setStyle(buildCardStyleHover());
        });
        card.setOnMouseExited(e -> {
            btnDel.setVisible(false);
            card.setStyle(buildCardStyle(isActive));
        });

        // Click → load session
        card.setOnMouseClicked(e -> {
            if (e.getTarget() != btnDel)
                loadSession(s.sessionId, s.tenPhien);
        });

        return card;
    }

    private String buildCardStyle(boolean active) {
        if (active)
            return "-fx-background-color: #8B0000; -fx-background-radius: 8; -fx-cursor: hand;";
        return "-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;";
    }

    private String buildCardStyleHover() {
        return "-fx-background-color: " + (isDarkMode ? "#2A2A2A" : "#E8DFC8")
            + "; -fx-background-radius: 8; -fx-cursor: hand;";
    }

    /** Tải một phiên cũ vào khung chat. */
    private void loadSession(int sessionId, String sessionName) {
        try {
            List<ChatMessage> msgs = ChatHistory_DAO.layTinNhanTheoPhien(sessionId);
            chatHistory.clear();
            geminiHistory.clear();
            chatBox.getChildren().clear();

            currentSessionId   = sessionId;
            currentSessionName = sessionName;
            updateSessionNameLabel();

            for (ChatMessage m : msgs) {
                String[] entry = {m.noiDung, String.valueOf(m.isUser), m.thoiGian};
                chatHistory.add(entry);
                renderMessage(m.noiDung, m.isUser, m.thoiGian);

                // Rebuild gemini history (chỉ lấy 10 cuối)
                JSONObject jMsg = new JSONObject();
                jMsg.put("role", m.isUser ? "user" : "model");
                jMsg.put("parts", new JSONArray().put(new JSONObject().put("text", m.noiDung)));
                geminiHistory.add(jMsg);
            }
            // Refresh sidebar để highlight session đang active
            loadSessionList(txtSearchHistory.getText());
        } catch (Exception e) {
            showError("Không thể tải lịch sử: " + e.getMessage());
        }
    }

    private void confirmAndDeleteSession(int sessionId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa phiên");
        alert.setHeaderText("Bạn có chắc muốn xóa phiên chat này?");
        alert.setContentText("Hành động này không thể hoàn tác.");
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    ChatHistory_DAO.xoaPhien(sessionId);
                    if (sessionId == currentSessionId) {
                        handleNewChat();
                    }
                    loadSessionList(txtSearchHistory.getText());
                } catch (Exception ex) {
                    showError("Lỗi xóa phiên: " + ex.getMessage());
                }
            }
        });
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HEADER HANDLERS
    // ══════════════════════════════════════════════════════════════════════════

    @FXML public void handleNewChat() {
        chatHistory.clear();
        geminiHistory.clear();
        chatBox.getChildren().clear();
        currentSessionId   = -1;
        currentSessionName = "Cuộc trò chuyện mới";
        updateSessionNameLabel();
        addBotMessage("Cuộc trò chuyện mới bắt đầu! 😊 Tôi có thể giúp gì cho bạn?");
        loadSessionList(txtSearchHistory.getText());
    }

    @FXML public void handleSaveSession() {
        if (chatHistory.isEmpty()) {
            showInfo("Không có gì để lưu.");
            return;
        }

        // Sinh tên phiên từ tin nhắn đầu tiên của user
        String autoName = chatHistory.stream()
            .filter(m -> "true".equals(m[1]))
            .map(m -> m[0].length() > 40 ? m[0].substring(0, 40) + "..." : m[0])
            .findFirst()
            .orElse("Cuộc trò chuyện " +
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Hỏi tên phiên
        TextInputDialog dialog = new TextInputDialog(autoName);
        dialog.setTitle("Lưu lịch sử");
        dialog.setHeaderText("Đặt tên cho cuộc trò chuyện này:");
        dialog.setContentText("Tên:");
        styleDialog(dialog);

        dialog.showAndWait().ifPresent(name -> {
            if (name.isBlank()) name = autoName;
            try {
                if (currentSessionId == -1) {
                    currentSessionId = ChatHistory_DAO.taoPhienMoi(name);
                } else {
                    ChatHistory_DAO.doiTenPhien(currentSessionId, name);
                }
                currentSessionName = name;
                ChatHistory_DAO.luuTatCaTinNhan(currentSessionId, chatHistory);
                updateSessionNameLabel();
                loadSessionList(txtSearchHistory.getText());
                showInfo("✅ Đã lưu: \"" + name + "\"");
            } catch (Exception ex) {
                showError("Lỗi lưu: " + ex.getMessage());
            }
        });
    }

    @FXML public void handleDeleteAllSessions() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa tất cả");
        alert.setHeaderText("Xóa toàn bộ lịch sử chat?");
        alert.setContentText("Hành động này không thể hoàn tác.");
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    ChatHistory_DAO.xoaTatCaPhien();
                    handleNewChat();
                    loadSessionList("");
                } catch (Exception ex) {
                    showError("Lỗi: " + ex.getMessage());
                }
            }
        });
    }

    @FXML public void handleToggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme(isDarkMode);
        loadSessionList(txtSearchHistory.getText()); // re-render cards
    }

    @FXML public void handleClear() {
        chatHistory.clear();
        geminiHistory.clear();
        chatBox.getChildren().clear();
        currentSessionId   = -1;
        currentSessionName = "Cuộc trò chuyện mới";
        updateSessionNameLabel();
        addBotMessage("Cuộc trò chuyện đã được xóa. Tôi có thể giúp gì cho bạn? 😊");
    }

    @FXML public void handleSend() {
        String msg = txtInput.getText().trim();
        if (msg.isEmpty()) return;
        txtInput.clear();
        processUserMessage(msg);
    }

    @FXML public void handleQuickAction(javafx.event.ActionEvent e) {
        Button src = (Button) e.getSource();
        String raw = src.getText().replaceAll("^[^\\w\\u00C0-\\u024F]+", "").trim();
        String q = switch (raw) {
            case "Doanh thu hôm nay" -> "Doanh thu hôm nay là bao nhiêu?";
            case "Trạng thái bàn"    -> "Hiện tại những bàn nào đang trống, đang phục vụ và đã đặt trước?";
            case "Thống kê tháng"    -> "Cho tôi xem thống kê doanh thu và số hóa đơn tháng này.";
            case "Số hóa đơn"        -> "Hôm nay có bao nhiêu hóa đơn?";
            case "Món bán chạy"      -> "Hôm nay bán được bao nhiêu món ăn?";
            default                  -> raw + "?";
        };
        txtInput.setText(q);
        handleSend();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CORE MESSAGE FLOW
    // ══════════════════════════════════════════════════════════════════════════

    private void processUserMessage(String message) {
        String ts = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        chatHistory.add(new String[]{message, "true", ts});
        renderMessage(message, true, ts);

        HBox typingRow = createTypingIndicator();
        chatBox.getChildren().add(typingRow);

        new Thread(() -> {
            String reply = callGeminiAPI(message);
            Platform.runLater(() -> {
                if (typingTL != null) typingTL.stop();
                chatBox.getChildren().remove(typingRow);
                addBotMessage(reply);

                // Auto-save nếu phiên đã được đặt tên (đã lưu lần đầu)
                if (currentSessionId != -1) {
                    try {
                        ChatHistory_DAO.luuTatCaTinNhan(currentSessionId, chatHistory);
                        loadSessionList(txtSearchHistory.getText());
                    } catch (Exception ex) {
                        System.err.println("[AutoSave] " + ex.getMessage());
                    }
                }
            });
        }).start();
    }

    private void addBotMessage(String text) {
        String ts = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        chatHistory.add(new String[]{text, "false", ts});
        renderMessage(text, false, ts);
    }

    private void updateSessionNameLabel() {
        if (lblSessionName != null)
            lblSessionName.setText(currentSessionName +
                (currentSessionId == -1 ? " (chưa lưu)" : ""));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // RENDER
    // ══════════════════════════════════════════════════════════════════════════

    private void renderMessage(String text, boolean isUser, String timestamp) {
        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.setMaxWidth(820);

        if (isUser) {
            bubble.setStyle(
                "-fx-padding: 12 18; -fx-background-radius: 18 18 4 18; -fx-font-size: 14.5; "
                + "-fx-background-color: linear-gradient(to bottom right, #8B0000, #B00000); "
                + "-fx-text-fill: white; -fx-effect: dropshadow(gaussian,rgba(0,0,0,.3),6,0,2,2);");
        } else {
            String bg  = isDarkMode ? "#2C2C2C" : "#FFFFFF";
            String fg  = isDarkMode ? "#F0E6C8" : "#222222";
            String brd = isDarkMode ? "#3A3A3A" : "#D4AF37";
            bubble.setStyle(
                "-fx-padding: 12 18; -fx-background-radius: 18 18 18 4; -fx-font-size: 14.5; "
                + "-fx-background-color:" + bg + "; -fx-text-fill:" + fg + "; "
                + "-fx-border-color:" + brd + "; -fx-border-radius: 18 18 18 4; -fx-border-width:1; "
                + "-fx-effect: dropshadow(gaussian,rgba(0,0,0,.2),6,0,2,2);");
        }

        Label ts = new Label(timestamp);
        ts.setStyle("-fx-font-size:10; -fx-text-fill:" + (isDarkMode ? "#555" : "#AAA")
            + "; -fx-padding: 2 6 0 6;");

        VBox col = new VBox(2, bubble, ts);
        col.setMaxWidth(850);
        col.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        if (!isUser) {
            Circle av = new Circle(16); av.setFill(Color.web("#8B0000"));
            Label ai = new Label("AI");
            ai.setStyle("-fx-font-size:9; -fx-text-fill:#D4AF37; -fx-font-weight:bold;");
            StackPane avPane = new StackPane(av, ai);
            HBox row = new HBox(10, avPane, col);
            row.setAlignment(Pos.BOTTOM_LEFT);
            chatBox.getChildren().add(row);
            return;
        }
        HBox row = new HBox(col);
        row.setAlignment(Pos.CENTER_RIGHT);
        chatBox.getChildren().add(row);
    }

    private HBox createTypingIndicator() {
        Label dots = new Label(DOTS[0]);
        dots.setStyle("-fx-font-size:20; -fx-text-fill:#D4AF37; -fx-padding: 4 10;");
        Label lbl  = new Label("Đang xử lý");
        lbl.setStyle("-fx-font-size:13; -fx-text-fill:" + (isDarkMode ? "#888" : "#AAA")
            + "; -fx-font-style:italic;");
        String bg = isDarkMode ? "#2C2C2C" : "#FFFFFF";
        HBox bubble = new HBox(6, dots, lbl);
        bubble.setStyle("-fx-background-color:" + bg + "; -fx-background-radius:18; -fx-padding:10 16; "
            + "-fx-border-color:#3A3A3A; -fx-border-radius:18; -fx-border-width:1;");
        bubble.setAlignment(Pos.CENTER_LEFT);

        typingTL = new Timeline(new KeyFrame(Duration.millis(400), e -> {
            dotFrame = (dotFrame + 1) % DOTS.length;
            dots.setText(DOTS[dotFrame]);
        }));
        typingTL.setCycleCount(Timeline.INDEFINITE);
        typingTL.play();

        Circle av = new Circle(16); av.setFill(Color.web("#8B0000"));
        Label ai  = new Label("AI");
        ai.setStyle("-fx-font-size:9; -fx-text-fill:#D4AF37; -fx-font-weight:bold;");
        StackPane avPane = new StackPane(av, ai);
        HBox row = new HBox(10, avPane, bubble);
        row.setAlignment(Pos.BOTTOM_LEFT);
        return row;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // THEME
    // ══════════════════════════════════════════════════════════════════════════

    private void applyTheme(boolean dark) {
        String root = dark ? D_ROOT : L_ROOT;
        String hdr  = dark ? D_HEADER : L_HEADER;
        String qk   = dark ? D_QUICK  : L_QUICK;
        String chat = dark ? D_CHAT   : L_CHAT;
        String inp  = dark ? D_INPUT  : L_INPUT;
        String side = dark ? D_SIDE   : L_SIDE;
        String titC = dark ? D_TITLE  : L_TITLE;
        String brd  = "#D4AF37";

        rootPane.setStyle("-fx-background-color:" + root + ";");
        sidebar.setStyle("-fx-background-color:" + side + "; -fx-border-color:#2A2A2A; -fx-border-width:0 1 0 0;");
        headerBar.setStyle("-fx-background-color:" + hdr + "; -fx-border-color:" + brd
            + "; -fx-border-width:0 0 2 0; -fx-padding:0 20 0 16;");
        quickActionsBar.setStyle("-fx-background-color:" + qk + "; -fx-padding:10 20 10 20; "
            + "-fx-border-color:" + (dark ? "#2A2A2A" : "#E8DFC8") + "; -fx-border-width:0 0 1 0;");
        chatBox.setStyle("-fx-padding:24 30 24 30; -fx-background-color:" + chat + ";");
        inputBar.setStyle("-fx-background-color:" + inp + "; -fx-border-color:" + brd
            + "; -fx-border-width:2 0 0 0; -fx-padding:14 20 14 20;");

        String fldStyle = dark
            ? "-fx-background-color:#2A2A2A; -fx-text-fill:#EEEEEE; -fx-prompt-text-fill:#666; "
              + "-fx-background-radius:25; -fx-border-radius:25; -fx-border-color:#3A3A3A; "
              + "-fx-border-width:1; -fx-padding:0 20; -fx-font-size:15;"
            : "-fx-background-color:#FFFFFF; -fx-text-fill:#222; -fx-prompt-text-fill:#999; "
              + "-fx-background-radius:25; -fx-border-radius:25; -fx-border-color:#D4AF37; "
              + "-fx-border-width:1.5; -fx-padding:0 20; -fx-font-size:15;";
        txtInput.setStyle(fldStyle);

        lblTitle.setStyle("-fx-font-size:19; -fx-font-weight:bold; -fx-text-fill:" + titC
            + "; -fx-font-family:'Georgia';");

        btnToggleTheme.setText(dark ? "☀ Sáng" : "🌙 Tối");
        btnToggleTheme.setStyle("-fx-background-color:" + (dark ? "#2A2A2A" : "#F0E6D2")
            + "; -fx-text-fill:" + (dark ? "#D4AF37" : "#8B0000")
            + "; -fx-background-radius:18; -fx-border-color:" + brd
            + "; -fx-border-radius:18; -fx-font-size:13; -fx-cursor:hand;");

        // Re-render all bubbles
        chatBox.getChildren().clear();
        for (String[] m : chatHistory)
            renderMessage(m[0], Boolean.parseBoolean(m[1]), m.length > 2 ? m[2] : "");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DATABASE — real-time data for AI context
    // ══════════════════════════════════════════════════════════════════════════

    private String layDuLieuDatabase() {
        try {
            LocalDate today = LocalDate.now();
            DecimalFormat df = new DecimalFormat("#,###");
            StringBuilder banTrong = new StringBuilder(),
                          banDaDat = new StringBuilder(),
                          banDangPV = new StringBuilder();
            int tongSoBan = 0;

            String sql = """
                SELECT b.maBan,
                    ISNULL(SUM(CASE WHEN hd.checkIn=1 AND hd.trangThaiHoaDon='CHUA_THANH_TOAN' THEN 1 ELSE 0 END),0) soCheckIn,
                    ISNULL(SUM(CASE WHEN hd.checkIn=0 AND hd.trangThaiHoaDon='CHUA_THANH_TOAN' THEN 1 ELSE 0 END),0) soChuaCheckIn
                FROM Ban b
                LEFT JOIN HoaDon hd ON b.maBan=hd.maBan AND CAST(hd.ngayDat AS DATE)=?
                GROUP BY b.maBan ORDER BY b.maBan
                """;
            try (Connection c = ConnectDB.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(today));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    tongSoBan++;
                    String mb = rs.getString("maBan");
                    if      (rs.getInt("soCheckIn") > 0)    banDangPV.append(mb).append(", ");
                    else if (rs.getInt("soChuaCheckIn") > 0) banDaDat.append(mb).append(", ");
                    else                                      banTrong.append(mb).append(", ");
                }
            }

            double dtHom   = ThongKe_DAO.getDoanhThuDay(today);
            int    hdHom   = ThongKe_DAO.getHoaDonDay(today);
            int    monHom  = ThongKe_DAO.getMonAnCountDay(today);
            double dtThang = ThongKe_DAO.getDoanhThuThang(today.getMonthValue(), today.getYear());
            int    hdThang = ThongKe_DAO.getHoaDonThang(today.getMonthValue(), today.getYear());

            return "=== DỮ LIỆU THỜI GIAN THỰC (" + today + ") ===\n"
                + "[BÀN - Tổng: " + tongSoBan + "]\n"
                + "Trống: "     + tc(banTrong, "Không có") + "\n"
                + "Đã đặt: "   + tc(banDaDat, "Không có") + "\n"
                + "Đang PV: "  + tc(banDangPV,"Không có") + "\n"
                + "[HÔM NAY]\n"
                + "Doanh thu: " + df.format(dtHom) + " VNĐ | HĐ: " + hdHom + " | Món: " + monHom + "\n"
                + "[THÁNG " + today.getMonthValue() + "/" + today.getYear() + "]\n"
                + "Doanh thu: " + df.format(dtThang) + " VNĐ | HĐ: " + hdThang + "\n"
                + "==========================================\n";
        } catch (Exception e) {
            return "Không thể tải dữ liệu DB: " + e.getMessage() + "\n";
        }
    }

    private String tc(StringBuilder sb, String fb) {
        return sb.length() == 0 ? fb : sb.toString().replaceAll(",\\s*$", "");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GEMINI API
    // ══════════════════════════════════════════════════════════════════════════

    private String callGeminiAPI(String userText) {
        try {
            String dbData = layDuLieuDatabase();
            JSONObject root = new JSONObject();

            JSONObject sysPart = new JSONObject();
            sysPart.put("text",
                "Bạn là Trợ lý Nghiệp vụ AI của nhà hàng Tân Hải Vân.\n"
                + "Hỗ trợ nhân viên và quản lý tra cứu thông tin nội bộ theo thời gian thực.\n\n"
                + "DỮ LIỆU THỰC TẾ:\n" + dbData
                + "QUY TẮC:\n"
                + "1. Trả lời DỰA TRÊN dữ liệu được cung cấp, không bịa số liệu.\n"
                + "2. Không có dữ liệu → 'Hệ thống không có dữ liệu cho yêu cầu này.'\n"
                + "3. Số tiền: thêm VNĐ và dấu phân cách hàng nghìn.\n"
                + "4. Tiếng Việt, chuyên nghiệp, súc tích, thân thiện. Dùng emoji phù hợp.\n"
                + "5. Nhất quán với lịch sử hội thoại.\n"
                + "6. Câu hỏi phân tích: đưa ra nhận xét hữu ích ngắn gọn.");
            JSONObject sysInst = new JSONObject();
            sysInst.put("parts", new JSONArray().put(sysPart));
            root.put("system_instruction", sysInst);

            // Add new user turn to gemini history
            JSONObject newMsg = new JSONObject();
            newMsg.put("role", "user");
            newMsg.put("parts", new JSONArray().put(new JSONObject().put("text", userText)));
            geminiHistory.add(newMsg);

            // Keep last 10 turns
            int start = Math.max(0, geminiHistory.size() - 10);
            JSONArray contents = new JSONArray();
            for (int i = start; i < geminiHistory.size(); i++) contents.put(geminiHistory.get(i));
            root.put("contents", contents);

            JSONObject cfg = new JSONObject();
            cfg.put("temperature", 0.25);
            cfg.put("maxOutputTokens", 1024);
            cfg.put("topP", 0.8);
            root.put("generationConfig", cfg);

            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(java.time.Duration.ofSeconds(20)).build();
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + API_KEY))
                .header("Content-Type", "application/json")
                .timeout(java.time.Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(root.toString())).build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("[ChatBot] HTTP " + res.statusCode());

            if (res.statusCode() == 200) {
                JSONObject body = new JSONObject(res.body());
                JSONArray cands = body.optJSONArray("candidates");
                if (cands == null || cands.isEmpty()) return "⚠️ AI không thể tạo phản hồi.";
                JSONObject first = cands.getJSONObject(0);
                String fin = first.optString("finishReason", "");
                if ("SAFETY".equals(fin) || "RECITATION".equals(fin))
                    return "⚠️ AI từ chối câu hỏi này. Vui lòng đặt lại.";

                String reply = first.getJSONObject("content")
                    .getJSONArray("parts").getJSONObject(0).getString("text").trim();

                JSONObject aMsg = new JSONObject();
                aMsg.put("role", "model");
                aMsg.put("parts", new JSONArray().put(new JSONObject().put("text", reply)));
                geminiHistory.add(aMsg);
                return reply;
            } else {
                return switch (res.statusCode()) {
                    case 400 -> "❌ Lỗi 400: Yêu cầu không hợp lệ.";
                    case 401, 403 -> "❌ API Key không hợp lệ.";
                    case 404 -> "❌ Model không tìm thấy.";
                    case 429 -> "⚠️ Vượt quá giới hạn API. Thử lại sau.";
                    default  -> "❌ Lỗi " + res.statusCode();
                };
            }
        } catch (java.net.http.HttpTimeoutException e) {
            return "⏱️ Timeout kết nối Google API. Kiểm tra mạng.";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Lỗi AI: " + e.getMessage();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    private void showInfo(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Thông báo"); a.setHeaderText(null); a.setContentText(msg);
            a.showAndWait();
        });
    }

    private void showError(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Lỗi"); a.setHeaderText(null); a.setContentText(msg);
            a.showAndWait();
        });
    }

    private void styleDialog(Dialog<?> d) {
        // Optional: apply dark styling to dialog if needed
        d.getDialogPane().setStyle(isDarkMode
            ? "-fx-background-color:#1E1E1E; -fx-text-fill:#EEEEEE;"
            : "-fx-background-color:#FAFAFA;");
    }
}
