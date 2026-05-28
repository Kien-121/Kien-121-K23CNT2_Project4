package com.shopquanao.Service;

import com.shopquanao.Entity.N11NHK_TinTuc;
import com.shopquanao.Repository.N11NHK_TinTucRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class N11NHK_TinTucService {

    private final N11NHK_TinTucRepository tinTucRepository;

    public List<N11NHK_TinTuc> getAllTinTuc() {
        return tinTucRepository.findAll();
    }

    public N11NHK_TinTuc getTinTucById(Integer id) {
        return tinTucRepository.findById(id).orElse(null);
    }

    public void saveTinTuc(N11NHK_TinTuc tinTuc) {
        tinTucRepository.save(tinTuc);
    }

    public void deleteTinTuc(Integer id) {
        tinTucRepository.deleteById(id);
    }
    public List<N11NHK_TinTuc> getDanhSachTinTucMoiNhat() {
        // Lấy tất cả bài viết nhưng sắp xếp theo thứ tự ID từ lớn đến bé
        return tinTucRepository.findAll().stream()
                .sorted((t1, t2) -> t2.getId().compareTo(t1.getId()))
                .toList();
    }

}