import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import './vendor';
// import { SharedModule } from 'app/shared/shared.module';
import { GatewayCommonModule } from 'app/common/common.module';
import { GatewayCoreModule } from 'app/core/core.module';
import { GatewayAppRoutingModule } from './app-routing.module';
import { GatewayHomeModule } from './home/home.module';
import { GatewayEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
// + service-worker
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
// + spinner
import { NgxSpinnerModule } from 'ngx-spinner';
// + device detector
import { DeviceDetectorModule } from 'ngx-device-detector';

@NgModule({
  imports: [
    // + angular
    BrowserModule,
    BrowserAnimationsModule,
    DeviceDetectorModule.forRoot(),
    ServiceWorkerModule.register('/service-worker.js', { enabled: !DEBUG_INFO_ENABLED }),
    // SharedModule,
    NgxSpinnerModule,
    GatewayCommonModule,
    // + jhipster
    GatewayCoreModule,
    GatewayHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    GatewayEntityModule,
    GatewayAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class GatewayAppModule {}
