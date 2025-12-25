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

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/Portfolio-manager.git
   cd Portfolio-manager
   ```

2. **Build and run**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the application**
   - Open your browser and navigate to `http://localhost:8080`

## 📝 License

This project is licensed under the MIT License.
