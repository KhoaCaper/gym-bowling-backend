# 🚀 Deploy Guide - Gym Bowling Backend

## 📋 **Các phương pháp Deploy**

### 1. 🐳 **Docker Compose (Recommended cho development)**
### 2. ☁️ **Heroku (Free tier)**  
### 3. 🌐 **Railway/Render (Alternative cloud)**
### 4. 🏢 **VPS/Server riêng**

---

## 🐳 **1. Docker Compose Deploy**

### **Bước 1: Chuẩn bị**
```bash
# Clone project
git clone <your-repo>
cd gym-bowling-backend

# Tạo Firebase service account file
# Đặt file firebase-service-account.json vào thư mục root
```

### **Bước 2: Cấu hình môi trường**
```bash
# Tạo file .env
cat > .env << EOF
VNPAY_TMN_CODE=your_vnpay_tmn_code
VNPAY_HASH_SECRET=your_vnpay_hash_secret
VNPAY_RETURN_URL=http://your-domain.com/api/payment/vnpay-return
EOF
```

### **Bước 3: Deploy**
```bash
# Build và chạy
docker-compose up --build -d

# Kiểm tra logs
docker-compose logs -f app

# Kiểm tra health
curl http://localhost:8080/api/packages
```

### **Bước 4: Setup database**
```bash
# Execute SQL script
docker exec -it gym-bowling-db /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P 'YourStrong@Passw0rd' \
  -i /docker-entrypoint-initdb.d/database-setup.sql
```

---

## ☁️ **2. Heroku Deploy**

### **Bước 1: Chuẩn bị Heroku**
```bash
# Install Heroku CLI
# https://devcenter.heroku.com/articles/heroku-cli

# Login
heroku login

# Tạo app
heroku create gym-bowling-backend-app
```

### **Bước 2: Thêm database addon**
```bash
# Thêm SQL Server addon (hoặc PostgreSQL)
heroku addons:create heroku-postgresql:hobby-dev

# Hoặc dùng external SQL Server
heroku config:set SPRING_DATASOURCE_URL="jdbc:sqlserver://your-server:1433;databaseName=gym_bowling"
heroku config:set SPRING_DATASOURCE_USERNAME="your-username"
heroku config:set SPRING_DATASOURCE_PASSWORD="your-password"
```

### **Bước 3: Cấu hình Firebase**
```bash
# Upload Firebase config as base64
base64 firebase-service-account.json > firebase-config.txt
heroku config:set FIREBASE_CONFIG=$(cat firebase-config.txt)

# Cập nhật FirebaseConfig.java để đọc từ environment variable
```

### **Bước 4: Deploy**
```bash
# Deploy
git add .
git commit -m "Deploy to Heroku"
git push heroku main

# Kiểm tra logs
heroku logs --tail

# Mở app
heroku open
```

---

## 🌐 **3. Railway Deploy**

### **Bước 1: Connect GitHub**
```bash
# 1. Truy cập railway.app
# 2. Connect GitHub repository
# 3. Select gym-bowling-backend repo
```

### **Bước 2: Thêm Database**
```bash
# 1. Add service > Database > PostgreSQL
# 2. Copy connection string
# 3. Update application properties
```

### **Bước 3: Environment Variables**
```bash
# Thêm trong Railway dashboard:
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=postgresql://...
VNPAY_TMN_CODE=your_code
VNPAY_HASH_SECRET=your_secret
FIREBASE_CONFIG=base64_encoded_json
```

### **Bước 4: Deploy**
```bash
# Railway tự động deploy khi push code
git push origin main
```

---

## 🏢 **4. VPS/Server Deploy**

### **Bước 1: Chuẩn bị server**
```bash
# Ubuntu/CentOS server
sudo apt update
sudo apt install docker docker-compose nginx

# Start services
sudo systemctl start docker
sudo systemctl enable docker
```

### **Bước 2: Clone và setup**
```bash
# Clone project
git clone <your-repo>
cd gym-bowling-backend

# Copy Firebase config
scp firebase-service-account.json user@server:/path/to/project/

# Update docker-compose.yml với domain thật
```

### **Bước 3: Nginx reverse proxy**
```nginx
# /etc/nginx/sites-available/gym-bowling
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### **Bước 4: SSL với Let's Encrypt**
```bash
# Install certbot
sudo apt install certbot python3-certbot-nginx

# Get SSL certificate
sudo certbot --nginx -d your-domain.com

# Auto renewal
sudo crontab -e
# Add: 0 12 * * * /usr/bin/certbot renew --quiet
```

---

## 🔧 **Environment Variables cần thiết**

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:sqlserver://...
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=your_password

# Firebase
FIREBASE_CONFIG=base64_encoded_json

# VNPay
VNPAY_TMN_CODE=your_tmn_code
VNPAY_HASH_SECRET=your_hash_secret
VNPAY_RETURN_URL=https://your-domain.com/api/payment/vnpay-return

# Optional
SPRING_PROFILES_ACTIVE=production
```

---

## 🧪 **Testing Deploy**

### **Health Checks**
```bash
# API Health
curl https://your-domain.com/api/packages

# Swagger UI
https://your-domain.com/swagger-ui.html

# Database connection
curl https://your-domain.com/actuator/health
```

### **Load Testing**
```bash
# Install Apache Bench
sudo apt install apache2-utils

# Test API
ab -n 100 -c 10 https://your-domain.com/api/packages
```

---

## 🚨 **Troubleshooting**

### **Common Issues**

1. **Database Connection Failed**
```bash
# Check connection string
# Verify SQL Server is running
# Check firewall rules
```

2. **Firebase Authentication Failed**
```bash
# Verify firebase-service-account.json exists
# Check file permissions
# Validate JSON format
```

3. **VNPay Callback Failed**
```bash
# Check return URL is accessible from internet
# Verify HTTPS for production
# Test with VNPay sandbox
```

4. **Memory Issues**
```bash
# Increase JVM heap size
JAVA_OPTS="-Xms512m -Xmx1024m"
```

---

## 📊 **Monitoring & Logs**

### **Docker Logs**
```bash
docker-compose logs -f app
docker-compose logs -f sqlserver
```

### **Application Metrics**
```bash
# Add to application.properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

### **Log Aggregation**
```bash
# Use ELK Stack hoặc cloud logging
# Grafana + Prometheus for metrics
```

---

## 🔐 **Security Checklist**

- [ ] Change default passwords
- [ ] Use HTTPS in production  
- [ ] Secure Firebase service account
- [ ] Enable SQL Server encryption
- [ ] Configure CORS properly
- [ ] Use environment variables for secrets
- [ ] Regular security updates
- [ ] Backup strategy

---

## 📝 **Quick Commands**

```bash
# Local development
docker-compose up -d

# Production deploy
docker-compose -f docker-compose.prod.yml up -d

# Database backup
docker exec gym-bowling-db sqlcmd -S localhost -U sa -P 'password' -Q "BACKUP DATABASE gym_bowling TO DISK='/backup/gym_bowling.bak'"

# Scale application
docker-compose up -d --scale app=3
```
