package com.shopquanao.Controller;

import com.shopquanao.Entity.*;
import com.shopquanao.Repository.N11NHK_DonHangRepository;
import com.shopquanao.Repository.N11NHK_NguoiDungRepository;
import com.shopquanao.Repository.N11NHK_YeuCauNapTienRepository;
import com.shopquanao.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class N11NHK_AdminController {

    private final N11NHK_SanPhamService sanPhamService;
    private final N11NHK_DanhMucService danhMucService;
    private final N11NHK_DonHangService donHangService;
    private final N11NHK_DonHangRepository donHangRepository;
    private final N11NHK_KhachHangService khachHangService;
    private final N11NHK_NhanVienService nhanVienService;
    private final N11NHK_TinTucService tinTucService;
    private final N11NHK_KhuyenMaiService khuyenMaiService;

    private final N11NHK_YeuCauNapTienRepository yeuCauNapTienRepository;
    private final N11NHK_NguoiDungRepository nguoiDungRepository;

    // Repositories cho Nhập Kho và Banner
    private final com.shopquanao.Repository.N11NHK_PhieuNhapKhoRepository phieuNhapKhoRepository;
    private final com.shopquanao.Repository.N11NHK_BannerRepository bannerRepository;

    // Thêm Repository Quản lý tồn kho chi tiết
    private final com.shopquanao.Repository.N11NHK_ChiTietKhoRepository chiTietKhoRepository;

    // CÔNG CỤ MÃ HÓA MẬT KHẨU CỦA SPRING SECURITY
    private final PasswordEncoder passwordEncoder;

    @GetMapping({"", "/"})
    public String chuyenHuongVeDashboard() {
        return "redirect:/admin/dashboard";
    }

    // ==========================================
    // 1. DASHBOARD THỐNG KÊ (CHO PHÉP ADMIN VÀ STAFF)
    // ==========================================
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/dashboard")
    public String trangDashboard(Model model) {
        Double tongThu = donHangRepository.sumAllRevenue();
        if (tongThu == null) tongThu = 0.0;
        Double tongChi = tongThu * 0.6;
        Double loiNhuan = tongThu - tongChi;
        model.addAttribute("tongThu", tongThu);
        model.addAttribute("tongChi", tongChi);
        model.addAttribute("loiNhuan", loiNhuan);
        model.addAttribute("soDonHang", donHangRepository.countAllOrders());
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }

    // ==========================================
    // 2. QUẢN LÝ SẢN PHẨM
    // ==========================================
    @GetMapping("/sanpham")
    public String trangQuanLySanPham(Model model) {
        model.addAttribute("danhSachSanPham", sanPhamService.getAllSanPham());
        model.addAttribute("activePage", "sanpham");
        return "admin/sanpham-list";
    }

    @GetMapping("/sanpham/add")
    public String hienThiFormThemSanPham(Model model) {
        model.addAttribute("sanPham", new N11NHK_SanPham());
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("activePage", "sanpham");
        return "admin/sanpham-form";
    }

    @GetMapping("/sanpham/edit/{id}")
    public String hienThiFormSuaSanPham(@PathVariable Integer id, Model model) {
        N11NHK_SanPham sanPhamEdit = sanPhamService.getSanPhamById(id);
        if (sanPhamEdit == null) return "redirect:/admin/sanpham";
        model.addAttribute("sanPham", sanPhamEdit);
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("activePage", "sanpham");
        return "admin/sanpham-form";
    }

    @PostMapping("/sanpham/save")
    public String luuSanPham(@ModelAttribute("sanPham") N11NHK_SanPham sanPham) {
        sanPhamService.saveSanPham(sanPham);
        return "redirect:/admin/sanpham?success";
    }

    @GetMapping("/sanpham/delete/{id}")
    public String deleteSanPham(@PathVariable("id") Integer id) {
        sanPhamService.deleteSanPham(id);
        return "redirect:/admin/sanpham?deleted";
    }

    // ==========================================
    // 3. QUẢN LÝ DANH MỤC
    // ==========================================
    @GetMapping("/danhmuc")
    public String trangQuanLyDanhMuc(Model model) {
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("activePage", "danhmuc");
        return "admin/danhmuc-list";
    }

    @GetMapping("/danhmuc/add")
    public String hienThiFormThemDanhMuc(Model model) {
        model.addAttribute("danhMuc", new N11NHK_DanhMuc());
        model.addAttribute("activePage", "danhmuc");
        return "admin/danhmuc-form";
    }

    @PostMapping("/danhmuc/save")
    public String luuDanhMuc(@ModelAttribute("danhMuc") N11NHK_DanhMuc danhMuc) {
        danhMucService.saveDanhMuc(danhMuc);
        return "redirect:/admin/danhmuc?success";
    }

    @GetMapping("/danhmuc/delete/{id}")
    public String deleteDanhMuc(@PathVariable("id") Integer id) {
        try {
            danhMucService.deleteDanhMuc(id);
            return "redirect:/admin/danhmuc?deleted";
        } catch (Exception e) {
            return "redirect:/admin/danhmuc?error";
        }
    }

    // ==========================================
    // 4. QUẢN LÝ ĐƠN HÀNG
    // ==========================================
    @GetMapping("/donhang")
    public String trangQuanLyDonHang(Model model, Principal principal) {
        model.addAttribute("danhSachDonHang", donHangService.getAllDonHang());
        model.addAttribute("activePage", "donhang");

        if (principal != null) {
            String username = principal.getName();
            LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
            Double doanhThuCaNhan = donHangRepository.sumDoanhThuTheoNguoiDuyetTrongNgay(username, startOfToday);
            if (doanhThuCaNhan == null) doanhThuCaNhan = 0.0;
            model.addAttribute("doanhThuCaNhan", doanhThuCaNhan);
            model.addAttribute("tenNhanVien", username);
        }
        return "admin/donhang-list";
    }

    @PostMapping("/donhang/update-status")
    public String updateOrderStatus(@RequestParam("orderId") Integer id,
                                    @RequestParam("status") String trangThai,
                                    Principal principal) {
        String nguoiDuyet = (principal != null) ? principal.getName() : "Unknown";
        N11NHK_DonHang donHang = donHangRepository.findById(id).orElse(null);
        if (donHang != null) {
            donHang.setTrangThai(trangThai);
            donHang.setNguoiDuyet(nguoiDuyet);
            donHang.setNgayDuyet(LocalDateTime.now());
            donHangRepository.save(donHang);
        }
        return "redirect:/admin/donhang?success";
    }

    @GetMapping("/donhang/delete/{id}")
    public String xoaDonHang(@PathVariable Integer id) {
        donHangService.deleteDonHang(id);
        return "redirect:/admin/donhang?deleted";
    }

    // ==========================================
    // 5. QUẢN LÝ KHÁCH HÀNG
    // ==========================================
    @GetMapping("/khachhang")
    public String trangQuanLyKhachHang(Model model) {
        model.addAttribute("danhSachKhachHang", khachHangService.getAllKhachHang());
        model.addAttribute("activePage", "khachhang");
        return "admin/khachhang-list";
    }

    // ==========================================
    // 6. QUẢN LÝ NHÂN VIÊN
    // ==========================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nhanvien")
    public String trangQuanLyNhanVien(Model model) {
        model.addAttribute("danhSachNhanVien", nhanVienService.getAllNhanVien());
        model.addAttribute("activePage", "nhanvien");
        return "admin/nhanvien-list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nhanvien/add")
    public String hienThiFormThemNhanVien(Model model) {
        model.addAttribute("nhanVien", new N11NHK_NhanVien());
        model.addAttribute("activePage", "nhanvien");
        return "admin/nhanvien-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nhanvien/edit/{id}")
    public String hienThiFormSuaNhanVien(@PathVariable Integer id, Model model) {
        model.addAttribute("nhanVien", nhanVienService.getNhanVienById(id));
        model.addAttribute("activePage", "nhanvien");
        return "admin/nhanvien-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nhanvien/save")
    public String luuNhanVien(@ModelAttribute("nhanVien") N11NHK_NhanVien nhanVien,
                              @RequestParam(value = "new_username", required = false) String username,
                              @RequestParam(value = "new_password", required = false) String password) {

        // 1. Kiểm tra và xử lý phần TÀI KHOẢN trước
        if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {

            N11NHK_NguoiDung checkTonTai = nguoiDungRepository.findByUsername(username).orElse(null);

            if (checkTonTai == null) {
                // Nếu chưa có thì tạo tài khoản mới
                N11NHK_NguoiDung taiKhoanMoi = new N11NHK_NguoiDung();
                taiKhoanMoi.setUsername(username.trim());

                // MÃ HÓA MẬT KHẨU TẠI ĐÂY
                taiKhoanMoi.setPassword(passwordEncoder.encode(password.trim()));

                // Gán quyền STAFF
                taiKhoanMoi.setRole(N11NHK_NguoiDung.Role.ROLE_STAFF);

                // Lưu tài khoản vào DB để hệ thống tự cấp ID
                taiKhoanMoi = nguoiDungRepository.save(taiKhoanMoi);

                // Gắn tài khoản vừa tạo vào hồ sơ nhân viên
                nhanVien.setUser(taiKhoanMoi);
            } else {
                // Nếu lỡ nhập trùng tên tài khoản đã có sẵn, thì tự động móc nối luôn tài khoản đó
                nhanVien.setUser(checkTonTai);
            }
        }

        // 2. Lưu hồ sơ NHÂN VIÊN sau cùng
        nhanVienService.saveNhanVien(nhanVien);

        return "redirect:/admin/nhanvien?success";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nhanvien/delete/{id}")
    public String xoaNhanVien(@PathVariable Integer id) {
        nhanVienService.deleteNhanVien(id);
        return "redirect:/admin/nhanvien?deleted";
    }

    // ==========================================
    // 7. QUẢN LÝ TIN TỨC
    // ==========================================
    @GetMapping("/tintuc")
    public String trangQuanLyTinTuc(Model model) {
        model.addAttribute("danhSachTinTuc", tinTucService.getAllTinTuc());
        model.addAttribute("activePage", "tintuc");
        return "admin/tintuc-list";
    }

    @GetMapping("/tintuc/add")
    public String hienThiFormThemTinTuc(Model model) {
        model.addAttribute("tinTuc", new N11NHK_TinTuc());
        model.addAttribute("activePage", "tintuc");
        return "admin/tintuc-form";
    }

    @GetMapping("/tintuc/edit/{id}")
    public String hienThiFormSuaTinTuc(@PathVariable Integer id, Model model) {
        model.addAttribute("tinTuc", tinTucService.getTinTucById(id));
        model.addAttribute("activePage", "tintuc");
        return "admin/tintuc-form";
    }

    @PostMapping("/tintuc/save")
    public String luuTinTuc(@ModelAttribute("tinTuc") N11NHK_TinTuc tinTuc) {
        try {
            if (tinTuc.getId() != null) {
                N11NHK_TinTuc baiVietCu = tinTucService.getTinTucById(tinTuc.getId());
                if (baiVietCu != null) {
                    baiVietCu.setTitle(tinTuc.getTitle());
                    baiVietCu.setImage(tinTuc.getImage());
                    baiVietCu.setContent(tinTuc.getContent());
                    tinTucService.saveTinTuc(baiVietCu);
                    return "redirect:/admin/tintuc?success";
                }
            }
            tinTucService.saveTinTuc(tinTuc);
            return "redirect:/admin/tintuc?success";
        } catch (Exception e) {
            return "redirect:/admin/tintuc?error=" + e.getClass().getSimpleName();
        }
    }

    @GetMapping("/tintuc/delete/{id}")
    public String xoaTinTuc(@PathVariable Integer id) {
        tinTucService.deleteTinTuc(id);
        return "redirect:/admin/tintuc?deleted";
    }

    // ==========================================
    // 8. QUẢN LÝ VOUCHER
    // ==========================================
    @GetMapping("/voucher")
    public String trangQuanLyVoucher(Model model) {
        model.addAttribute("danhSachVoucher", khuyenMaiService.getAllKhuyenMai());
        model.addAttribute("activePage", "voucher");
        return "admin/voucher-list";
    }

    @GetMapping("/voucher/add")
    public String hienThiFormThemVoucher(Model model) {
        model.addAttribute("voucher", new N11NHK_KhuyenMai());
        model.addAttribute("activePage", "voucher");
        return "admin/voucher-form";
    }

    @GetMapping("/voucher/edit/{id}")
    public String hienThiFormSuaVoucher(@PathVariable Integer id, Model model) {
        model.addAttribute("voucher", khuyenMaiService.getKhuyenMaiById(id));
        model.addAttribute("activePage", "voucher");
        return "admin/voucher-form";
    }

    @PostMapping("/voucher/save")
    public String luuVoucher(@ModelAttribute("voucher") N11NHK_KhuyenMai khuyenMai) {
        khuyenMaiService.saveKhuyenMai(khuyenMai);
        return "redirect:/admin/voucher?success";
    }

    @GetMapping("/voucher/delete/{id}")
    public String xoaVoucher(@PathVariable Integer id) {
        khuyenMaiService.deleteKhuyenMai(id);
        return "redirect:/admin/voucher?deleted";
    }

    // ==========================================
    // 9. QUẢN LÝ XÉT DUYỆT NẠP TIỀN
    // ==========================================
    @GetMapping("/naptien")
    public String trangQuanLyNapTien(Model model) {
        model.addAttribute("danhSachYeuCau", yeuCauNapTienRepository.findAll());
        model.addAttribute("activePage", "naptien");
        return "admin/naptien-list";
    }

    @GetMapping("/naptien/duyet/{id}")
    public String duyetNapTien(@PathVariable Integer id) {
        N11NHK_YeuCauNapTien yeuCau = yeuCauNapTienRepository.findById(id).orElse(null);
        if(yeuCau != null && "CHỜ DUYỆT".equals(yeuCau.getTrangThai())) {
            yeuCau.setTrangThai("ĐĐÃ DUYỆT");
            yeuCauNapTienRepository.save(yeuCau);
            N11NHK_NguoiDung khachHang = yeuCau.getUser();
            Double soDuHienTai = khachHang.getSoDuVi() != null ? khachHang.getSoDuVi() : 0.0;
            khachHang.setSoDuVi(soDuHienTai + yeuCau.getSoTien());
            nguoiDungRepository.save(khachHang);
        }
        return "redirect:/admin/naptien?success";
    }

    @GetMapping("/naptien/tuchoi/{id}")
    public String tuChoiNapTien(@PathVariable Integer id) {
        N11NHK_YeuCauNapTien yeuCau = yeuCauNapTienRepository.findById(id).orElse(null);
        if(yeuCau != null && "CHỜ DUYỆT".equals(yeuCau.getTrangThai())) {
            yeuCau.setTrangThai("TỪ CHỐI");
            yeuCauNapTienRepository.save(yeuCau);
        }
        return "redirect:/admin/naptien?rejected";
    }

    // ==========================================
    // 10. QUẢN LÝ NHẬP KHO VÀ TỒN KHO
    // ==========================================
    @GetMapping("/kho")
    public String trangQuanLyKho(Model model) {
        model.addAttribute("danhSachPhieuNhap", phieuNhapKhoRepository.findAll());
        model.addAttribute("activePage", "kho");
        return "admin/kho-list";
    }

    @GetMapping("/kho/add")
    public String hienThiFormNhapKho(Model model) {
        model.addAttribute("phieuNhap", new N11NHK_PhieuNhapKho());
        model.addAttribute("danhSachSanPham", sanPhamService.getAllSanPham());
        model.addAttribute("activePage", "kho");
        return "admin/kho-form";
    }

    @PostMapping("/kho/save")
    public String luuPhieuNhapKho(@ModelAttribute("phieuNhap") N11NHK_PhieuNhapKho phieuNhap, Principal principal) {
        String nguoiNhap = (principal != null) ? principal.getName() : "Unknown";
        phieuNhap.setNguoiNhap(nguoiNhap);
        phieuNhap.setNgayNhap(LocalDateTime.now());

        // 1. Lưu lại lịch sử nhập kho
        phieuNhapKhoRepository.save(phieuNhap);

        // 2. LOGIC CẬP NHẬT TỒN KHO THỰC TẾ
        // Kiểm tra xem sản phẩm + size này đã có trong kho chưa
        N11NHK_ChiTietKho tonKho = chiTietKhoRepository.findBySanPhamAndSize(phieuNhap.getSanPham(), phieuNhap.getSize());

        if (tonKho == null) {
            // Nếu kho chưa từng có size này -> Tạo mới
            tonKho = new N11NHK_ChiTietKho();
            tonKho.setSanPham(phieuNhap.getSanPham());
            tonKho.setSize(phieuNhap.getSize());
            tonKho.setSoLuongTon(phieuNhap.getSoLuongNhap());
        } else {
            // Nếu kho đã có size này -> Cộng dồn số lượng
            tonKho.setSoLuongTon(tonKho.getSoLuongTon() + phieuNhap.getSoLuongNhap());
        }

        chiTietKhoRepository.save(tonKho); // Cập nhật sổ cái

        return "redirect:/admin/kho?success";
    }

    @GetMapping("/kho/delete/{id}")
    public String xoaPhieuNhap(@PathVariable Integer id) {
        phieuNhapKhoRepository.deleteById(id);
        return "redirect:/admin/kho?deleted";
    }

    // ==========================================
    // 11. QUẢN LÝ BANNER QUẢNG CÁO
    // ==========================================
    @GetMapping("/banner")
    public String trangQuanLyBanner(Model model) {
        model.addAttribute("danhSachBanner", bannerRepository.findAll());
        model.addAttribute("activePage", "banner");
        return "admin/banner-list";
    }

    @GetMapping("/banner/add")
    public String hienThiFormThemBanner(Model model) {
        model.addAttribute("banner", new N11NHK_Banner());
        model.addAttribute("activePage", "banner");
        return "admin/banner-form";
    }

    @PostMapping("/banner/save")
    public String luuBanner(@ModelAttribute("banner") N11NHK_Banner banner) {
        if (banner.getTrangThai() == null) {
            banner.setTrangThai(true);
        }
        bannerRepository.save(banner);
        return "redirect:/admin/banner?success";
    }

    @GetMapping("/banner/toggle/{id}")
    public String toggleTrangThaiBanner(@PathVariable Integer id) {
        N11NHK_Banner banner = bannerRepository.findById(id).orElse(null);
        if (banner != null) {
            banner.setTrangThai(!banner.getTrangThai());
            bannerRepository.save(banner);
        }
        return "redirect:/admin/banner?success";
    }

    @GetMapping("/banner/delete/{id}")
    public String xoaBanner(@PathVariable Integer id) {
        bannerRepository.deleteById(id);
        return "redirect:/admin/banner?deleted";
    }
}