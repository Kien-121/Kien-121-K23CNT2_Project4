package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "N11NHK_nguoidung")
@Data
public class N11NHK_NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N11NHK_id")
    private Integer id;

    @Column(name = "N11NHK_username", unique = true, length = 100)
    private String username;

    @Column(name = "N11NHK_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "N11NHK_role", columnDefinition = "ENUM('admin','staff','customer') DEFAULT 'customer'")
    private Role role;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // --- CÁC TRƯỜNG THÔNG TIN CÁ NHÂN ---
    @Column(name = "N11NHK_avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "N11NHK_hoten", length = 100)
    private String hoTen;

    @Column(name = "N11NHK_sodienthoai", length = 15)
    private String soDienThoai;

    @Column(name = "N11NHK_diachi", length = 255)
    private String diaChi;

    @Column(name = "N11NHK_ngaysinh")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;
    // --- TÍNH NĂNG VÍ ĐIỆN TỬ ---
    @Column(name = "N11NHK_sodu_vi")
    private Double soDuVi = 0.0; // Mặc định tài khoản mới tạo có 0đ

    // --- CỘT LƯU NGÀY ĐỔI TÊN (ĐỂ TÍNH LUẬT 3 THÁNG) ---
    @Column(name = "N11NHK_ngaydoi_username")
    private LocalDate ngayDoiUsername;

    public enum Role {
        ROLE_ADMIN, ROLE_STAFF, ROLE_CUSTOMER
    }
}