@echo off
set GRADLE_WRAPPER_JAR=gradle\wrapper\gradle-wrapper.jar
set MAIN_CLASS=org.gradle.wrapper.GradleWrapperMain

"C:\Program Files\Java\jdk-25\bin\java.exe" -cp "%GRADLE_WRAPPER_JAR%" %MAIN_CLASS% %*