@echo off
echo ========================================
echo Starting with Docker Compose
echo ========================================

echo Stopping old containers...
docker-compose -f docker-compose-simple.yml down 2>nul

echo Creating directories...
if not exist selenoid-config mkdir selenoid-config
if not exist selenoid-videos mkdir selenoid-videos
if not exist test-results mkdir test-results

copy /Y browsers.json selenoid-config\ 2>nul

echo.
echo Starting infrastructure...
docker-compose -f docker-compose-simple.yml up -d selenoid selenoid-ui

echo Waiting for Selenoid...
timeout /t 15 /nobreak >nul

echo Checking Selenoid status...
curl http://localhost:4444/status 2>nul && echo Selenoid is ready!

echo.
echo Running tests...
docker-compose -f docker-compose-simple.yml up tests

echo.
echo ========================================
echo All done!
echo ========================================
echo.
echo Selenoid: http://localhost:4444
echo Selenoid UI: http://localhost:8080
echo.
echo To stop: docker-compose -f docker-compose-simple.yml down
echo.
pause