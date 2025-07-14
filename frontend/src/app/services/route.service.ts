import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BaseApiService } from '../core/base-api.service';
import { AvailableRoutesQuery, AvailableRoutesResponse, SearchableLocationResponse } from '../models/route.model';
import { BaseListResponse } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class RouteService {
  private endpoint = 'routes';
  private locationEndpoint = 'locations';

  constructor(private baseApi: BaseApiService) { }

  // Route hesaplama - backend POST /routes endpoint'i
  calculateRoute(query: AvailableRoutesQuery): Observable<AvailableRoutesResponse> {
    return this.baseApi.post<AvailableRoutesResponse>(this.endpoint, query);
  }

  // Searchable locations - backend GET /locations/all endpoint'i
  getAllSearchableLocations(): Observable<BaseListResponse<SearchableLocationResponse>> {
    return this.baseApi.get<BaseListResponse<SearchableLocationResponse>>(`${this.locationEndpoint}/all`);
  }
}
