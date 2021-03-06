import { Injectable } from '@angular/core';

import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';

import { HomeComponent } from './home.component';

// + rendering markdown pages
import { HttpResponse, HttpClient } from '@angular/common/http';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

import { DocsComponent } from './docs/docs.component';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';

// + resolve markdown documentation
@Injectable({ providedIn: 'root' })
export class MarkdownFileResolver implements Resolve<string> {
  constructor(private service: HttpClient) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any> {
    const url = `assets/docs/${route.params.doc}.md` + (DEBUG_INFO_ENABLED ? '?ts=' + new Date().getTime() : '');
    return this.service.get(url, { responseType: 'text', observe: 'response' }).pipe(
      filter((response: HttpResponse<string>) => response.ok),
      map((content: HttpResponse<string>) => content.body)
    );
  }
}

// + home module routes
export const HOME_ROUTES: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      authorities: [],
      pageTitle: 'home.title'
    }
  },
  // Static pages
  {
    path: 'docs/:doc',
    component: DocsComponent,
    resolve: {
      markdown: MarkdownFileResolver
    },
    data: { pageTitle: 'home.title' }
  }
];
