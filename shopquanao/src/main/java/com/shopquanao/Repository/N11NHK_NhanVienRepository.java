package com.shopquanao.Repository;

import com.shopquanao.Entity.N11NHK_NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface N11NHK_NhanVienRepository extends JpaRepository<N11NHK_NhanVien, Integer> {
}