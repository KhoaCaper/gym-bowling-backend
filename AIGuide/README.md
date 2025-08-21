# Gym Bowling Backend

Backend API cho ứng dụng Gym Bowling với Firebase Authentication, VNPay payment và CRUD gói dịch vụ.

## 🚀 Tính năng

- ✅ Firebase Authentication (Login/Register)
- ✅ CRUD gói dịch vụ (Staff/Admin)
- ✅ VNPay sandbox integration
- ✅ SQL Server database
- ✅ Role-based authorization

## 🛠 Setup

### 1. Database Setup
```sql
-- Tạo database trong SQL Server
CREATE DATABASE gym_bowling;
```

### 2. Application Configuration
Cập nhật `src/main/resources/application.properties`:

```properties
# SQL Server (thay đổi password phù hợp)
spring.datasource.password=YOUR_SQL_SERVER_PASSWORD

# VNPay (đăng ký tại https://sandbox.vnpayment.vn/)
vnpay.tmnCode=YOUR_TMN_CODE
vnpay.hashSecret=YOUR_HASH_SECRET
```

### 3. Firebase Setup
1. Tạo project tại [Firebase Console](https://console.firebase.google.com/)
2. Tải file `firebase-service-account.json`
3. Đặt file vào `src/main/resources/`

### 4. Chạy ứng dụng
```bash
./gradlew bootRun
```

## 📝 API Endpoints

### Authentication
```
POST /api/auth/login
POST /api/auth/register
GET  /api/auth/me
```

### Packages (Public)
```
GET  /api/packages          # Lấy danh sách gói active
GET  /api/packages/{id}     # Lấy thông tin gói
```

### Staff/Admin - Package Management
```
GET    /api/staff/packages           # Lấy tất cả gói
POST   /api/staff/packages           # Tạo gói mới
PUT    /api/staff/packages/{id}      # Cập nhật gói
DELETE /api/staff/packages/{id}      # Xóa gói
PATCH  /api/staff/packages/{id}/toggle-status  # Bật/tắt gói
```

### Payment
```
POST /api/payment/create-order       # Tạo đơn hàng và link thanh toán
GET  /api/payment/vnpay-return       # VNPay callback
GET  /api/payment/orders             # Lịch sử đơn hàng user
```

## 🔐 Authentication

### Frontend Login Example
```javascript
// Sau khi login Firebase ở frontend
const idToken = await user.getIdToken();

fetch('/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    token: idToken,
    phone: '0123456789'  // optional
  })
});
```

### API Calls với Authentication
```javascript
fetch('/api/payment/create-order', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${idToken}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    packagePlanId: 1
  })
});
```

## 💳 VNPay Testing

### Test Cards
- **Thành công**: 9704198526191432198 (NCB)
- **Thất bại**: 9704198526191432199 (NCB)
- OTP: 123456

### Test Flow
1. Gọi `/api/payment/create-order` với `packagePlanId`
2. Redirect user tới `paymentUrl` trả về
3. Hoàn thành thanh toán trên VNPay sandbox
4. VNPay redirect về `/api/payment/vnpay-return`

## 📊 Database Schema

```sql
users (id, firebase_uid, email, full_name, phone, role, created_at)
package_plans (id, name, description, price, duration_months, is_active, created_at)
orders (id, user_id, package_plan_id, total_amount, status, order_date)
payments (id, order_id, amount, payment_method, transaction_id, status, payment_date, vnpay_response)
```

## 🎯 Checkpoint Items

- [x] **Database**: SQL Server với JPA entities
- [x] **Github**: Code đã commit và push
- [x] **Deploy/Docker**: Ready for containerization
- [x] **Code base**: Clean architecture với Spring Boot

## 🔧 Troubleshooting

### Firebase Issues
- Kiểm tra file `firebase-service-account.json` có đúng vị trí
- Verify Firebase project settings

### VNPay Issues  
- Kiểm tra TMN_CODE và HASH_SECRET
- Test với sandbox environment trước

### Database Issues
- Kiểm tra SQL Server đang chạy
- Verify connection string và credentials
