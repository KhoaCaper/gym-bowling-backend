@echo off
echo ========================================
echo GYM BOWLING BACKEND - DOCKER DEPLOY
echo ========================================

echo.
echo [1/5] Stopping existing containers...
docker-compose down

echo.
echo [2/5] Removing old images...
docker rmi gym-bowling-backend_app 2>nul
docker rmi gym-bowling-backend_nginx 2>nul

echo.
echo [3/5] Building and starting services...
docker-compose up --build -d

echo.
echo [4/5] Waiting for services to start...
timeout /t 30 /nobreak >nul

echo.
echo [5/5] Checking service status...
docker-compose ps

echo.
echo ========================================
echo DEPLOYMENT COMPLETED!
echo ========================================
echo.
echo Services:
echo - Database: localhost:1433
echo - App: http://localhost:8080
echo - Nginx: http://localhost:80
echo - Swagger: http://localhost:8080/swagger-ui.html
echo.
echo Commands:
echo - View logs: docker-compose logs -f
echo - Stop: docker-compose down
echo - Restart: docker-compose restart
echo ========================================

pause
