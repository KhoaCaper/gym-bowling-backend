# 🔥 Firebase Setup Guide

## Bước 1: Tạo Firebase Project

1. Truy cập [Firebase Console](https://console.firebase.google.com/)
2. Click **"Create a project"** hoặc **"Add project"**
3. Nhập tên project: `gym-bowling-app`
4. Bỏ tick **"Enable Google Analytics"** (không cần thiết)
5. Click **"Create project"**

## Bước 2: Setup Authentication

1. Trong Firebase Console, click **"Authentication"** ở sidebar trái
2. Click tab **"Sign-in method"**
3. Enable các provider cần thiết:
   - ✅ **Email/Password**
   - ✅ **Google** (optional)
   - ✅ **Phone** (optional)

## Bước 3: Tạo Service Account

1. Click **⚙️ Settings** > **"Project settings"**
2. Click tab **"Service accounts"**
3. Click **"Generate new private key"**
4. Confirm bằng cách click **"Generate key"**
5. File JSON sẽ được download về máy

## Bước 4: Setup Backend

1. **Đổi tên file** JSON vừa download thành: `firebase-service-account.json`
2. **Copy file** vào thư mục: `src/main/resources/`
3. **Cấu trúc file** sẽ như thế này:

```
src/
  main/
    resources/
      application.properties
      firebase-service-account.json  ← File này
```

## Bước 5: Setup Frontend (Web)

1. Trong Firebase Console, click **⚙️ Settings** > **"General"**
2. Scroll xuống **"Your apps"**, click **"Web app"** icon `</>`
3. Nhập app nickname: `gym-bowling-web`
4. Click **"Register app"**
5. Copy **Firebase config object**:

```javascript
// Firebase config cho frontend
const firebaseConfig = {
  apiKey: "AIza...",
  authDomain: "gym-bowling-app.firebaseapp.com",
  projectId: "gym-bowling-app",
  storageBucket: "gym-bowling-app.appspot.com",
  messagingSenderId: "123456789",
  appId: "1:123456789:web:abcdef"
};
```

## Bước 6: Test Firebase Connection

### Backend Test
```bash
# Chạy ứng dụng
./gradlew bootRun

# Kiểm tra log, không có lỗi Firebase = OK
```

### Frontend Test (HTML đơn giản)
```html
<!DOCTYPE html>
<html>
<head>
    <title>Firebase Test</title>
    <script src="https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js"></script>
    <script src="https://www.gstatic.com/firebasejs/9.0.0/firebase-auth-compat.js"></script>
</head>
<body>
    <h1>Firebase Auth Test</h1>
    <div id="auth-section">
        <input type="email" id="email" placeholder="Email">
        <input type="password" id="password" placeholder="Password">
        <button onclick="signUp()">Sign Up</button>
        <button onclick="signIn()">Sign In</button>
        <button onclick="signOut()">Sign Out</button>
    </div>
    <div id="user-info"></div>

    <script>
        // Firebase config (thay bằng config của bạn)
        const firebaseConfig = {
            apiKey: "YOUR_API_KEY",
            authDomain: "your-project.firebaseapp.com",
            projectId: "your-project",
            // ... other config
        };

        firebase.initializeApp(firebaseConfig);
        const auth = firebase.auth();

        // Sign Up
        async function signUp() {
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            try {
                const result = await auth.createUserWithEmailAndPassword(email, password);
                console.log('User created:', result.user);
                testBackendLogin(result.user);
            } catch (error) {
                console.error('Sign up error:', error);
            }
        }

        // Sign In
        async function signIn() {
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            try {
                const result = await auth.signInWithEmailAndPassword(email, password);
                console.log('User signed in:', result.user);
                testBackendLogin(result.user);
            } catch (error) {
                console.error('Sign in error:', error);
            }
        }

        // Sign Out
        async function signOut() {
            try {
                await auth.signOut();
                console.log('User signed out');
                document.getElementById('user-info').innerHTML = '';
            } catch (error) {
                console.error('Sign out error:', error);
            }
        }

        // Test Backend Login
        async function testBackendLogin(user) {
            try {
                const idToken = await user.getIdToken();
                
                const response = await fetch('http://localhost:8080/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        token: idToken,
                        phone: '0123456789'
                    })
                });
                
                const data = await response.json();
                console.log('Backend response:', data);
                
                document.getElementById('user-info').innerHTML = `
                    <h3>User Info:</h3>
                    <p>Email: ${user.email}</p>
                    <p>UID: ${user.uid}</p>
                    <p>Backend Status: ${data.message || 'Error'}</p>
                `;
            } catch (error) {
                console.error('Backend test error:', error);
            }
        }

        // Auth state listener
        auth.onAuthStateChanged((user) => {
            if (user) {
                console.log('User is signed in:', user.email);
            } else {
                console.log('User is signed out');
            }
        });
    </script>
</body>
</html>
```

## Bước 7: Troubleshooting

### Lỗi thường gặp:

1. **"Failed to initialize Firebase"**
   - ✅ Kiểm tra file `firebase-service-account.json` có đúng vị trí
   - ✅ Kiểm tra file có đúng format JSON

2. **"Invalid Firebase token"**
   - ✅ Kiểm tra token có được generate từ đúng project
   - ✅ Kiểm tra thời gian token (có thể đã expire)

3. **CORS Error**
   - ✅ Backend đã config CORS trong `SecurityConfig.java`
   - ✅ Frontend gọi đúng URL `http://localhost:8080`

### Kiểm tra setup thành công:

1. ✅ Firebase Console hiển thị users khi đăng ký
2. ✅ Backend log không có lỗi Firebase
3. ✅ API `/api/auth/login` trả về user info
4. ✅ Swagger UI có thể truy cập: `http://localhost:8080/swagger-ui.html`

## 🎯 Quick Test Commands

```bash
# 1. Chạy backend
./gradlew bootRun

# 2. Test API không cần auth
curl http://localhost:8080/api/packages

# 3. Truy cập Swagger
# http://localhost:8080/swagger-ui.html

# 4. Test Firebase (cần có token từ frontend)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"token":"YOUR_FIREBASE_TOKEN","phone":"0123456789"}'
```
