@echo off
cd /d "%~dp0"

javac -d out -cp "libs/*;src" src\main\Main.java
java -cp "out;libs/*;assets" main.Main

pause
