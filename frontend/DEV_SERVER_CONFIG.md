# Angular Development Server Configuration

Bu dosya Angular development server'ının otomatik reload özelliklerini açıklamaktadır.

## 🔄 Otomatik Reload Özellikleri

### 1. **Live Reload** ✅
- Dosya değişikliklerinde sayfa otomatik yenilenir
- Tarayıcı full refresh yapar
- Uygulama state'i kaybolur

### 2. **Hot Module Replacement (HMR)** 🔥
- Sadece değişen modüller güncellenir
- Sayfa yenilenmeden değişiklikler uygulanır
- Uygulama state'i korunur (experimental)

## 🚀 Kullanılabilir Komutlar

```bash
# Standart dev server (Live Reload ile)
npm run start
npm run dev

# Tüm network'e açık server
npm run serve

# Hot Module Replacement ile (experimental)
npm run serve-hmr

# Watch mode ile build
npm run watch
```

## ⚙️ Konfigürasyon Detayları

### Angular.json ayarları:
- `liveReload: true` - Otomatik sayfa yenileme
- `watch: true` - Dosya değişikliklerini izleme
- `poll: 1000` - 1 saniyede bir dosya kontrolü
- `port: 4200` - Development server portu
- `open: true` - Tarayıcıyı otomatik açma

### Package.json script'leri:
- `start` - Standart development server
- `dev` - Live reload ile gelişmiş server
- `serve` - Network'e açık server
- `serve-hmr` - Hot Module Replacement ile
- `watch` - Watch mode ile build

## 🔧 VS Code Entegrasyonu

### Task'lar (Ctrl+Shift+P -> Tasks: Run Task):
1. **Angular: Start Dev Server** - Ana development server
2. **Angular: Serve with HMR** - HMR ile server
3. **Angular: Build Development** - Development build
4. **Angular: Build Production** - Production build
5. **Angular: Watch Build** - Watch mode build

### Debug konfigürasyonu:
- Chrome debugger desteği
- Source map desteği
- Breakpoint desteği

## 📁 İzlenen Dosyalar

Angular CLI otomatik olarak şu dosyaları izler:

- `src/**/*.ts` - TypeScript dosyaları
- `src/**/*.html` - Template dosyaları
- `src/**/*.scss` - Stil dosyaları
- `src/**/*.css` - CSS dosyaları
- `angular.json` - Angular konfigürasyonu
- `package.json` - Dependency'ler
- `tsconfig.json` - TypeScript konfigürasyonu

## 🚨 Önemli Notlar

1. **Server başlatma**: `npm run dev` komutunu kullanın
2. **Port değişikliği**: angular.json'da port ayarını değiştirin
3. **Network erişimi**: `npm run serve` ile diğer cihazlardan erişim
4. **Performance**: Büyük projelerde poll değerini artırın
5. **Memory**: Node.js memory limit'i artırılabilir

## 🔍 Sorun Giderme

### Server başlamıyor:
```bash
# Port kontrolü
netstat -ano | findstr :4200

# Cache temizleme
npm run ng cache clean
```

### Reload çalışmıyor:
1. Tarayıcı cache'ini temizleyin
2. Angular CLI cache'ini temizleyin: `ng cache clean`
3. node_modules'ü yeniden kurun: `rm -rf node_modules && npm install`

### HMR çalışmıyor:
- HMR henüz experimental özellik
- Her zaman çalışmayabilir
- Live reload'a geri dönün

## 📈 Performance İpuçları

1. **Dosya sayısını azaltın**: Gereksiz dosyaları .gitignore'a ekleyin
2. **Poll interval'ı artırın**: Yavaş sistemlerde poll: 2000
3. **Source map'leri kapatın**: Production build'de
4. **Memory'yi artırın**: `--max-old-space-size=8192`

## 🎯 Önerilen Workflow

1. `npm run dev` ile server'ı başlatın
2. VS Code'da dosyaları düzenleyin
3. Tarayıcıda otomatik güncellemeleri görün
4. Debug için VS Code debugger'ı kullanın
5. Build için `npm run build-dev` çalıştırın
