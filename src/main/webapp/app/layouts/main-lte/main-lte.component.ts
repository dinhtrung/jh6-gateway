import { Component, Renderer2, ViewChild, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, Event, NavigationStart, NavigationEnd, NavigationCancel, NavigationError } from '@angular/router';
// + ngx-spinner
import { NgxSpinnerService } from 'ngx-spinner';
import { TranslateService } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
// + sidebar support
import { JhiEventManager } from 'ng-jhipster';
import { SessionStorageService } from 'ngx-webstorage';
import * as _ from 'lodash';

@Component({
  selector: 'jhi-main-lte',
  templateUrl: './main-lte.component.html'
})
export class MainLteComponent implements OnInit {
  public sidebarMenuOpened = true;
  @ViewChild('contentWrapper', { static: false }) contentWrapper: any;

  isNavbarCollapsed = false;
  // + Extra menu items
  _ = _;
  public menuItems: any[] = [];
  constructor(
    private renderer: Renderer2,
    // + sidebar menu items
    private eventManager: JhiEventManager,
    private sessionStorage: SessionStorageService,
    // + spinner
    private spinner: NgxSpinnerService,
    private accountService: AccountService,
    private translateService: TranslateService,
    private titleService: Title,
    private router: Router
  ) {}

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationStart) {
        this.spinner.show();
      } else if (event instanceof NavigationEnd || event instanceof NavigationCancel || event instanceof NavigationError) {
        this.spinner.hide();
      }
      if (event instanceof NavigationEnd) {
        this.updateTitle();
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

    this.translateService.onLangChange.subscribe(() => this.updateTitle());
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
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

  mainSidebarHeight(height: number): void {
    this.renderer.setStyle(this.contentWrapper.nativeElement, 'min-height', height - 114 + 'px');
  }

  toggleMenuSidebar(): void {
    if (this.sidebarMenuOpened) {
      this.renderer.removeClass(document.body, 'sidebar-open');
      this.renderer.addClass(document.body, 'sidebar-collapse');
      this.sidebarMenuOpened = false;
    } else {
      this.renderer.removeClass(document.body, 'sidebar-collapse');
      this.renderer.addClass(document.body, 'sidebar-open');
      this.sidebarMenuOpened = true;
    }
  }
}
