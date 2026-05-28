package com.shopquanao.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "N11NHK_blog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class N11NHK_TinTuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N11NHK_id")
    private Integer id;

    @Column(name = "N11NHK_title", length = 200)
    private String title;

    // ĐÃ KHÔI PHỤC: Cột chứa link ảnh bìa bài viết
    // Xóa cái length = 500 đi, đổi thành columnDefinition = "TEXT"
    @Column(name = "N11NHK_image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "N11NHK_content", columnDefinition = "TEXT")
    private String content;

    // Khóa ngày đăng để khi SỬA không bị lỗi mất ngày giờ hoặc sai định dạng
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}