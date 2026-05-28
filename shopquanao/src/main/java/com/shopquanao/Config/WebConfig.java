package com.shopquanao.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấp phép cho web lấy ảnh từ thư mục "uploads" ở ngoài ổ cứng
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}