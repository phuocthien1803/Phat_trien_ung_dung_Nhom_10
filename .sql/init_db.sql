USE [master]
GO
/****** Object:  Database [QLiNhaHang]    Script Date: 12/2/2024 12:59:31 PM ******/
CREATE DATABASE [QLiNhaHang]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'QLiNhaHang', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\QLiNhaHang.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'QLiNhaHang_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\QLiNhaHang_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [QLiNhaHang] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QLiNhaHang].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QLiNhaHang] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QLiNhaHang] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QLiNhaHang] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QLiNhaHang] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QLiNhaHang] SET ARITHABORT OFF 
GO
ALTER DATABASE [QLiNhaHang] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [QLiNhaHang] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QLiNhaHang] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QLiNhaHang] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QLiNhaHang] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [QLiNhaHang] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QLiNhaHang] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QLiNhaHang] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QLiNhaHang] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QLiNhaHang] SET  ENABLE_BROKER 
GO
ALTER DATABASE [QLiNhaHang] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QLiNhaHang] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [QLiNhaHang] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [QLiNhaHang] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QLiNhaHang] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QLiNhaHang] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [QLiNhaHang] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [QLiNhaHang] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [QLiNhaHang] SET  MULTI_USER 
GO
ALTER DATABASE [QLiNhaHang] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [QLiNhaHang] SET DB_CHAINING OFF 
GO
ALTER DATABASE [QLiNhaHang] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [QLiNhaHang] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [QLiNhaHang] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [QLiNhaHang] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [QLiNhaHang] SET QUERY_STORE = ON
GO
ALTER DATABASE [QLiNhaHang] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [QLiNhaHang]
GO
/****** Object:  Table [dbo].[Ban]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Ban](
	[maBan] [nvarchar](3) NOT NULL,
	[soLuongGhe] [int] NULL,
	[khuVuc] [nvarchar](50) NULL,
	[trangThaiBan] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[maBan] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ChiTietHD_MonAn]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietHD_MonAn](
	[maHD] [nvarchar](12) NOT NULL,
	[maMonAn] [nvarchar](4) NOT NULL,
	[soLuong] [int] NULL,
	[thanhTien] [float] NULL,
	[ghiChu] [nvarchar](500) NULL,
 CONSTRAINT [PK_ChiTietHD_MonAn] PRIMARY KEY CLUSTERED 
(
	[maHD] ASC,
	[maMonAn] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HoaDon]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HoaDon](
	[maHD] [nvarchar](12) NOT NULL,
	[ngayTaoHD] [date] NULL,
	[trangThaiHoaDon] [nvarchar](50) NULL,
	[ngayDat] [date] NULL,
	[maBan] [nvarchar](3) NULL,
	[maNV] [nvarchar](12) NULL,
	[maKH] [nvarchar](11) NULL,
	[tienCoc] [float] NULL,
	[gioDatBan] [time](7) NULL,
	[maKM] [nvarchar](5) NULL,
	[tongTien] [float] NULL,
	[soLuongKH] [int] NULL,
	[hinhThuc] [nvarchar](50) NULL,
	[checkIn] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[maHD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KhachHang]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KhachHang](
	[maKH] [nvarchar](11) NOT NULL,
	[tenKH] [nvarchar](100) NULL,
	[sDT] [nvarchar](10) NULL,
	[diemTichLuy] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[maKH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KhuyenMai]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KhuyenMai](
	[maKM] [nvarchar](5) NOT NULL,
	[tenKM] [nvarchar](100) NULL,
	[ngayHetHan] [date] NULL,
	[ngayBatDau] [date] NULL,
	[phanTramGiam] [float] NULL,
PRIMARY KEY CLUSTERED 
(
	[maKM] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoaiMonAn]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiMonAn](
	[maLoai] [nvarchar](2) NOT NULL,
	[tenLoai] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[maLoai] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MonAn]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MonAn](
	[maMonAn] [nvarchar](4) NOT NULL,
	[tenMonAn] [nvarchar](50) NULL,
	[gia] [float] NULL,
	[trangThaiMonAn] [nvarchar](50) NULL,
	[VAT] [float] NULL,
	[maLoai] [nvarchar](2) NULL,
	[tenLoai] [nvarchar](100) NULL,
	[hinhAnh] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[maMonAn] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NhanVien]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhanVien](
	[maNV] [nvarchar](12) NOT NULL,
	[tenNV] [nvarchar](100) NULL,
	[gioiTinh] [bit] NULL,
	[sDT] [nvarchar](10) NULL,
	[chucVu] [nvarchar](20) NULL,
	[trangThai] [nvarchar](50) NULL,
	[ngayVaoLam] [date] NULL,
	[ngayNghiLam] [date] NULL,
	[ngaySinh] [date] NULL,
	[question] [nvarchar](100) NULL,
	[answer] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[maNV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TaiKhoan]    Script Date: 12/2/2024 12:59:31 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TaiKhoan](
	[tenDangNhap] [nvarchar](12) NOT NULL,
	[matKhau] [varchar](255) NULL
	PRIMARY KEY CLUSTERED 
(
	[tenDangNhap] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A01', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A02', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A03', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A04', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A05', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A06', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A07', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A08', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A09', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A10', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A11', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A12', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A13', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A14', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'A15', 2, N'A', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B01', 4, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B02', 4, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B03', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B04', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B05', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B06', 4, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B07', 4, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B08', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B09', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B10', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B11', 4, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B12', 4, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B13', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B14', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'B15', 6, N'B', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C01', 8, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C02', 8, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C03', 12, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C04', 12, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C05', 16, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C06', 8, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C07', 8, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C08', 12, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C09', 12, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C10', 16, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C11', 8, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C12', 8, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C13', 12, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C14', 12, N'C', N'TRONG')
INSERT [dbo].[Ban] ([maBan], [soLuongGhe], [khuVuc], [trangThaiBan]) VALUES (N'C15', 16, N'C', N'TRONG')
GO
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA011223001', N'FO06', 1, 240000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA011223001', N'FO08', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA011224001', N'DR02', 1, 20000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA011224001', N'FO01', 1, 100000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA011224001', N'FO05', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA011224001', N'FO08', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224006', N'AC09', 1, 380000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224006', N'DR05', 1, 12000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224006', N'FO04', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224006', N'FO07', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224006', N'FO09', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224006', N'FO11', 1, 100000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224007', N'DR02', 1, 20000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224007', N'DR05', 1, 12000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224007', N'FO01', 1, 100000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224007', N'FO04', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA021224007', N'FO08', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA041124001', N'FO05', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA041124001', N'FO07', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA041124002', N'FO04', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA041124002', N'FO08', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA041124003', N'FO06', 1, 240000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA041124003', N'FO08', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA041124006', N'FO06', 1, 240000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDA041124006', N'FO10', 1, 120000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224001', N'AC06', 24, 360000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224001', N'AC08', 1, 1680000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224001', N'FO01', 1, 100000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224001', N'FO09', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224002', N'AC06', 24, 360000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224002', N'FO04', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224002', N'FO05', 2, 500000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224002', N'FO08', 2, 300000, N'2')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224002', N'FO12', 2, 500000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224002', N'FO13', 2, 500000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224004', N'DR01', 6, 120000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224004', N'FO03', 2, 300000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224004', N'FO06', 2, 480000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224004', N'FO07', 2, 500000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224004', N'FO08', 2, 300000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224005', N'AC07', 1, 1000000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224005', N'DR05', 1, 12000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224005', N'FO01', 1, 100000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224005', N'FO04', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224005', N'FO07', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB021224005', N'FO08', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB041124004', N'FO10', 1, 120000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB041124004', N'FO14', 1, 75000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB041124008', N'FO07', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB041124008', N'FO08', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDB041124008', N'FO12', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC021224003', N'AC08', 2, 3360000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC021224003', N'FO02', 4, 140000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC021224003', N'FO04', 2, 500000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC021224003', N'FO05', 4, 1000000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC021224003', N'FO07', 4, 1000000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC021224003', N'FO09', 4, 1000000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC021224003', N'FO12', 4, 1000000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC021224003', N'FO13', 2, 500000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC041124005', N'FO06', 1, 240000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC041124005', N'FO09', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC041124005', N'FO14', 1, 75000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC041124010', N'FO03', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC041124010', N'FO05', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC041124010', N'FO08', 1, 150000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC101024001', N'FO06', 1, 240000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC101024001', N'FO09', 1, 250000, N'')
INSERT [dbo].[ChiTietHD_MonAn] ([maHD], [maMonAn], [soLuong], [thanhTien], [ghiChu]) VALUES (N'HDC101024001', N'FO14', 1, 75000, N'')
GO
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA011223001', CAST(N'2023-12-01' AS Date), N'DA_THANH_TOAN', CAST(N'2023-11-30' AS Date), N'A01', N'NVTN01012401', N'KH230924008', 195000, CAST(N'08:15:00' AS Time), NULL, 421200, 2, N'DAT_BAN_TRUOC', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA011224001', CAST(N'2024-12-01' AS Date), N'DA_THANH_TOAN', CAST(N'2024-12-01' AS Date), N'A02', N'NVTN03112401', N'KH000', 0, CAST(N'23:52:00' AS Time), NULL, 562000, 2, N'DAT_BAN', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA021224006', CAST(N'2024-12-02' AS Date), N'BI_HUY', CAST(N'2023-11-15' AS Date), N'A02', N'NVTN03112401', N'KH201024018', 621000, CAST(N'09:00:00' AS Time), NULL, 1349200, 2, N'DAT_BAN_TRUOC', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA021224007', CAST(N'2024-12-02' AS Date), N'DA_THANH_TOAN', CAST(N'2023-11-13' AS Date), N'A01', N'NVTN03112401', N'KH150624002', 266000, CAST(N'09:30:00' AS Time), NULL, 575200, 2, N'DAT_BAN_TRUOC', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA041124001', CAST(N'2024-11-04' AS Date), N'DA_THANH_TOAN', CAST(N'2024-11-04' AS Date), N'A01', N'NVQL24102402', N'KH000', 0, CAST(N'09:23:00' AS Time), NULL, 540000, 2, N'DAT_BAN', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA041124002', CAST(N'2024-11-04' AS Date), N'DA_THANH_TOAN', CAST(N'2024-11-04' AS Date), N'A01', N'NVQL24102402', N'KH191024001', 200000, CAST(N'20:00:00' AS Time), NULL, 432000, 2, N'DAT_BAN_TRUOC', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA041124003', CAST(N'2024-11-04' AS Date), N'BI_HUY', CAST(N'2024-11-04' AS Date), N'A01', N'NVTN04112401', N'KH191024001', 195000, CAST(N'19:15:00' AS Time), NULL, 421200, 2, N'DAT_BAN_TRUOC', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA041124006', CAST(N'2024-11-04' AS Date), N'DA_THANH_TOAN', CAST(N'2024-11-04' AS Date), N'A01', N'NVTN01012401', N'KH000', 0, CAST(N'10:37:00' AS Time), NULL, 388800, 2, N'DAT_BAN', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDA041124007', CAST(N'2024-11-04' AS Date), N'BI_HUY', CAST(N'2024-12-03' AS Date), N'A01', N'NVQL24102402', N'KH230924008', 0, CAST(N'10:00:00' AS Time), NULL, 0, 2, N'DAT_BAN_TRUOC', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDB021224001', CAST(N'2024-12-02' AS Date), N'BI_HUY', CAST(N'2024-02-22' AS Date), N'B08', N'NVQL24102402', N'KH231024004', 1195000, CAST(N'10:20:00' AS Time), NULL, 2622000, 2, N'DAT_BAN_TRUOC', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDB021224002', CAST(N'2024-12-02' AS Date), N'BI_HUY', CAST(N'2024-02-06' AS Date), N'B03', N'NVTN03112401', N'KH140224001', 1205000, CAST(N'10:30:00' AS Time), NULL, 2610000, 6, N'DAT_BAN_TRUOC', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDB021224004', CAST(N'2024-12-02' AS Date), N'DA_THANH_TOAN', CAST(N'2024-12-02' AS Date), N'B02', N'NVTN03112401', N'KH240424017', 0, CAST(N'00:19:00' AS Time), NULL, 1838400, 4, N'DAT_BAN', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDB021224005', CAST(N'2024-12-02' AS Date), N'BI_HUY', CAST(N'2023-11-19' AS Date), N'B03', N'NVTN03112401', N'KH201024018', 881000, CAST(N'09:00:00' AS Time), NULL, 1923200, 4, N'DAT_BAN_TRUOC', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDB041124004', CAST(N'2024-11-04' AS Date), N'DA_THANH_TOAN', CAST(N'2024-11-04' AS Date), N'B03', N'NVTN01012401', N'KH000', 0, CAST(N'08:19:00' AS Time), NULL, 210600, 7, N'DAT_BAN', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDB041124008', CAST(N'2024-11-04' AS Date), N'BI_HUY', CAST(N'2024-11-04' AS Date), N'B02', N'NVQL24102402', N'KH000', 0, CAST(N'09:47:00' AS Time), NULL, 0, 4, N'DAT_BAN', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDC021224003', CAST(N'2024-12-02' AS Date), N'DA_THANH_TOAN', CAST(N'2024-12-02' AS Date), N'C05', N'NVTN03112401', N'KH231024003', 0, CAST(N'00:16:00' AS Time), NULL, 9247200, 16, N'DAT_BAN', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDC031124001', CAST(N'2024-11-03' AS Date), N'BI_HUY', CAST(N'2024-11-03' AS Date), N'C03', N'NVTN03112401', N'KH000', 0, CAST(N'19:47:00' AS Time), NULL, 0, 0, N'DAT_BAN', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDC041124005', CAST(N'2024-11-04' AS Date), N'DA_THANH_TOAN', CAST(N'2024-11-04' AS Date), N'C03', N'NVTN03112401', N'KH000', 0, CAST(N'18:21:00' AS Time), NULL, 610200, 12, N'DAT_BAN', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDC041124009', CAST(N'2024-11-04' AS Date), N'BI_HUY', CAST(N'2024-11-06' AS Date), N'C10', N'NVQL24102402', N'KH231024007', 0, CAST(N'19:30:00' AS Time), NULL, 0, 20, N'DAT_BAN_TRUOC', 0)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDC041124010', CAST(N'2024-11-04' AS Date), N'DA_THANH_TOAN', CAST(N'2024-11-06' AS Date), N'C05', N'NVQL24102402', N'KH191024001', 0, CAST(N'08:15:00' AS Time), NULL, 594000, 15, N'DAT_BAN_TRUOC', 1)
INSERT [dbo].[HoaDon] ([maHD], [ngayTaoHD], [trangThaiHoaDon], [ngayDat], [maBan], [maNV], [maKH], [tienCoc], [gioDatBan], [maKM], [tongTien], [soLuongKH], [hinhThuc], [checkIn]) VALUES (N'HDC101024001', CAST(N'2024-10-10' AS Date), N'DA_THANH_TOAN', CAST(N'2024-10-10' AS Date), N'C06', N'NVTN01012401', N'KH231024007', 0, CAST(N'06:15:00' AS Time), NULL, 594000, 15, N'DAT_BAN', 1)
GO
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH000', N'Khách Vãng Lai', NULL, 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH031124001', N'Huỳnh Nam', N'0919104794', 385)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH121024019', N'Đỗ Thị Sang', N'0936469061', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH140224001', N'Lê Thành Giang', N'0390821110', 26)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH141024020', N'Đỗ Thị Sang', N'0708903202', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH150624002', N'Lê Thành Giang', N'0889631484', 5)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH191024001', N'Tống Diệm', N'0323456789', 100)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH201024018', N'Võ Thị Mến', N'0871668578', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH230924008', N'Nguyễn Yến Ngọc', N'0700465131', 450)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024003', N'Trần Thị Bình', N'0900508584', 92)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024004', N'Lê Thành Giang', N'0312420566', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024005', N'Nguyễn Thị Em', N'0871785060', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024007', N'Nguyễn Thị Em', N'0720235118', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024009', N'Nguyễn Hữu Cường', N'0829896244', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024010', N'Đỗ Thị Sang', N'0378584246', 150)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024011', N'Lê Hoàng Chung', N'0899252331', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024012', N'Hoàng Văn Phước', N'0917130027', 0)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024013', N'Nguyễn Hữu Cường', N'0835513295', 50)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024014', N'Trần Văn Liêm', N'0705825430', 10)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024015', N'Doàn Thị Hạnh', N'0543500258', 20)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH231024016', N'Lê Hoàng Chung', N'0705634418', 30)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH240424017', N'Nguyễn Quỳnh Anh', N'0373445232', 216)
INSERT [dbo].[KhachHang] ([maKH], [tenKH], [sDT], [diemTichLuy]) VALUES (N'KH300424006', N'Chung Thoại Nga', N'0896223693', 100)
GO
INSERT [dbo].[KhuyenMai] ([maKM], [tenKM], [ngayHetHan], [ngayBatDau], [phanTramGiam]) VALUES (N'KM001', N'Ngày phụ nữ', CAST(N'2024-10-21' AS Date), CAST(N'2024-10-20' AS Date), 0.1)
INSERT [dbo].[KhuyenMai] ([maKM], [tenKM], [ngayHetHan], [ngayBatDau], [phanTramGiam]) VALUES (N'KM002', N'Noel-Giáng sinh', CAST(N'2024-12-30' AS Date), CAST(N'2024-12-01' AS Date), 0.1)
INSERT [dbo].[KhuyenMai] ([maKM], [tenKM], [ngayHetHan], [ngayBatDau], [phanTramGiam]) VALUES (N'KM003', N'Ngày khai trương', CAST(N'2024-10-14' AS Date), CAST(N'2024-10-13' AS Date), 0.2)
INSERT [dbo].[KhuyenMai] ([maKM], [tenKM], [ngayHetHan], [ngayBatDau], [phanTramGiam]) VALUES (N'KM004', N'Ngày Valentine', CAST(N'2024-02-15' AS Date), CAST(N'2024-02-14' AS Date), 0.1)
INSERT [dbo].[KhuyenMai] ([maKM], [tenKM], [ngayHetHan], [ngayBatDau], [phanTramGiam]) VALUES (N'KM005', N'Ngày Quốc Khánh', CAST(N'2024-09-03' AS Date), CAST(N'2024-09-02' AS Date), 0.05)
GO
INSERT [dbo].[LoaiMonAn] ([maLoai], [tenLoai]) VALUES (N'AC', N'Rượu bia')
INSERT [dbo].[LoaiMonAn] ([maLoai], [tenLoai]) VALUES (N'DR', N'Đồ uống')
INSERT [dbo].[LoaiMonAn] ([maLoai], [tenLoai]) VALUES (N'FO', N'Thức ăn')
GO
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC01', N'Bia Sài Gòn Lager ', 12000, N'SANCO', 10, N'AC', N'Rượu bia', N'sgLarger.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC02', N'Bia Sài Gòn Export', 13000, N'SANCO', 10, N'AC', N'Rượu bia', N'sgExpert.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC03', N'Bia Sài Gòn Chill', 18000, N'SANCO', 10, N'AC', N'Rượu bia', N'sgChill.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC04', N'Bia Sài Gòn Special Sleek', 16000, N'SANCO', 10, N'AC', N'Rượu bia', N'sgSpecial.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC05', N'Bia Tiger Platinum', 21000, N'SANCO', 10, N'AC', N'Rượu bia', N'tigerPlatium.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC06', N'Bia Tiger Bạc', 15000, N'SANCO', 10, N'AC', N'Rượu bia', N'tigerBac.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC07', N'Rượu Glenlivet 1824 Founders Reserve Whisky', 1000000, N'SANCO', 10, N'AC', N'Rượu bia', N'glenlive.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC08', N'Vang F Gold Limited Edition', 1680000, N'SANCO', 10, N'AC', N'Rượu bia', N'vangGoldF.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC09', N'Rượu Cheers Gin Blue Floral 750ml - 40%', 380000, N'SANCO', 10, N'AC', N'Rượu bia', N'cheersBlue.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'AC10', N'Rượu Don Julio Blanco Tequila (750ml)', 1550000, N'SANCO', 10, N'AC', N'Rượu bia', N'donJulio.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'DR01', N'Pepsi', 20000, N'SANCO', 10, N'DR', N'Đồ uống', N'pep.jpg')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'DR02', N'Coca', 20000, N'SANCO', 10, N'DR', N'Đồ uống', N'coca-cola.jpg')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'DR03', N'Mirinda cam', 15000, N'SANCO', 10, N'DR', N'Đồ uống', N'Mirinda-scaled.jpg')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'DR04', N'Sting', 20000, N'SANCO', 10, N'DR', N'Đồ uống', N'sting.jpg')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'DR05', N'Nước suối Lavie', 12000, N'SANCO', 10, N'DR', N'Đồ uống', N'lavie.jpg')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO01', N'Đặt món online nhà hàng Tân Hải Vân', 100000, N'SANCO', 8, N'FO', N'Thức ăn', N'gaRan.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO02', N'Cơm Chiên Gà Xé Cá Mặn', 35000, N'SANCO', 8, N'FO', N'Thức ăn', N'comChien.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO03', N'Hủ Tiếu Sate Bò', 150000, N'SANCO', 8, N'FO', N'Thức ăn', N'gaChienNuocMam.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO04', N'Bồ Câu Phong Sa', 250000, N'SANCO', 8, N'FO', N'Thức ăn', N'lauThaiHaiSan.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO05', N'Vịt Quay Bắc Kinh', 250000, N'SANCO', 8, N'FO', N'Thức ăn', N'tomHumSotTrungMuoi.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO06', N'Cháo Ếch Singapore', 240000, N'SANCO', 8, N'FO', N'Thức ăn', N'tomTitHapSa.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO07', N'Há Cảo Bào Ngư (2 Viên)', 250000, N'SANCO', 8, N'FO', N'Thức ăn', N'baChiBoSotCay.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO08', N'Sườn Rang Muối Tỏi', 150000, N'SANCO', 8, N'FO', N'Thức ăn', N'mucSotBoToi.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO09', N'Bánh Bao Đặc Biệt (2 cái)', 250000, N'SANCO', 8, N'FO', N'Thức ăn', N'tomNuongMoMai.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO10', N'Cua Rang Tiêu Singapore', 120000, N'SANCO', 8, N'FO', N'Thức ăn', N'chanGaRutXuongSotThai.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO11', N'Bánh Bao Trứng Muối (2 cái)', 100000, N'SANCO', 8, N'FO', N'Thức ăn', N'caLocKhoTieu.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO12', N'Bắp Bò Tiềm Thuốc Bắc', 250000, N'SANCO', 8, N'FO', N'Thức ăn', N'suonXaoChuaNgot.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO13', N'Bánh Bao Trà Xanh Cheese', 250000, N'SANCO', 8, N'FO', N'Thức ăn', N'gaNuongThanHong.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO14', N'Mì Gà Xé', 75000, N'SANCO', 8, N'FO', N'Thức ăn', N'goiGaXe.png')
INSERT [dbo].[MonAn] ([maMonAn], [tenMonAn], [gia], [trangThaiMonAn], [VAT], [maLoai], [tenLoai], [hinhAnh]) VALUES (N'FO15', N'Bánh Cuốn Chiên Sốt Tương X.O', 10000, N'SANCO', 8, N'FO', N'Thức ăn', N'vitLonSotMe.png')
GO
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [sDT], [chucVu], [trangThai], [ngayVaoLam], [ngayNghiLam], [ngaySinh], [question], [answer]) VALUES (N'NVQL24102402', N'Ngô Phước Thiện', 1, N'0812777990', N'Quản lý', N'DANG_LAM', CAST(N'2026-04-11' AS Date), NULL, CAST(N'2004-10-14' AS Date), N'Màu sắc yêu thích của bạn là gì?', N'xanh lá')
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [sDT], [chucVu], [trangThai], [ngayVaoLam], [ngayNghiLam], [ngaySinh], [question], [answer]) VALUES (N'NVTN01012401', N'Nguyễn Tuấn Đạt', 1, N'0312345678', N'Nhân viên', N'DANG_LAM', CAST(N'2026-04-01' AS Date), NULL, CAST(N'2005-11-11' AS Date), N'Màu sắc yêu thích của bạn là gì?', N'hồng')
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [sDT], [chucVu], [trangThai], [ngayVaoLam], [ngayNghiLam], [ngaySinh], [question], [answer]) VALUES (N'NVTN03112401', N'Thái Bá Cường', 0, N'0812398767', N'Nhân viên', N'DANG_LAM', CAST(N'2026-04-03' AS Date), NULL, CAST(N'2003-02-14' AS Date), N'Món uống yêu thích của bạn là gì?', N'bia hơi')
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [sDT], [chucVu], [trangThai], [ngayVaoLam], [ngayNghiLam], [ngaySinh], [question], [answer]) VALUES (N'NVTN04112401', N'Lê Hoàng Bảo', 1, N'0845036425', N'Nhân viên', N'DANG_LAM', CAST(N'2026-05-04' AS Date), NULL, CAST(N'2003-10-03' AS Date), N'Món ăn yêu thích của bạn là gì?', N'hột vịt lộn')
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [sDT], [chucVu], [trangThai], [ngayVaoLam], [ngayNghiLam], [ngaySinh], [question], [answer]) VALUES (N'NVTN18112404', N'Nguyễn Đẹp Trai', 1, N'0812999001', N'Nhân viên', N'DANG_LAM', CAST(N'2026-04-18' AS Date), NULL, CAST(N'2004-04-30' AS Date), N'Món uống yêu thích của bạn là gì?', N'rượu đế')
GO
INSERT [dbo].[TaiKhoan] ([tenDangNhap], [matKhau]) VALUES (N'NVTN01012401', N'$2a$10$Z.fFqy7jjuG297u7AhZkaeZhH1N6MsSNBbsAx6TMF2A8nLXF0XcmC')
INSERT [dbo].[TaiKhoan] ([tenDangNhap], [matKhau]) VALUES (N'NVQL24102402', N'$2a$10$6QNt2JklRIPs90/2WNR5pubF8mjJoFEM2B.m/StlV9fo1/XzE1g8q')
INSERT [dbo].[TaiKhoan] ([tenDangNhap], [matKhau]) VALUES (N'NVTN03112401', N'$2a$10$Haee5q0RgC3cYBH0ClDEzeR4rzk0t0L7DM1YsrBr6zkwv1DfT0C4G')
INSERT [dbo].[TaiKhoan] ([tenDangNhap], [matKhau]) VALUES (N'NVTN04112401', N'$2a$10$dcZKgpAhyRVRtmNNynRPkuA6BMfoxjKqlgxUBcghqWSr9dY3dhmLG')
INSERT [dbo].[TaiKhoan] ([tenDangNhap], [matKhau]) VALUES (N'NVTN18112404', N'$2a$10$aB2fVkjKYFmZC6G5zXYlJOP/uUkxy7Jw8U3ddvRS40JBD4fjnYUrq')
GO
ALTER TABLE [dbo].[ChiTietHD_MonAn]  WITH CHECK ADD FOREIGN KEY([maHD])
REFERENCES [dbo].[HoaDon] ([maHD])
GO
ALTER TABLE [dbo].[ChiTietHD_MonAn]  WITH CHECK ADD FOREIGN KEY([maMonAn])
REFERENCES [dbo].[MonAn] ([maMonAn])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([maBan])
REFERENCES [dbo].[Ban] ([maBan])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([maKH])
REFERENCES [dbo].[KhachHang] ([maKH])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([maKM])
REFERENCES [dbo].[KhuyenMai] ([maKM])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([maNV])
REFERENCES [dbo].[NhanVien] ([maNV])
GO
ALTER TABLE [dbo].[MonAn]  WITH CHECK ADD FOREIGN KEY([maLoai])
REFERENCES [dbo].[LoaiMonAn] ([maLoai])
GO
ALTER TABLE [dbo].[TaiKhoan]  WITH CHECK ADD FOREIGN KEY([tenDangNhap])
REFERENCES [dbo].[NhanVien] ([maNV])
GO
USE [master]
GO
ALTER DATABASE [QLiNhaHang] SET  READ_WRITE 
GO
