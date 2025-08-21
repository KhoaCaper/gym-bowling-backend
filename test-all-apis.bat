@echo off
echo ========================================
echo 🧪 Testing ALL API Endpoints for FE Team
echo ========================================
echo.

set NGROK_URL=https://77d0e633886f.ngrok-free.app

echo 🎯 Testing Ngrok URL: %NGROK_URL%
echo 📍 Swagger UI: %NGROK_URL%/swagger-ui.html
echo.

echo ========================================
echo 📦 Package Management APIs
echo ========================================
echo [1/4] Testing Package Plans API...
curl -s "%NGROK_URL%/api/package-plans" >nul
if %errorlevel% equ 0 (
    echo ✅ Package Plans API: Working!
) else (
    echo ❌ Package Plans API: Failed!
)

echo [2/4] Testing Package Plan Details API...
curl -s "%NGROK_URL%/api/package-plan-details/packages" >nul
if %errorlevel% equ 0 (
    echo ✅ Package Plan Details API: Working!
) else (
    echo ❌ Package Plan Details API: Failed!
)

echo ========================================
echo 🏢 Center Management APIs
echo ========================================
echo [3/4] Testing Centers API...
curl -s "%NGROK_URL%/api/centers" >nul
if %errorlevel% equ 0 (
    echo ✅ Centers API: Working!
) else (
    echo ❌ Centers API: Failed!
)

echo ========================================
echo 🎯 Service Management APIs
echo ========================================
echo [4/4] Testing Service Types API...
curl -s "%NGROK_URL%/api/service-types" >nul
if %errorlevel% equ 0 (
    echo ✅ Service Types API: Working!
) else (
    echo ❌ Service Types API: Failed!
)

echo [5/4] Testing Services API...
curl -s "%NGROK_URL%/api/staff/services" >nul
if %errorlevel% equ 0 (
    echo ✅ Services API: Working!
) else (
    echo ❌ Services API: Failed!
)

echo ========================================
echo ⏰ Time Management APIs
echo ========================================
echo [6/4] Testing Time Frames API...
curl -s "%NGROK_URL%/api/timeframes" >nul
if %errorlevel% equ 0 (
    echo ✅ Time Frames API: Working!
) else (
    echo ❌ Time Frames API: Failed!
)

echo ========================================
echo 👥 User & Auth APIs
echo ========================================
echo [7/4] Testing Auth API...
curl -s "%NGROK_URL%/api/auth" >nul
if %errorlevel% equ 0 (
    echo ✅ Auth API: Working!
) else (
    echo ❌ Auth API: Failed!
)

echo [8/4] Testing Users API...
curl -s "%NGROK_URL%/api/users" >nul
if %errorlevel% equ 0 (
    echo ✅ Users API: Working!
) else (
    echo ❌ Users API: Failed!
)

echo [9/4] Testing Firebase Auth API...
curl -s "%NGROK_URL%/api/firebase-auth" >nul
if %errorlevel% equ 0 (
    echo ✅ Firebase Auth API: Working!
) else (
    echo ❌ Firebase Auth API: Failed!
)

echo ========================================
echo 💰 Payment & Order APIs
echo ========================================
echo [10/4] Testing Payment API...
curl -s "%NGROK_URL%/api/payment" >nul
if %errorlevel% equ 0 (
    echo ✅ Payment API: Working!
) else (
    echo ❌ Payment API: Failed!
)

echo [11/4] Testing Orders API...
curl -s "%NGROK_URL%/api/orders" >nul
if %errorlevel% equ 0 (
    echo ✅ Orders API: Working!
) else (
    echo ❌ Orders API: Failed!
)

echo ========================================
echo 🔧 Development & Admin APIs
echo ========================================
echo [12/4] Testing Dev API...
curl -s "%NGROK_URL%/api/dev" >nul
if %errorlevel% equ 0 (
    echo ✅ Dev API: Working!
) else (
    echo ❌ Dev API: Failed!
)

echo [13/4] Testing Admin API...
curl -s "%NGROK_URL%/api/admin" >nul
if %errorlevel% equ 0 (
    echo ✅ Admin API: Working!
) else (
    echo ❌ Admin API: Failed!
)

echo ========================================
echo 🌐 Swagger & Documentation
echo ========================================
echo [14/4] Testing Swagger UI...
curl -s "%NGROK_URL%/swagger-ui.html" >nul
if %errorlevel% equ 0 (
    echo ✅ Swagger UI: Working!
) else (
    echo ❌ Swagger UI: Failed!
)

echo [15/4] Testing API Docs...
curl -s "%NGROK_URL%/api-docs" >nul
if %errorlevel% equ 0 (
    echo ✅ API Docs: Working!
) else (
    echo ❌ API Docs: Failed!
)

echo.
echo ========================================
echo 🎯 Test Results Summary
echo ========================================
echo.
echo 💡 For FE Team Development:
echo    Base URL: %NGROK_URL%
echo    Swagger UI: %NGROK_URL%/swagger-ui.html
echo    API Docs: %NGROK_URL%/api-docs
echo.
echo 🔍 Manual Testing:
echo    1. Open Swagger UI in browser
echo    2. Test individual endpoints
echo    3. Check CORS headers
echo.
echo 📱 CORS is enabled for all origins (*)
echo 🔐 Firebase Auth is configured
echo 💳 VNPay payment integration ready
echo.
pause
