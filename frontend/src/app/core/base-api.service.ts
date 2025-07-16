import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { CustomToastrService } from '../services/toastr.service';

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
  error?: string;
}

export interface RequestOptions {
  headers?: HttpHeaders | { [header: string]: string | string[] };
  params?: HttpParams | { [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean> };
  observe?: 'body';
  responseType?: 'json';
}

@Injectable({
  providedIn: 'root'
})
export class BaseApiService {
  private baseUrl = environment.apiUrl; // Environment'dan alır

  constructor(
    private http: HttpClient,
    private toastrService: CustomToastrService
  ) {}

  /**
   * Base URL'i ayarlar
   * @param url Yeni base URL
   */
  setBaseUrl(url: string): void {
    this.baseUrl = url.endsWith('/') ? url.slice(0, -1) : url;
  }

  /**
   * GET request
   * @param endpoint API endpoint (örn: '/users', '/users/123')
   * @param options Request options
   * @returns Observable<T>
   */
  get<T>(endpoint: string, options?: RequestOptions): Observable<T> {
    const url = this.buildUrl(endpoint);
    return this.http.get<T>(url, options).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * POST request
   * @param endpoint API endpoint
   * @param body Request body
   * @param options Request options
   * @returns Observable<T>
   */
  post<T>(endpoint: string, body: any = {}, options?: RequestOptions): Observable<T> {
    const url = this.buildUrl(endpoint);
    return this.http.post<T>(url, body, {
      ...options,
      headers: this.getHeaders(options?.headers)
    }).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * PUT request
   * @param endpoint API endpoint
   * @param body Request body
   * @param options Request options
   * @returns Observable<T>
   */
  put<T>(endpoint: string, body: any = {}, options?: RequestOptions): Observable<T> {
    const url = this.buildUrl(endpoint);
    return this.http.put<T>(url, body, {
      ...options,
      headers: this.getHeaders(options?.headers)
    }).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * PATCH request
   * @param endpoint API endpoint
   * @param body Request body
   * @param options Request options
   * @returns Observable<T>
   */
  patch<T>(endpoint: string, body: any = {}, options?: RequestOptions): Observable<T> {
    const url = this.buildUrl(endpoint);
    return this.http.patch<T>(url, body, {
      ...options,
      headers: this.getHeaders(options?.headers)
    }).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * DELETE request
   * @param endpoint API endpoint
   * @param options Request options
   * @returns Observable<T>
   */
  delete<T>(endpoint: string, options?: RequestOptions): Observable<T> {
    const url = this.buildUrl(endpoint);
    return this.http.delete<T>(url, options).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * File upload
   * @param endpoint API endpoint
   * @param file File to upload
   * @param additionalData Additional form data
   * @returns Observable<T>
   */
  uploadFile<T>(endpoint: string, file: File, additionalData?: { [key: string]: any }): Observable<T> {
    const url = this.buildUrl(endpoint);
    const formData = new FormData();
    formData.append('file', file);
    
    if (additionalData) {
      Object.keys(additionalData).forEach(key => {
        formData.append(key, additionalData[key]);
      });
    }

    return this.http.post<T>(url, formData).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Download file
   * @param endpoint API endpoint
   * @param filename Optional filename
   * @returns Observable<Blob>
   */
  downloadFile(endpoint: string, filename?: string): Observable<Blob> {
    const url = this.buildUrl(endpoint);
    return this.http.get(url, { 
      responseType: 'blob',
      observe: 'response'
    }).pipe(
      map(response => {
        if (filename) {
          const blob = new Blob([response.body as Blob]);
          const downloadUrl = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = downloadUrl;
          link.download = filename;
          link.click();
          window.URL.revokeObjectURL(downloadUrl);
        }
        return response.body as Blob;
      }),
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Query parameters ile GET request
   * @param endpoint API endpoint
   * @param queryParams Query parameters object
   * @returns Observable<T>
   */
  getWithParams<T>(endpoint: string, queryParams: { [key: string]: any }): Observable<T> {
    let params = new HttpParams();
    
    Object.keys(queryParams).forEach(key => {
      if (queryParams[key] !== null && queryParams[key] !== undefined) {
        params = params.set(key, queryParams[key].toString());
      }
    });

    return this.get<T>(endpoint, { params });
  }

  /**
   * Pagination ile GET request
   * @param endpoint API endpoint
   * @param page Sayfa numarası
   * @param limit Sayfa başına kayıt sayısı
   * @param additionalParams Ek parametreler
   * @returns Observable<T>
   */
  getPaginated<T>(
    endpoint: string, 
    page: number = 1, 
    limit: number = 10, 
    additionalParams?: { [key: string]: any }
  ): Observable<T> {
    const params = {
      page: page.toString(),
      limit: limit.toString(),
      ...additionalParams
    };
    
    return this.getWithParams<T>(endpoint, params);
  }

  /**
   * API endpoint'ine göre tam URL oluşturur
   * @param endpoint API endpoint
   * @returns Tam URL
   */
  private buildUrl(endpoint: string): string {
    const cleanEndpoint = endpoint.startsWith('/') ? endpoint.slice(1) : endpoint;
    return `${this.baseUrl}/${cleanEndpoint}`;
  }

  /**
   * Default headers'ı ayarlar
   * @param customHeaders Custom headers
   * @returns HttpHeaders
   */
  private getHeaders(customHeaders?: HttpHeaders | { [header: string]: string | string[] }): HttpHeaders {
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    if (customHeaders) {
      if (customHeaders instanceof HttpHeaders) {
        customHeaders.keys().forEach(key => {
          headers = headers.set(key, customHeaders.get(key) || '');
        });
      } else {
        Object.keys(customHeaders).forEach(key => {
          headers = headers.set(key, customHeaders[key] as string);
        });
      }
    }

    // JWT token varsa ekle
    const token = this.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  /**
   * Auth token'ı localStorage'dan alır
   * @returns Auth token or null
   */
  private getAuthToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  /**
   * Auth token'ı localStorage'a kaydeder
   * @param token Auth token
   */
  setAuthToken(token: string): void {
    localStorage.setItem('auth_token', token);
  }

  /**
   * Auth token'ı localStorage'dan kaldırır
   */
  removeAuthToken(): void {
    localStorage.removeItem('auth_token');
  }

  /**
   * HTTP hatalarını handle eder
   * @param error HTTP Error Response
   * @returns Observable error
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Bilinmeyen bir hata oluştu';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `İstemci Hatası: ${error.error.message}`;
    } else {
      // Server-side error
      switch (error.status) {
        case 400:
          errorMessage = 'Geçersiz istek. Lütfen girdiğiniz bilgileri kontrol edin.';
          break;
        case 401:
          errorMessage = 'Yetkisiz erişim. Lütfen giriş yapın.';
          break;
        case 403:
          errorMessage = 'Bu işlemi gerçekleştirmek için yetkiniz bulunmuyor.';
          break;
        case 404:
          errorMessage = 'İstenen kaynak bulunamadı.';
          break;
        case 500:
          errorMessage = 'Sunucu hatası. Lütfen daha sonra tekrar deneyin.';
          break;
        case 503:
          errorMessage = 'Servis şu anda kullanılamıyor. Lütfen daha sonra tekrar deneyin.';
          break;
        default:
          errorMessage = `Bir hata oluştu (${error.status}). Lütfen tekrar deneyin.`;
      }
      
      // Backend'den gelen spesifik hata mesajı varsa onu kullan
      if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.error?.error) {
        errorMessage = error.error.error;
      }
    }

    // Toastr ile hata mesajını göster
    this.toastrService.showError(errorMessage);

    console.error('HTTP Error:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
}
