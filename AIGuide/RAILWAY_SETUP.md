# 🚀 RAILWAY DEPLOYMENT GUIDE

## 📋 **BƯỚC 1: TẠO PROJECT TRÊN RAILWAY**

1. Truy cập [railway.app](https://railway.app)
2. Đăng nhập bằng GitHub
3. Click "New Project"
4. Chọn "Deploy from GitHub repo"
5. Chọn repo `gym-bowling-backend`

## 🗄️ **BƯỚC 2: TẠO DATABASE POSTGRESQL**

1. Trong project Railway, click "New"
2. Chọn "Database" → "PostgreSQL"
3. Đặt tên: `gym-bowling-db`
4. Ghi nhớ thông tin database

## ⚙️ **BƯỚC 3: SETUP ENVIRONMENT VARIABLES**

Trong project Railway, vào tab "Variables" và thêm:

### **Database:**
```
DATABASE_URL=postgresql://username:password@host:port/database
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password_here
```

### **JWT:**
```
JWT_SECRET=your_super_long_jwt_secret_key_here_make_it_very_secure
JWT_EXPIRATION=86400000
```

### **VNPay (Sandbox):**
```
VNPAY_TMN_CODE=2QXUI4J4
VNPAY_HASH_SECRET=RAOEVONQL21F7OBDXJLXG6Z4GPGIDTYN
VNPAY_PAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
VNPAY_RETURN_URL=https://your-app-name.up.railway.app/api/payment/vnpay-return
```

### **Firebase (Production):**
```
GOOGLE_APPLICATION_CREDENTIALS_JSON={"type":"service_account","project_id":"your_project","private_key_id":"...","private_key":"-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n","client_email":"...","client_id":"...","auth_uri":"https://accounts.google.com/o/oauth2/auth","token_uri":"https://oauth2.googleapis.com/token","auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs","client_x509_cert_url":"..."}
```

## 🚀 **BƯỚC 4: DEPLOY**

1. Railway sẽ tự động build và deploy
2. Đợi build hoàn thành (khoảng 5-10 phút)
3. Kiểm tra logs nếu có lỗi

## ✅ **BƯỚC 5: KIỂM TRA**

1. Truy cập URL: `https://your-app-name.up.railway.app`
2. Health check: `https://your-app-name.up.railway.app/`
3. API docs: `https://your-app-name.up.railway.app/swagger-ui.html`

## 🔧 **TROUBLESHOOTING**

- **Healthcheck fail**: Kiểm tra logs, có thể cần restart
- **Database connection**: Kiểm tra DATABASE_URL format
- **Firebase error**: Kiểm tra GOOGLE_APPLICATION_CREDENTIALS_JSON format

## 📱 **TESTING**

Sau khi deploy thành công, test các API:
- Login Firebase: `POST /api/auth/firebase-login`
- Login truyền thống: `POST /api/auth/login`
- CRUD Packages: `GET/POST/PUT/DELETE /api/packages`
- VNPay payment: `POST /api/payment/vnpay`
