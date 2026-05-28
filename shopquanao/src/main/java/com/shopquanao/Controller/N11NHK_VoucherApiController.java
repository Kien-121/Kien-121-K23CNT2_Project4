package com.shopquanao.Controller;

import com.shopquanao.Entity.N11NHK_KhuyenMai;
import com.shopquanao.Repository.N11NHK_KhuyenMaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/voucher")
@RequiredArgsConstructor
public class N11NHK_VoucherApiController {

    private final N11NHK_KhuyenMaiRepository khuyenMaiRepository;

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkVoucher(@RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();
        Optional<N11NHK_KhuyenMai> khuyenMaiOpt = khuyenMaiRepository.findByName(code);

        if (khuyenMaiOpt.isPresent()) {
            N11NHK_KhuyenMai km = khuyenMaiOpt.get();
            // Kiểm tra hạn sử dụng
            if (km.getEndDate() != null && km.getEndDate().isBefore(LocalDate.now())) {
                response.put("valid", false);
                response.put("message", "Mã giảm giá đã hết hạn!");
                return ResponseEntity.ok(response);
            }

            response.put("valid", true);
            response.put("discount", km.getDiscount()); // Trả về phần trăm giảm
            response.put("message", "Áp dụng mã thành công!");
        } else {
            response.put("valid", false);
            response.put("message", "Mã giảm giá không tồn tại!");
        }
        return ResponseEntity.ok(response);
    }
}