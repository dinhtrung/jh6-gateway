import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewayCommonModule } from 'app/common/common.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [GatewayCommonModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent]
})
export class GatewayHomeModule {}
