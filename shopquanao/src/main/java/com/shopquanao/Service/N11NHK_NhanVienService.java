package com.shopquanao.Service;

import com.shopquanao.Entity.N11NHK_NhanVien;
import com.shopquanao.Repository.N11NHK_NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class N11NHK_NhanVienService {

    private final N11NHK_NhanVienRepository nhanVienRepository;

    public List<N11NHK_NhanVien> getAllNhanVien() {
        return nhanVienRepository.findAll();
    }

    // THÊM 3 HÀM NÀY VÀO:
    public N11NHK_NhanVien getNhanVienById(Integer id) {
        return nhanVienRepository.findById(id).orElse(null);
    }

    public void saveNhanVien(N11NHK_NhanVien nhanVien) {
        nhanVienRepository.save(nhanVien);
    }

    public void deleteNhanVien(Integer id) {
        nhanVienRepository.deleteById(id);
    }
}