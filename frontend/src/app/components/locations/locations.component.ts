import { Component, OnInit } from '@angular/core';
import { Location, LocationCreateCommand, SearchQuery, BaseListResponse, Country, City } from '../../models/location.model';
import { LocationService } from '../../services/location.service';

@Component({
  selector: 'app-locations',
  templateUrl: './locations.component.html',
  styleUrls: ['./locations.component.scss']
})
export class LocationsComponent implements OnInit {
  locations: Location[] = [];
  selectedLocation: LocationCreateCommand | null = null;
  isEditing = false;
  loading = false;
  searchTerm = '';
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalItems = 0;

  // Dropdown data
  countries: Country[] = [];
  availableCities: City[] = [];
  selectedCountryId: number | null = null;

  newLocation: LocationCreateCommand = {
    name: '',
    city: 0,
    locationCode: ''
  };

  constructor(private locationService: LocationService) {}

  ngOnInit(): void {
    this.loadLocations();
    this.loadCountries();
  }

  loadCountries(): void {
    this.locationService.getAllCountriesWithCities().subscribe({
      next: (response: BaseListResponse<Country>) => {
        this.countries = response.items;
      },
      error: (error) => {
        console.error('Ülkeler yüklenirken hata:', error);
      }
    });
  }

  onCountryChange(countryId: number | string): void {
    // String olarak gelen ID'yi number'a çevir
    const numericCountryId = typeof countryId === 'string' ? parseInt(countryId, 10) : countryId;
    this.selectedCountryId = numericCountryId;
    
    const selectedCountry = this.countries.find(c => c.id === numericCountryId);
    this.availableCities = selectedCountry ? selectedCountry.cities : [];
    
    console.log('Selected country ID:', numericCountryId);
    console.log('Found country:', selectedCountry);
    console.log('Available cities:', this.availableCities);
    
    // Reset city selection when country changes
    this.newLocation.city = 0;
    if (this.selectedLocation) {
      this.selectedLocation.city = 0;
    }
  }

  loadLocations(): void {
    this.loading = true;
    const searchQuery: SearchQuery = {
      searchTerm: this.searchTerm || '',
      pageNo: this.currentPage,
      pageSize: this.pageSize
    };

    this.locationService.searchLocations(searchQuery).subscribe({
      next: (response: BaseListResponse<Location>) => {
        this.locations = response.items;
        this.currentPage = response.page;
        this.totalPages = response.totalPages;
        this.totalItems = response.total;
        this.loading = false;
      },
      error: (error) => {
        console.error('Lokasyonlar yüklenirken hata:', error);
        this.loading = false;
      }
    });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadLocations();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadLocations();
  }

  selectLocation(location: Location): void {
    this.selectedLocation = {
      id: location.id,
      name: location.name,
      city: 0, // We need to find the city ID from the location's city name
      locationCode: location.locationCode
    };
    
    // Find and set the country and city based on location data
    this.findAndSetLocationCountryCity(location);
    this.isEditing = true;
  }

  findAndSetLocationCountryCity(location: Location): void {
    // Find the country that contains a city with the same name
    for (const country of this.countries) {
      const city = country.cities.find(c => c.name === location.city);
      if (city) {
        this.selectedCountryId = country.id;
        this.availableCities = country.cities;
        if (this.selectedLocation) {
          this.selectedLocation.city = city.id;
        }
        break;
      }
    }
  }

  createLocation(): void {
    if (this.isValidLocation(this.newLocation)) {
      this.loading = true;
      this.locationService.createLocation(this.newLocation).subscribe({
        next: () => {
          this.loadLocations();
          this.resetForm();
          this.loading = false;
        },
        error: (error) => {
          console.error('Lokasyon oluşturulurken hata:', error);
          this.loading = false;
        }
      });
    }
  }

  updateLocation(): void {
    if (this.selectedLocation && this.isValidLocation(this.selectedLocation)) {
      this.loading = true;
      this.locationService.updateLocation(this.selectedLocation).subscribe({
        next: () => {
          this.loadLocations();
          this.resetForm();
          this.loading = false;
        },
        error: (error) => {
          console.error('Lokasyon güncellenirken hata:', error);
          this.loading = false;
        }
      });
    }
  }

  deleteLocation(id: string): void {
    if (confirm('Bu lokasyonu silmek istediğinizden emin misiniz?')) {
      this.loading = true;
      this.locationService.deleteLocation(id).subscribe({
        next: () => {
          this.loadLocations();
          this.loading = false;
        },
        error: (error) => {
          console.error('Lokasyon silinirken hata:', error);
          this.loading = false;
        }
      });
    }
  }

  resetForm(): void {
    this.selectedLocation = null;
    this.isEditing = false;
    this.selectedCountryId = null;
    this.availableCities = [];
    this.newLocation = {
      name: '',
      city: 0,
      locationCode: ''
    };
  }

  cancelEdit(): void {
    this.resetForm();
  }

  isValidLocation(location: LocationCreateCommand): boolean {
    return !!(location.name && location.city > 0 && location.locationCode);
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const startPage = Math.max(1, this.currentPage - 2);
    const endPage = Math.min(this.totalPages, this.currentPage + 2);
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }
}
