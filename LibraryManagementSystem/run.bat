@echo off
REM Compilation and Execution Script for Library Management System
REM This script compiles all Java files and runs the program

echo =====================================
echo Library Management System
echo Compilation and Execution Script
echo =====================================
echo.

REM Navigate to the src directory
cd /d "%~dp0src"

echo [1/3] Cleaning old compiled files...
if exist *.class del /Q *.class
if exist models\*.class del /Q models\*.class
if exist manager\*.class del /Q manager\*.class
if exist storage\*.class del /Q storage\*.class
echo       Done!
echo.

echo [2/3] Compiling Java files...
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

echo [3/3] Running the Library Management System...
echo =====================================
echo.

java LibraryManagementSystem

echo.
echo =====================================
echo Program terminated.
echo.
pause
