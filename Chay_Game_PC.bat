@echo off
chcp 65001 >nul
title Khởi động Game Làng Lá - PC (LibGDX)

set "JAR_PATH=desktop\build\libs\LangLaLibgdx-desktop-1.0.jar"

if not exist "%JAR_PATH%" (
    echo [THÔNG BÁO] Chưa tìm thấy file JAR compiled. Đang tự động build...
    call gradlew.bat :desktop:dist
)

echo ===================================================
echo     KHỞI ĐỘNG GAME LÀNG LÁ - PHIÊN BẢN PC (WINDOWS)
echo ===================================================

if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" (
        "%JAVA_HOME%\bin\java.exe" -jar "%JAR_PATH%" %*
        goto :END
    )
)

java -jar "%JAR_PATH%" %*

:END
if %errorlevel% neq 0 (
    echo.
    echo [LỖI] Không thể khởi chạy. Vui lòng kiểm tra môi trường Java (JDK).
    pause
)
