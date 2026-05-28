package com.shopquanao.Service;

import com.shopquanao.Entity.N11NHK_KhuyenMai;
import com.shopquanao.Repository.N11NHK_KhuyenMaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class N11NHK_KhuyenMaiService {

    private final N11NHK_KhuyenMaiRepository khuyenMaiRepository;

    public List<N11NHK_KhuyenMai> getAllKhuyenMai() {
        return khuyenMaiRepository.findAll();
    }

    public N11NHK_KhuyenMai getKhuyenMaiById(Integer id) {
        return khuyenMaiRepository.findById(id).orElse(null);
    }

    public void saveKhuyenMai(N11NHK_KhuyenMai khuyenMai) {
        khuyenMaiRepository.save(khuyenMai);
    }

    public void deleteKhuyenMai(Integer id) {
        khuyenMaiRepository.deleteById(id);
    }
}