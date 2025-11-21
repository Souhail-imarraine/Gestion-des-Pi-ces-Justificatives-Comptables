# 🐳 Guide Docker - Alamane Docs

## 📋 Prérequis

- Docker Desktop installé
- Docker Compose installé

---

## 🚀 Démarrage Rapide

### **Option 1 : Avec Docker Compose (Recommandé)**

```bash
# Build et démarrer
docker-compose up -d

# Voir les logs
docker-compose logs -f

# Arrêter
docker-compose down
```

### **Option 2 : Avec Docker uniquement**

```bash
# Build l'image
docker build -t alamanedocs:latest .

# Run le conteneur
docker run -d \
  --name alamanedocs \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -v $(pwd)/uploads:/app/uploads \
  alamanedocs:latest

# Voir les logs
docker logs -f alamanedocs

# Arrêter et supprimer
docker stop alamanedocs
docker rm alamanedocs
```

---

## 🔧 Commandes Utiles

### **Build**

```bash
# Build l'image
docker build -t alamanedocs:latest .

# Build sans cache
docker build --no-cache -t alamanedocs:latest .

# Build avec tag spécifique
docker build -t alamanedocs:v1.0.0 .
```

### **Run**

```bash
# Run en mode détaché
docker run -d -p 8080:8080 --name alamanedocs alamanedocs:latest

# Run en mode interactif
docker run -it -p 8080:8080 --name alamanedocs alamanedocs:latest

# Run avec variables d'environnement
docker run -d -p 8080:8080 \
  -e JWT_SECRET=your-secret-key \
  -e JWT_EXPIRATION=86400000 \
  --name alamanedocs alamanedocs:latest
```

### **Logs & Debug**

```bash
# Voir les logs
docker logs alamanedocs

# Suivre les logs en temps réel
docker logs -f alamanedocs

# Entrer dans le conteneur
docker exec -it alamanedocs sh

# Inspecter le conteneur
docker inspect alamanedocs
```

### **Gestion**

```bash
# Lister les conteneurs
docker ps

# Arrêter le conteneur
docker stop alamanedocs

# Démarrer le conteneur
docker start alamanedocs

# Redémarrer le conteneur
docker restart alamanedocs

# Supprimer le conteneur
docker rm alamanedocs

# Supprimer l'image
docker rmi alamanedocs:latest
```

---

## 📦 Push vers Docker Hub

```bash
# Login
docker login

# Tag l'image
docker tag alamanedocs:latest votre-username/alamanedocs:latest
docker tag alamanedocs:latest votre-username/alamanedocs:v1.0.0

# Push
docker push votre-username/alamanedocs:latest
docker push votre-username/alamanedocs:v1.0.0
```

---

## 🌐 Accès à l'Application

Une fois le conteneur démarré :

- **API** : http://localhost:8080
- **H2 Console** : http://localhost:8080/h2-console
- **Health Check** : http://localhost:8080/actuator/health

---

## 📊 Taille de l'Image

```bash
# Voir la taille de l'image
docker images alamanedocs

# Résultat attendu : ~200-250 MB
```

---

## 🔒 Variables d'Environnement

| Variable | Description | Défaut |
|----------|-------------|--------|
| `SPRING_PROFILES_ACTIVE` | Profil Spring | `prod` |
| `JWT_SECRET` | Secret JWT | (voir application.yml) |
| `JWT_EXPIRATION` | Expiration JWT (ms) | `86400000` (24h) |

---

## 🐛 Troubleshooting

### **Le conteneur ne démarre pas**

```bash
# Vérifier les logs
docker logs alamanedocs

# Vérifier le statut
docker ps -a
```

### **Port déjà utilisé**

```bash
# Changer le port
docker run -d -p 8081:8080 --name alamanedocs alamanedocs:latest
```

### **Problème de permissions**

```bash
# Donner les permissions au dossier uploads
chmod -R 777 uploads/
```

---

## 🎯 Multi-Stage Build

Le Dockerfile utilise un build multi-stage :

1. **Stage 1 (build)** : Compile l'application avec Maven
2. **Stage 2 (run)** : Image légère avec JRE uniquement

**Avantages** :
- ✅ Image finale plus petite (~200 MB vs ~600 MB)
- ✅ Pas de Maven dans l'image finale
- ✅ Plus sécurisé

---

## 📝 Notes

- L'application utilise H2 en mémoire par défaut
- Les fichiers uploadés sont stockés dans `/app/uploads`
- Le conteneur s'exécute avec un utilisateur non-root pour la sécurité
- Health check configuré pour vérifier l'état de l'application

---

## ✅ Checklist Déploiement

- [ ] Build l'image : `docker build -t alamanedocs:latest .`
- [ ] Tester localement : `docker run -p 8080:8080 alamanedocs:latest`
- [ ] Vérifier l'accès : http://localhost:8080
- [ ] Tester le login
- [ ] Tag l'image : `docker tag alamanedocs:latest username/alamanedocs:v1.0.0`
- [ ] Push vers Docker Hub : `docker push username/alamanedocs:v1.0.0`

---

**🎉 Votre application est maintenant containerisée !**
