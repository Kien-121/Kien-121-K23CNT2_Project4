package com.shopquanao.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 1. NHÓM TỰ DO (Ai cũng xem được)
                        .requestMatchers("/", "/products", "/api/**", "/danhmuc/**", "/sanpham/**","/tintuc/**","/voucher/**", "/search", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()

                        // 2. GIỎ HÀNG: Cho phép khách vãng lai thêm đồ vào giỏ
                        .requestMatchers("/cart/**").permitAll()

                        // 3. THANH TOÁN & TÀI KHOẢN: Bắt buộc phải đăng nhập mới được thao tác
                        .requestMatchers("/checkout/**", "/tai-khoan/**").authenticated()

                        // 4. CHỨC NĂNG RIÊNG CỦA GIÁM ĐỐC (Chỉ ADMIN)
                        // Bỏ "/admin/dashboard" ra khỏi đây. Chỉ khóa mục Quản lý nhân sự
                        .requestMatchers("/admin/nhanvien/**").hasRole("ADMIN")

                        // 5. CHỨC NĂNG VẬN HÀNH CHUNG (Cả ADMIN và STAFF đều vào được)
                        // Các chức năng duyệt đơn, thêm sửa sản phẩm, và BÂY GIỜ GỒM CẢ DASHBOARD
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "STAFF")

                        // Các đường dẫn khác chưa khai báo thì bắt buộc đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true) // Đăng nhập xong về trang chủ
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Đăng xuất xong về trang chủ
                        .permitAll()
                )
                // Tạm thời tắt CSRF để dễ test form
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}