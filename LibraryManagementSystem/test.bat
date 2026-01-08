@echo off
REM Automated test script for Library Management System

cd /d "%~dp0src"

echo =====================================
echo Library Management System
echo AUTOMATED TEST SCRIPT
echo =====================================
echo.

echo [1/2] Compiling all Java files...
javac -d . LibraryManagementSystem.java models\*.java manager\*.java storage\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed!
    echo.
    pause
    exit /b 1
)

echo [SUCCESS] Compilation completed!
echo.
echo [2/2] Starting the Library Management System...
echo =====================================
echo.

REM Create test input file
(
    echo 1
    echo 1
    echo B001
    echo The Great Gatsby
    echo F. Scott Fitzgerald
    echo 1925
    echo 8
    echo 1
    echo 2
    echo B002
    echo 1984
    echo George Orwell
    echo 1949
    echo 8
    echo 4
    echo 8
    echo 2
    echo 1
    echo P001
    echo Alice Johnson
    echo 4
    echo 7
    echo 2
    echo 1
    echo P002
    echo Bob Smith
    echo 4
    echo 7
    echo 3
    echo 1
    echo B001
    echo P001
    echo 1
    echo 4
    echo 4
    echo 1
    echo 3
    echo 1
    echo B001
    echo P001
    echo 4
    echo 5
) | java LibraryManagementSystem

echo.
echo =====================================
echo Test completed!
echo.
pause
