package com.shopquanao.Controller;

import com.shopquanao.Entity.N11NHK_NguoiDung;
import com.shopquanao.Entity.N11NHK_YeuCauNapTien;
import com.shopquanao.Repository.N11NHK_DonHangRepository;
import com.shopquanao.Repository.N11NHK_NguoiDungRepository;
import com.shopquanao.Repository.N11NHK_YeuCauNapTienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/tai-khoan")
@RequiredArgsConstructor
public class N11NHK_TaiKhoanController {

    private final N11NHK_NguoiDungRepository nguoiDungRepository;
    private final N11NHK_DonHangRepository donHangRepository;

    // Kho dữ liệu Yêu cầu nạp tiền
    private final N11NHK_YeuCauNapTienRepository yeuCauNapTienRepository;

    @GetMapping
    public String hienThiTaiKhoan(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        N11NHK_NguoiDung currentUser = nguoiDungRepository.findByUsername(principal.getName()).orElse(null);
        if (currentUser == null) return "redirect:/login";

        model.addAttribute("user", currentUser);
        model.addAttribute("lichSuDonHang", donHangRepository.findByUserOrderByIdDesc(currentUser));

        // ĐÃ SỬA: Đẩy danh sách lịch sử nạp tiền từ DB ra giao diện HTML
        model.addAttribute("lichSuNapTien", yeuCauNapTienRepository.findByUserOrderByIdDesc(currentUser));

        return "tai-khoan";
    }

    @PostMapping("/cap-nhat")
    public String capNhatThongTin(
            @ModelAttribute("user") N11NHK_NguoiDung userForm,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) return "redirect:/login";
        N11NHK_NguoiDung currentUser = nguoiDungRepository.findByUsername(principal.getName()).orElse(null);
        if (currentUser == null) return "redirect:/login";

        // 1. TẢI ẢNH TỪ MÁY LÊN
        if (!avatarFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                try (InputStream inputStream = avatarFile.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    currentUser.setAvatar("/uploads/" + fileName);
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi tải ảnh lên hệ thống!");
                return "redirect:/tai-khoan";
            }
        }

        // 2. KIỂM TRA ĐỔI USERNAME
        String newUsername = userForm.getUsername();
        if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(currentUser.getUsername())) {

            if (currentUser.getNgayDoiUsername() != null) {
                long monthsBetween = ChronoUnit.MONTHS.between(currentUser.getNgayDoiUsername(), LocalDate.now());
                if (monthsBetween < 3) {
                    redirectAttributes.addFlashAttribute("error", "❌ Thất bại: Bạn chỉ được đổi Tên đăng nhập 3 tháng một lần!");
                    return "redirect:/tai-khoan";
                }
            }

            if (nguoiDungRepository.findByUsername(newUsername).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "❌ Thất bại: Tên đăng nhập này đã có người sử dụng!");
                return "redirect:/tai-khoan";
            }

            currentUser.setUsername(newUsername);
            currentUser.setNgayDoiUsername(LocalDate.now());

            luuThongTinChung(currentUser, userForm);
            return "redirect:/logout";
        }

        // 3. LƯU CÁC THÔNG TIN CÁ NHÂN
        luuThongTinChung(currentUser, userForm);
        redirectAttributes.addFlashAttribute("success", "✅ Đã lưu thông tin cá nhân thành công!");
        return "redirect:/tai-khoan";
    }

    private void luuThongTinChung(N11NHK_NguoiDung dbUser, N11NHK_NguoiDung formUser) {
        dbUser.setHoTen(formUser.getHoTen());
        dbUser.setSoDienThoai(formUser.getSoDienThoai());
        dbUser.setDiaChi(formUser.getDiaChi());
        dbUser.setNgaySinh(formUser.getNgaySinh());
        nguoiDungRepository.save(dbUser);
    }

    // ==========================================
    // XỬ LÝ GỬI YÊU CẦU NẠP TIỀN
    // ==========================================
    @PostMapping("/nap-tien")
    public String napTienVaoVi(@RequestParam("soTienNap") Double soTienNap,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";
        N11NHK_NguoiDung currentUser = nguoiDungRepository.findByUsername(principal.getName()).orElse(null);

        if (currentUser != null && soTienNap > 0) {
            // TẠO YÊU CẦU NẠP TIỀN CHỜ ADMIN DUYỆT
            N11NHK_YeuCauNapTien yeuCau = new N11NHK_YeuCauNapTien();
            yeuCau.setUser(currentUser);
            yeuCau.setSoTien(soTienNap);
            yeuCau.setTrangThai("CHỜ DUYỆT"); // Bạn có thể chuyển thành SUCCESS nếu muốn tiền vào luôn
            yeuCau.setNgayTao(LocalDateTime.now());

            yeuCauNapTienRepository.save(yeuCau);

            redirectAttributes.addFlashAttribute("success", "✅ Yêu cầu nạp " + String.format("%,.0f", soTienNap) + " đ đã được gửi! Vui lòng chờ Admin xét duyệt.");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Số tiền nạp không hợp lệ!");
        }

        return "redirect:/tai-khoan";
    }
}