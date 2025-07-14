# BaseApiService Kullanım Kılavuzu

Bu kılavuz, projedeki `BaseApiService` sınıfının nasıl kullanılacağını açıklamaktadır.

## 🚀 Temel Özellikler

- **Tüm HTTP metodları**: GET, POST, PUT, PATCH, DELETE
- **Otomatik hata yönetimi**: Merkezi error handling
- **Token yönetimi**: JWT token desteği
- **File operations**: Upload/Download desteği
- **Query parameters**: Otomatik parametre işleme
- **Pagination**: Sayfalama desteği
- **Environment integration**: Çevre değişkenleri desteği

## 📝 Temel Kullanım

### 1. Service'i inject edin:

```typescript
import { BaseApiService } from '../core/base-api.service';

@Injectable({
  providedIn: 'root'
})
export class MyService {
  constructor(private baseApi: BaseApiService) {}
}
```

### 2. HTTP İşlemleri:

```typescript
// GET Request
getUsers(): Observable<User[]> {
  return this.baseApi.get<User[]>('users');
}

// POST Request (Create)
createUser(user: User): Observable<User> {
  return this.baseApi.post<User>('users', user);
}

// PUT Request (Update)
updateUser(id: number, user: User): Observable<User> {
  return this.baseApi.put<User>(`users/${id}`, user);
}

// DELETE Request
deleteUser(id: number): Observable<void> {
  return this.baseApi.delete<void>(`users/${id}`);
}
```

## 🔍 Gelişmiş Kullanım

### Query Parameters ile:

```typescript
// Arama yapma
searchUsers(searchTerm: string): Observable<User[]> {
  return this.baseApi.getWithParams<User[]>('users/search', {
    q: searchTerm,
    active: true,
    limit: 50
  });
}

// Pagination
getUsersPaginated(page: number = 1): Observable<any> {
  return this.baseApi.getPaginated('users', page, 10);
}
```

### File Operations:

```typescript
// File Upload
uploadAvatar(userId: number, file: File): Observable<any> {
  return this.baseApi.uploadFile(`users/${userId}/avatar`, file);
}

// File Download
downloadReport(): Observable<Blob> {
  return this.baseApi.downloadFile('reports/monthly', 'monthly-report.pdf');
}
```

### Authentication:

```typescript
// Login
login(credentials: LoginRequest): Observable<AuthResponse> {
  return this.baseApi.post<AuthResponse>('auth/login', credentials);
}

// Token kaydetme
setToken(token: string): void {
  this.baseApi.setAuthToken(token);
}

// Token temizleme  
logout(): void {
  this.baseApi.removeAuthToken();
}
```

## 🎯 Component'te Kullanım

```typescript
export class UserComponent implements OnInit {
  users: User[] = [];
  loading = false;

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: (error) => {
        console.error('Kullanıcılar yüklenirken hata:', error);
        this.loading = false;
        // Error mesajı BaseApiService'den otomatik gelir
      }
    });
  }

  searchUsers(searchTerm: string) {
    this.userService.searchUsers(searchTerm).subscribe({
      next: (results) => {
        this.users = results;
      },
      error: (error) => {
        console.error('Arama hatası:', error);
      }
    });
  }

  createUser(userData: User) {
    this.userService.createUser(userData).subscribe({
      next: (newUser) => {
        this.users.push(newUser);
        console.log('Kullanıcı oluşturuldu:', newUser);
      },
      error: (error) => {
        console.error('Kullanıcı oluşturulurken hata:', error);
      }
    });
  }
}
```

## ⚙️ Konfigürasyon

### Base URL değiştirme:

```typescript
constructor(private baseApi: BaseApiService) {
  // Runtime'da URL değiştirme
  this.baseApi.setBaseUrl('https://api.example.com');
}
```

### Environment ayarları:

```typescript
// src/environments/environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:3000/api'
};

// src/environments/environment.prod.ts  
export const environment = {
  production: true,
  apiUrl: 'https://production-api.com/api'
};
```

## 🛡️ Error Handling

BaseApiService otomatik error handling sağlar:

- **400**: Geçersiz istek
- **401**: Yetkisiz erişim  
- **403**: Yasaklanmış erişim
- **404**: Kaynak bulunamadı
- **500**: Sunucu hatası

Error mesajları console'a loglanır ve observable error olarak döner.

## 📋 Mevcut Endpoint'ler

Projenizde şu endpoint'ler kullanılabilir:

```typescript
// Lokasyonlar
GET    /locations
GET    /locations/:id
POST   /locations
PUT    /locations/:id
DELETE /locations/:id

// Ulaşım Araçları
GET    /transports
GET    /transports/:id
POST   /transports
PUT    /transports/:id
DELETE /transports/:id

// Route Hesaplama
POST   /routes/calculate
GET    /routes
```

## 💡 İpuçları

1. **Endpoint'leri string olarak verin**: `'users'` veya `'users/123'`
2. **Body verilerini object olarak gönderin**: `{ name: 'John', email: 'john@example.com' }`
3. **Type-safety için generic kullanın**: `get<User[]>('users')`
4. **Error handling'i component'te yapın**: `.subscribe()` içinde error callback
5. **Token'ları localStorage'da saklayın**: Otomatik olarak header'a eklenir

## 🔧 Özelleştirme

İhtiyacınıza göre BaseApiService'i genişletebilirsiniz:

```typescript
@Injectable()
export class CustomApiService extends BaseApiService {
  
  // Custom method ekleme
  bulkUpdate<T>(endpoint: string, items: any[]): Observable<T> {
    return this.post<T>(`${endpoint}/bulk-update`, { items });
  }

  // Custom error handling
  protected override handleError(error: HttpErrorResponse) {
    // Custom error logic
    return super.handleError(error);
  }
}
```
