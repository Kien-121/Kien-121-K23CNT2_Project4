package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "N11NHK_yeucau_naptien")
@Data
public class N11NHK_YeuCauNapTien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết với tài khoản khách hàng
    @ManyToOne
    @JoinColumn(name = "user_id")
    private N11NHK_NguoiDung user;

    private Double soTien;

    @Column(length = 50)
    private String trangThai; // Sẽ có 3 trạng thái: "CHỜ DUYỆT", "ĐÃ DUYỆT", "TỪ CHỐI"

    private LocalDateTime ngayTao;
}