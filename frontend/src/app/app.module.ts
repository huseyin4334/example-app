import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { routes } from './app.routes';
import { LocationsComponent } from './components/locations/locations.component';
import { RouteCalculatorComponent } from './components/route-calculator/route-calculator.component';
import { TransportsComponent } from './components/transports/transports.component';

@NgModule({
  declarations: [
    AppComponent,
    LocationsComponent,
    TransportsComponent,
    RouteCalculatorComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
