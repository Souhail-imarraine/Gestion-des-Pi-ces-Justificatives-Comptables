@echo off
REM Script de build et push Docker pour Windows
REM Usage: docker-build.bat [version]

SET VERSION=%1
IF "%VERSION%"=="" SET VERSION=latest

SET IMAGE_NAME=alamanedocs
SET DOCKER_USERNAME=votre-username

echo 🐳 Building Docker image: %IMAGE_NAME%:%VERSION%

REM Build l'image
docker build -t %IMAGE_NAME%:%VERSION% .

IF %ERRORLEVEL% EQU 0 (
    echo ✅ Build réussi!
    
    REM Tag avec latest si version spécifiée
    IF NOT "%VERSION%"=="latest" (
        docker tag %IMAGE_NAME%:%VERSION% %IMAGE_NAME%:latest
        echo ✅ Tagged as latest
    )
    
    REM Demander si on veut push
    SET /P PUSH="Voulez-vous push vers Docker Hub? (y/n): "
    IF /I "%PUSH%"=="y" (
        echo 🚀 Pushing to Docker Hub...
        docker tag %IMAGE_NAME%:%VERSION% %DOCKER_USERNAME%/%IMAGE_NAME%:%VERSION%
        docker push %DOCKER_USERNAME%/%IMAGE_NAME%:%VERSION%
        
        IF NOT "%VERSION%"=="latest" (
            docker tag %IMAGE_NAME%:latest %DOCKER_USERNAME%/%IMAGE_NAME%:latest
            docker push %DOCKER_USERNAME%/%IMAGE_NAME%:latest
        )
        
        echo ✅ Push terminé!
    )
) ELSE (
    echo ❌ Build échoué!
    exit /b 1
)
