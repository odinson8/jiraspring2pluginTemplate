@echo off

title This is BERK!
echo Welcome to batch scripting!

C:\Applications\Atlassian\atlassian-plugin-sdk-8.0.16\apache-maven-3.5.4\bin\mvn.cmd -gs C:\Applications\Atlassian\atlassian-plugin-sdk-8.0.16\apache-maven-3.5.4/conf/settings.xml package
atlas-install-plugin --username plugin --password asd --plugin-key com.veniture.resourceManagement --server https://veniture.tk -p 8073 --context-path

pause