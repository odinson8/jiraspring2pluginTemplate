REM Written by Berk
echo Starting install script

CALL "C:\Applications\Atlassian\atlassian-plugin-sdk-8.0.16\apache-maven-3.5.4\bin\mvn.cmd" -gs C:\Applications\Atlassian\atlassian-plugin-sdk-8.0.16\apache-maven-3.5.4/conf/settings.xml -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true -Dmaven.test.skip=true package

if %ERRORLEVEL% == 0 goto :upload
echo "Errors encountered during build.  Exited with status: %errorlevel%"
goto :endofscript

:upload
echo Skipped Upload

:endofscript
echo "Script complete"


