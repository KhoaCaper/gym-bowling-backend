# 🚀 QUICK START - Gym Bowling Backend (NO SECURITY)

## ⚡ CÁCH NHANH NHẤT ĐỂ TEST API

### 1. Chạy Spring Boot App
- Mở IntelliJ
- Chạy `GymBowlingBackendApplication.java`
- Đợi: "Started GymBowlingBackendApplication in X.XXX seconds"

### 2. Test Local (nhanh nhất)
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Test API**: http://localhost:8080/api/users

### 3. Chạy Ngrok (cho FE team)
```bash
ngrok http 8080
# Copy URL mới (ví dụ: https://abc123.ngrok-free.app)
```

### 4. Cập nhật SwaggerConfig.java
```java
// Thay YOUR_NEW_NGROK_URL bằng URL thực tế
new Server().url("https://abc123.ngrok-free.app").description("Ngrok HTTPS Server")
```

### 5. Share với FE team
- **Swagger**: https://abc123.ngrok-free.app/swagger-ui.html
- **API Base**: https://abc123.ngrok-free.app/api

## ✅ ĐÃ DISABLE GÌ
- ❌ Spring Security
- ❌ CORS restrictions  
- ❌ Firebase Auth
- ❌ JWT Auth
- ❌ Tất cả filters

## 🎯 KẾT QUẢ
- **100% API accessible** - không cần auth
- **Không có CORS issues**
- **Test ngay lập tức** - không cần setup gì thêm

## 🚨 Nếu vẫn lỗi
1. Restart Spring Boot app
2. Kiểm tra database connection
3. Xem log trong IntelliJ console
