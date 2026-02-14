# Portfolio Manager

A comprehensive portfolio management system built with **Spring Boot** and modern web technologies. This application provides a complete admin dashboard for managing professional profiles, skills, projects, education, and job experiences.

## 🚀 Technologies Used

### Backend
- **Java 17+** - Core programming language
- **Spring Boot** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **Maven** - Dependency management

### Frontend
- **Thymeleaf** - Server-side templating
- **HTML5/CSS3** - Markup and styling
- **JavaScript** - Client-side functionality
- **Bootstrap** - Responsive design

### Database
- **H2/MySQL** - Data persistence (configurable)

### Features
- 📊 **Profile Management** - Complete CRUD operations for professional profiles
- 🎓 **Education Tracking** - Academic background management
- 💼 **Job Experience** - Professional experience recording
- 🛠️ **Skills Management** - Technical and soft skills catalog
- 📁 **Project Portfolio** - Project showcase with file uploads
- 🔐 **Secure Authentication** - Role-based access control
- 🌍 **Multilingual Support** - Internationalization ready
- 📱 **Responsive Design** - Mobile-friendly interface

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── Managefolio/admin/
│   │       ├── config/          # Security & Configuration
│   │       ├── controller/      # REST Controllers
│   │       ├── model/          # Entity Classes
│   │       ├── repository/     # Data Access Layer
│   │       └── services/       # Business Logic
│   └── resources/
│       ├── static/             # CSS, JS, Images
│       ├── templates/          # Thymeleaf Templates
│       └── application.properties
```

## 🚀 Getting Started

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/Portfolio-manager.git
   cd Portfolio-manager
   ```

2. **Build and run locally**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the application**
   - Open your browser and navigate to `http://localhost:8080`

### 🌐 Google Cloud Deployment

Deploy your Portfolio Manager to Google Cloud Platform using various services:

#### Option 1: Google Cloud Run (Recommended for Containerized Apps)

1. **Prerequisites**
   - Install [Google Cloud CLI](https://cloud.google.com/sdk/docs/install)
   - Authenticate: `gcloud auth login`
   - Set project: `gcloud config set project YOUR_PROJECT_ID`

2. **Build and Deploy**
   ```bash
   # Build the Docker image
   docker build -t gcr.io/YOUR_PROJECT_ID/portfolio-manager .
   
   # Push to Google Container Registry
   docker push gcr.io/YOUR_PROJECT_ID/portfolio-manager
   
   # Deploy to Cloud Run
   gcloud run deploy portfolio-manager \
     --image gcr.io/YOUR_PROJECT_ID/portfolio-manager \
     --platform managed \
     --region us-central1 \
     --allow-unauthenticated \
     --port 8080 \
     --memory 1Gi \
     --cpu 1 \
     --set-env-vars SPRING_PROFILES_ACTIVE=prod
   ```

#### Option 2: Google App Engine

1. **Create app.yaml** in your project root:
   ```yaml
   runtime: java17
   instance_class: F2
   
   env_variables:
     SPRING_PROFILES_ACTIVE: prod
   
   automatic_scaling:
     min_instances: 1
     max_instances: 10
   ```

2. **Deploy**
   ```bash
   gcloud app deploy
   ```

#### Option 3: Google Kubernetes Engine (GKE)

1. **Create GKE cluster**
   ```bash
   gcloud container clusters create portfolio-cluster \
     --num-nodes=3 \
     --machine-type=e2-medium \
     --region=us-central1
   ```

2. **Deploy using kubectl**
   ```bash
   # Apply Kubernetes manifests (create deployment.yaml and service.yaml)
   kubectl apply -f k8s/
   ```

### 🗄️ Database Options on Google Cloud

- **Cloud SQL** (MySQL/PostgreSQL) for production
- **Firestore** for NoSQL requirements  
- **Cloud Spanner** for global scale
- **H2** for development (default)

### 🔧 Environment Configuration

Update your `application-prod.properties` for Google Cloud:

```properties
# Cloud SQL connection (if using)
spring.datasource.url=jdbc:mysql://google/YOUR_DATABASE?cloudSqlInstance=YOUR_PROJECT_ID:REGION:INSTANCE_NAME&socketFactory=com.google.cloud.sql.mysql.SocketFactory
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}

# File storage (Cloud Storage)
app.upload.dir=/tmp/uploads
app.cloud.storage.bucket=YOUR_BUCKET_NAME

# Logging for Cloud Operations
logging.level.com.google.cloud=INFO
```

### 📊 Monitoring & Observability

- **Cloud Logging**: Automatic log collection
- **Cloud Monitoring**: Performance metrics
- **Error Reporting**: Exception tracking
- **Cloud Trace**: Request tracing

### 💰 Cost Optimization

- Use **Cloud Run** for variable traffic (pay-per-request)
- Use **App Engine** for consistent traffic
- Enable **auto-scaling** to handle traffic spikes
- Use **Cloud Storage** for file uploads

## 📝 License

This project is licensed under the MIT License.
