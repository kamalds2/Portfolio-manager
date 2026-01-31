# Railway Deployment Guide

## 🚂 Deploy to Railway (FREE)

### Step 1: Install Railway CLI

```cmd
# Using npm
npm install -g @railway/cli

# Or using curl (if you have WSL/Git Bash)
curl -fsSL https://railway.app/install.sh | sh
```

### Step 2: Login to Railway

```cmd
railway login
```
This opens your browser - sign up with GitHub (recommended)

### Step 3: Deploy Your Project

```cmd
cd C:\Project\admin

# Initialize Railway project
railway init

# Add MySQL database
railway add mysql

# Deploy your application
railway up
```

### Step 4: Set Environment Variables (Auto-configured)

Railway automatically sets:
- `DATABASE_URL` (MySQL connection)
- `PORT` (dynamic port assignment)
- `SPRING_PROFILES_ACTIVE=railway`

### Step 5: Access Your App

After deployment, Railway provides:
- **Live URL**: `https://your-app-name.up.railway.app`
- **Dashboard**: Monitor logs, metrics, environment variables

## 📋 Manual Deployment Steps

If CLI doesn't work, use Railway Dashboard:

1. **Go to**: https://railway.app
2. **Sign up** with GitHub
3. **Create New Project** → **Deploy from GitHub**
4. **Connect Repository**: Select your portfolio repo
5. **Add MySQL**: Click "Add Service" → "Database" → "MySQL"
6. **Environment Variables**: Auto-configured
7. **Deploy**: Automatic on git push

## 🔧 Configuration Details

### Automatic Features:
- ✅ **SSL Certificate** (HTTPS enabled)
- ✅ **Custom Domain** support
- ✅ **Auto-deployments** on git push
- ✅ **Environment injection**
- ✅ **Log streaming**
- ✅ **Metrics monitoring**

### Resource Limits (Free Tier):
- **Memory**: 512MB RAM
- **CPU**: Shared
- **Storage**: 1GB
- **Bandwidth**: 100GB/month
- **Hours**: 500 execution hours/month
- **Credit**: $5/month

## 🚀 Quick Commands

```cmd
# View logs
railway logs

# Open in browser
railway open

# Check status
railway status

# Add environment variable
railway variables set KEY=value

# Connect to database
railway connect mysql
```

## 🔄 Auto-Deploy Setup

1. **Push to GitHub**: Changes auto-deploy
2. **Branch Protection**: Configure in Railway dashboard
3. **Preview Deployments**: Each PR gets preview URL

## 📊 Monitoring

Railway provides:
- **Real-time logs**
- **CPU/Memory usage**
- **Response times**
- **Error tracking**

## ⚡ Performance Tips

1. **Enable JVM optimizations** in `railway.json`
2. **Configure heap size** for Railway's 512MB limit
3. **Use connection pooling** (already configured)
4. **Enable compression** (already configured)

## 🆙 Upgrade Path to DigitalOcean

When ready for production:
1. Export Railway database
2. Set up DigitalOcean droplet
3. Import database
4. Update DNS records

Your Railway URL stays active for testing!