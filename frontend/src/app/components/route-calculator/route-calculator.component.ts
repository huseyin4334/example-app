import { Component, OnInit } from '@angular/core';
import { AvailableRoutesQuery, AvailableRoutesResponse, SearchableLocationResponse } from '../../models/route.model';
import { RouteService } from '../../services/route.service';

@Component({
  selector: 'app-route-calculator',
  templateUrl: './route-calculator.component.html',
  styleUrls: ['./route-calculator.component.scss']
})
export class RouteCalculatorComponent implements OnInit {
  locations: SearchableLocationResponse[] = [];
  
  routeRequest: AvailableRoutesQuery = {
    originId: '',
    destinationId: '',
    selectedDate: new Date().toISOString().split('T')[0] // Today's date as YYYY-MM-DD
  };
  
  routeResult: AvailableRoutesResponse | null = null;
  isCalculating = false;
  error: string | null = null;
  selectedDate: string = new Date().toISOString().split('T')[0];

  constructor(
    private routeService: RouteService
  ) {}

  ngOnInit(): void {
    this.loadLocations();
  }

  loadLocations(): void {
    this.routeService.getAllSearchableLocations().subscribe({
      next: (response) => {
        this.locations = response.items;
      },
      error: (error) => {
        console.error('Lokasyonlar yüklenirken hata:', error);
        this.error = 'Lokasyonlar yüklenirken hata oluştu.';
      }
    });
  }

  onDateChange(): void {
    // Date picker'dan gelen string değeri direkt kullan
    this.routeRequest.selectedDate = this.selectedDate;
  }

  calculateRoute(): void {
    if (!this.routeRequest.originId || !this.routeRequest.destinationId) {
      this.error = 'Lütfen başlangıç ve hedef lokasyonlarını seçin.';
      return;
    }
    
    if (this.routeRequest.originId === this.routeRequest.destinationId) {
      this.error = 'Başlangıç ve hedef lokasyonları aynı olamaz.';
      return;
    }

    if (!this.selectedDate) {
      this.error = 'Lütfen bir tarih seçin.';
      return;
    }

    this.error = null;
    this.isCalculating = true;
    this.routeResult = null;

    // Date'i ISO string'e çevir
    this.routeRequest.selectedDate = this.selectedDate;

    this.routeService.calculateRoute(this.routeRequest).subscribe({
      next: (data) => {
        this.routeResult = data;
        this.isCalculating = false;
        if (!data.availableRoutes || data.availableRoutes.length === 0) {
          this.error = 'Seçilen kriterlere uygun rota bulunamadı.';
        }
      },
      error: (error) => {
        console.error('Rota hesaplanırken hata:', error);
        this.error = 'Rota hesaplanırken bir hata oluştu. Lütfen tekrar deneyin.';
        this.isCalculating = false;
      }
    });
  }

  resetForm(): void {
    this.routeRequest = {
      originId: '',
      destinationId: '',
      selectedDate: new Date().toISOString().split('T')[0]
    };
    this.selectedDate = new Date().toISOString().split('T')[0]; // String olarak initialize et
    this.routeResult = null;
    this.error = null;
  }

  getLocationName(locationId: string): string {
    const location = this.locations.find(l => l.id === locationId);
    return location ? location.name : 'Bilinmeyen Lokasyon';
  }

  getTransportTypeLabel(transportType: string): string {
    const types: {[key: string]: string} = {
      'BUS': 'Otobüs',
      'FLIGHT': 'Uçak',
      'UBER': 'Uber',
      'SUBWAY': 'Metro'
    };
    return types[transportType] || transportType;
  }

  getTomorrowDate(): string {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return tomorrow.toISOString().split('T')[0];
  }
}
