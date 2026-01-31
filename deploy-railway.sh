#!/bin/bash
echo "🚂 Railway Deployment Script"

# Check if Railway CLI is installed
if ! command -v railway &> /dev/null; then
    echo "❌ Railway CLI not found. Installing..."
    npm install -g @railway/cli
fi

# Login check
if ! railway whoami &> /dev/null; then
    echo "🔑 Please login to Railway..."
    railway login
fi

echo "📦 Building application..."
mvn clean package -DskipTests

echo "🚀 Deploying to Railway..."

# Initialize if not already done
if [ ! -f ".railwayapp.json" ]; then
    railway init
fi

# Add MySQL if not exists
railway add mysql --name portfolio-db

# Set environment variables
railway variables set SPRING_PROFILES_ACTIVE=railway
railway variables set JAVA_OPTS="-Xms256m -Xmx512m"

# Deploy
railway up

echo "✅ Deployment complete!"
echo "🌐 Your app will be available at your Railway URL"
echo "📊 Check status: railway status"
echo "📋 View logs: railway logs"