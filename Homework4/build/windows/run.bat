@ECHO Removing compiled files if exist.
call clean.bat

@ECHO Compiling files.
call make.bat

@ECHO Program starting...
java -cp "../../lib/*:../../bin/" main.PSS

pause