CREATE DATABASE QuanLyBanAo
GO
USE QuanLyBanAo
GO

-- Bảng NhânVien 
CREATE TABLE NhanVien (
    id_nhan_vien INT IDENTITY(1,1) PRIMARY KEY,
    ma_nhan_vien VARCHAR(10) NOT NULL UNIQUE,
    ho_ten NVARCHAR(100) NOT NULL,
    chuc_vu INT, -- 1: Quản lý, 2: Nhân viên
    email VARCHAR(100),
    so_dien_thoai VARCHAR(15),
    gioi_tinh INT,
    trang_thai INT,
    dia_chi NVARCHAR(200),
    ten_dang_nhap VARCHAR(30) NOT NULL UNIQUE, 
    mat_khau VARCHAR(30) NOT NULL
);

-- Bảng KhachHang
CREATE TABLE KhachHang (
    id_khach_hang INT IDENTITY(1,1) PRIMARY KEY,
    ma_khach_hang VARCHAR(10) NOT NULL UNIQUE,
    ho_ten NVARCHAR(100) NOT NULL,
    so_dien_thoai VARCHAR(15),
    email VARCHAR(100),
    dia_chi NVARCHAR(200),
    gioi_tinh INT,
    ngay_sinh DATE
);	

-- Bảng SanPham
CREATE TABLE SanPham (
    id_san_pham INT IDENTITY(1,1) PRIMARY KEY,
    ma_san_pham VARCHAR(10) NOT NULL UNIQUE,
    ten_san_pham NVARCHAR(100) NOT NULL,
    trang_thai INT
);

-- Bảng ChatLieu
CREATE TABLE ChatLieu (
    id_chat_lieu INT IDENTITY(1,1) PRIMARY KEY,
    ma_chat_lieu VARCHAR(10) NOT NULL UNIQUE,
    ten_chat_lieu NVARCHAR(100) NOT NULL
);

-- Bảng MauSac
CREATE TABLE MauSac (
    id_mau INT IDENTITY(1,1) PRIMARY KEY,
    ma_mau VARCHAR(10) NOT NULL UNIQUE,
    ten_mau NVARCHAR(50) NOT NULL
);

-- Bảng Size
CREATE TABLE Size (
    id_size INT IDENTITY(1,1) PRIMARY KEY,
    ma_size VARCHAR(10) NOT NULL UNIQUE,
    ten_size NVARCHAR(50) NOT NULL
);

-- Bảng SanPhamChiTiet
CREATE TABLE SanPhamChiTiet (
    id_ctsp INT IDENTITY(1,1) PRIMARY KEY,
    so_luong INT,
    gia_ban DECIMAL(10, 2),
    trang_thai INT,
    id_mau INT,
    id_size INT,
    id_chat_lieu INT,
    id_san_pham INT,
    FOREIGN KEY (id_mau) REFERENCES MauSac(id_mau),
    FOREIGN KEY (id_size) REFERENCES Size(id_size),
    FOREIGN KEY (id_chat_lieu) REFERENCES ChatLieu(id_chat_lieu),
    FOREIGN KEY (id_san_pham) REFERENCES SanPham(id_san_pham)
);

-- Bảng HoaDon
CREATE TABLE HoaDon (
    id_hoa_don INT IDENTITY(1,1) PRIMARY KEY,
    ma_hoa_don VARCHAR(10) NOT NULL UNIQUE,
    ngay_tao DATE,
    thanh_tien DECIMAL(15, 2),
    id_khach_hang INT,
    id_nhan_vien INT,
    trang_thai INT,
    FOREIGN KEY (id_khach_hang) REFERENCES KhachHang(id_khach_hang),
    FOREIGN KEY (id_nhan_vien) REFERENCES NhanVien(id_nhan_vien)
);

-- Bảng HoaDonChiTiet
CREATE TABLE HoaDonChiTiet (
    id_hdct INT IDENTITY(1,1) PRIMARY KEY,
    id_hoa_don INT,
    id_ctsp INT,
    so_luong INT,
    thanh_tien DECIMAL(15, 2),
    trang_thai INT,
    FOREIGN KEY (id_hoa_don) REFERENCES HoaDon(id_hoa_don),
    FOREIGN KEY (id_ctsp) REFERENCES SanPhamChiTiet(id_ctsp)
);

-- **Xóa bảng TaiKhoan vì không cần nữa**
-- (Đã không tạo bảng TaiKhoan trong script này)

-- Thêm dữ liệu mẫu cho các bảng

-- Bảng MauSac
INSERT INTO MauSac (ma_mau, ten_mau) VALUES 
('MS01', N'Đỏ'), 
('MS02', N'Xanh'), 
('MS03', N'Vàng');

-- Bảng Size
INSERT INTO Size (ma_size, ten_size) VALUES 
('SZ01', N'Nhỏ'), 
('SZ02', N'Vừa'), 
('SZ03', N'Lớn');

-- Bảng ChatLieu
INSERT INTO ChatLieu (ma_chat_lieu, ten_chat_lieu) VALUES 
('CL01', N'Cotton'), 
('CL02', N'Polyester'), 
('CL03', N'Lụa');

