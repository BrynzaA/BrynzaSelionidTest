@echo off
echo Stopping existing containers...
docker stop selenoid selenoid-ui 2>nul
docker rm selenoid selenoid-ui 2>nul

echo Creating config directory...
if not exist C:\selenoid mkdir C:\selenoid
copy browsers.json C:\selenoid\ >nul

echo Starting Selenoid...
docker run -d ^
  --name selenoid ^
  -p 4444:4444 ^
  -v //var/run/docker.sock:/var/run/docker.sock ^
  -v C:/selenoid/:/etc/selenoid/ ^
  aerokube/selenoid:latest ^
  -conf /etc/selenoid/browsers.json ^
  -limit 5 ^
  -timeout 3m

echo Starting Selenoid UI...
docker run -d ^
  --name selenoid-ui ^
  --link selenoid ^
  -p 8080:8080 ^
  aerokube/selenoid-ui:latest ^
  --selenoid-uri=http://selenoid:4444

echo.
echo Waiting for Selenoid to start...
timeout /t 10 /nobreak >nul

echo.
echo Selenoid status:
curl http://localhost:4444/status 2>nul

echo.
echo URLs:
echo Selenoid: http://localhost:4444
echo Selenoid UI: http://localhost:8080
echo.
echo To stop: docker stop selenoid selenoid-ui