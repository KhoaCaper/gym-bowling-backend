# 🔒 SECURITY NOTES - IMPORTANT!

## ⚠️ KHÔNG BAO GIỜ COMMIT:

### ❌ Files cấm commit:
- `src/main/resources/application.properties` (chứa credentials thật)
- `src/main/resources/application-simple.properties` (testing config)
- `src/main/resources/application-team.properties` (testing config)
- `src/main/resources/firebase-service-account.json` (Firebase keys)
- Bất kỳ file nào chứa passwords, API keys, secrets

### ✅ Files an toàn để commit:
- `src/main/resources/application.properties.template`
- `src/main/java/` (source code)
- `build.gradle`, `settings.gradle`
- `README.md`, `SECURITY_NOTES.md`

## 🚨 Trước khi commit:

1. **Kiểm tra git status:**
   ```bash
   git status
   ```

2. **Chỉ commit source code:**
   ```bash
   git add src/main/java/
   git add build.gradle
   git add README.md
   git add SECURITY_NOTES.md
   ```

3. **KHÔNG commit:**
   ```bash
   # ĐỪNG làm những điều này!
   git add src/main/resources/application.properties
   git add src/main/resources/firebase-service-account.json
   ```

## 🔄 Để test locally:

1. Copy `application.properties.template` thành `application.properties`
2. Điền credentials thật vào `application.properties`
3. File này sẽ bị git ignore

## 📝 Commit message an toàn:

```bash
git commit -m "feat: add package plan detail functionality

- Add PackagePlanDetail entity and repository
- Add PackagePlanDetail service and controller
- Update security config for team testing
- Add comprehensive API documentation"
```
