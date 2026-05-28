package com.shopquanao.Repository;

import com.shopquanao.Entity.N11NHK_ChiTietKho;
import com.shopquanao.Entity.N11NHK_SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface N11NHK_ChiTietKhoRepository extends JpaRepository<N11NHK_ChiTietKho, Integer> {

    // Tìm tồn kho của 1 size cụ thể trong 1 sản phẩm
    N11NHK_ChiTietKho findBySanPhamAndSize(N11NHK_SanPham sanPham, String size);

    // Lấy toàn bộ danh sách các size và số lượng tồn của 1 sản phẩm (để hiển thị ra web)
    List<N11NHK_ChiTietKho> findBySanPham(N11NHK_SanPham sanPham);
}