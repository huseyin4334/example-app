export interface Transportation {
  id?: string;
  origin: string;
  destination: string;
  transportType: TransportationTypeEnum;
  availableDays: Day[];
}

export interface TransportationTypeResponse {
  id: number;
  name: string;
  description?: string;
}

export interface TransportationCreateCommand {
  id?: string;
  origin: string;
  destination: string;
  transportationType: number;
  availableDays: Day[];
}

export interface TransportationResponse {
  id: string;
  origin: TransportationLocation;
  destination: TransportationLocation;
  transportType: string;
  availableDays: string[];
}

export interface TransportationLocation {
  id: string;
  name: string;
}


export enum TransportationTypeEnum {
  BUS = 'BUS',
  FLIGHT = 'FLIGHT',
  UBER = 'UBER',
  SUBWAY = 'SUBWAY'
}

export enum Day {
  MONDAY = 'MONDAY',
  TUESDAY = 'TUESDAY',
  WEDNESDAY = 'WEDNESDAY',
  THURSDAY = 'THURSDAY',
  FRIDAY = 'FRIDAY',
  SATURDAY = 'SATURDAY',
  SUNDAY = 'SUNDAY'
}

// For display purposes
export const TRANSPORT_TYPE_LABELS = {
  [TransportationTypeEnum.BUS]: 'Otobüs',
  [TransportationTypeEnum.FLIGHT]: 'Uçak',
  [TransportationTypeEnum.UBER]: 'Uber/Taksi',
  [TransportationTypeEnum.SUBWAY]: 'Metro'
};

export const DAY_LABELS = {
  [Day.MONDAY]: 'Pazartesi',
  [Day.TUESDAY]: 'Salı',
  [Day.WEDNESDAY]: 'Çarşamba',
  [Day.THURSDAY]: 'Perşembe',
  [Day.FRIDAY]: 'Cuma',
  [Day.SATURDAY]: 'Cumartesi',
  [Day.SUNDAY]: 'Pazar'
};
