package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "N11NHK_banner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class N11NHK_Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N11NHK_id")
    private Integer id;

    @Column(name = "N11NHK_ten_banner", length = 150)
    private String tenBanner;

    @Column(name = "N11NHK_hinh_anh", length = 255)
    private String hinhAnh;

    // true = Đang hiển thị trên trang chủ, false = Đã ẩn
    @Column(name = "N11NHK_trang_thai")
    private Boolean trangThai = true;

    // Thêm trường vị trí để phân loại banner
    @Column(name = "N11NHK_vi_tri", length = 50)
    private String viTri; // Lưu giá trị: "SLIDER", "TINTUC", "KHUYENMAI"
}