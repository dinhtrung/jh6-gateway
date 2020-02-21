import { Component, OnInit, OnDestroy } from '@angular/core';
// + Formly Support
import { FormGroup } from '@angular/forms';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { filter, map } from 'rxjs/operators';
// + HTTP support
import { HttpClient, HttpResponse } from '@angular/common/http';
import { FieldType } from '@ngx-formly/core';
// + look for anything
import * as _ from 'lodash';
import * as jsyaml from 'js-yaml';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-remote-form-type',
  template: `
    <div *ngIf="ready">
      <formly-form [model]="model" [fields]="formFields" [options]="options" [form]="form"></formly-form>
    </div>
  `
})
export class RemoteFormTypeComponent extends FieldType implements OnInit, OnDestroy {
  formFields: FormlyFieldConfig[] = [];
  ready = false;
  constructor(private httpClient: HttpClient) {
    super();
    this.ready = false;
  }

  ngOnInit() {
    if (this.to.fields) {
      this.formFields = this.to.fields;
      this.ready = true;
    } else if (this.to.yamlResource) {
      this.loadRemoteForm();
    }
  }

  ngOnDestroy() {}

  loadRemoteForm() {
    // FIXME: Load existings value from formControl to populate into of
    this.httpClient
      .get(this.to.yamlResource + '?ts=' + new Date().getTime(), { responseType: 'text', observe: 'response' })
      .pipe(
        filter(res => res.ok),
        map(res => jsyaml.load(res.body || ''))
      )
      .subscribe(res => {
        this.formFields = _.get(res, 'fields', []);
        this.ready = true;
      });
  }
}
