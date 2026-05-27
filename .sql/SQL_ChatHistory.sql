-- ═══════════════════════════════════════════════════════════════════
-- SQL Script: Tạo bảng lưu lịch sử chat — Tân Hải Vân AI Assistant
-- Chạy 1 lần trên SQL Server trước khi dùng tính năng lưu lịch sử
-- (ChatHistory_DAO.ensureTablesExist() cũng tự chạy script này)
-- ═══════════════════════════════════════════════════════════════════

-- ── Bảng phiên chat ──────────────────────────────────────────────
IF NOT EXISTS (
    SELECT * FROM sysobjects WHERE name='ChatSession' AND xtype='U'
)
CREATE TABLE ChatSession (
    sessionId   INT IDENTITY(1,1) PRIMARY KEY,
    tenPhien    NVARCHAR(200)  NOT NULL DEFAULT N'Cuộc trò chuyện mới',
    thoiGianTao DATETIME       NOT NULL DEFAULT GETDATE(),
    thoiGianSua DATETIME       NOT NULL DEFAULT GETDATE()
);

-- ── Bảng tin nhắn ────────────────────────────────────────────────
IF NOT EXISTS (
    SELECT * FROM sysobjects WHERE name='ChatMessage' AND xtype='U'
)
CREATE TABLE ChatMessage (
    messageId   INT IDENTITY(1,1) PRIMARY KEY,
    sessionId   INT           NOT NULL
                REFERENCES ChatSession(sessionId) ON DELETE CASCADE,
    noiDung     NVARCHAR(MAX) NOT NULL,
    isUser      BIT           NOT NULL,      -- 1=người dùng, 0=bot
    thoiGian    NVARCHAR(5)   NOT NULL DEFAULT '',   -- "HH:mm"
    thuTu       INT           NOT NULL DEFAULT 0     -- thứ tự trong phiên
);

-- ── Index để tăng tốc query ───────────────────────────────────────
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name='IX_ChatMessage_sessionId')
    CREATE INDEX IX_ChatMessage_sessionId ON ChatMessage(sessionId, thuTu);

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name='IX_ChatSession_thoiGianSua')
    CREATE INDEX IX_ChatSession_thoiGianSua ON ChatSession(thoiGianSua DESC);

PRINT 'ChatSession và ChatMessage tables ready.';
