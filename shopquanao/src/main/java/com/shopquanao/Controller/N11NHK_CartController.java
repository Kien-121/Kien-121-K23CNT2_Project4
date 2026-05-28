package com.shopquanao.Controller;

import com.shopquanao.Dto.CartItemDTO;
import com.shopquanao.Entity.N11NHK_ChiTietDonHang;
import com.shopquanao.Entity.N11NHK_DonHang;
import com.shopquanao.Entity.N11NHK_NguoiDung;
import com.shopquanao.Entity.N11NHK_SanPham;
import com.shopquanao.Repository.N11NHK_DonHangRepository;
import com.shopquanao.Repository.N11NHK_NguoiDungRepository;
import com.shopquanao.Service.N11NHK_CartService;
import com.shopquanao.Service.N11NHK_SanPhamService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class N11NHK_CartController {

    private final N11NHK_CartService cartService;
    private final N11NHK_SanPhamService sanPhamService;

    // REPOSITORY ĐỂ TƯƠNG TÁC VỚI DATABASE
    private final N11NHK_DonHangRepository donHangRepository;
    private final N11NHK_NguoiDungRepository nguoiDungRepository;

    // ==============================================================
    // HÀM HỖ TRỢ: CẬP NHẬT MINI CART LÊN SESSION DÀNH CHO HEADER
    // ==============================================================
    private void updateMiniCartSession(HttpSession session) {
        List<CartItemDTO> cartItems = cartService.getCartItems();
        int tongSoLuong = 0;
        for (CartItemDTO item : cartItems) {
            tongSoLuong += item.getQuantity();
        }
        // Bơm dữ liệu vào Session để file Header.html có thể đọc được ở mọi trang
        session.setAttribute("danhSachGioHang", cartItems);
        session.setAttribute("tongSoLuongGioHang", tongSoLuong);
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        updateMiniCartSession(session); // Cập nhật lại session phòng khi bị reset
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Integer productId,
                            @RequestParam("quantity") int quantity,
                            @RequestParam(value = "size", required = false, defaultValue = "") String size,
                            HttpSession session) {
        N11NHK_SanPham sp = sanPhamService.getSanPhamById(productId);
        if (sp != null) {
            // ĐÃ FIX: Truyền đầy đủ 3 tham số (sản phẩm, số lượng, size) vào Service
            cartService.addToCart(sp, quantity, size);
        }

        // Gọi hàm cập nhật Mini Cart sau khi thêm thành công
        updateMiniCartSession(session);

        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") Integer productId, HttpSession session) {
        cartService.removeFromCart(productId);

        // Gọi hàm cập nhật Mini Cart sau khi xóa
        updateMiniCartSession(session);

        return "redirect:/cart";
    }

    // ==============================================================
    // TRANG THANH TOÁN (GET)
    // ==============================================================
    @GetMapping("/checkout")
    public String showCheckoutForm(Model model, Principal principal) {
        // Bắt buộc đăng nhập
        if (principal == null) {
            return "redirect:/login";
        }
        // Giỏ hàng trống thì quay lại
        if (cartService.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        // 1. Lấy thông tin user để hiển thị SĐT, Địa chỉ, Số dư ví ra HTML
        N11NHK_NguoiDung user = nguoiDungRepository.findByUsername(principal.getName()).orElse(null);
        model.addAttribute("user", user);

        // 2. Truyền danh sách giỏ hàng và tổng tiền
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("tongTien", cartService.getTotalPrice());

        return "checkout";
    }

    // ==============================================================
    // XỬ LÝ ĐẶT HÀNG (POST)
    // ==============================================================
    @PostMapping("/checkout/xac-nhan")
    public String processCheckout(
            @RequestParam("diaChi") String diaChi,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam("phuongThucThanhToan") String phuongThucThanhToan,
            @RequestParam("tongTienThucTe") Double tongTienThucTe,
            Principal principal,
            RedirectAttributes redirectAttributes,
            HttpSession session) { // Thêm tham số HttpSession vào đây

        if (principal == null) {
            return "redirect:/login";
        }

        N11NHK_NguoiDung user = nguoiDungRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // ================= XỬ LÝ LOGIC THANH TOÁN =================
        String trangThaiThanhToan = "CHƯA THANH TOÁN";

        // NẾU KHÁCH CHỌN TRẢ BẰNG VÍ N11NHK
        if ("WALLET".equals(phuongThucThanhToan)) {
            Double soDuHienTai = user.getSoDuVi() != null ? user.getSoDuVi() : 0.0;

            // Kiểm tra xem ví có đủ tiền không
            if (soDuHienTai < tongTienThucTe) {
                redirectAttributes.addFlashAttribute("error", "Số dư ví không đủ! Vui lòng nạp thêm hoặc chọn phương thức khác.");
                return "redirect:/checkout";
            }

            // Đủ tiền -> Trừ tiền trong ví và lưu lại
            user.setSoDuVi(soDuHienTai - tongTienThucTe);
            nguoiDungRepository.save(user);
            trangThaiThanhToan = "ĐÃ THANH TOÁN (VÍ N11NHK)";
        }
        // CÁC PHƯƠNG THỨC KHÁC HIỂN THỊ QR
        else if ("BANK".equals(phuongThucThanhToan) || "MOMO".equals(phuongThucThanhToan) || "ZALOPAY".equals(phuongThucThanhToan)) {
            trangThaiThanhToan = "CHỜ CHUYỂN KHOẢN (" + phuongThucThanhToan + ")";
        }
        else {
            trangThaiThanhToan = "CHƯA THANH TOÁN (COD)";
        }

        // ================= TẠO HÓA ĐƠN MỚI =================
        N11NHK_DonHang donHang = new N11NHK_DonHang();
        donHang.setUser(user);
        donHang.setNgayDatHang(LocalDateTime.now());
        donHang.setTongTien(tongTienThucTe); // Tiền này là tiền ĐÃ TRỪ VOUCHER
        donHang.setTrangThai("Chờ xác nhận");

        // Gắn trạng thái thanh toán vào đơn hàng để HTML nhận diện được mã QR
        donHang.setTrangThaiThanhToan(trangThaiThanhToan);

        // Chuyển CartItem thành Chi tiết đơn hàng
        List<N11NHK_ChiTietDonHang> danhSachChiTiet = new ArrayList<>();
        for (CartItemDTO item : cartService.getCartItems()) {
            N11NHK_ChiTietDonHang chiTiet = new N11NHK_ChiTietDonHang();
            chiTiet.setDonHang(donHang);
            chiTiet.setSanPham(item.getSanPham());
            chiTiet.setSoLuong(item.getQuantity());
            chiTiet.setGiaLucMua(Double.valueOf(item.getSanPham().getPrice().toString()));
            danhSachChiTiet.add(chiTiet);
        }

        donHang.setChiTietDonHangs(danhSachChiTiet);

        // Lưu Hóa đơn vào CSDL
        donHangRepository.save(donHang);

        // Xóa giỏ hàng sau khi thanh toán thành công
        cartService.clearCart();

        // Gọi hàm cập nhật Mini Cart (lúc này giỏ hàng đã bị clear nên sẽ reset về 0)
        updateMiniCartSession(session);

        // Chuyển hướng mang theo ID Đơn hàng
        return "redirect:/checkout/success?orderId=" + donHang.getId();
    }

    // ==============================================================
    // TRANG BÁO THÀNH CÔNG VÀ HIỂN THỊ QR (GET)
    // ==============================================================
    @GetMapping("/checkout/success")
    public String checkoutSuccess(@RequestParam(value = "orderId", required = false) Integer orderId, Model model) {
        if (orderId != null) {
            N11NHK_DonHang donHang = donHangRepository.findById(orderId).orElse(null);
            model.addAttribute("donHang", donHang);
        }
        return "checkout-success";
    }
}