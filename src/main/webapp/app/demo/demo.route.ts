import { Route } from '@angular/router';

import { DemoComponent } from './demo.component';

export const DEMO_ROUTE: Route = {
  path: '',
  component: DemoComponent,
  data: {
    authorities: [],
    pageTitle: 'demo.title'
  }
};
