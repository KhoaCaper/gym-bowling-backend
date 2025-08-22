# 🚀 Railway Deployment Guide - GYM BOWLING BACKEND

## 📋 **DEPLOYMENT PROCESS (ONE TIME ONLY)**

### **🎯 Mục tiêu:**
- Deploy ứng dụng lên Railway **một lần duy nhất**
- Tránh push liên tục và logs rối rắm
- Cấu hình database và health check đúng cách

---

## 🔧 **BƯỚC 1: CHUẨN BỊ LOCAL**

### **Chạy deployment script:**
```bash
# Windows
deploy.bat

# Linux/Mac
chmod +x deploy.sh
./deploy.sh
```

### **Script sẽ tự động:**
1. ✅ Kiểm tra Git status
2. ✅ Add tất cả thay đổi
3. ✅ Commit với message rõ ràng
4. ✅ Push lên GitHub
5. ✅ Hiển thị next steps

---

## 🌐 **BƯỚC 2: RAILWAY DASHBOARD**

### **2.1. Kiểm tra PostgreSQL Service:**
- Vào **Railway Dashboard**
- Kiểm tra xem có **PostgreSQL service** nào không
- Nếu chưa có → **New Service** → **Database** → **PostgreSQL**

### **2.2. Link Database với App:**
- Click vào **PostgreSQL service**
- Copy **Connection URL**
- Vào **App service** → **Variables**
- Thêm các biến sau:

```bash
# REQUIRED: Database Connection
DATABASE_URL=postgresql://username:password@host:port/database

# OPTIONAL: Individual components (if DATABASE_URL fails)
PGHOST=host
PGPORT=port
PGDATABASE=database
PGUSER=username
PGPASSWORD=password

# REQUIRED: App Configuration
JWT_SECRET=your-super-secret-jwt-key-here-make-it-very-long-and-secure
RAILWAY_DOMAIN=your-app-name.up.railway.app
FIREBASE_ENABLED=false
```

---

## 🔍 **BƯỚC 3: MONITORING**

### **3.1. Deployment Logs:**
- Đợi **build** hoàn tất
- Kiểm tra **deployment logs**
- Tìm lỗi **database connection**

### **3.2. Health Check:**
- Test endpoint: `https://your-app.up.railway.app/ping`
- Expected response: `"pong"`
- Nếu OK → App đã chạy thành công

---

## 🚨 **TROUBLESHOOTING**

### **Vấn đề thường gặp:**

#### **1. Database Connection Failed:**
```
Driver org.postgresql.Driver claims to not accept jdbcUrl
```
**Giải pháp:** Kiểm tra `DATABASE_URL` trong Railway Variables

#### **2. Health Check Failed:**
```
Healthcheck failed! service unavailable
```
**Giải pháp:** Đợi app khởi động hoàn tất (có thể mất 2-3 phút)

#### **3. Build Failed:**
```
failed to build: process did not complete successfully
```
**Giải pháp:** Kiểm tra `gradlew` permissions và Java version

---

## ✅ **SUCCESS CRITERIA**

### **App được deploy thành công khi:**
1. ✅ **Build** hoàn tất không lỗi
2. ✅ **Health check** `/ping` trả về `"pong"`
3. ✅ **Database** kết nối thành công (nếu có)
4. ✅ **Logs** không có error nghiêm trọng

---

## 📝 **IMPORTANT NOTES**

### **❌ KHÔNG BAO GIỜ:**
- Push code liên tục
- Thay đổi cấu hình sau khi deploy
- Restart service không cần thiết

### **✅ LUÔN LUÔN:**
- Test local trước khi deploy
- Sử dụng deployment script
- Monitor logs sau khi deploy
- Backup database trước khi thay đổi

---

## 🎯 **NEXT STEPS AFTER DEPLOYMENT**

1. **Test API endpoints** với Postman/Insomnia
2. **Verify database tables** được tạo đúng
3. **Check Firebase integration** (nếu cần)
4. **Monitor performance** và logs
5. **Setup monitoring** (nếu cần)

---

## 📞 **SUPPORT**

Nếu gặp vấn đề:
1. Kiểm tra **Railway logs** trước
2. Xem **GitHub Actions** (nếu có)
3. Kiểm tra **environment variables**
4. Restart service nếu cần thiết

---

**🎉 Chúc bạn deploy thành công!**
