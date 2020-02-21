import { Component } from '@angular/core';
import { FieldType } from '@ngx-formly/core';

@Component({
  selector: 'jhi-formly-field-quill',
  template: `
    <quill-editor [formControl]="formControl" [formlyAttributes]="field" [styles]="{ height: '250px' }"></quill-editor>
  `
})
export class QuillTypeComponent extends FieldType {}
