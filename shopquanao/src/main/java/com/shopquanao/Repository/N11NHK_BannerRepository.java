package com.shopquanao.Repository;

import com.shopquanao.Entity.N11NHK_Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface N11NHK_BannerRepository extends JpaRepository<N11NHK_Banner, Integer> {

    // Lấy danh sách banner theo vị trí (SLIDER, TINTUC, KHUYENMAI) và đang được bật (trangThai = true)
    List<N11NHK_Banner> findByViTriAndTrangThaiTrue(String viTri);

}