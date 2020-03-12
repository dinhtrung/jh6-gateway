import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';

import { AlertManagerComponent } from './alert-manager.component';

import { alertManagerRoute } from './alert-manager.route';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([alertManagerRoute])],
  declarations: [AlertManagerComponent]
})
export class AlertManagerModule {}
