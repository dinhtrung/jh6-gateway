import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';

@Injectable({ providedIn: 'root' })
export class EntityService {
  public endpoint = '';
  public i18n: any = {};

  constructor(protected http: HttpClient) {}

  create(entity: any, endpoint?: string): Observable<HttpResponse<any>> {
    return this.http
      .post<any>(SERVER_API_URL + `${endpoint ? endpoint : this.endpoint}`, entity, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => res));
  }

  update(entity: any, endpoint?: string): Observable<HttpResponse<any>> {
    return this.http
      .put<any>(SERVER_API_URL + `${endpoint ? endpoint : this.endpoint}`, entity, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => res));
  }

  find(id: string, endpoint?: string): Observable<HttpResponse<any>> {
    return this.http
      .get<any>(SERVER_API_URL + `${endpoint ? endpoint : this.endpoint}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => res));
  }

  query(req?: any, endpoint?: string): Observable<HttpResponse<any[]>> {
    return this.http
      .get<any[]>(SERVER_API_URL + `${endpoint ? endpoint : this.endpoint}`, { params: createRequestOption(req), observe: 'response' })
      .pipe(map((res: HttpResponse<any[]>) => res));
  }

  delete(id: string, endpoint?: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(SERVER_API_URL + `${endpoint ? endpoint : this.endpoint}/${id}`, { observe: 'response' });
  }
}
