@echo off
echo Setting up Java environment...

:: Set JAVA_HOME
setx JAVA_HOME "C:\Program Files\Java\jdk-21"

:: Add Java to PATH
setx PATH "%PATH%;%JAVA_HOME%\bin"

echo.
echo JAVA_HOME has been set to: %JAVA_HOME%
echo Please download and install Java 21 from: https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.exe
echo After installation, restart your terminal and run: java -version
pause
