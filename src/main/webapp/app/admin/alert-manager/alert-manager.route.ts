import { Route } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';

import { AlertManagerComponent } from './alert-manager.component';

export const alertManagerRoute: Route = {
  path: '',
  component: AlertManagerComponent,
  resolve: {
    pagingParams: JhiResolvePagingParams
  },
  data: {
    pageTitle: 'alert-manager.title',
    defaultSort: 'auditEventDate,desc'
  }
};
