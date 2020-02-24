import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTES } from './home.route';
import { HomeComponent } from './home.component';
// + rendering markdown pages
import { MarkdownModule } from 'ngx-markdown';
import { DocsComponent } from './docs/docs.component';

@NgModule({
  imports: [SharedModule, MarkdownModule.forRoot(), RouterModule.forChild(HOME_ROUTES)],
  declarations: [HomeComponent, DocsComponent]
})
export class GatewayHomeModule {}
