@echo off
echo ========================================
echo Gym Bowling Backend - Deploy & Test
echo ========================================
echo.

echo [1/4] Building Docker image...
docker build -t gym-bowling-backend:latest .
if %errorlevel% neq 0 (
    echo ERROR: Docker build failed!
    pause
    exit /b 1
)

echo [2/4] Starting services with Docker Compose...
docker-compose up -d
if %errorlevel% neq 0 (
    echo ERROR: Docker Compose failed!
    pause
    exit /b 1
)

echo [3/4] Waiting for services to be ready...
timeout /t 30 /nobreak >nul

echo [4/4] Testing API endpoints...
echo Testing localhost:8080...
curl -s http://localhost:8080/api/package-plans >nul
if %errorlevel% equ 0 (
    echo ✅ Local API is working!
) else (
    echo ❌ Local API test failed!
)

echo.
echo ========================================
echo 🚀 DEPLOYMENT COMPLETE!
echo ========================================
echo.
echo 📍 Local API: http://localhost:8080
echo 📍 Swagger UI: http://localhost:8080/swagger-ui.html
echo 📍 Ngrok URL: https://2be318c6f89a.ngrok-free.app
echo.
echo 🔍 Test ngrok endpoint:
echo curl -X GET "https://2be318c6f89a.ngrok-free.app/api/package-plans"
echo.
echo 💡 Your FE team can now use the ngrok URL!
echo.
pause
