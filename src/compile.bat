@echo off
javac %1/*.java -d C:/Users/Isaac/Desktop/Stuff/java/game_dev/Hackour/bin -cp C:/Users/Isaac/Desktop/Stuff/java/game_dev/Hackour/bin

if "%2"=="-test" goto run
goto end

:run
cd ../bin
java hackour.game.HackourGame
cd ../src

:end