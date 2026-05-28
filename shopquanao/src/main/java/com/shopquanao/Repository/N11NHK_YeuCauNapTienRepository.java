package com.shopquanao.Repository;

import com.shopquanao.Entity.N11NHK_NguoiDung;
import com.shopquanao.Entity.N11NHK_YeuCauNapTien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface N11NHK_YeuCauNapTienRepository extends JpaRepository<N11NHK_YeuCauNapTien, Integer> {

    // Tìm tất cả yêu cầu nạp tiền của 1 user cụ thể, sắp xếp ID giảm dần (Giao dịch mới nhất lên đầu)
    List<N11NHK_YeuCauNapTien> findByUserOrderByIdDesc(N11NHK_NguoiDung user);

}