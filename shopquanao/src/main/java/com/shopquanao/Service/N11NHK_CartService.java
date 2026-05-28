package com.shopquanao.Service;

import com.shopquanao.Dto.CartItemDTO;
import com.shopquanao.Entity.N11NHK_SanPham;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope // ĐIỂM CỐT LÕI: Mỗi khách hàng (mỗi trình duyệt) sẽ có một "giỏ" riêng biệt
public class N11NHK_CartService {

    // Danh sách các món hàng đang có trong giỏ
    private List<CartItemDTO> cartItems = new ArrayList<>();

    // 1. Lấy toàn bộ hàng trong giỏ
    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    // 2. Thêm một sản phẩm vào giỏ (ĐÃ CẬP NHẬT BIẾN SIZE)
    public void addToCart(N11NHK_SanPham sanPham, int quantity, String size) {
        // Kiểm tra xem sản phẩm này CÙNG SIZE đã có trong giỏ chưa
        for (CartItemDTO item : cartItems) {
            // Phải trùng cả ID sản phẩm VÀ trùng kích cỡ (size) thì mới cộng dồn số lượng
            if (item.getSanPham().getId().equals(sanPham.getId()) &&
                    ((item.getSize() == null && size == null) || (item.getSize() != null && item.getSize().equals(size)))) {

                // Nếu có rồi thì chỉ tăng số lượng
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Nếu chưa có (hoặc cùng áo nhưng khác size) thì thêm dòng mới vào danh sách
        cartItems.add(new CartItemDTO(sanPham, quantity, size));
    }

    // 3. Xóa một món khỏi giỏ
    // (Tạm thời xóa theo ID sản phẩm. Nếu khách thêm nhiều size của cùng 1 áo, thao tác này sẽ xóa tất cả các size của áo đó)
    public void removeFromCart(Integer sanPhamId) {
        cartItems.removeIf(item -> item.getSanPham().getId().equals(sanPhamId));
    }

    // 4. Xóa sạch giỏ hàng (Dùng sau khi thanh toán xong)
    public void clearCart() {
        cartItems.clear();
    }

    // 5. Tính tổng tiền cả giỏ
    public double getTotalPrice() {
        double total = 0;
        for (CartItemDTO item : cartItems) {
            total += item.getSubTotal();
        }
        return total;
    }
}