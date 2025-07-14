# Lokasyon ve Ulaşım Yönetim Sistemi

Bu Angular uygulaması, lokasyonları ve ulaşım araçlarını yönetmek ve aralarında route hesaplamalarını yapmak için geliştirilmiştir.

## Özellikler

### 1. 📍 Lokasyon Yönetimi
- Lokasyon ekleme, düzenleme, silme
- Enlem/boylam koordinatları ile lokasyon tanımlama
- Adres bilgisi ekleme

### 2. 🚍 Ulaşım Araçları Yönetimi
- Farklı ulaşım türleri (otobüs, metro, tren, taksi, yürüyüş, araba)
- Kapasite, hız ve km başına fiyat bilgileri
- Ulaşım aracı ekleme, düzenleme, silme

### 3. 🗺️ Route Hesaplama
- Başlangıç ve bitiş lokasyonu seçimi
- Ulaşım aracı seçimi (isteğe bağlı)
- Mesafe, süre ve maliyet hesaplaması
- Çoklu route seçenekleri

## Proje Yapısı

```
src/
├── app/
│   ├── components/
│   │   ├── locations/           # Lokasyon yönetimi
│   │   ├── transports/          # Ulaşım araçları yönetimi
│   │   └── route-calculator/    # Route hesaplama
│   ├── models/                  # TypeScript modelleri
│   │   ├── location.model.ts
│   │   ├── transport.model.ts
│   │   └── route.model.ts
│   ├── services/                # API servisleri
│   │   ├── location.service.ts
│   │   ├── transport.service.ts
│   │   └── route.service.ts
│   ├── app.component.*          # Ana component
│   └── app.routes.ts            # Routing konfigürasyonu
├── styles.scss                 # Global stiller
└── main.ts                     # Uygulama başlatma
```

## Kurulum

1. Proje bağımlılıklarını yükleyin:
```bash
npm install
```

2. Geliştirme sunucusunu başlatın:
```bash
npm start
```

3. Tarayıcınızda `http://localhost:4200` adresine gidin.

## Backend API Konfigürasyonu

Backend API URL'lerini aşağıdaki dosyalarda güncelleyin:

- `src/app/services/location.service.ts` - Lokasyon API'si
- `src/app/services/transport.service.ts` - Ulaşım API'si  
- `src/app/services/route.service.ts` - Route hesaplama API'si

Varsayılan API URL: `http://localhost:3000/api`

## API Endpoint'leri

### Lokasyon API
- `GET /api/locations` - Tüm lokasyonları listele
- `GET /api/locations/:id` - Belirli lokasyonu getir
- `POST /api/locations` - Yeni lokasyon oluştur
- `PUT /api/locations/:id` - Lokasyonu güncelle
- `DELETE /api/locations/:id` - Lokasyonu sil

### Ulaşım API
- `GET /api/transports` - Tüm ulaşım araçlarını listele
- `GET /api/transports/:id` - Belirli ulaşım aracını getir
- `POST /api/transports` - Yeni ulaşım aracı oluştur
- `PUT /api/transports/:id` - Ulaşım aracını güncelle
- `DELETE /api/transports/:id` - Ulaşım aracını sil

### Route API
- `POST /api/routes/calculate` - Route hesapla

## Geliştirme

Yeni özellik eklemek için:

1. İlgili modeli `src/app/models/` klasöründe tanımlayın
2. Servisi `src/app/services/` klasöründe oluşturun
3. Component'i `src/app/components/` klasöründe oluşturun
4. Route'u `src/app/app.routes.ts` dosyasına ekleyin

## Build

Üretim için build almak için:

```bash
npm run build
```

Build dosyaları `dist/` klasöründe oluşturulacaktır.

## Teknolojiler

- **Angular 17** - Frontend framework
- **TypeScript** - Programming language
- **SCSS** - Styling
- **RxJS** - Reactive programming
- **Angular Router** - Routing
- **Angular Forms** - Form handling

## Responsive Tasarım

Uygulama mobil ve desktop cihazlarda çalışmak üzere responsive olarak tasarlanmıştır.

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.
