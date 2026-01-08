@echo off
REM Compilation Script for Library Management System

echo =====================================
echo Library Management System
echo Compilation Script
echo =====================================
echo.

REM Navigate to the src directory
cd /d "%~dp0src"

echo [1/2] Cleaning old compiled files...
if exist *.class del /Q *.class
if exist models\*.class del /Q models\*.class
if exist manager\*.class del /Q manager\*.class
if exist storage\*.class del /Q storage\*.class
echo       Done!
echo.

echo [2/2] Compiling Java files...
javac -d . LibraryManagementSystem.java models\*.java manager\*.java storage\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed! Please check for errors above.
    echo.
    pause
    exit /b 1
)

echo       Compilation successful!
echo.
echo All .class files have been generated in the src directory.
echo To run the program, execute: java LibraryManagementSystem
echo Or simply double-click run.bat
echo.
pause
