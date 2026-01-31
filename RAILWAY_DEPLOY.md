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

**⚠️ CLI Issue Fix: Use Web Dashboard Instead**

The Railway CLI has changed. Instead of using CLI, use the web dashboard:

1. **Go to**: https://railway.com/project/b4b0ba4f-f842-494b-9944-13016f4e3366
2. **Add New Service** → **GitHub Repo** → Connect your repository
3. **Wait for build** to complete
4. **Add Database**: New Service → Database → MySQL
5. **Verify**: Check logs and live URL

**If you want to continue with CLI:**
```cmd
# Create app service (not database)
railway up --service app

# Add database through dashboard only
# CLI database commands are deprecated
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

**Recommended approach:**

1. **Go to**: https://railway.app
2. **Sign up** with GitHub
3. **Create New Project** → **Deploy from GitHub**
4. **Connect Repository**: Select your portfolio repo
5. **Wait for first deployment** to complete
6. **Add MySQL**: In project dashboard → "New Service" → "Database" → "MySQL"
7. **Environment Variables**: Auto-configured by Railway
8. **Redeploy**: Your app will restart with database connection

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