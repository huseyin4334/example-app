import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BaseApiService } from '../core/base-api.service';

@Injectable({
  providedIn: 'root'
})
export class ExampleApiUsageService {

  constructor(private baseApi: BaseApiService) {
    // Base URL'i environment'a göre ayarlayabilirsiniz
    this.baseApi.setBaseUrl('http://localhost:3000/api');
  }

  // **TEMEL KULLANIM ÖRNEKLERİ**

  // 1. Basit GET request
  getUsers(): Observable<any[]> {
    return this.baseApi.get<any[]>('users');
  }

  // 2. ID ile tek kayıt alma
  getUserById(id: number): Observable<any> {
    return this.baseApi.get<any>(`users/${id}`);
  }

  // 3. POST request - yeni kayıt oluşturma
  createUser(userData: any): Observable<any> {
    return this.baseApi.post<any>('users', userData);
  }

  // 4. PUT request - kayıt güncelleme
  updateUser(id: number, userData: any): Observable<any> {
    return this.baseApi.put<any>(`users/${id}`, userData);
  }

  // 5. DELETE request - kayıt silme
  deleteUser(id: number): Observable<void> {
    return this.baseApi.delete<void>(`users/${id}`);
  }

  // **GELİŞMİŞ KULLANIM ÖRNEKLERİ**

  // 6. Query parameters ile GET
  searchUsers(searchTerm: string, status: string): Observable<any[]> {
    return this.baseApi.getWithParams<any[]>('users/search', {
      q: searchTerm,
      status: status,
      active: true
    });
  }

  // 7. Pagination ile veri alma
  getUsersPaginated(page: number = 1, limit: number = 10): Observable<any> {
    return this.baseApi.getPaginated('users', page, limit);
  }

  // 8. Custom headers ile request
  getUsersWithCustomHeaders(): Observable<any[]> {
    return this.baseApi.get<any[]>('users', {
      headers: {
        'X-Custom-Header': 'custom-value',
        'Accept-Language': 'tr-TR'
      }
    });
  }

  // 9. File upload
  uploadUserAvatar(userId: number, file: File): Observable<any> {
    return this.baseApi.uploadFile<any>(`users/${userId}/avatar`, file);
  }

  // 10. File download
  downloadUserReport(userId: number): Observable<Blob> {
    return this.baseApi.downloadFile(`users/${userId}/report`, `user-${userId}-report.pdf`);
  }

  // **AUTHENTİCATİON ÖRNEKLERİ**

  // 11. Login
  login(credentials: { email: string, password: string }): Observable<any> {
    return this.baseApi.post<any>('auth/login', credentials);
  }

  // 12. Token ayarlama (login'den sonra)
  setAuthToken(token: string): void {
    this.baseApi.setAuthToken(token);
  }

  // 13. Logout
  logout(): Observable<any> {
    return this.baseApi.post<any>('auth/logout');
  }

  // 14. Token temizleme
  clearAuthToken(): void {
    this.baseApi.removeAuthToken();
  }

  // **COMPLEX ÖRNEKLERİ**

  // 15. Batch operations
  batchUpdateUsers(userUpdates: any[]): Observable<any> {
    return this.baseApi.post<any>('users/batch-update', { updates: userUpdates });
  }

  // 16. Filter ve sort ile veri alma
  getFilteredUsers(filters: any): Observable<any> {
    return this.baseApi.getWithParams<any>('users', {
      ...filters,
      sort: 'created_at',
      order: 'desc'
    });
  }

  // 17. Form data ile POST
  createUserWithFormData(userData: any, profileImage?: File): Observable<any> {
    if (profileImage) {
      return this.baseApi.uploadFile<any>('users', profileImage, userData);
    } else {
      return this.baseApi.post<any>('users', userData);
    }
  }

  // **ERROR HANDLING ÖRNEĞİ**
  
  // 18. Error handling ile kullanım
  getUsersWithErrorHandling(): Observable<any[]> {
    return this.baseApi.get<any[]>('users');
    // Error handling BaseApiService'de otomatik olarak yapılır
    // Component'te .subscribe() içinde error callback'i kullanabilirsiniz
  }

  // **USAGE EXAMPLE IN COMPONENT:**
  /*
  constructor(private exampleService: ExampleApiUsageService) {}

  loadUsers() {
    this.exampleService.getUsers().subscribe({
      next: (users) => {
        console.log('Users loaded:', users);
        this.users = users;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        // Error mesajı BaseApiService'den gelir
      }
    });
  }

  searchUsers() {
    this.exampleService.searchUsers('john', 'active').subscribe({
      next: (results) => {
        this.searchResults = results;
      },
      error: (error) => {
        console.error('Search failed:', error);
      }
    });
  }

  uploadFile(file: File) {
    this.exampleService.uploadUserAvatar(123, file).subscribe({
      next: (response) => {
        console.log('File uploaded successfully:', response);
      },
      error: (error) => {
        console.error('Upload failed:', error);
      }
    });
  }
  */
}
