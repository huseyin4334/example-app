// Backend API DTO'larına uygun modeller

export interface AvailableRoutesQuery {
  originId: string;
  destinationId: string;
  selectedDate: string; // ISO date string
}

export interface TransportDto {
  origin: string;
  destination: string;
  transportType: string;
}

export interface AvailableRouteDto {
  beforeFlight: TransportDto;
  flight: TransportDto;
  afterFlight: TransportDto;
}

export interface AvailableRoutesResponse {
  availableRoutes: AvailableRouteDto[];
}

export interface SearchableLocationResponse {
  id: string;
  name: string;
}

// UI için yardımcı interface'ler
export interface RouteRequest {
  fromLocationId: string;
  toLocationId: string;
  selectedDate: Date;
}

export interface RouteResponse {
  availableRoutes: AvailableRouteDto[];
}
