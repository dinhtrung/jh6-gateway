import { Component } from '@angular/core';
import { FieldArrayType } from '@ngx-formly/core';

@Component({
  selector: 'jhi-formly-tabset',
  template: `
    <ngb-tabset [type]="to.type || 'tabs'" [justify]="to.justify || 'justified'">
      <ngb-tab
        *ngFor="let panel of field.fieldGroup; let i = index"
        [title]="panel.templateOptions.label"
        [disabled]="panel.templateOptions.disabled"
      >
        <ng-template ngbTabContent>
          <div class="card border-top-0">
            <div class="card-body">
              <formly-field class="col" [field]="f" *ngFor="let f of panel.fieldGroup"></formly-field>
            </div>
          </div>
        </ng-template>
      </ngb-tab>
    </ngb-tabset>
  `
})
export class FormlyTabsetTypeComponent extends FieldArrayType {}
