CREATE DATABASE IF NOT EXISTS N11NHK_shop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE N11NHK_shop;

-- 1. NGUOIDUNG
CREATE TABLE N11NHK_nguoidung (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_username VARCHAR(100) UNIQUE,
    N11NHK_password VARCHAR(255) NOT NULL,
    N11NHK_role ENUM('admin','staff','customer') DEFAULT 'customer',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. KHACHHANG
CREATE TABLE N11NHK_khachhang (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_user_id INT,
    N11NHK_name VARCHAR(150),
    N11NHK_email VARCHAR(150),
    N11NHK_phone VARCHAR(20),
    N11NHK_address VARCHAR(255),
    FOREIGN KEY (N11NHK_user_id) REFERENCES N11NHK_nguoidung(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 3. NHANVIEN
CREATE TABLE N11NHK_nhanvien (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_user_id INT,
    N11NHK_name VARCHAR(150),
    N11NHK_position VARCHAR(100),
    FOREIGN KEY (N11NHK_user_id) REFERENCES N11NHK_nguoidung(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 4. DANHMUC
CREATE TABLE N11NHK_danhmuc (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_name VARCHAR(100),
    N11NHK_description TEXT
);

-- 5. NHACUNGCAP
CREATE TABLE N11NHK_nhacungcap (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_name VARCHAR(150),
    N11NHK_contact VARCHAR(100),
    N11NHK_address VARCHAR(255)
);

-- 6. SANPHAM
CREATE TABLE N11NHK_sanpham (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_name VARCHAR(150),
    N11NHK_price DECIMAL(10,2),
    N11NHK_description TEXT,
    N11NHK_category_id INT,
    N11NHK_supplier_id INT,
    FOREIGN KEY (N11NHK_category_id) REFERENCES N11NHK_danhmuc(N11NHK_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (N11NHK_supplier_id) REFERENCES N11NHK_nhacungcap(N11NHK_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- 7. PRODUCT_VARIANTS
CREATE TABLE N11NHK_product_variants (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_product_id INT,
    N11NHK_size VARCHAR(20),
    N11NHK_color VARCHAR(50),
    N11NHK_stock INT DEFAULT 0,
    FOREIGN KEY (N11NHK_product_id) REFERENCES N11NHK_sanpham(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 8. GIOHANG
CREATE TABLE N11NHK_giohang (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_customer_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (N11NHK_customer_id) REFERENCES N11NHK_khachhang(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 9. CART_ITEMS
CREATE TABLE N11NHK_cart_items (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_cart_id INT,
    N11NHK_variant_id INT,
    N11NHK_quantity INT DEFAULT 1,
    FOREIGN KEY (N11NHK_cart_id) REFERENCES N11NHK_giohang(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (N11NHK_variant_id) REFERENCES N11NHK_product_variants(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 10. DONHANG
CREATE TABLE N11NHK_donhang (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_customer_id INT,
    N11NHK_order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    N11NHK_status VARCHAR(50),
    N11NHK_total DECIMAL(10,2),
    FOREIGN KEY (N11NHK_customer_id) REFERENCES N11NHK_khachhang(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 11. CHITIET_DONHANG
CREATE TABLE N11NHK_chitiet_donhang (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_order_id INT,
    N11NHK_variant_id INT,
    N11NHK_quantity INT,
    N11NHK_price DECIMAL(10,2),
    FOREIGN KEY (N11NHK_order_id) REFERENCES N11NHK_donhang(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (N11NHK_variant_id) REFERENCES N11NHK_product_variants(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 12. THANHTOAN
CREATE TABLE N11NHK_thanhtoan (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_order_id INT,
    N11NHK_method VARCHAR(50),
    N11NHK_status VARCHAR(50),
    N11NHK_paid_at DATETIME,
    FOREIGN KEY (N11NHK_order_id) REFERENCES N11NHK_donhang(N11NHK_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 13. KHUYENMAI
CREATE TABLE N11NHK_khuyenmai (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_name VARCHAR(100),
    N11NHK_discount DECIMAL(5,2),
    N11NHK_start DATE,
    N11NHK_end DATE
);

-- 14. BLOG
CREATE TABLE N11NHK_blog (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_title VARCHAR(200),
    N11NHK_content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 15. LIENHE
CREATE TABLE N11NHK_lienhe (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_name VARCHAR(150),
    N11NHK_email VARCHAR(150),
    N11NHK_message TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 16. BANNERS
CREATE TABLE N11NHK_banners (
    N11NHK_id INT AUTO_INCREMENT PRIMARY KEY,
    N11NHK_image VARCHAR(255),
    N11NHK_link VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- 1. Thêm dữ liệu Người dùng (Bảng gốc)
-- ==========================================
INSERT INTO N11NHK_nguoidung (N11NHK_username, N11NHK_password, N11NHK_role) VALUES
('admin', '$2a$10$hashedpassword123', 'admin'),
('staff1', '$2a$10$hashedpassword123', 'staff'),
('khachhang1', '$2a$10$hashedpassword123', 'customer'),
('khachhang2', '$2a$10$hashedpassword123', 'customer');

-- ==========================================
-- 2. Thêm dữ liệu Khách hàng & Nhân viên 
-- (Liên kết với N11NHK_nguoidung)
-- ==========================================
INSERT INTO N11NHK_khachhang (N11NHK_user_id, N11NHK_name, N11NHK_email, N11NHK_phone, N11NHK_address) VALUES
(3, 'Nguyễn Văn Kiên', 'kien@gmail.com', '0345785205', 'Hà Nội'),
(4, 'Trần Thị A', 'trantha@gmail.com', '0987654321', 'TP. Hồ Chí Minh');

INSERT INTO N11NHK_nhanvien (N11NHK_user_id, N11NHK_name, N11NHK_position) VALUES
(2, 'Lê Văn Nhân', 'Nhân viên kho');

-- ==========================================
-- 3. Thêm dữ liệu Danh mục & Nhà cung cấp
-- ==========================================
INSERT INTO N11NHK_danhmuc (N11NHK_name, N11NHK_description) VALUES
('Áo thun', 'Các loại áo thun cotton mát mẻ mùa hè'),
('Quần Jean', 'Quần Jean nam nữ form chuẩn'),
('Váy', 'Váy thời trang công sở và dạo phố');

INSERT INTO N11NHK_nhacungcap (N11NHK_name, N11NHK_contact, N11NHK_address) VALUES
('Xưởng may Hà Đông', '0911111111', 'Hà Đông, Hà Nội'),
('Kho sỉ Tân Bình', '0922222222', 'Tân Bình, TP. HCM');

-- ==========================================
-- 4. Thêm dữ liệu Sản phẩm & Biến thể
-- ==========================================
INSERT INTO N11NHK_sanpham (N11NHK_name, N11NHK_price, N11NHK_description, N11NHK_category_id, N11NHK_supplier_id) VALUES
('Áo thun Basic Trắng', 150000, 'Áo thun 100% cotton thoáng mát', 1, 1),
('Quần Jean Ống Rộng', 350000, 'Quần jean style Hàn Quốc', 2, 2),
('Váy hoa nhí dạo phố', 250000, 'Váy hoa nhí điệu đà', 3, 1);

INSERT INTO N11NHK_product_variants (N11NHK_product_id, N11NHK_size, N11NHK_color, N11NHK_stock) VALUES
(1, 'M', 'Trắng', 50),
(1, 'L', 'Trắng', 30),
(2, '29', 'Xanh', 40),
(2, '30', 'Xanh', 20),
(3, 'Freesize', 'Vàng', 15);

-- ==========================================
-- 5. Thêm dữ liệu Giỏ hàng & Chi tiết giỏ hàng
-- ==========================================
INSERT INTO N11NHK_giohang (N11NHK_customer_id) VALUES (1), (2);

INSERT INTO N11NHK_cart_items (N11NHK_cart_id, N11NHK_variant_id, N11NHK_quantity) VALUES
(1, 1, 2), -- Khách 1 thêm 2 áo thun Trắng size M
(1, 3, 1), -- Khách 1 thêm 1 quần Jean Xanh size 29
(2, 5, 1); -- Khách 2 thêm 1 váy hoa nhí

-- ==========================================
-- 6. Thêm dữ liệu Đơn hàng & Chi tiết đơn hàng
-- ==========================================
INSERT INTO N11NHK_donhang (N11NHK_customer_id, N11NHK_status, N11NHK_total) VALUES
(1, 'PENDING', 650000),  -- 2 áo (300k) + 1 quần (350k)
(2, 'COMPLETED', 250000); -- 1 váy

INSERT INTO N11NHK_chitiet_donhang (N11NHK_order_id, N11NHK_variant_id, N11NHK_quantity, N11NHK_price) VALUES
(1, 1, 2, 150000),
(1, 3, 1, 350000),
(2, 5, 1, 250000);

-- ==========================================
-- 7. Thêm dữ liệu Thanh toán
-- ==========================================
INSERT INTO N11NHK_thanhtoan (N11NHK_order_id, N11NHK_method, N11NHK_status, N11NHK_paid_at) VALUES
(1, 'COD', 'PENDING', NULL),
(2, 'VNPAY', 'SUCCESS', CURRENT_TIMESTAMP);

-- ==========================================
-- 8. Thêm dữ liệu Phụ (Khuyến mãi, Blog, Liên hệ, Banners)
-- ==========================================
INSERT INTO N11NHK_khuyenmai (N11NHK_name, N11NHK_discount, N11NHK_start, N11NHK_end) VALUES
('Mã giảm giá hè', 50.00, '2026-05-01', '2026-05-31'),
('Freeship toàn quốc', 30.00, '2026-01-01', '2026-12-31');

INSERT INTO N11NHK_blog (N11NHK_title, N11NHK_content) VALUES
('5 cách phối đồ cực đỉnh cho mùa hè 2026', 'Mùa hè này, hãy thử kết hợp áo thun basic và quần jean...'),
('Cách bảo quản quần áo luôn mới', 'Để quần jean không phai màu, bạn hãy lộn trái khi giặt...');

INSERT INTO N11NHK_lienhe (N11NHK_name, N11NHK_email, N11NHK_message) VALUES
('Khách vãng lai', 'guest@gmail.com', 'Shop ơi cho mình hỏi địa chỉ cửa hàng ở đâu ạ?');

INSERT INTO N11NHK_banners (N11NHK_image, N11NHK_link) VALUES
('banner_summer.png', '/danh-muc/ao-thun'),
('banner_sale.png', '/khuyen-mai');

ALTER TABLE N11NHK_blog MODIFY COLUMN N11NHK_image TEXT;
ALTER TABLE N11NHK_nguoidung MODIFY COLUMN N11NHK_role VARCHAR(50);
SET SQL_SAFE_UPDATES = 0;

UPDATE N11NHK_nguoidung SET N11NHK_role = 'ROLE_ADMIN' WHERE N11NHK_role = 'admin';
UPDATE N11NHK_nguoidung SET N11NHK_role = 'ROLE_STAFF' WHERE N11NHK_role = 'staff';
UPDATE N11NHK_nguoidung SET N11NHK_role = 'ROLE_CUSTOMER' WHERE N11NHK_role = 'customer';

ALTER TABLE N11NHK_nguoidung MODIFY COLUMN N11NHK_role ENUM('ROLE_ADMIN','ROLE_STAFF','ROLE_CUSTOMER') DEFAULT 'ROLE_CUSTOMER';

SET SQL_SAFE_UPDATES = 0;

UPDATE N11NHK_nguoidung 
SET N11NHK_password = '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a';

SET SQL_SAFE_UPDATES = 1;