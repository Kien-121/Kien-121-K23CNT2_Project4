package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "N11NHK_phieunhapkho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class N11NHK_PhieuNhapKho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N11NHK_id")
    private Integer id;

    // Liên kết với bảng Sản phẩm
    @ManyToOne
    @JoinColumn(name = "N11NHK_san_pham_id", referencedColumnName = "N11NHK_id")
    private N11NHK_SanPham sanPham;

    // Quần áo thì bắt buộc phải nhập theo Size
    @Column(name = "N11NHK_size", length = 20)
    private String size;

    @Column(name = "N11NHK_so_luong_nhap")
    private Integer soLuongNhap;

    @Column(name = "N11NHK_ngay_nhap")
    private LocalDateTime ngayNhap;

    // Lưu lại tài khoản Admin/Staff nào đã thực hiện nhập lô hàng này
    @Column(name = "N11NHK_nguoi_nhap", length = 50)
    private String nguoiNhap;
}