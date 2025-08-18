# 🏋️ Gym Bowling Backend

Spring Boot backend API for Gym Bowling application with Firebase Authentication and VNPay payment integration.

## 🚀 Features

- ✅ **Firebase Authentication** - Login/Register with Google
- ✅ **Role-based Authorization** - USER/STAFF/ADMIN roles
- ✅ **Package Management** - CRUD operations for service packages
- ✅ **VNPay Integration** - Sandbox payment processing
- ✅ **SQL Server Database** - Complete relational database
- ✅ **Swagger API Documentation** - Interactive API testing
- ✅ **Docker Support** - Containerized deployment

## 🛠 Tech Stack

- **Java 24** with Spring Boot 3.5.4
- **Spring Security** with Firebase JWT
- **Spring Data JPA** with Hibernate
- **SQL Server** database
- **Firebase Admin SDK** for authentication
- **VNPay API** for payments
- **Swagger/OpenAPI** for documentation
- **Docker & Docker Compose** for deployment

## 📋 Database Schema

### Core Tables:
- `roles` - User roles (USER, STAFF, ADMIN)
- `users` - User accounts with Firebase UID
- `service_types` - Categories (Gym, Bowling, Spa, PT)
- `centers` - Physical locations
- `services` - Available services at each center
- `package_plans` - Service packages for sale
- `orders` - Customer orders
- `payments` - Payment transactions

## 🔧 Setup

### 1. Database Setup
```sql
-- Run SQL script to create database and sample data
-- File: AIGuide/final-database-setup.sql (local only)
```

### 2. Application Configuration
```bash
# Copy template and update with your values
cp src/main/resources/application.properties.template src/main/resources/application.properties

# Update these values:
# - spring.datasource.password=YOUR_SQL_PASSWORD
# - vnpay.tmnCode=YOUR_TMN_CODE  
# - vnpay.hashSecret=YOUR_HASH_SECRET
```

### 3. Firebase Setup
```bash
# 1. Create Firebase project at console.firebase.google.com
# 2. Enable Authentication > Email/Password and Google
# 3. Download service account JSON
# 4. Save as: src/main/resources/firebase-service-account.json
```

### 4. Run Application
```bash
./gradlew bootRun
```

## 📚 API Documentation

Access Swagger UI: `http://localhost:8080/swagger-ui.html`

### Public Endpoints:
- `GET /api/packages` - List active packages
- `POST /api/auth/login` - Login with Firebase token

### Protected Endpoints:
- `GET /api/auth/me` - Current user info
- `POST /api/payment/create-order` - Create order & payment
- `GET /api/payment/orders` - User order history

### Staff/Admin Endpoints:
- `GET /api/staff/packages` - Manage all packages
- `POST /api/staff/packages` - Create new package
- `PUT /api/staff/packages/{id}` - Update package
- `DELETE /api/staff/packages/{id}` - Delete package

## 🐳 Docker Deployment

```bash
# Local development with SQL Server
docker-compose up --build -d

# Check health
curl http://localhost:8080/api/packages
```

## 🔐 Security

- Firebase JWT tokens for authentication
- Role-based access control
- CORS configured for frontend integration
- Sensitive configs excluded from version control

## 💳 VNPay Testing

Use VNPay sandbox environment:
- Test card: `9704198526191432198` (NCB)
- OTP: `123456`

## 📖 Development Notes

- **Java 24** required
- **SQL Server** database required
- **Firebase project** required for authentication
- **VNPay sandbox account** required for payments

## 🤝 Contributing

1. Clone repository
2. Setup database and Firebase
3. Update application.properties
4. Run tests: `./gradlew test`
5. Start application: `./gradlew bootRun`

---

**Built with ❤️ for Gym Bowling management system**
