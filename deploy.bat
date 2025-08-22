@echo off
echo ========================================
echo    GYM BOWLING BACKEND - DEPLOY
echo ========================================
echo.

echo [1/5] Checking Git status...
git status --porcelain
if %errorlevel% neq 0 (
    echo ERROR: Git not available
    pause
    exit /b 1
)

echo.
echo [2/5] Adding all changes...
git add .

echo.
echo [3/5] Committing changes...
git commit -m "Complete Railway deployment configuration: Fixed database fallback, disabled JPA until DB available, optimized health checks"

echo.
echo [4/5] Pushing to GitHub...
git push origin master

echo.
echo [5/5] Deployment complete!
echo.
echo ========================================
echo    NEXT STEPS:
echo ========================================
echo 1. Go to Railway Dashboard
echo 2. Check if PostgreSQL service is linked
echo 3. Verify environment variables are set
echo 4. Monitor deployment logs
echo ========================================
echo.
pause
