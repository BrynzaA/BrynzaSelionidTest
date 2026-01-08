@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Starting Complete Selenium Test Environment
echo ========================================

echo.
echo [1/6] Stopping and removing old containers...
docker stop selenoid selenoid-ui selenium-tests 2>nul
docker rm selenoid selenoid-ui selenium-tests 2>nul

echo.
echo [2/6] Creating required directories...
if not exist C:\selenoid mkdir C:\selenoid
if not exist test-results mkdir test-results
if not exist selenoid-videos mkdir selenoid-videos

copy /Y browsers.json C:\selenoid\browsers.json >nul

echo.
echo [3/6] Starting Selenoid...
docker run -d ^
  --name selenoid ^
  -p 4444:4444 ^
  -v //var/run/docker.sock:/var/run/docker.sock ^
  -v C:/selenoid/:/etc/selenoid/ ^
  -v %CD%\selenoid-videos:/opt/selenoid/video ^
  aerokube/selenoid:latest-release ^
  -conf /etc/selenoid/browsers.json ^
  -limit 5 ^
  -timeout 3m

echo.
echo [4/6] Starting Selenoid UI...
docker run -d ^
  --name selenoid-ui ^
  --link selenoid ^
  -p 8080:8080 ^
  aerokube/selenoid-ui:latest-release ^
  --selenoid-uri=http://selenoid:4444

echo.
echo [5/6] Waiting for Selenoid to start...
echo Checking Selenoid status...
set attempts=0

:check_selenoid
set /a attempts+=1
if !attempts! gtr 30 (
    echo ERROR: Selenoid failed to start after 30 attempts
    echo Trying to continue anyway...
    goto continue_anyway
)

curl -s http://localhost:4444/status >nul 2>nul
if !errorlevel! neq 0 (
    echo Waiting... (!attempts!/30)
    REM Используем ping для паузы (2 секунды)
    ping -n 3 127.0.0.1 >nul
    goto check_selenoid
)

echo Selenoid is ready!
curl -s http://localhost:4444/status | findstr /i "total"
if errorlevel 1 (
    echo WARNING: Could not get Selenoid status details
)

:continue_anyway
echo.
echo [6/6] Building and running test container...
echo ========================================

echo Creating Dockerfile for tests...
(
echo FROM maven:3.8.4-openjdk-11-slim
echo WORKDIR /app
echo COPY pom.xml .
echo COPY src ./src
echo COPY testng.xml .
echo COPY config.properties .
echo RUN mvn dependency:go-offline
echo CMD ["mvn", "test", "-Dselenium.host=selenoid", "-Dselenium.port=4444"]
) > Dockerfile.test

echo Building test container...
docker build -f Dockerfile.test -t selenium-tests:latest .

echo Running tests in container...
echo Note: Tests will connect to Selenoid at: selenoid:4444
echo.

docker run --rm ^
  --name selenium-tests ^
  --link selenoid ^
  -v %CD%\test-results:/app/target ^
  -e SELENIUM_HOST=selenoid ^
  -e SELENIUM_PORT=4444 ^
  selenium-tests:latest

echo.
echo ========================================
echo Test Execution Completed!
echo ========================================
echo.
echo URLs:
echo Selenoid Grid:    http://localhost:4444
echo Selenoid UI:      http://localhost:8080
echo.
echo Directories:
echo Test Results:     %CD%\test-results
echo Video Recordings: %CD%\selenoid-videos
echo.
echo Commands:
echo - Stop all:       docker stop selenoid selenoid-ui
echo - View logs:      docker logs selenoid
echo - See UI:         http://localhost:8080
echo.
echo Press any key to stop containers and exit...
pause >nul

echo.
echo Stopping containers...
docker stop selenoid selenoid-ui 2>nul
echo Cleanup complete!