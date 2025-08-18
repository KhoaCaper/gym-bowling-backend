# 🚀 Railway Deployment Guide

## Prerequisites
- Railway account connected to GitHub
- Firebase service account JSON
- VNPay credentials

## Step-by-Step Deployment

### 1. Create Railway Project
1. Go to [railway.app](https://railway.app)
2. Click "New Project" → "Deploy from GitHub repo"
3. Select `gym-bowling-backend`

### 2. Add PostgreSQL Database
1. In Railway dashboard: "Add Service" → "Database" → "PostgreSQL"
2. Railway auto-generates `DATABASE_URL`

### 3. Configure Environment Variables

Add these variables in Railway dashboard → Variables:

```bash
# Spring Profile
SPRING_PROFILES_ACTIVE=railway

# Firebase (Option 1: JSON String - Recommended)
GOOGLE_APPLICATION_CREDENTIALS_JSON={"type":"service_account","project_id":"your-project-id",...}

# VNPay Configuration
VNPAY_TMN_CODE=your_actual_vnpay_tmn_code
VNPAY_HASH_SECRET=your_actual_vnpay_hash_secret
VNPAY_RETURN_URL=https://your-app-name.railway.app/api/payment/vnpay-return

# Optional: Custom Port (Railway handles this automatically)
PORT=8080
```

### 4. Deploy
1. Railway will automatically build using Dockerfile
2. Wait for deployment to complete
3. Check logs for any errors

### 5. Test Deployment

Your app will be available at: `https://your-app-name.railway.app`

Test endpoints:
- Health check: `/api/packages`
- Swagger UI: `/swagger-ui.html`
- API docs: `/api-docs`

### 6. Update VNPay Return URL
After deployment, update the `VNPAY_RETURN_URL` with your actual Railway domain.

## Troubleshooting

### Common Issues:

1. **Firebase not initializing**
   - Check `GOOGLE_APPLICATION_CREDENTIALS_JSON` format
   - Ensure JSON is properly escaped

2. **Database connection issues**
   - Verify PostgreSQL service is running
   - Check `DATABASE_URL` is set

3. **Build failures**
   - Check Java version (should be 21)
   - Verify all dependencies in `build.gradle`

### Logs
View logs in Railway dashboard → Deployments → View logs

## Environment Variables Template

Copy your Firebase service account JSON and format as single line:

```json
{"type":"service_account","project_id":"your-project","private_key_id":"...","private_key":"-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n","client_email":"...","client_id":"...","auth_uri":"https://accounts.google.com/o/oauth2/auth","token_uri":"https://oauth2.googleapis.com/token","auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs","client_x509_cert_url":"..."}
```

## Next Steps
- Set up custom domain (optional)
- Configure CI/CD for automatic deployments
- Set up monitoring and alerts
