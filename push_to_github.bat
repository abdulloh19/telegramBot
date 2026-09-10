@echo off
chcp 65001 >nul
title GitHub Push - mnemonic-webapp

echo.
echo ============================================
echo   mnemonic-webapp ni GitHub ga yuklash
echo ============================================
echo.

cd /d "%~dp0"

set /p GITHUB_USER="GitHub username kiriting (masalan: abdulloh19): "
set /p REPO_NAME="Repo nomi (default: mnemonic-webapp): "

if "%REPO_NAME%"=="" set REPO_NAME=mnemonic-webapp

echo.
echo GitHub da yangi repo yarating:
echo   1. https://github.com/new oching
echo   2. Repository name: %REPO_NAME%
echo   3. Public tanlang
echo   4. README, .gitignore, License ni QOSHMAING
echo   5. "Create repository" bosing
echo.
pause

git remote add origin https://github.com/%GITHUB_USER%/%REPO_NAME%.git
git branch -M main
git push -u origin main

if %errorlevel% neq 0 (
    echo.
    echo [XATO] Push bajarilmadi. Tokenni tekshiring.
    pause
    exit /b 1
)

echo.
echo ============================================
echo   MUVAFFAQIYAT! Repo joylashtirildi:
echo   https://github.com/%GITHUB_USER%/%REPO_NAME%
echo ============================================
echo.
echo Keyingi qadam: render.com ga boring va
echo "New Web Service" yarating, GitHub repo ni ulang.
echo.
pause
