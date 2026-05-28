package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "N11NHK_khuyenmai")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class N11NHK_KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N11NHK_id")
    private Integer id;

    @Column(name = "N11NHK_name", length = 100)
    private String name;

    @Column(name = "N11NHK_discount")
    private Double discount;

    // Dùng @DateTimeFormat để Spring Boot hiểu được ngày tháng gửi từ Form HTML lên
    @Column(name = "N11NHK_start")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "N11NHK_end")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}