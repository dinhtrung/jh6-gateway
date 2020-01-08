import { Component } from '@angular/core';
import { FieldType } from '@ngx-formly/core';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { plainToFlattenObject } from 'app/common/util/request-util';
import * as _ from 'lodash';

@Component({
  selector: 'jhi-formly-button',
  template: `
    <div>
      <button type="button" [ngClass]="'btn btn-' + (to.btnType ? to.btnType : 'outline-primary')" (click)="onClick($event)">
        {{ to.label }}
      </button>
    </div>
  `
})
export class ButtonTypeComponent extends FieldType {
  constructor(protected httpClient: HttpClient, private alertService: JhiAlertService) {
    super();
  }
  onClick($event: any) {
    if (this.to.onClick) {
      this.to.onClick($event);
    } else if (this.to.apiEndpoint) {
      this.createRequest().subscribe(
        res => this.formControl.setValue(res.body),
        err => this.alertService.error(err.message)
      );
    }
  }

  createRequest() {
    const params = _.omitBy(plainToFlattenObject(this.to.params), _.isNull);
    const body = _.omitBy(plainToFlattenObject(this.to.body), _.isNull);
    if (_.isEmpty(body)) {
      return this.httpClient.get<HttpResponse<any[]>>(SERVER_API_URL + this.to.apiEndpoint, {
        params,
        observe: 'response'
      });
    } else {
      return this.httpClient.post<any>(SERVER_API_URL + this.to.apiEndpoint, body, {
        params,
        observe: 'response'
      });
    }
  }
}
