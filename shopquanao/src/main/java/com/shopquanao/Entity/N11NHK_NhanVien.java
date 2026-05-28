package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "N11NHK_nhanvien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class N11NHK_NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N11NHK_id")
    private Integer id;

    // Liên kết 1-1 hoặc N-1 với tài khoản người dùng (N11NHK_nguoidung)
    @ManyToOne
    @JoinColumn(name = "N11NHK_user_id", referencedColumnName = "N11NHK_id")
    private N11NHK_NguoiDung user;

    @Column(name = "N11NHK_name", length = 150)
    private String name; // Khớp với nv.name ngoài HTML

    @Column(name = "N11NHK_position", length = 100)
    private String position; // Khớp với nv.position ngoài HTML
}