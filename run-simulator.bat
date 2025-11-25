@echo off
echo Starting Device Simulator...
cd device-simulator
call mvnw.cmd clean javafx:run
pause
