@echo off
chcp 65001 >nul
title Day len GitHub & Build IPA
echo ========================================================
echo   DANG DAY CODE LEN GITHUB & KICH HOAT BUILD IPA
echo ========================================================
echo.
cd /d "%~dp0"

echo Dang kiem tra trang thai Git...
git status
echo.
echo Dang day code len GitHub (origin main)...
echo Neu hien cua so dang nhap GitHub, vui long chon dang nhap bang trinh duyet (Sign in with your browser).
echo.
git push -u origin main

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================================
    echo   [THANH CONG] Code da duoc day len GitHub!
    echo   GitHub Actions dang tu dong build file IPA.
    echo   Theo doi qua trinh build va tai IPA tai:
    echo   https://github.com/phanvanchien18052002-cloud/okem/actions
    echo ========================================================
) else (
    echo.
    echo ========================================================
    echo   [THAT BAI] Khong the day code len GitHub.
    echo   Vui long kiem tra lai ket noi hoac quyen truy cap repository.
    echo ========================================================
)

echo.
pause
