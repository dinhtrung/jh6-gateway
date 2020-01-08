import { Component, OnInit } from '@angular/core';
import { FieldType } from '@ngx-formly/core';
import { FormControl } from '@angular/forms';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import * as moment from 'moment';

@Component({
  selector: 'jhi-formly-field-datetimepicker',
  template: `
    <div class="input-group">
      <input class="form-control" [formControl]="formControl" [formlyAttributes]="field" />
      <div class="input-group-append">
        <button class="btn btn-outline-secondary" [ngbPopover]="calendarContent" autoClose="outside" type="button" #p="ngbPopover">
          <fa-icon [icon]="'calendar-alt'"></fa-icon>
        </button>
      </div>
    </div>
    <ng-template #calendarContent>
      <div>
        <div *ngIf="!showTimePickerToggle">
          <ngb-datepicker #dp name="datepicker" [formControl]="date" (select)="onDateSelect()"> </ngb-datepicker>
          <button class="btn btn-block btn-outline-secondary" type="button" (click)="toggleDateTimeState($event)">
            <fa-icon [icon]="'clock'"></fa-icon>
          </button>
        </div>
        <div *ngIf="showTimePickerToggle">
          <button class="btn btn-block btn-outline-secondary" type="button" (click)="toggleDateTimeState($event)">
            <fa-icon [icon]="'calendar-alt'"></fa-icon>
          </button>
          <div class="mt-auto">
            <ngb-timepicker [formControl]="time"></ngb-timepicker>
          </div>
          <button class="btn btn-block btn-primary" type="button" (click)="onDateTimeSelected(); p.close()" [disabled]="!time.value">
            <fa-icon icon="save"></fa-icon>
          </button>
        </div>
      </div>
    </ng-template>
  `
})
export class DateTimeTypeComponent extends FieldType implements OnInit {
  defaultOptions = {
    templateOptions: {
      wrappers: ['form-field']
    }
  };
  showTimePickerToggle = false;
  date: FormControl = new FormControl();
  time: FormControl = new FormControl();

  ngOnInit(): void {
    this.date.setValue(this.formControl.value ? moment(this.formControl.value) : moment());
    this.time.setValue((this.formControl.value ? moment(this.formControl.value) : moment()).format('HH:mm:00'));
  }

  toggleDateTimeState($event: any): void {
    this.showTimePickerToggle = !this.showTimePickerToggle;
    $event.stopPropagation();
  }

  onDateSelect(): void {
    this.formControl.setValue(moment(this.date.value).format(this.to.format || DATE_FORMAT));
    this.showTimePickerToggle = !this.showTimePickerToggle;
  }
  onDateTimeSelected(): void {
    const tmp = moment(this.time.value, 'HH:mm');
    const dt = this.date.value.set({
      hour: tmp.get('hour'),
      minute: tmp.get('minute'),
      second: '00'
    });
    this.formControl.setValue(dt.format(this.to.format || DATE_TIME_FORMAT));
  }
}
