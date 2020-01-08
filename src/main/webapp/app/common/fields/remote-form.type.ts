import { Component, OnInit } from '@angular/core';
// + Formly Support
import { FormlyFieldConfig } from '@ngx-formly/core';
import { filter, map } from 'rxjs/operators';
// + HTTP support
import { HttpClient } from '@angular/common/http';
import { FieldType } from '@ngx-formly/core';
// + look for anything
import * as _ from 'lodash';
import * as jsyaml from 'js-yaml';

@Component({
  selector: 'jhi-remote-form-type',
  template: `
    <div *ngIf="ready">
      <formly-form [model]="model" [fields]="formFields" [options]="options" [form]="form"></formly-form>
    </div>
  `
})
export class RemoteFormTypeComponent extends FieldType implements OnInit {
  formFields: FormlyFieldConfig[] = [];
  ready = false;
  constructor(private httpClient: HttpClient) {
    super();
    this.ready = false;
  }

  ngOnInit(): void {
    if (this.to.fields) {
      this.formFields = this.to.fields;
      this.ready = true;
    } else if (this.to.yamlResource) {
      this.loadRemoteForm();
    }
  }

  loadRemoteForm(): void {
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
