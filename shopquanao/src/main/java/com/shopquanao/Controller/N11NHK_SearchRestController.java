package com.shopquanao.Controller;

import com.shopquanao.Entity.N11NHK_SanPham;
import com.shopquanao.Service.N11NHK_SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class N11NHK_SearchRestController {

    private final N11NHK_SanPhamService sanPhamService;

    // API này sẽ trả về dữ liệu dạng JSON cho JavaScript đọc
    @GetMapping("/api/search-live")
    public ResponseEntity<?> searchLive(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(List.of()); // Rỗng thì không trả về gì
        }

        // Gọi Service tìm kiếm (Giới hạn hiển thị 5 sản phẩm để dropdown không bị quá dài)
        List<N11NHK_SanPham> ketQua = sanPhamService.searchSanPhamByName(keyword);

        // Đóng gói dữ liệu thành dạng Map để tránh lỗi đụng độ Entity khi chuyển sang JSON
        var danhSachTraVe = ketQua.stream().limit(5).map(sp -> Map.of(
                "id", sp.getId(),
                "name", sp.getName(),
                "price", sp.getPrice(),
                "image", sp.getImage() != null && !sp.getImage().isEmpty() ? sp.getImage() : "https://via.placeholder.com/50"
        )).collect(Collectors.toList());

        return ResponseEntity.ok(danhSachTraVe);
    }
}