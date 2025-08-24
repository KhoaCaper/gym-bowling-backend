# 🚀 Deployment Guide - Gym Bowling Backend

## 📋 Prerequisites
- Java 17+
- Gradle 8.14.3+
- SQL Server database
- Firebase project
- VNPay merchant account

## 🌍 Supported Platforms
- **Render.com** (Recommended - Simple & Stable)
- Railway
- Heroku
- Vercel
- Any platform supporting Java 17

## ⚙️ Environment Variables Setup

### Required Variables:
Copy from `environment-variables-template.txt` and set your production values:

```bash
# Database
DATABASE_URL=jdbc:sqlserver://your-server:1433;databaseName=gym_bowling;encrypt=true;trustServerCertificate=true
DATABASE_USERNAME=your_username
DATABASE_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your_very_long_and_secure_jwt_secret_key_for_production

# VNPay (Production)
VNPAY_TMN_CODE=your_production_tmn_code
VNPAY_HASH_SECRET=your_production_hash_secret
VNPAY_RETURN_URL=https://your-domain.com/api/payment/vnpay-return
```

## 🚀 Quick Deploy on Render.com

### Step 1: Prepare Repository
1. Push code to GitHub
2. Ensure `firebase-service-account.json` is in the repo

### Step 2: Deploy on Render
1. Go to [render.com](https://render.com)
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Configure:
   - **Name:** `gym-bowling-backend`
   - **Environment:** `Java`
   - **Build Command:** `./gradlew build`
   - **Start Command:** `java -jar build/libs/gym-bowling-backend-0.0.1-SNAPSHOT.jar`
   - **Port:** `8080`

### Step 3: Set Environment Variables
Add all variables from `environment-variables-template.txt`

### Step 4: Deploy
Click "Create Web Service" and wait for deployment

## 🔍 Health Check Endpoints

After deployment, test these endpoints:

- **Home:** `https://your-app.onrender.com/`
- **Health:** `https://your-app.onrender.com/health`
- **Ping:** `https://your-app.onrender.com/ping`
- **Swagger UI:** `https://your-app.onrender.com/swagger-ui.html`

## 🐛 Troubleshooting

### Common Issues:
1. **Port binding error:** Ensure `PORT` environment variable is set
2. **Database connection:** Check `DATABASE_URL` and credentials
3. **Firebase error:** Ensure `firebase-service-account.json` is in repo
4. **Health check fail:** Wait for app to fully start (may take 2-3 minutes)

### Logs:
Check deployment logs in your platform's dashboard

## 🔒 Security Notes

- ✅ CORS is automatically configured based on environment
- ✅ JWT secret is configurable via environment variable
- ✅ Database credentials are externalized
- ✅ Production logging is reduced for security

## 📱 Frontend Integration

Your frontend can now call APIs at:
```
https://your-app.onrender.com/api/*
```

## 🎯 Next Steps

1. Test all API endpoints
2. Update frontend with new API URL
3. Monitor app performance
4. Set up monitoring and alerts
