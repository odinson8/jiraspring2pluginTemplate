REM Berk

CALL "C:\Applications\Atlassian\atlassian-plugin-sdk-8.0.16\apache-maven-3.5.4\bin\mvn.cmd" -gs C:\Applications\Atlassian\atlassian-plugin-sdk-8.0.16\apache-maven-3.5.4/conf/settings.xml -Dmaven.test.skip=true package

CALL "C:\Applications\Atlassian\atlassian-plugin-sdk-8.0.16\apache-maven-3.5.4\bin\mvn.cmd" com.atlassian.maven.plugins:amps-dispatcher-maven-plugin:8.0.2:install -gs C:\Applications\Atlassian\atlassian-plugin-sdk-8.0.16\apache-maven-3.5.4/conf/settings.xml -Dusername=admin -Dpassword=asd -Datlassian.plugin.key=com.veniture.resourceManagement -Dserver=http://veniture.tk -Dhttp.port=8073 -Dcontext.path=