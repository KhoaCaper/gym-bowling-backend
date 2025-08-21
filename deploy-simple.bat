@echo off
echo ========================================
echo 🚀 Gym Bowling Backend - Simple Deploy
echo ========================================
echo.

echo [1/3] Building Docker image...
echo This may take a few minutes for the first time...
docker build -t gym-bowling-backend:latest .
if %errorlevel% neq 0 (
    echo.
    echo ❌ ERROR: Docker build failed!
    echo Please check if Docker is running and try again.
    echo.
    pause
    exit /b 1
)

echo.
echo [2/3] Starting services...
docker-compose up -d
if %errorlevel% neq 0 (
    echo.
    echo ❌ ERROR: Failed to start services!
    echo Please check docker-compose.yml file.
    echo.
    pause
    exit /b 1
)

echo.
echo [3/3] Waiting for services to be ready...
echo Please wait 30 seconds...
timeout /t 30 /nobreak >nul

echo.
echo ========================================
echo ✅ DEPLOYMENT SUCCESSFUL!
echo ========================================
echo.
echo 🌐 Your APIs are now available at:
echo    Local: http://localhost:8080
echo    Ngrok: https://2be318c6f89a.ngrok-free.app
echo.
echo 📚 Swagger UI:
echo    http://localhost:8080/swagger-ui.html
echo.
echo 🧪 Test ngrok (for your FE team):
echo    curl -X GET "https://2be318c6f89a.ngrok-free.app/api/package-plans"
echo.
echo 💡 Your FE team can now use the ngrok URL!
echo.
echo Press any key to continue...
pause >nul
