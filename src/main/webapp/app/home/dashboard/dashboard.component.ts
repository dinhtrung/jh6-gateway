import { Component, OnInit } from '@angular/core';
// + download sidebar icons
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL, BUILD_TIMESTAMP } from 'app/app.constants';
import { map, filter, tap } from 'rxjs/operators';
import * as jsyaml from 'js-yaml';
import * as _ from 'lodash';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  _ = _;
  isReady = false;
  rows: any[];
  menuItems: any = [];

  constructor(private httpClient: HttpClient) {
    this.rows = _.chunk(this.menuItems, 6);
  }

  ngOnInit(): void {
    this.isReady = false;
    this.httpClient
      .get(SERVER_API_URL + 'assets/config/dashboard.yml?ts=' + BUILD_TIMESTAMP, { responseType: 'text', observe: 'response' })
      .pipe(
        filter((response: HttpResponse<string>) => response.ok),
        map((content: HttpResponse<string>) => jsyaml.load(content.body || '')),
        tap(() => (this.isReady = true))
      )
      .subscribe(menuItems => (this.menuItems = menuItems));
  }

  trackId(index: number, item: any): string {
    return item.url;
  }
}
