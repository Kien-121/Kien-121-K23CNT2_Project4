package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "N11NHK_sanpham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class N11NHK_SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N11NHK_id")
    private Integer id;

    @Column(name = "N11NHK_name", length = 150)
    private String name;

    @Column(name = "N11NHK_price")
    private BigDecimal price; // Sử dụng BigDecimal cho tiền tệ (DECIMAL)

    @Column(name = "N11NHK_description", columnDefinition = "TEXT")
    private String description;

    // Thêm trường này vào Entity N11NHK_SanPham
    @Column(name = "sizes")
    private String sizes; // Lưu trữ dạng chuỗi: "S, M, L, XL"

    // Nhớ tạo thêm Getter và Setter cho trường sizes này nhé!
    public String getSizes() { return sizes; }
    public void setSizes(String sizes) { this.sizes = sizes; }

    // Quan hệ Nhiều-1 với Danh Mục
    @ManyToOne
    @JoinColumn(name = "N11NHK_category_id")
    private N11NHK_DanhMuc danhMuc;

    // Quan hệ Nhiều-1 với Nhà Cung Cấp
    // (Giả sử bạn sẽ tạo entity NhaCungCap sau, tạm thời ta để khóa ngoại kiểu Integer hoặc Mapping trực tiếp)
    @Column(name = "N11NHK_supplier_id")
    private Integer supplierId;

    @Column(name = "N11NHK_image", columnDefinition = "TEXT")
    private String image;
}