package com.shopquanao.Repository;

import com.shopquanao.Entity.N11NHK_PhieuNhapKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface N11NHK_PhieuNhapKhoRepository extends JpaRepository<N11NHK_PhieuNhapKho, Integer> {
}