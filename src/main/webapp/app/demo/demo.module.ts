import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewayCommonModule } from 'app/common/common.module';
import { DEMO_ROUTE } from './demo.route';
import { DemoComponent } from './demo.component';
import { NgxFormlyComponent } from './ngx-formly/ngx-formly.component';
import { JsYamlComponent } from './js-yaml/js-yaml.component';
import { FormJsonComponent } from './form-json/form-json.component';

@NgModule({
  imports: [GatewayCommonModule, RouterModule.forChild([DEMO_ROUTE])],
  declarations: [
    NgxFormlyComponent,
    JsYamlComponent,
    // TreeComponent,
    // FormDesignerComponent,
    FormJsonComponent,
    // FormBuilderComponent
    DemoComponent
  ]
})
export class DemoModule {}
