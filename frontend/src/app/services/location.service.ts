import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BaseApiService } from '../core/base-api.service';
import { Location, LocationCreateCommand, SearchQuery, BaseListResponse, Country } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private endpoint = 'locations';

  constructor(private baseApi: BaseApiService) { }

  // GET /locations/{id} - Get single location
  getLocation(id: string): Observable<Location> {
    return this.baseApi.get<Location>(`${this.endpoint}/${id}`);
  }

  // PUT /locations - Create location
  createLocation(location: LocationCreateCommand): Observable<void> {
    return this.baseApi.put<void>(this.endpoint, location);
  }

  // POST /locations - Update location
  updateLocation(location: LocationCreateCommand): Observable<void> {
    return this.baseApi.post<void>(this.endpoint, location);
  }

  // DELETE /locations/{id} - Delete location
  deleteLocation(id: string): Observable<void> {
    return this.baseApi.delete<void>(`${this.endpoint}/${id}`);
  }

  // POST /locations/page - Search locations with pagination
  searchLocations(searchQuery: SearchQuery): Observable<BaseListResponse<Location>> {
    return this.baseApi.post<BaseListResponse<Location>>(`${this.endpoint}/page`, searchQuery);
  }

  // GET /locations/all - Get all searchable locations
  getAllSearchableLocations(): Observable<BaseListResponse<Location>> {
    return this.baseApi.get<BaseListResponse<Location>>(`${this.endpoint}/all`);
  }

  // GET /locations/cities - Get all countries with cities
  getAllCountriesWithCities(): Observable<BaseListResponse<Country>> {
    return this.baseApi.get<BaseListResponse<Country>>(`${this.endpoint}/cities`);
  }
}
