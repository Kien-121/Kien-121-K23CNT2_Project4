package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "N11NHK_chitietkho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class N11NHK_ChiTietKho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N11NHK_id")
    private Integer id;

    // Liên kết với Sản phẩm
    @ManyToOne
    @JoinColumn(name = "N11NHK_san_pham_id", referencedColumnName = "N11NHK_id")
    private N11NHK_SanPham sanPham;

    @Column(name = "N11NHK_size", length = 20)
    private String size;

    // Số lượng thực tế đang còn trong kho của size này
    @Column(name = "N11NHK_so_luong_ton")
    private Integer soLuongTon;
}