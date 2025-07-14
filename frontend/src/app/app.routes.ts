import { Routes } from '@angular/router';
import { LocationsComponent } from './components/locations/locations.component';
import { TransportsComponent } from './components/transports/transports.component';
import { RouteCalculatorComponent } from './components/route-calculator/route-calculator.component';

export const routes: Routes = [
  { path: '', redirectTo: '/locations', pathMatch: 'full' },
  { path: 'locations', component: LocationsComponent },
  { path: 'transports', component: TransportsComponent },
  { path: 'route-calculator', component: RouteCalculatorComponent },
  { path: '**', redirectTo: '/locations' }
];
