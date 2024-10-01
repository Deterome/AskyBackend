@echo off 
setlocal enabledelayedexpansion

set POSTGRES_IMAGE_NAME=postgres
set TOMCAT_WEB_APP_IMAGE_NAME=web-app

set NETWORK_NAME=tomcat_database_connection

set POSTGRES_CONTAINER_NAME=postg
set TOMCAT_WEB_APP_CONTAINER_NAME=wb

call :CheckIfNetworkExist %NETWORK_NAME%
if "%DOES_NETWORK_EXIST%"=="false" (
    :: создание докер-сети
    docker network create %NETWORK_NAME% 1>nul
)
:: настройка контейнера postgres
call :CheckIfContainerExist %POSTGRES_CONTAINER_NAME%
if "%CONTAINER_EXIST%"=="false" (
    :: создание контейнера postgres
    docker run --name %POSTGRES_CONTAINER_NAME% -d -p 5433:5432 -e POSTGRES_PASSWORD=%DATABASE_PASSWORD% %POSTGRES_IMAGE_NAME% 1>nul
)
:: настройка контейнера tomcat
call :CheckIfContainerExist %TOMCAT_WEB_APP_CONTAINER_NAME%
if "%CONTAINER_EXIST%"=="false" (
    :: создание контейнера tomcat
    set DOCKER_DATABASE_URL="jdbc:postgresql://%POSTGRES_CONTAINER_NAME%:5432/postgres"
    docker run --name %TOMCAT_WEB_APP_CONTAINER_NAME% -d -p 8081:8080 -e DATABASE_PASSWORD=%DATABASE_PASSWORD% -e DATABASE_USERNAME=%DATABASE_USERNAME% -e DATABASE_URL=!DOCKER_DATABASE_URL! %TOMCAT_WEB_APP_IMAGE_NAME% 1>nul
)

:: подключение контейнеров к одной сети
docker network connect %NETWORK_NAME% %POSTGRES_CONTAINER_NAME% 2>nul
docker network connect %NETWORK_NAME% %TOMCAT_WEB_APP_CONTAINER_NAME% 2>nul

:: запуск контейнеров
docker start %TOMCAT_WEB_APP_CONTAINER_NAME% 1>nul
docker start %POSTGRES_CONTAINER_NAME% 1>nul

explorer http://localhost:8081

exit /b


:CheckIfContainerExist
    for /f "skip=1" %%i in ('docker inspect %1 2^>nul') do (
        set CONTAINER_EXIST=true
        goto :eof
    )
    set CONTAINER_EXIST=false
goto :eof

:CheckIfNetworkExist
    for /f "skip=1" %%i in ('docker network inspect %1 2^>nul') do (
        set DOES_NETWORK_EXIST=true
        goto :eof
    )
    set DOES_NETWORK_EXIST=false
goto :eof
