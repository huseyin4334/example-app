# Location Transport App

Bu proje, lokasyon ve ulaşım bilgilerini yönetmek ve rota hesaplaması yapmak için geliştirilmiş bir web uygulamasıdır.

## Teknolojiler

### Backend
- Java 17
- Spring Boot 3
- Spring Data JPA
- H2 Database (Development)
- Maven
- Docker

### Frontend
- Angular 15
- TypeScript
- SCSS
- ngx-toastr (Bildirimler için)
- Bootstrap

## Kurulum ve Çalıştırma

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

### Docker ile Çalıştırma
```bash
docker-compose up
```

## API Dokümantasyonu

Base URL: `http://localhost:8080/`

### 🏢 Location Management API

#### 1. Location Listesi (Arama ile)
```http
GET /api/locations/search?searchTerm=istanbul&pageNo=0&pageSize=10
```

**Response:**
```json
{
  "items": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "İstanbul Havalimanı",
      "city": "İstanbul",
      "locationCode": "IST",
      "createdAt": "2023-07-16T10:30:00Z"
    }
  ],
  "page": 0,
  "pageSize": 10,
  "total": 1,
  "totalPages": 1
}
```

#### 2. Location Oluşturma
```http
POST /api/locations
Content-Type: application/json

{
  "name": "Sabiha Gökçen Havalimanı",
  "city": 1,
  "locationCode": "SAW"
}
```

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174001",
  "name": "Sabiha Gökçen Havalimanı",
  "city": "İstanbul",
  "locationCode": "SAW",
  "createdAt": "2023-07-16T10:35:00Z"
}
```

#### 3. Location Güncelleme
```http
PUT /api/locations/{id}
Content-Type: application/json

{
  "id": "123e4567-e89b-12d3-a456-426614174001",
  "name": "Sabiha Gökçen Havalimanı - Terminal 1",
  "city": 1,
  "locationCode": "SAW"
}
```

#### 4. Location Silme
```http
DELETE /api/locations/{id}
```

#### 5. Tüm Searchable Location'ları Getir
```http
GET /api/locations/all
```

**Response:**
```json
{
  "items": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "İstanbul Havalimanı"
    },
    {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "name": "Sabiha Gökçen Havalimanı"
    }
  ],
  "page": 0,
  "pageSize": 100,
  "total": 2,
  "totalPages": 1
}
```

#### 6. Ülkeler ve Şehirler Listesi
```http
GET /api/locations/countries
```

**Response:**
```json
{
  "items": [
    {
      "id": 1,
      "name": "Türkiye",
      "cities": [
        {
          "id": 1,
          "name": "İstanbul"
        },
        {
          "id": 2,
          "name": "Ankara"
        }
      ]
    }
  ],
  "page": 0,
  "pageSize": 100,
  "total": 1,
  "totalPages": 1
}
```

### 🚌 Transportation Management API

#### 1. Transportation Listesi (Arama ile)
```http
GET /api/transportations/search?searchTerm=otobüs&pageNo=0&pageSize=10
```

**Response:**
```json
{
  "items": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174100",
      "origin": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "name": "İstanbul Havalimanı"
      },
      "destination": {
        "id": "123e4567-e89b-12d3-a456-426614174001",
        "name": "Sabiha Gökçen Havalimanı"
      },
      "transportType": "BUS",
      "availableDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
      "createdAt": "2023-07-16T10:40:00Z"
    }
  ],
  "page": 0,
  "pageSize": 10,
  "total": 1,
  "totalPages": 1
}
```

#### 2. Transportation Oluşturma
```http
POST /api/transportations
Content-Type: application/json

{
  "origin": "123e4567-e89b-12d3-a456-426614174000",
  "destination": "123e4567-e89b-12d3-a456-426614174001",
  "transportationType": 1,
  "availableDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]
}
```

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174100",
  "origin": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "İstanbul Havalimanı"
  },
  "destination": {
    "id": "123e4567-e89b-12d3-a456-426614174001",
    "name": "Sabiha Gökçen Havalimanı"
  },
  "transportType": "BUS",
  "availableDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
  "createdAt": "2023-07-16T10:45:00Z"
}
```

#### 3. Transportation Güncelleme
```http
PUT /api/transportations/{id}
Content-Type: application/json

{
  "id": "123e4567-e89b-12d3-a456-426614174100",
  "origin": "123e4567-e89b-12d3-a456-426614174000",
  "destination": "123e4567-e89b-12d3-a456-426614174001",
  "transportationType": 1,
  "availableDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"]
}
```

#### 4. Transportation Silme
```http
DELETE /api/transportations/{id}
```

#### 5. Transportation Türleri Listesi
```http
GET /api/transportations/types
```

**Response:**
```json
{
  "items": [
    {
      "id": 1,
      "name": "BUS"
    },
    {
      "id": 2,
      "name": "FLIGHT"
    },
    {
      "id": 3,
      "name": "UBER"
    },
    {
      "id": 4,
      "name": "SUBWAY"
    }
  ],
  "page": 0,
  "pageSize": 10,
  "total": 4,
  "totalPages": 1
}
```

