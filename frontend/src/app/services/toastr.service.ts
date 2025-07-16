import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class CustomToastrService {

  constructor(private toastr: ToastrService) { }

  /**
   * Başarı mesajı gösterir
   * @param message Mesaj içeriği
   * @param title Başlık (opsiyonel)
   */
  showSuccess(message: string, title: string = 'Başarılı'): void {
    this.toastr.success(message, title);
  }

  /**
   * Hata mesajı gösterir
   * @param message Mesaj içeriği
   * @param title Başlık (opsiyonel)
   */
  showError(message: string, title: string = 'Hata'): void {
    this.toastr.error(message, title);
  }

  /**
   * Uyarı mesajı gösterir
   * @param message Mesaj içeriği
   * @param title Başlık (opsiyonel)
   */
  showWarning(message: string, title: string = 'Uyarı'): void {
    this.toastr.warning(message, title);
  }

  /**
   * Bilgi mesajı gösterir
   * @param message Mesaj içeriği
   * @param title Başlık (opsiyonel)
   */
  showInfo(message: string, title: string = 'Bilgi'): void {
    this.toastr.info(message, title);
  }

  /**
   * Tüm toastr mesajlarını temizler
   */
  clear(): void {
    this.toastr.clear();
  }

  /**
   * HTTP hata response'unu parse ederek uygun mesajı gösterir
   * @param error HTTP error response veya string
   */
  showHttpError(error: any): void {
    let errorMessage = 'Bilinmeyen bir hata oluştu';
    
    if (typeof error === 'string') {
      errorMessage = error;
    } else if (error?.message) {
      errorMessage = error.message;
    } else if (error?.error?.message) {
      errorMessage = error.error.message;
    } else if (error?.error) {
      errorMessage = error.error;
    }

    this.showError(errorMessage);
  }

  /**
   * Başarı işlemi için kısa mesaj
   * @param operation İşlem adı
   */
  showOperationSuccess(operation: string): void {
    this.showSuccess(`${operation} işlemi başarıyla tamamlandı.`);
  }

  /**
   * Hata işlemi için kısa mesaj
   * @param operation İşlem adı
   */
  showOperationError(operation: string): void {
    this.showError(`${operation} işlemi sırasında bir hata oluştu.`);
  }
}
