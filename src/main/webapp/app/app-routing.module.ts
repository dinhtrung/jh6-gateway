import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/shared/constants/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

import { MainJhiComponent } from './layouts/main-jhi/main-jhi.component';
import { MainLteComponent } from './layouts/main-lte/main-lte.component';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          component: MainJhiComponent,
          data: {
            authorities: [Authority.ADMIN]
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule)
        },
        {
          path: 'management',
          component: MainJhiComponent,
          data: {
            authorities: ['ROLE_ADMIN']
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./management/management.module').then(m => m.ManagementModule)
        },
        {
          path: 'account',
          component: MainLteComponent,
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule)
        },
        // + crud module
        {
          path: 'data',
          loadChildren: () => import('./data/data.module').then(m => m.GatewayDataModule)
        },
        // + demo module
        {
          path: 'demo',
          component: MainLteComponent,
          loadChildren: () => import('./demo/demo.module').then(m => m.DemoModule)
        },
        ...LAYOUT_ROUTES
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