### 🗺️ Route Calculation API

#### Rota Hesaplama
```http
POST /api/routes
Content-Type: application/json

{
  "originId": "123e4567-e89b-12d3-a456-426614174000",
  "destinationId": "123e4567-e89b-12d3-a456-426614174001",
  "selectedDate": "2023-07-17"
}
```

**Response:**
```json
{
  "origin": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "İstanbul Havalimanı"
  },
  "destination": {
    "id": "123e4567-e89b-12d3-a456-426614174001",
    "name": "Sabiha Gökçen Havalimanı"
  },
  "selectedDate": "2023-07-17",
  "availableRoutes": [
    {
      "transportationType": "BUS",
      "estimatedDuration": "45 dakika",
      "description": "Otobüs ile direkt ulaşım"
    },
    {
      "transportationType": "UBER",
      "estimatedDuration": "35 dakika",
      "description": "Uber ile hızlı ulaşım"
    }
  ]
}
```

## Hata Yönetimi

Uygulama, tüm HTTP hatalarını ngx-toastr kullanarak kullanıcıya bildirir:

### Hata Türleri:
- **400 Bad Request**: "Geçersiz istek. Lütfen girdiğiniz bilgileri kontrol edin."
- **401 Unauthorized**: "Yetkisiz erişim. Lütfen giriş yapın."
- **403 Forbidden**: "Bu işlemi gerçekleştirmek için yetkiniz bulunmuyor."
- **404 Not Found**: "İstenen kaynak bulunamadı."
- **500 Internal Server Error**: "Sunucu hatası. Lütfen daha sonra tekrar deneyin."
- **503 Service Unavailable**: "Servis şu anda kullanılamıyor. Lütfen daha sonra tekrar deneyin."

### Başarı Bildirimleri:
- Veri yükleme işlemleri
- CRUD operasyonları (Create, Update, Delete)
- Form sıfırlama işlemleri

### Uyarı Bildirimleri:
- Form validasyon hataları
- Eksik alan uyarıları

## Postman Collection

Postman kullanarak API'leri test etmek için aşağıdaki collection'ı kullanabilirsiniz:

### Dosyalar:
- **Collection**: `Location-Transport-App-API.postman_collection.json`
- **Environment**: `Location-Transport-App-Local.postman_environment.json`

### Kurulum:
1. Postman'da **Import** butonuna tıklayın
2. **Upload Files** seçeneğini kullanarak her iki dosyayı da import edin
3. Environment'ı aktif hale getirin (sağ üst köşeden seçim yapın)

### Environment Variables
```json
{
  "baseUrl": "http://localhost:8080/api",
  "locationId": "123e4567-e89b-12d3-a456-426614174000",
  "transportationId": "123e4567-e89b-12d3-a456-426614174100",
  "originId": "123e4567-e89b-12d3-a456-426614174000",
  "destinationId": "123e4567-e89b-12d3-a456-426614174001"
}
```

### Collection İçeriği:
📁 **🏢 Location Management** (6 API)
- Search Locations
- Create Location  
- Update Location
- Delete Location
- Get All Searchable Locations
- Get Countries with Cities

📁 **🚌 Transportation Management** (5 API)
- Search Transportations
- Create Transportation
- Update Transportation
- Delete Transportation
- Get Transportation Types

📁 **🗺️ Route Calculation** (1 API)
- Calculate Route

### Kullanım İpuçları:
- Tüm isteklerde örnek response'lar mevcuttur
- Environment variable'ları kullanarak ID'leri dinamik olarak değiştirebilirsiniz
- Create işlemlerinden dönen ID'leri kopyalayıp environment'a kaydedin
- Search işlemlerinde query parametreleri opsiyoneldir

## Frontend Kullanımı

### Sayfalar:
1. **Locations** (`/locations`): Lokasyon yönetimi
2. **Transportations** (`/transports`): Ulaşım bilgileri yönetimi
3. **Route Calculator** (`/route-calculator`): Rota hesaplama

### Özellikler:
- Sayfalama (Pagination)
- Arama (Search)
- CRUD operasyonları
- Form validasyonu
- Hata ve başarı bildirimleri (Toastr)
- Responsive tasarım

## Geliştirici Notları

### Backend Geliştirme:
```bash
# Test çalıştırma
./mvnw test

# Paket oluşturma
./mvnw clean package

# Docker image oluşturma
docker build -t location-transport-backend .
```

### Frontend Geliştirme:
```bash
# Development server
npm run dev

# Production build
npm run build-prod

# Test çalıştırma
npm test

# Linting
ng lint
```

## Veritabanı Şeması

### Location Tablosu
- id (UUID)
- name (VARCHAR)
- city_id (BIGINT)
- location_code (VARCHAR)
- created_at (TIMESTAMP)

### Transportation Tablosu
- id (UUID)
- origin_id (UUID)
- destination_id (UUID)
- transportation_type_id (BIGINT)
- created_at (TIMESTAMP)

### Transportation_Available_Days Tablosu
- transportation_id (UUID)
- available_days (VARCHAR)

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.