-- Bảng SanPham
INSERT INTO SanPham (ma_san_pham, ten_san_pham, trang_thai) VALUES 
('HD01', N'Áo Hoodie Trơn', 1), 
('HD02', N'Áo Hoodie In Hình', 1), 
('HD03', N'Áo Hoodie Oversize', 1), 
('HD04', N'Áo Hoodie Kẻ Sọc', 1), 
('HD05', N'Áo Hoodie Tay Dài', 1), 
('HD06', N'Áo Hoodie Có Mũ', 1), 
('HD07', N'Áo Hoodie Thể Thao', 1), 
('HD08', N'Áo Hoodie Phong Cách Nhật', 1), 
('HD09', N'Áo Hoodie Chống Nắng', 1), 
('HD10', N'Áo Hoodie Thun', 1);

-- Bảng SanPhamChiTiet
INSERT INTO SanPhamChiTiet (so_luong, gia_ban, trang_thai, id_mau, id_size, id_chat_lieu, id_san_pham) VALUES 
(50, 300000, 0, 1, 1, 1, 1), 
(30, 200000, 0, 2, 2, 2, 2), 
(20, 250000, 0, 3, 3, 3, 3), 
(40, 350000, 0, 1, 2, 1, 4), 
(25, 400000, 0, 2, 3, 2, 5), 
(10, 500000, 0, 3, 1, 3, 6), 
(15, 100000, 0, 1, 2, 1, 7), 
(35, 1000000, 0, 2, 3, 2, 8), 
(50, 800000, 0, 3, 1, 3, 9), 
(20, 450000, 0, 1, 2, 1, 10);

-- Bảng NhanVien (Thêm ten_dang_nhap và mat_khau từ dữ liệu cũ của TaiKhoan)
INSERT INTO NhanVien (ma_nhan_vien, ho_ten, chuc_vu, email, so_dien_thoai, gioi_tinh, trang_thai, dia_chi, ten_dang_nhap, mat_khau) VALUES
('NV01', N'Phạm Ngọc Tuấn', 1, 'tuanpnph34294@fpt.edu.vn', '0346787569', 1, 1, N'Bắc Từ Liêm, Hà Nội', 'tuanpn1501', 'ql123456@'),
('NV02', N'Lê Thị Hồng', 2, 'hongle@example.com', '0923456789', 0, 1, N'Hải Phòng', 'tranthib', 'password456'),
('NV03', N'Trần Quốc Bảo', 1, 'baotran@example.com', '0934567890', 1, 1, N'Đà Nẵng', 'levanc', 'password789'),
('NV04', N'Phạm Thanh Tú', 2, 'tupham@example.com', '0945678901', 1, 1, N'Hồ Chí Minh', 'phamthanhtu', 'password123'),
('NV05', N'Trịnh Thị Mai', 1, 'maitrinh@example.com', '0956789012', 0, 1, N'Cần Thơ', 'trinhthimai', 'password456'),
('NV06', N'Hồ Thái Dương', 2, 'duongho@example.com', '0967890123', 1, 1, N'Huế', 'hothaiduong', 'password789'),
('NV07', N'Võ Thanh Hà', 1, 'havo@example.com', '0978901234', 0, 1, N'Nghệ An', 'vothanhha', 'password123'),
('NV08', N'Nguyễn Hoàng Anh', 2, 'anhnguyen@example.com', '0989012345', 1, 1, N'Bình Dương', 'nguyenhoanganh', 'password456'),
('NV09', N'Lý Minh Khang', 1, 'khangly@example.com', '0990123456', 1, 1, N'Lâm Đồng', 'lyminhkhang', 'password789'),
('NV10', N'Tạ Quang Vinh', 2, 'vinhta@example.com', '0901234567', 1, 1, N'Hà Nam', 'taquangvinh', 'password123');

-- Bảng KhachHang
INSERT INTO KhachHang (ma_khach_hang, ho_ten, so_dien_thoai, email, dia_chi, gioi_tinh, ngay_sinh) VALUES
('KH01', N'Nguyễn Thị Mai', '0945678901', 'mainguyen@example.com', N'Hồ Chí Minh', 0, '1990-01-01'),
('KH02', N'Phạm Văn Sơn', '0956789012', 'sonpham@example.com', N'Cần Thơ', 1, '1985-05-15'),
('KH03', N'Lê Văn Hùng', '0967890123', 'hungle@example.com', N'Huế', 1, '2000-08-20'),
('KH04', N'Hoàng Thu Lan', '0978901234', 'lanhoang@example.com', N'Hà Nội', 0, '1995-03-10'),
('KH05', N'Trần Quốc Toàn', '0989012345', 'toantran@example.com', N'Nghệ An', 1, '1992-07-25'),
('KH06', N'Võ Thị Ngọc', '0990123456', 'ngocvo@example.com', N'Tây Ninh', 0, '1993-12-01'),
('KH07', N'Nguyễn Văn Tâm', '0913456789', 'tamnguyen@example.com', N'Hà Nam', 1, '1988-06-18'),
('KH08', N'Trần Thị Phượng', '0925678901', 'phuongtran@example.com', N'Quảng Ngãi', 0, '1986-11-20'),
('KH09', N'Hồ Quốc Đạt', '0936789012', 'datho@example.com', N'Hà Tĩnh', 1, '1995-04-15'),
('KH10', N'Lý Bảo Châu', '0947890123', 'chauly@example.com', N'Đà Lạt', 0, '1999-08-30');

-- Kiểm tra dữ liệu
SELECT * FROM KhachHang;
SELECT * FROM SanPham;
SELECT * FROM SanPhamChiTiet;
SELECT * FROM NhanVien;
SELECT * FROM MauSac;
SELECT * FROM HoaDon;
SELECT * FROM ChatLieu;
SELECT * FROM Size;

