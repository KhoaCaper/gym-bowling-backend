# ✅ Setup Checklist - Gym Bowling Backend

## 🎯 **Thứ tự setup bắt buộc:**

### **BƯỚC 1: 🗄️ Database Setup**
```bash
☐ 1.1. Cài đặt SQL Server (nếu chưa có)
☐ 1.2. Chạy SQL Server Management Studio
☐ 1.3. Execute file database-setup.sql
☐ 1.4. Verify: Kiểm tra database gym_bowling đã có 10 tables
```

**Chi tiết:**
```sql
-- Mở SSMS, chạy file này:
C:\Users\Admin\gym-bowling-backend\database-setup.sql

-- Hoặc command line:
sqlcmd -S localhost -E -i database-setup.sql
```

---

### **BƯỚC 2: 🔥 Firebase Setup**
```bash
☐ 2.1. Tạo Firebase project tại console.firebase.google.com
☐ 2.2. Enable Authentication (Email/Password)
☐ 2.3. Download service account JSON file
☐ 2.4. Đổi tên thành firebase-service-account.json
☐ 2.5. Đặt file vào src/main/resources/
```

**Chi tiết theo file:** `FIREBASE_SETUP.md`

---

### **BƯỚC 3: ⚙️ Application Properties**
```bash
☐ 3.1. Cập nhật application.properties với thông tin SQL Server
☐ 3.2. Thay YOUR_TMN_CODE và YOUR_HASH_SECRET (VNPay)
☐ 3.3. Verify Firebase config path đúng
```

**File:** `src/main/resources/application.properties`
```properties
# Cập nhật những dòng này:
spring.datasource.password=YOUR_ACTUAL_SQL_PASSWORD
vnpay.tmnCode=YOUR_ACTUAL_TMN_CODE  
vnpay.hashSecret=YOUR_ACTUAL_HASH_SECRET
```

---

### **BƯỚC 4: 🚀 Test Application**
```bash
☐ 4.1. Build project: ./gradlew build
☐ 4.2. Run application: ./gradlew bootRun
☐ 4.3. Test API: http://localhost:8080/api/packages
☐ 4.4. Test Swagger: http://localhost:8080/swagger-ui.html
```

---

### **BƯỚC 5: 🔐 Admin User Setup**
```bash
☐ 5.1. Tạo admin user trong Firebase Console
☐ 5.2. Email: admin@gym.com, Password: addmini123
☐ 5.3. Copy Firebase UID từ console
☐ 5.4. Update database với UID thực
```

**Chi tiết theo file:** `ADMIN_SETUP.md`

---

### **BƯỚC 6: 🧪 Test Complete Flow**
```bash
☐ 6.1. Test Firebase login với admin user
☐ 6.2. Test API với Firebase token
☐ 6.3. Test CRUD packages (staff permission)
☐ 6.4. Test VNPay payment flow (optional)
```

---

## 🛠️ **Prerequisites (Cài đặt trước):**

### **Windows Requirements:**
```bash
☐ Java JDK 24 (đã có)
☐ SQL Server (Express/Developer edition)
☐ SQL Server Management Studio
☐ Git (để clone/push code)
☐ Docker Desktop (nếu muốn dùng Docker)
```

### **VNPay Sandbox Account:**
```bash
☐ Đăng ký tại: https://sandbox.vnpayment.vn/
☐ Lấy TMN_CODE và HASH_SECRET
☐ Test với thẻ: 9704198526191432198 (NCB)
```

---

## 🚨 **Troubleshooting Common Issues:**

### **Issue 1: SQL Server Connection Failed**
```bash
❌ Error: Login failed for user 'sa'
✅ Fix: 
   - Enable SQL Server Authentication
   - Reset sa password
   - Enable TCP/IP protocol
   - Restart SQL Server service
```

### **Issue 2: Firebase Service Account Not Found**
```bash
❌ Error: Failed to initialize Firebase
✅ Fix:
   - Check file exists: src/main/resources/firebase-service-account.json
   - Verify JSON format is valid
   - Check file permissions
```

### **Issue 3: Port 8080 Already in Use**
```bash
❌ Error: Port 8080 is already in use
✅ Fix:
   - netstat -ano | findstr :8080
   - taskkill /PID <PID> /F
   - Or change server.port=8081 in application.properties
```

### **Issue 4: Gradle Build Failed**
```bash
❌ Error: Could not resolve dependencies
✅ Fix:
   - Check internet connection
   - ./gradlew clean build
   - Delete .gradle folder and retry
```

---

## ⚡ **Quick Validation Commands:**

```bash
# Database check
sqlcmd -S localhost -E -Q "SELECT COUNT(*) FROM gym_bowling.dbo.users"

# Application health
curl http://localhost:8080/api/packages

# Firebase connection (check logs)
./gradlew bootRun | grep -i firebase

# VNPay config check
curl http://localhost:8080/swagger-ui.html
```

---

## 📋 **Final Checklist Before Deploy:**

```bash
☐ Database có data (users, packages, services)
☐ Application start thành công
☐ Swagger UI accessible
☐ Firebase authentication working
☐ Admin user có thể login
☐ API endpoints trả về data đúng
☐ No critical errors in logs
☐ Git repository up to date
```

---

## 🎯 **Estimated Time:**

- **Database Setup**: 15 phút
- **Firebase Setup**: 20 phút  
- **Application Config**: 10 phút
- **Testing**: 15 phút
- **Admin Setup**: 10 phút
- **Total**: ~70 phút

---

## 📞 **Nếu gặp vấn đề:**

1. **Check logs** trong console khi chạy `./gradlew bootRun`
2. **Verify prerequisites** đã cài đặt đúng
3. **Follow error messages** - thường có hint rõ ràng
4. **Test từng bước** thay vì chạy all-in-one

**Bắt đầu từ BƯỚC 1 nhé!** 🚀
