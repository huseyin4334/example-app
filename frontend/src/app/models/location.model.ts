export interface Location {
  id?: string;
  name: string;
  country: string;
  countryCode: string;
  city: string;
  locationCode: string;
}

export interface LocationCreateCommand {
  id?: string;
  name: string;
  city: number;
  locationCode: string;
}

export interface Country {
  id: number;
  name: string;
  cities: City[];
}

export interface City {
  id: number;
  name: string;
}

export interface SearchQuery {
  searchTerm?: string;
  pageNo: number;
  pageSize: number;
}

export interface BaseListResponse<T> {
  items: T[];
  page: number;
  pageSize: number;
  total: number;
  totalPages: number;
}
