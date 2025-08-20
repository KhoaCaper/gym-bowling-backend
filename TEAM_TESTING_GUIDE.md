# 🚀 Team Testing Guide - Gym Bowling Backend

## 📋 Prerequisites
- Java 17+ installed
- SQL Server running on localhost:1433
- Database `gym_bowling` created
- IntelliJ IDEA or similar IDE

## 🔧 Setup Steps

### 1. Start Spring Boot Application
```bash
# In IntelliJ, run GymBowlingBackendApplication.java
# Wait for: "Started GymBowlingBackendApplication in X.XXX seconds"
# Verify app is running on http://localhost:8080
```

### 2. Start Ngrok Tunnel
```bash
# Open new terminal
ngrok http 8080

# Copy the new URL (e.g., https://abc123.ngrok-free.app)
# Update SwaggerConfig.java with the new URL
```

### 3. Update SwaggerConfig.java
```java
// Replace YOUR_NEW_NGROK_URL with actual ngrok subdomain
new Server().url("https://abc123.ngrok-free.app").description("Ngrok HTTPS Server")
```

## 🧪 Testing APIs

### Local Testing
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

### Ngrok Testing (for FE team)
- **Swagger UI**: https://YOUR_NGROK_URL.ngrok-free.app/swagger-ui.html
- **API Endpoints**: https://YOUR_NGROK_URL.ngrok-free.app/api/...

## 🔓 Security Status (Temporary)
- ✅ All security disabled for team testing
- ✅ CORS allows all origins
- ✅ All API endpoints accessible
- ✅ Firebase auth temporarily disabled

## 📱 Available APIs for Testing

### User Management
- `GET /api/users` - Get all users
- `POST /api/users` - Create user
- `GET /api/users/{id}` - Get user by ID

### Package Management
- `GET /api/packages` - Get all packages
- `POST /api/packages` - Create package
- `GET /api/package-plans` - Get package plans

### Center Management
- `GET /api/centers` - Get all centers
- `POST /api/centers` - Create center

### Order Management
- `GET /api/orders` - Get all orders
- `POST /api/orders` - Create order

## 🚨 Troubleshooting

### Ngrok Issues
- Ensure Spring Boot app is running BEFORE starting ngrok
- Check ngrok tunnel status at http://localhost:4040
- Verify tunnel points to localhost:8080

### CORS Issues
- Check browser console for CORS errors
- Verify SecurityConfig.java CORS settings
- Try adding `ngrok-skip-browser-warning: true` header

### Database Issues
- Verify SQL Server is running
- Check database connection in application.properties
- Ensure database `gym_bowling` exists

## 📞 Support
- Check logs in IntelliJ console
- Verify all configurations in SecurityConfig.java
- Test localhost:8080 first before ngrok

## 🔄 Next Steps
1. Test all APIs locally first
2. Update ngrok URL in SwaggerConfig
3. Share ngrok URL with FE team
4. Test APIs through ngrok
5. Re-enable security after testing complete
