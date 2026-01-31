# Portfolio Admin - Deployment Guide

## 🚀 Deployment Options

### 1. Local Docker Development

```bash
# Build and run with Docker Compose
docker-compose up --build

# Access application
http://localhost:8080
```

### 2. Cloud Deployment Options

#### AWS Deployment

**Option A: AWS ECS Fargate**
```bash
# Install AWS CLI and configure
aws configure

# Create ECS cluster
aws ecs create-cluster --cluster-name portfolio-cluster

# Deploy with ECS
aws ecs create-service --cluster portfolio-cluster --service-name portfolio-service
```

**Option B: AWS Elastic Beanstalk**
```bash
# Install EB CLI
pip install awsebcli

# Initialize and deploy
eb init portfolio-admin
eb create production
eb deploy
```

#### Azure Deployment

**Azure Container Instances**
```bash
# Login to Azure
az login

# Create resource group
az group create --name portfolio-rg --location eastus

# Deploy container
az container create \
  --resource-group portfolio-rg \
  --name portfolio-app \
  --image your-registry/portfolio-admin:latest \
  --ports 8080 \
  --environment-variables SPRING_PROFILES_ACTIVE=prod
```

#### Google Cloud Platform

**Cloud Run**
```bash
# Build and push to GCR
docker build -t gcr.io/your-project/portfolio-admin .
docker push gcr.io/your-project/portfolio-admin

# Deploy to Cloud Run
gcloud run deploy portfolio-admin \
  --image gcr.io/your-project/portfolio-admin \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated
```

#### DigitalOcean App Platform

```yaml
# .do/app.yaml
name: portfolio-admin
services:
- name: web
  source_dir: /
  github:
    repo: your-username/portfolio-admin
    branch: main
  run_command: java -jar target/admin-*.jar
  environment_slug: java
  instance_count: 1
  instance_size_slug: basic-xxs
  http_port: 8080
  env:
  - key: SPRING_PROFILES_ACTIVE
    value: prod
databases:
- name: portfolio-db
  engine: MYSQL
  version: "8"
```

### 3. Production Checklist

#### Database Setup
- [ ] Create production MySQL database
- [ ] Configure connection pooling
- [ ] Set up database backups
- [ ] Configure SSL connections

#### Security
- [ ] Use HTTPS/TLS certificates
- [ ] Configure firewall rules
- [ ] Set up monitoring and logging
- [ ] Use environment variables for secrets

#### Performance
- [ ] Configure JVM heap size
- [ ] Set up caching (Redis)
- [ ] Configure CDN for static assets
- [ ] Enable compression

#### Monitoring
- [ ] Set up health checks
- [ ] Configure log aggregation
- [ ] Set up alerts and notifications
- [ ] Performance monitoring

## 🛠 Quick Start Commands

### Build Docker Image
```bash
docker build -t portfolio-admin .
```

### Run with Custom Environment
```bash
docker run -d \
  --name portfolio-app \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-db-host \
  -e DB_USER=your-user \
  -e DB_PASSWORD=your-password \
  portfolio-admin
```

### Scale with Docker Compose
```bash
docker-compose up --scale app=3
```

## 📊 Monitoring URLs

- Health Check: `http://your-domain/actuator/health`
- Application Info: `http://your-domain/actuator/info`
- Metrics: `http://your-domain/actuator/metrics`

## 🔧 Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `prod` |
| `DB_HOST` | Database host | `localhost` |
| `DB_PORT` | Database port | `3306` |
| `DB_NAME` | Database name | `portfolio` |
| `DB_USER` | Database username | `root` |
| `DB_PASSWORD` | Database password | - |
| `JAVA_OPTS` | JVM options | `-Xms512m -Xmx1024m` |

## 🆘 Troubleshooting

### Common Issues
1. **Database Connection**: Verify credentials and network access
2. **Memory Issues**: Increase JVM heap size with `JAVA_OPTS`
3. **File Uploads**: Ensure upload directory has write permissions
4. **Port Conflicts**: Change server port if 8080 is occupied

### Logs
```bash
# View container logs
docker logs portfolio-app

# Follow logs in real-time
docker logs -f portfolio-app
```