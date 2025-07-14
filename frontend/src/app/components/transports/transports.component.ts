import { Component, OnInit } from '@angular/core';
import { TransportationCreateCommand, TransportationResponse, TransportationTypeEnum, Day, TRANSPORT_TYPE_LABELS, DAY_LABELS, TransportationTypeResponse } from '../../models/transport.model';
import { Location, SearchQuery, BaseListResponse } from '../../models/location.model';
import { TransportService } from '../../services/transport.service';
import { LocationService } from '../../services/location.service';

@Component({
  selector: 'app-transports',
  templateUrl: './transports.component.html',
  styleUrls: ['./transports.component.scss']
})
export class TransportsComponent implements OnInit {
  transportations: TransportationResponse[] = [];
  locations: Location[] = [];
  selectedTransportation: TransportationCreateCommand | null = null;
  isEditing = false;
  loading = false;
  searchTerm = '';
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalItems = 0;

  // Transportation types from API
  transportationTypes: TransportationTypeResponse[] = [];

  newTransportation: TransportationCreateCommand = {
    origin: '',
    destination: '',
    transportationType: 0,
    availableDays: []
  };

  days = Object.values(Day);
  transportTypeLabels = TRANSPORT_TYPE_LABELS;
  dayLabels = DAY_LABELS;

  constructor(
    private transportService: TransportService,
    private locationService: LocationService
  ) {}

  ngOnInit(): void {
    this.loadTransportations();
    this.loadLocations();
    this.loadTransportationTypes();
  }

  loadTransportationTypes(): void {
    this.transportService.getAvailableTransportationTypes().subscribe({
      next: (response: BaseListResponse<TransportationTypeResponse>) => {
        this.transportationTypes = response.items;
      },
      error: (error) => {
        console.error('Ulaşım türleri yüklenirken hata:', error);
      }
    });
  }

  loadTransportations(): void {
    this.loading = true;
    const searchQuery: SearchQuery = {
      searchTerm: this.searchTerm || '',
      pageNo: this.currentPage,
      pageSize: this.pageSize
    };

    this.transportService.searchTransportations(searchQuery).subscribe({
      next: (response: BaseListResponse<TransportationResponse>) => {
        this.transportations = response.items;
        this.currentPage = response.page;
        this.totalPages = response.totalPages;
        this.totalItems = response.total;
        this.loading = false;
      },
      error: (error) => {
        console.error('Ulaşım bilgileri yüklenirken hata:', error);
        this.loading = false;
      }
    });
  }

  loadLocations(): void {
    this.locationService.getAllSearchableLocations().subscribe({
      next: (response: BaseListResponse<Location>) => {
        this.locations = response.items;
      },
      error: (error) => {
        console.error('Lokasyonlar yüklenirken hata:', error);
      }
    });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadTransportations();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadTransportations();
  }

  selectTransportation(transportation: TransportationResponse): void {
    // Find the transportation type ID from the name
    const transportType = this.transportationTypes.find(t => t.name === transportation.transportType);
    
    this.selectedTransportation = {
      id: transportation.id,
      origin: transportation.origin.id,
      destination: transportation.destination.id,
      transportationType: transportType ? transportType.id : 0,
      availableDays: transportation.availableDays.map(day => day as Day)
    };
    this.isEditing = true;
  }

  createTransportation(): void {
    if (this.isValidTransportation(this.newTransportation)) {
      this.loading = true;
      this.transportService.createTransportation(this.newTransportation).subscribe({
        next: () => {
          this.loadTransportations();
          this.resetForm();
          this.loading = false;
        },
        error: (error) => {
          console.error('Ulaşım bilgisi oluşturulurken hata:', error);
          this.loading = false;
        }
      });
    }
  }

  updateTransportation(): void {
    if (this.selectedTransportation && this.isValidTransportation(this.selectedTransportation)) {
      this.loading = true;
      this.transportService.updateTransportation(this.selectedTransportation).subscribe({
        next: () => {
          this.loadTransportations();
          this.resetForm();
          this.loading = false;
        },
        error: (error) => {
          console.error('Ulaşım bilgisi güncellenirken hata:', error);
          this.loading = false;
        }
      });
    }
  }

  deleteTransportation(id: string): void {
    if (confirm('Bu ulaşım bilgisini silmek istediğinizden emin misiniz?')) {
      this.loading = true;
      this.transportService.deleteTransportation(id).subscribe({
        next: () => {
          this.loadTransportations();
          this.loading = false;
        },
        error: (error) => {
          console.error('Ulaşım bilgisi silinirken hata:', error);
          this.loading = false;
        }
      });
    }
  }

  resetForm(): void {
    this.selectedTransportation = null;
    this.isEditing = false;
    this.newTransportation = {
      origin: '',
      destination: '',
      transportationType: 0,
      availableDays: []
    };
  }

  cancelEdit(): void {
    this.resetForm();
  }

  getTransportTypeLabel(type: string): string {
    const transportType = this.transportationTypes.find(t => t.name === type);
    return transportType ? transportType.name : type;
  }

  getTransportTypeLabelById(id: number): string {
    const transportType = this.transportationTypes.find(t => t.id === id);
    return transportType ? transportType.name : 'Bilinmeyen';
  }

  getDayLabel(day: string): string {
    return this.dayLabels[day as Day] || day;
  }

  isValidTransportation(transportation: TransportationCreateCommand | null): boolean {
    if (!transportation) return false;
    return !!(transportation.origin && 
              transportation.destination && 
              transportation.transportationType > 0 && 
              transportation.availableDays.length > 0);
  }

  onDayChange(day: Day, event: any): void {
    const transportation = this.isEditing ? this.selectedTransportation : this.newTransportation;
    if (transportation) {
      if (event.target.checked) {
        if (!transportation.availableDays.includes(day)) {
          transportation.availableDays.push(day);
        }
      } else {
        transportation.availableDays = transportation.availableDays.filter(d => d !== day);
      }
    }
  }

  isDaySelected(day: Day): boolean {
    const transportation = this.isEditing ? this.selectedTransportation : this.newTransportation;
    return transportation ? transportation.availableDays.includes(day) : false;
  }

  getLocationName(locationId: string): string {
    const location = this.locations.find(l => l.id === locationId);
    return location ? location.name : locationId;
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    // Backend 0-based, UI'da 1-based göster
    const displayCurrentPage = this.currentPage + 1;
    const startPage = Math.max(1, displayCurrentPage - 2);
    const endPage = Math.min(this.totalPages, displayCurrentPage + 2);
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }
}
