import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BaseApiService } from '../core/base-api.service';
import { TransportationCreateCommand, TransportationResponse, TransportationTypeResponse } from '../models/transport.model';
import { SearchQuery, BaseListResponse } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class TransportService {
  private endpoint = 'transportations';

  constructor(private baseApi: BaseApiService) { }

  // GET /transportations/{id} - Get single transportation
  getTransportation(id: string): Observable<TransportationResponse> {
    return this.baseApi.get<TransportationResponse>(`${this.endpoint}/${id}`);
  }

  // PUT /transportations - Create transportation
  createTransportation(transportation: TransportationCreateCommand): Observable<void> {
    return this.baseApi.put<void>(this.endpoint, transportation);
  }

  // POST /transportations - Update transportation
  updateTransportation(transportation: TransportationCreateCommand): Observable<void> {
    return this.baseApi.post<void>(this.endpoint, transportation);
  }

  // DELETE /transportations/{id} - Delete transportation
  deleteTransportation(id: string): Observable<void> {
    return this.baseApi.delete<void>(`${this.endpoint}/${id}`);
  }

  // POST /transportations/page - Search transportations with pagination
  searchTransportations(searchQuery: SearchQuery): Observable<BaseListResponse<TransportationResponse>> {
    return this.baseApi.post<BaseListResponse<TransportationResponse>>(`${this.endpoint}/page`, searchQuery);
  }

  // GET /transportations/available-types - Get available transportation types
  getAvailableTransportationTypes(): Observable<BaseListResponse<TransportationTypeResponse>> {
    return this.baseApi.get<BaseListResponse<TransportationTypeResponse>>(`${this.endpoint}/available-types`);
  }
}
