#!/bin/bash

# Script de build et push Docker
# Usage: ./docker-build.sh [version]

VERSION=${1:-latest}
IMAGE_NAME="alamanedocs"
DOCKER_USERNAME=${DOCKER_USERNAME:-"votre-username"}

echo "🐳 Building Docker image: $IMAGE_NAME:$VERSION"

# Build l'image
docker build -t $IMAGE_NAME:$VERSION .

if [ $? -eq 0 ]; then
    echo "✅ Build réussi!"
    
    # Tag avec latest si version spécifiée
    if [ "$VERSION" != "latest" ]; then
        docker tag $IMAGE_NAME:$VERSION $IMAGE_NAME:latest
        echo "✅ Tagged as latest"
    fi
    
    # Demander si on veut push
    read -p "Voulez-vous push vers Docker Hub? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "🚀 Pushing to Docker Hub..."
        docker tag $IMAGE_NAME:$VERSION $DOCKER_USERNAME/$IMAGE_NAME:$VERSION
        docker push $DOCKER_USERNAME/$IMAGE_NAME:$VERSION
        
        if [ "$VERSION" != "latest" ]; then
            docker tag $IMAGE_NAME:latest $DOCKER_USERNAME/$IMAGE_NAME:latest
            docker push $DOCKER_USERNAME/$IMAGE_NAME:latest
        fi
        
        echo "✅ Push terminé!"
    fi
else
    echo "❌ Build échoué!"
    exit 1
fi
