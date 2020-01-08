import { Directive, HostListener } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

@Directive({
  // tslint:disable-next-line
  selector: 'input[type=file]',
  // tslint:disable-next-line
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: FileValueAccessorDirective, multi: true }]
})
// https://github.com/angular/angular/issues/7341
export class FileValueAccessorDirective implements ControlValueAccessor {
  value: any;
  @HostListener('change', ['$event.target.files']) onChange = (files: any) => {};
  @HostListener('blur') onTouched = () => {};

  writeValue(value: any): any {}
  registerOnChange(fn: any): any {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): any {
    this.onTouched = fn;
  }
}
