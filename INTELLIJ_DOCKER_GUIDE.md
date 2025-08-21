# 🚀 Hướng dẫn Deploy Docker trong IntelliJ Ultimate

## 📋 Prerequisites
- ✅ IntelliJ Ultimate đã cài đặt
- ✅ Docker Desktop đang chạy
- ✅ Java 24 SDK đã setup trong IntelliJ

## 🔧 Setup Docker trong IntelliJ

### 1. Kích hoạt Docker Tool Window
- **View → Tool Windows → Services** (hoặc `Alt+8`)
- Trong Services tab, bạn sẽ thấy **Docker** section

### 2. Cài đặt Docker Plugin (nếu chưa có)
- **File → Settings → Plugins**
- Tìm "Docker" và cài đặt
- Restart IntelliJ

### 3. Cấu hình Docker Connection
- **File → Settings → Build, Execution, Deployment → Docker**
- Đảm bảo Docker daemon đang chạy

## 🎯 Workflow Development

### Phase 1: Local Development
1. **Run Spring Boot App** trong IntelliJ
   - Click nút ▶️ (Run) hoặc `Shift+F10`
   - App sẽ chạy trên `http://localhost:8080`

2. **Test APIs locally**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - Test các endpoints

### Phase 2: Docker Deploy
1. **Build Docker Image**
   - Mở Terminal trong IntelliJ (`Alt+F12`)
   - Chạy: `docker build -t gym-bowling-backend:latest .`

2. **Start Services**
   - Chạy: `docker-compose up -d`

3. **Monitor Containers**
   - Services tab → Docker → Containers
   - Xem logs, status của containers

## 🐳 Docker Commands trong IntelliJ Terminal

### Build Image
```bash
docker build -t gym-bowling-backend:latest .
```

### Start Services
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose down
```

### View Logs
```bash
docker-compose logs -f app
```

### Restart App
```bash
docker-compose restart app
```

## 🔍 Debug và Troubleshooting

### 1. Kiểm tra Container Status
- Services tab → Docker → Containers
- Xem status, ports, logs

### 2. View Logs
- Right-click container → View Logs
- Hoặc dùng terminal: `docker-compose logs app`

### 3. Access Container Shell
- Right-click container → Exec
- Hoặc: `docker exec -it gym-bowling-backend bash`

## 🌐 Test APIs

### Local Testing
- `http://localhost:8080/api/package-plans`
- `http://localhost:8080/swagger-ui.html`

### Ngrok Testing (cho FE team)
- `https://2be318c6f89a.ngrok-free.app/api/package-plans`
- `https://2be318c6f89a.ngrok-free.app/swagger-ui.html`

## 📱 IntelliJ Features

### 1. Docker Tool Window
- View containers, images, networks
- Start/stop containers
- View logs, exec commands

### 2. Run Configurations
- **Run → Edit Configurations**
- Add Docker Compose run configuration
- Debug containerized apps

### 3. Database Tools
- Connect to SQL Server in container
- Execute queries, view data

## 🚨 Common Issues

### 1. Port Already in Use
```bash
# Check what's using port 8080
netstat -ano | findstr :8080

# Kill process
taskkill /PID <PID> /F
```

### 2. Docker Build Failed
- Check Docker Desktop is running
- Check Dockerfile syntax
- Check Java version compatibility

### 3. Container Won't Start
- Check logs: `docker-compose logs app`
- Check port conflicts
- Check environment variables

## 💡 Tips

1. **Use IntelliJ Terminal** thay vì Windows Command Prompt
2. **Monitor Services tab** để theo dõi containers
3. **Use Docker Tool Window** để quản lý containers
4. **Check logs** khi có vấn đề
5. **Test locally first** trước khi deploy Docker

## 🎉 Success Checklist

- [ ] Docker image build thành công
- [ ] Containers start thành công
- [ ] Local API hoạt động (`localhost:8080`)
- [ ] Ngrok API hoạt động (`2be318c6f89a.ngrok-free.app`)
- [ ] FE team có thể call API qua ngrok
- [ ] Swagger UI hoạt động
- [ ] Database connection ổn định
