# 💳 VNPay Sandbox Setup Guide

## 🎯 **Bước 1: Đăng ký VNPay Sandbox**

### **1.1. Truy cập VNPay Sandbox**
- URL: https://sandbox.vnpayment.vn/
- Click **"Đăng ký tài khoản"**

### **1.2. Thông tin đăng ký**
```
Loại tài khoản: Doanh nghiệp (Business)
Tên doanh nghiệp: Gym Bowling App
Email: your-email@gmail.com
Số điện thoại: 0123456789
```

### **1.3. Xác thực tài khoản**
- Check email để xác thực
- Login vào sandbox dashboard

## 🔑 **Bước 2: Lấy API Credentials**

### **2.1. Trong VNPay Dashboard:**
1. Vào **"Cấu hình"** → **"API"**
2. Copy các thông tin:
   - **TMN Code** (Terminal Code)
   - **Hash Secret** (Secret Key)

### **2.2. Cập nhật application.properties:**
```properties
# VNPay Sandbox Configuration
vnpay.tmnCode=YOUR_ACTUAL_TMN_CODE
vnpay.hashSecret=YOUR_ACTUAL_HASH_SECRET
vnpay.payUrl=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.returnUrl=http://localhost:8080/api/payment/vnpay-return
```

## 🧪 **Bước 3: Test Cards VNPay Sandbox**

### **3.1. Thẻ test thành công:**
```
Ngân hàng: NCB (National Citizen Bank)
Số thẻ: 9704198526191432198
Tên chủ thẻ: NGUYEN VAN A
Ngày hết hạn: 07/15
OTP: 123456
```

### **3.2. Thẻ test thất bại:**
```
Ngân hàng: NCB
Số thẻ: 9704198526191432199
Tên chủ thẻ: NGUYEN VAN A  
Ngày hết hạn: 07/15
OTP: 123456
```

### **3.3. Các ngân hàng khác:**
```
Vietcombank: 9704061674622801
BIDV: 9704054736731904
Techcombank: 9704062701620093
Sacombank: 9704081487271139
```

## 🔧 **Bước 4: Test Payment Flow**

### **4.1. API Test với Postman:**
```bash
POST http://localhost:8080/api/payment/create-order
Headers:
  Authorization: Bearer YOUR_FIREBASE_TOKEN
  Content-Type: application/json
Body:
{
  "packagePlanId": 1
}
```

### **4.2. Response sẽ có:**
```json
{
  "orderId": 123,
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...",
  "message": "Order created successfully"
}
```

### **4.3. Test Payment:**
1. Copy `paymentUrl` vào browser
2. Chọn ngân hàng NCB
3. Nhập thẻ test: `9704198526191432198`
4. OTP: `123456`
5. Xác nhận thanh toán

## ⚠️ **Lưu ý quan trọng:**

### **Security:**
- **KHÔNG** commit TMN_CODE và HASH_SECRET vào Git
- Sử dụng environment variables cho production

### **Return URL:**
- URL phải accessible từ internet (không dùng localhost cho production)
- VNPay sẽ gọi callback về URL này

### **Testing:**
- Chỉ dùng trong môi trường sandbox
- Không dùng thẻ thật
- Kiểm tra logs để debug

## 🚨 **Troubleshooting:**

### **1. Invalid TMN_CODE:**
```
Error: TMN_CODE không hợp lệ
Fix: Kiểm tra lại TMN_CODE trong VNPay dashboard
```

### **2. Invalid Signature:**
```
Error: Chữ ký không hợp lệ
Fix: Kiểm tra HASH_SECRET và cách tạo signature
```

### **3. Return URL không accessible:**
```
Error: VNPay không callback được
Fix: Sử dụng ngrok hoặc deploy lên server public
```

## 📋 **Quick Setup Commands:**

```bash
# 1. Cập nhật config
vim src/main/resources/application.properties

# 2. Restart application
./gradlew bootRun

# 3. Test API
curl http://localhost:8080/api/packages

# 4. Test Swagger
# http://localhost:8080/swagger-ui.html
```

## 🔗 **Useful Links:**

- VNPay Sandbox: https://sandbox.vnpayment.vn/
- VNPay Documentation: https://sandbox.vnpayment.vn/apis/
- Test Cards: https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html#danh-sach-ngan-hang

**Sau khi setup VNPay, payment flow sẽ hoạt động hoàn chỉnh!** 💳
