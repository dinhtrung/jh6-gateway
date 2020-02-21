import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewayCommonModule } from 'app/common/common.module';
import { DataComponent } from './data.component';
import { DataDetailComponent } from './data-detail.component';
import { DataUpdateComponent } from './data-update.component';
import { dataRoute } from './data.route';

const ENTITY_STATES = [...dataRoute];

@NgModule({
  imports: [GatewayCommonModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [DataComponent, DataDetailComponent, DataUpdateComponent],
  entryComponents: [DataComponent, DataUpdateComponent]
})
export class GatewayDataModule {}
