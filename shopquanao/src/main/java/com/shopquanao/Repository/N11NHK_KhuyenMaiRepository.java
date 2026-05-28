package com.shopquanao.Repository;

import com.shopquanao.Entity.N11NHK_KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface N11NHK_KhuyenMaiRepository extends JpaRepository<N11NHK_KhuyenMai, Integer> {

    // Thêm hàm này để hệ thống biết cách tìm Khuyến mãi theo tên (mã code)
    Optional<N11NHK_KhuyenMai> findByName(String name);

}