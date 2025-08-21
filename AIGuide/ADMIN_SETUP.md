# 👑 Admin User Setup Guide

## Vấn đề về Password trong Firebase

Firebase Authentication **không lưu password dạng plain text**. Tất cả password đều được **mã hóa tự động** bởi Firebase. 

Database SQL Server của chúng ta **chỉ lưu Firebase UID**, không lưu password.

## 🔥 Cách tạo Admin User

### Bước 1: Tạo Admin trong Firebase Console

1. Truy cập [Firebase Console](https://console.firebase.google.com/)
2. Chọn project `gym-bowling-app`
3. Vào **Authentication** > **Users**
4. Click **"Add user"**
5. Nhập thông tin:
   - **Email**: `admin@gym.com`
   - **Password**: `addmini123`
6. Click **"Add user"**

### Bước 2: Lấy UID và cập nhật database

1. Sau khi tạo user, copy **User UID** từ Firebase Console
2. Cập nhật trong SQL Server:

```sql
-- Cập nhật admin user với UID thực từ Firebase
UPDATE users 
SET firebase_uid = 'UID_TỪ_FIREBASE_CONSOLE'  -- Thay bằng UID thực
WHERE email = 'admin@gym.com';
```

### Bước 3: Test Admin Login

```html
<!-- Test admin login -->
<script>
firebase.auth().signInWithEmailAndPassword('admin@gym.com', 'addmini123')
  .then(async (userCredential) => {
    const user = userCredential.user;
    const idToken = await user.getIdToken();
    
    // Test backend API
    fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ token: idToken })
    })
    .then(res => res.json())
    .then(data => {
      console.log('Admin login success:', data);
      // data.user.role should be 'ADMIN'
    });
  })
  .catch(error => console.error('Login failed:', error));
</script>
```

## 🛠 Alternative: Tự động tạo Admin qua API

Bạn cũng có thể tạo API endpoint để tự động tạo admin:

### Thêm vào AuthController:

```java
@PostMapping("/create-admin")
public ResponseEntity<?> createAdmin(@RequestBody Map<String, String> request) {
    try {
        String token = request.get("token");
        String newRole = request.get("role"); // "ADMIN" hoặc "STAFF"
        
        // Verify token và get user
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
        String firebaseUid = decodedToken.getUid();
        
        // Tìm user trong database
        User user = userService.findByFirebaseUid(firebaseUid)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        // Chỉ ADMIN mới có thể tạo ADMIN/STAFF khác
        if (!user.getRole().equals(User.Role.ADMIN)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Only admin can create admin/staff"));
        }
        
        // Update role
        user.setRole(User.Role.valueOf(newRole));
        userService.save(user);
        
        return ResponseEntity.ok(Map.of("message", "Role updated successfully"));
        
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "Failed to update role: " + e.getMessage()));
    }
}
```

## 📋 Summary

1. **Password**: Firebase tự động mã hóa, không cần lo lắng
2. **Admin User**: 
   - Email: `admin@gym.com`
   - Password: `addmini123` 
   - Role: `ADMIN`
3. **Database**: Chỉ lưu Firebase UID, không lưu password
4. **Login Flow**: Firebase Auth → Backend verify token → Return user info với role

## ⚠️ Lưu ý bảo mật

- Đổi password admin sau khi setup xong
- Không hardcode password trong code
- Sử dụng environment variables cho production
- Định kỳ rotate Firebase service account keys
