package com.shopquanao.Controller;

import com.shopquanao.Dto.N11NHK_RegisterDTO;
import com.shopquanao.Entity.N11NHK_Banner;
import com.shopquanao.Entity.N11NHK_ChiTietKho;
import com.shopquanao.Entity.N11NHK_SanPham;
import com.shopquanao.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class N11NHK_HomeController {

    private final N11NHK_SanPhamService sanPhamService;
    private final N11NHK_DanhMucService danhMucService;
    private final N11NHK_NguoiDungService nguoiDungService;
    private final N11NHK_TinTucService tinTucService;
    private final N11NHK_KhuyenMaiService khuyenMaiService;

    // Repositories quản lý Kho và Banner
    private final com.shopquanao.Repository.N11NHK_ChiTietKhoRepository chiTietKhoRepository;
    private final com.shopquanao.Repository.N11NHK_BannerRepository bannerRepository;

    // ==========================================
    // XỬ LÝ TRANG CHỦ
    // ==========================================
    @GetMapping({"/", "/trang-chu"})
    public String trangChu(Model model) {

        // 1. PHẦN DỮ LIỆU CŨ (Nếu thiếu phần này web sẽ trống trơn)
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("danhSachSanPham", sanPhamService.getAllSanPham());
        model.addAttribute("danhSachTinTucGiaoDien", tinTucService.getDanhSachTinTucMoiNhat());
        model.addAttribute("danhSachVoucher", khuyenMaiService.getAllKhuyenMai());

        // 2. PHẦN BANNER MỚI (Lấy riêng Banner cho từng vị trí)
        List<N11NHK_Banner> sliderBanners = bannerRepository.findByViTriAndTrangThaiTrue("SLIDER");
        List<N11NHK_Banner> tinTucBanners = bannerRepository.findByViTriAndTrangThaiTrue("TINTUC");
        List<N11NHK_Banner> khuyenMaiBanners = bannerRepository.findByViTriAndTrangThaiTrue("KHUYENMAI");

        model.addAttribute("sliderBanners", sliderBanners);
        model.addAttribute("tinTucBanners", tinTucBanners);
        model.addAttribute("khuyenMaiBanners", khuyenMaiBanners);

        return "index";
    }

    @GetMapping("/login")
    public String trangDangNhap() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new N11NHK_RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerProcess(@ModelAttribute("registerDTO") N11NHK_RegisterDTO dto, Model model) {
        try {
            nguoiDungService.registerNewCustomer(dto);
            return "redirect:/login?registerSuccess";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // ==========================================
    // TÌM KIẾM & LỌC SẢN PHẨM
    // ==========================================

    @GetMapping("/danhmuc/{id}")
    public String getProductsByCategory(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("danhSachSanPham", sanPhamService.getSanPhamByDanhMuc(id));
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        return "products";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("danhSachSanPham", sanPhamService.searchSanPhamByName(keyword));
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("keyword", keyword);
        return "products";
    }

    @GetMapping("/sanpham/{id}")
    public String xemChiTietSanPham(@PathVariable("id") Integer id, Model model) {
        N11NHK_SanPham sanPham = sanPhamService.getSanPhamById(id);
        if (sanPham == null) return "redirect:/";

        model.addAttribute("sanPham", sanPham);
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());

        // Truyền danh sách Size thực tế trong kho ra Giao diện khách
        List<N11NHK_ChiTietKho> danhSachSize = chiTietKhoRepository.findBySanPham(sanPham);
        model.addAttribute("danhSachSize", danhSachSize);

        if (sanPham.getDanhMuc() != null) {
            model.addAttribute("sanPhamLienQuan", sanPhamService.getSanPhamByDanhMuc(sanPham.getDanhMuc().getId()));
        }
        return "sanpham-detail";
    }

    @GetMapping("/products")
    public String trangTatCaSanPham(Model model) {
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("danhSachSanPham", sanPhamService.getAllSanPham());
        return "products";
    }

    // ==========================================
    // TRANG VOUCHER
    // ==========================================
    @GetMapping("/voucher")
    public String trangVoucher(Model model) {
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        var danhSachKM = khuyenMaiService.getAllKhuyenMai();
        model.addAttribute("danhSachVoucher", danhSachKM);
        return "voucher";
    }

    // ==========================================
    // TIN TỨC
    // ==========================================
    @GetMapping("/tintuc")
    public String trangDanhSachTinTuc(Model model) {
        model.addAttribute("danhSachTinTuc", tinTucService.getDanhSachTinTucMoiNhat());
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        return "tintuc";
    }

    @GetMapping("/tintuc/{id}")
    public String xemChiTietTinTuc(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("tinTuc", tinTucService.getTinTucById(id));
        model.addAttribute("danhSachDanhMuc", danhMucService.getAllDanhMuc());
        return "tintuc-detail";
    }
}