import { Component, OnInit } from '@angular/core';
import { FieldType } from '@ngx-formly/core';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { filter, map, tap } from 'rxjs/operators';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { plainToFlattenObject } from 'app/common/util/request-util';
import * as _ from 'lodash';

@Component({
  selector: 'jhi-formly-template',
  template: `
    <div [innerHTML]="to.innerHTML"></div>
  `
})
export class TemplateTypeComponent extends FieldType implements OnInit {
  constructor(protected httpClient: HttpClient) {
    super();
  }

  ngOnInit() {
    if (this.to.apiEndpoint) {
      this.loadRemote().subscribe(res => (this.to.innerHTML = res));
    }
  }

  loadRemote() {
    return this.to.key
      ? this.loadRemoteApi().pipe(
          filter(res => res.ok),
          map(res => res.body),
          map(res => _.get(res, this.to.key)),
          tap(res => console.log(res))
        )
      : this.loadRemoteText().pipe(
          filter(res => res.ok),
          map(res => res.body),
          tap(res => console.log(res))
        );
  }

  loadRemoteText() {
    const query = _.assign({}, this.to.params);
    const body = _.assign({}, this.to.body);
    if (_.isEmpty(body)) {
      return this.httpClient.get(SERVER_API_URL + this.to.apiEndpoint, {
        params: createRequestOption(_.omitBy(plainToFlattenObject(query), _.isNull)),
        observe: 'response',
        responseType: 'text'
      });
    } else {
      return this.httpClient.post(SERVER_API_URL + this.to.apiEndpoint, _.omitBy(plainToFlattenObject(body), _.isNull), {
        params: createRequestOption(_.omitBy(plainToFlattenObject(query), _.isNull)),
        observe: 'response',
        responseType: 'text'
      });
    }
  }

  loadRemoteApi() {
    const query = _.assign({}, this.to.params);
    const body = _.assign({}, this.to.body);
    if (_.isEmpty(body)) {
      return this.httpClient.get<HttpResponse<any>>(SERVER_API_URL + this.to.apiEndpoint, {
        params: createRequestOption(_.omitBy(plainToFlattenObject(query), _.isNull)),
        observe: 'response'
      });
    } else {
      return this.httpClient.post<HttpResponse<any>>(SERVER_API_URL + this.to.apiEndpoint, _.omitBy(plainToFlattenObject(body), _.isNull), {
        params: createRequestOption(_.omitBy(plainToFlattenObject(query), _.isNull)),
        observe: 'response'
      });
    }
  }
}
