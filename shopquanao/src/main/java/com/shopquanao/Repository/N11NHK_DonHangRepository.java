package com.shopquanao.Repository;

import com.shopquanao.Entity.N11NHK_DonHang;
import com.shopquanao.Entity.N11NHK_NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface N11NHK_DonHangRepository extends JpaRepository<N11NHK_DonHang, Integer> {

    // ĐÃ SỬA CHUẨN: Tên hàm là findByUserId
    List<N11NHK_DonHang> findByUserId(Integer userId);

    List<N11NHK_DonHang> findByTrangThai(String trangThai);

    // Câu lệnh tính tổng doanh thu từ tất cả đơn hàng
    @Query("SELECT SUM(d.tongTien) FROM N11NHK_DonHang d")
    Double sumAllRevenue();

    // Đếm tổng số đơn hàng
    @Query("SELECT COUNT(d) FROM N11NHK_DonHang d")
    Long countAllOrders();

    // Tính tổng doanh thu trong ngày của 1 nhân viên cụ thể (Chỉ tính đơn đã hoàn thành)
    @org.springframework.data.jpa.repository.Query("SELECT SUM(d.tongTien) FROM N11NHK_DonHang d WHERE d.nguoiDuyet = :username AND d.ngayDuyet >= :startOfDay AND (d.trangThai = 'Hoàn thành' OR d.trangThai = 'COMPLETED')")
    Double sumDoanhThuTheoNguoiDuyetTrongNgay(@org.springframework.data.repository.query.Param("username") String username, @org.springframework.data.repository.query.Param("startOfDay") java.time.LocalDateTime startOfDay);

    // ==========================================
    // THÊM MỚI: LẤY LỊCH SỬ ĐƠN HÀNG (MỚI NHẤT LÊN ĐẦU)
    // ==========================================
    List<N11NHK_DonHang> findByUserOrderByIdDesc(N11NHK_NguoiDung user);
}