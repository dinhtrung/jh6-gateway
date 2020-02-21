import { AbstractControl, ValidationErrors } from '@angular/forms';

export class AppValidators {
  static phoneNumberValidator(control: AbstractControl): { [key: string]: any } | null {
    const valid = /^d+$/.test(control.value);
    return valid ? null : { invalidNumber: { valid: false, value: control.value } };
  }

  static regexpValidator(control: AbstractControl): { [key: string]: any } | null {
    try {
      const valid = new RegExp(control.value);
    } catch (e) {
      return { invalidRegexp: e.message };
    }
    return null;
  }

  static jsonValidator(control: AbstractControl): { [key: string]: any } | null {
    try {
      JSON.parse(control.value);
    } catch (e) {
      return { invalidJson: e.message };
    }
    return null;
  }

  // Example validator from ngx-formly
  static IpValidator(control: AbstractControl) {
    return control.value == null || /(\d{1,3}\.){3}\d{1,3}/.test(control.value) ? null : { ip: true };
  }

  static minlengthMessage(err: any, field: any) {
    return `This field is required to be at least ${err.requiredLength} characters.`;
  }
  static maxlengthMessage(err: any, field: any) {
    return `This field cannot be longer than ${err.requiredLength} characters.`;
  }
  static minMessage(err: any, field: any) {
    return `This field should be at least ${err.min}.`;
  }
  static maxMessage(err: any, field: any) {
    return `This field cannot be more than ${err.max}.`;
  }
  static minbytesMessage(err: any, field: any) {
    return `This field should be at least ${err.minbytes} bytes.`;
  }
  static maxbytesMessage(err: any, field: any) {
    return `This field cannot be more than ${err.maxbytes} bytes.`;
  }
  static patternMessage(err: any, field: any) {
    return `${field.templateOptions.label} is should follow pattern for ${err.requiredPattern}.`;
  }
  static ipMessage(err: any, field: any) {
    return `"${field.formControl.value}" is not a valid IP Address`;
  }
}
