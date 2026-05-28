package com.shopquanao.Controller;

import com.shopquanao.Entity.N11NHK_SanPham;
import com.shopquanao.Service.N11NHK_SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Đánh dấu đây là REST API Controller
@RestController
// Đặt tiền tố /api/ để không bị trùng với các đường dẫn của giao diện web
@RequestMapping("/api/admin/sanpham")
@RequiredArgsConstructor
public class N11NHK_AdminApiController {

    private final N11NHK_SanPhamService sanPhamService;


    // 1. API Lấy danh sách toàn bộ sản phẩm (Test bằng GET trên Postman)
    @GetMapping
    public ResponseEntity<List<N11NHK_SanPham>> getAllSanPhamAPI() {
        List<N11NHK_SanPham> danhSach = sanPhamService.getAllSanPham();
        return new ResponseEntity<>(danhSach, HttpStatus.OK);
    }

    // 2. API Lấy thông tin 1 sản phẩm theo ID (Test bằng GET)
    @GetMapping("/{id}")
    public ResponseEntity<N11NHK_SanPham> getSanPhamByIdAPI(@PathVariable Integer id) {
        N11NHK_SanPham sp = sanPhamService.getSanPhamById(id);
        if (sp != null) {
            return new ResponseEntity<>(sp, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Trả về lỗi 404 nếu không thấy
    }

    // 3. API Thêm mới sản phẩm (Test bằng POST)
    @PostMapping
    public ResponseEntity<N11NHK_SanPham> addSanPhamAPI(@RequestBody N11NHK_SanPham sanPhamMoi) {
        N11NHK_SanPham savedSP = sanPhamService.saveSanPham(sanPhamMoi);
        return new ResponseEntity<>(savedSP, HttpStatus.CREATED); // Trả về mã 201 Created
    }

    // Bạn có thể tự viết thêm các hàm @PutMapping (Sửa) và @DeleteMapping (Xóa) ở đây...
}