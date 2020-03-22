import { Component, OnInit } from '@angular/core';
import { Router, Event, NavigationEnd, NavigationError } from '@angular/router';
// + ngx-spinner
import { TranslateService } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
// + sidebar support
import { JhiEventManager } from 'ng-jhipster';
import { SessionStorageService } from 'ngx-webstorage';
import * as _ from 'lodash';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  isNavbarCollapsed = false;
  // + Extra menu items
  _ = _;
  public menuItems: any[] = [];
  constructor(
    // + sidebar menu items
    private eventManager: JhiEventManager,
    private sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private translateService: TranslateService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        // Once logged in, load extra menus();
        this.accountService.identity().subscribe(account => {
          if (account) {
            this.loadExtraMenu();
            this.eventManager.subscribe('reloadSidebar', () => this.loadExtraMenu());
          } else {
            this.menuItems = [];
            this.sessionStorage.clear('sidebarMenuItems');
          }
        });
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });
  }

  // + sidebar support
  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
  // + Load Extra Menu
  loadExtraMenu(): void {
    this.menuItems = this.sessionStorage.retrieve('sidebarMenuItems');
  }
}
