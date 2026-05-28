package com.shopquanao.Service;

import com.shopquanao.Entity.N11NHK_DonHang;
import com.shopquanao.Repository.N11NHK_DonHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class N11NHK_DonHangService {

    private final N11NHK_DonHangRepository donHangRepository;

    // Lấy tất cả đơn hàng (có thể sắp xếp mới nhất lên đầu nếu muốn)
    public List<N11NHK_DonHang> getAllDonHang() {
        return donHangRepository.findAll();
    }

    // Sửa lại hàm update trạng thái để nhận thêm tham số nguoiDuyet
    public void updateTrangThai(Integer id, String trangThai, String nguoiDuyet) {
        N11NHK_DonHang donHang = donHangRepository.findById(id).orElse(null);
        if (donHang != null) {
            donHang.setTrangThai(trangThai);

            // Nếu đơn hàng được duyệt/cập nhật thì lưu vết người làm
            donHang.setNguoiDuyet(nguoiDuyet);
            donHang.setNgayDuyet(java.time.LocalDateTime.now());

            donHangRepository.save(donHang);
        }
    }
    // Thêm hàm xóa đơn hàng này vào file
    public void deleteDonHang(Integer id) {
        donHangRepository.deleteById(id);
    }
}