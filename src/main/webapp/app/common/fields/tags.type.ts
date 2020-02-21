import { Component, OnInit, OnDestroy } from '@angular/core';
// + HTTP support
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
// + ng-select
import { Subject, Observable, of, concat } from 'rxjs';
import { FieldType } from '@ngx-formly/core';
import { distinctUntilChanged, debounceTime, switchMap, tap, catchError, map } from 'rxjs/operators';
import * as _ from 'lodash';

@Component({
  selector: 'jhi-formly-field-tags',
  template: `
    <ng-select
      [items]="to.items"
      [bindValue]="to.key"
      [bindLabel]="to.val"
      [placeholder]="to.placeholder"
      [multiple]="true"
      [hideSelected]="to.hideSelected"
      [addTag]="true"
      [formControl]="formControl"
    >
    </ng-select>
  `
})
export class TagsTypeComponent extends FieldType {}
