# 🚀 Railway Deployment Guide for Gym Bowling Backend

## 📋 **Prerequisites**
- ✅ GitHub repository connected to Railway
- ✅ PostgreSQL database added to Railway project
- ✅ Environment variables configured

## 🔧 **Environment Variables Setup**

### **Required Variables:**
```bash
# Database Configuration (Auto-provided by Railway PostgreSQL)
PGHOST=your-postgres-host
PGPORT=5432
PGDATABASE=your-database-name
PGUSER=your-database-user
PGPASSWORD=your-database-password

# Firebase Configuration (Disabled by default)
FIREBASE_ENABLED=false

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-here-make-it-very-long-and-secure-for-production-use
JWT_EXPIRATION=86400000

# VNPay Configuration (Sandbox)
VNPAY_TMN_CODE=2QXUI4J4
VNPAY_HASH_SECRET=RAOEVONQL21F7OBDXJLXG6Z4GPGIDTYN
VNPAY_PAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
VNPAY_RETURN_URL=https://your-railway-domain.up.railway.app/api/payment/vnpay-return
```

### **Optional Variables:**
```bash
# Server Configuration
PORT=8080  # Railway auto-provides this
```

## 🚀 **Deployment Steps**

### **Step 1: Connect GitHub Repository**
1. Go to [Railway.app](https://railway.app)
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Choose your `gym-bowling-backend` repository
5. Click "Deploy"

### **Step 2: Add PostgreSQL Database**
1. In your Railway project, click "New"
2. Select "Database" → "PostgreSQL"
3. Wait for database to be provisioned
4. Note down the connection details

### **Step 3: Configure Environment Variables**
1. Go to your service (not database)
2. Click "Variables" tab
3. Add all required environment variables above
4. **IMPORTANT**: Set `FIREBASE_ENABLED=false` for production

### **Step 4: Deploy Application**
1. Railway will automatically detect changes from GitHub
2. Build process will start automatically
3. Monitor build logs for any errors
4. Wait for deployment to complete

### **Step 5: Verify Deployment**
1. Check health endpoint: `https://your-domain.up.railway.app/health`
2. Check root endpoint: `https://your-domain.up.railway.app/`
3. Monitor logs for any runtime errors

## 🔍 **Troubleshooting**

### **Build Failures:**
- Check if `gradlew` has execute permissions
- Verify Java 17 is specified in `system.properties`
- Check build logs for specific error messages

### **Runtime Errors:**
- Verify all environment variables are set
- Check database connection in logs
- Ensure `FIREBASE_ENABLED=false` is set

### **Health Check Failures:**
- Check if database is accessible
- Verify PostgreSQL service is running
- Check application logs for connection errors

## 📊 **Monitoring**

### **Health Check Endpoints:**
- **Root**: `/` → Returns "OK"
- **Health**: `/health` → Returns detailed health status

### **Expected Health Response:**
```json
{
  "status": "UP",
  "service": "gym-bowling-backend",
  "version": "1.0.0",
  "timestamp": "2024-01-01T12:00:00",
  "environment": "production",
  "database": "connected",
  "userCount": 0,
  "message": "Service is healthy and database is connected"
}
```

## 🎯 **Success Criteria**
- ✅ Application builds successfully
- ✅ Health check passes (`/health` returns 200)
- ✅ Database connection established
- ✅ All public endpoints accessible
- ✅ No Firebase initialization errors
- ✅ Application stays running without crashes

## 🔄 **Update Process**
1. Make changes to code locally
2. Commit and push to GitHub
3. Railway automatically detects changes
4. New deployment starts automatically
5. Monitor deployment progress
6. Verify new version works correctly
