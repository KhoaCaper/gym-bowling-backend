@echo off
echo ========================================
echo Testing Ngrok Endpoints
echo ========================================
echo.

set NGROK_URL=https://2be318c6f89a.ngrok-free.app

echo 🧪 Testing Ngrok Endpoints...
echo 📍 Ngrok URL: %NGROK_URL%
echo.

echo [1/5] Testing Package Plans API...
curl -s "%NGROK_URL%/api/package-plans" >nul
if %errorlevel% equ 0 (
    echo ✅ Package Plans API: Working!
) else (
    echo ❌ Package Plans API: Failed!
)

echo [2/5] Testing Centers API...
curl -s "%NGROK_URL%/api/centers" >nul
if %errorlevel% equ 0 (
    echo ✅ Centers API: Working!
) else (
    echo ❌ Centers API: Failed!
)

echo [3/5] Testing Service Types API...
curl -s "%NGROK_URL%/api/service-types" >nul
if %errorlevel% equ 0 (
    echo ✅ Service Types API: Working!
) else (
    echo ❌ Service Types API: Failed!
)

echo [4/5] Testing Time Frames API...
curl -s "%NGROK_URL%/api/time-frames" >nul
if %errorlevel% equ 0 (
    echo ✅ Time Frames API: Working!
) else (
    echo ❌ Time Frames API: Failed!
)

echo [5/5] Testing Swagger UI...
curl -s "%NGROK_URL%/swagger-ui.html" >nul
if %errorlevel% equ 0 (
    echo ✅ Swagger UI: Working!
) else (
    echo ❌ Swagger UI: Failed!
)

echo.
echo ========================================
echo 🎯 Test Results Summary
echo ========================================
echo.
echo 💡 If all tests pass, your FE team can use:
echo    %NGROK_URL%
echo.
echo 🔍 Manual test in browser:
echo    %NGROK_URL%/swagger-ui.html
echo.
pause
