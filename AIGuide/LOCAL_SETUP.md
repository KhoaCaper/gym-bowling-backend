# 🏠 LOCAL DEVELOPMENT SETUP

## 📋 **BƯỚC 1: CẤU HÌNH DATABASE**

1. **SQL Server đang chạy** trên `localhost:1433`
2. **Database:** `gym_bowling`
3. **Username:** `sa`
4. **Password:** `123123` (hoặc password của bạn)

## ⚙️ **BƯỚC 2: CẤU HÌNH APPLICATION.PROPERTIES**

1. **Copy template:**
   ```bash
   cp src/main/resources/application.properties.template src/main/resources/application.properties
   ```

2. **Sửa các giá trị:**
   - `spring.datasource.password=YOUR_DATABASE_PASSWORD_HERE` → `spring.datasource.password=123123`
   - `app.jwt.secret=YOUR_LOCAL_JWT_SECRET_HERE...` → `app.jwt.secret=local-dev-jwt-secret-key-for-development-only-not-for-production-use`

## 🔥 **BƯỚC 3: CẤU HÌNH FIREBASE**

1. **File:** `src/main/resources/firebase-service-account.json`
2. **Đã có sẵn** cho local development
3. **Không commit** file này lên Git

## 🚀 **BƯỚC 4: CHẠY PROJECT**

1. **Build:**
   ```bash
   ./gradlew build
   ```

2. **Run:**
   ```bash
   ./gradlew bootRun
   ```

3. **Test:**
   - Health check: `http://localhost:8080/`
   - API docs: `http://localhost:8080/swagger-ui.html`

## 📱 **TESTING LOCAL**

- **Login Firebase:** `POST http://localhost:8080/api/auth/firebase-login`
- **Login truyền thống:** `POST http://localhost:8080/api/auth/login`
- **CRUD Packages:** `GET/POST/PUT/DELETE http://localhost:8080/api/packages`
- **VNPay payment:** `POST http://localhost:8080/api/payment/vnpay`

## ⚠️ **LƯU Ý QUAN TRỌNG**

- **KHÔNG BAO GIỜ commit** `application.properties` hoặc `firebase-service-account.json`
- **Chỉ dùng cho local development**
- **Railway sẽ dùng** `application-railway.properties` và environment variables
