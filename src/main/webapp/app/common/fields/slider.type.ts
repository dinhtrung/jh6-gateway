import { Component } from '@angular/core';
import { FieldType } from '@ngx-formly/core';
// Usage:
// { key: 'mySlider', type: 'slider', templateOptions: { label: 'Sliders', options: {
//    floor: 0, ceil: 100, step: 5
// }}
// options: Options = {
@Component({
  selector: 'jhi-formly-field-slider',
  template: `
    <ng5-slider [options]="to.options" [formControl]="formControl" [formlyAttributes]="field"></ng5-slider>
  `
})
export class SliderTypeComponent extends FieldType {}
