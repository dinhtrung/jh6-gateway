import { Component } from '@angular/core';
import { FieldType } from '@ngx-formly/core';

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
export class TagsTypeComponent extends FieldType {
  defaultOptions = {
    wrappers: ['form-field']
  };
}
