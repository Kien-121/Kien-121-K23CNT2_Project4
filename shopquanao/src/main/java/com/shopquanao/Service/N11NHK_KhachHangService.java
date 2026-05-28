package com.shopquanao.Service;

import com.shopquanao.Entity.N11NHK_KhachHang;
import com.shopquanao.Repository.N11NHK_KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class N11NHK_KhachHangService {

    private final N11NHK_KhachHangRepository khachHangRepository;

    // Hàm lấy toàn bộ danh sách khách hàng
    public List<N11NHK_KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }

    // Sau này bạn có thể viết thêm các hàm như lấy theo ID, xóa khách hàng ở đây...
}