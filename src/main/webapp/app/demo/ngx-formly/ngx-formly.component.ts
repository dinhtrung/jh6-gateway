import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';

@Component({
  selector: 'jhi-ngx-formly',
  templateUrl: './ngx-formly.component.html'
})
export class NgxFormlyComponent implements OnInit {
  form = new FormGroup({});
  model: any = {};
  options: FormlyFormOptions = {};

  fields: FormlyFieldConfig[] = [
    {
      key: 'Input',
      type: 'input',
      templateOptions: {
        label: 'Input',
        placeholder: 'Placeholder',
        description: 'Description',
        required: true
      }
    },
    {
      key: 'Textarea',
      type: 'textarea',
      templateOptions: {
        label: 'Textarea',
        placeholder: 'Placeholder',
        description: 'Description',
        required: true
      }
    },
    {
      key: 'Checkbox',
      type: 'checkbox',
      templateOptions: {
        label: 'Accept terms',
        description: 'In order to proceed, please accept terms',
        pattern: 'true',
        required: true
      },
      validation: {
        messages: {
          pattern: 'Please accept the terms'
        }
      }
    },
    {
      key: 'Radio',
      type: 'radio',
      templateOptions: {
        label: 'Radio',
        placeholder: 'Placeholder',
        description: 'Description',
        required: true,
        options: [
          { value: 1, label: 'Option 1' },
          { value: 2, label: 'Option 2' },
          { value: 3, label: 'Option 3' },
          { value: 4, label: 'Option 4' }
        ]
      }
    },
    {
      key: 'Select',
      type: 'select',
      templateOptions: {
        label: 'Select',
        placeholder: 'Placeholder',
        description: 'Description',
        required: true,
        options: [
          { value: 1, label: 'Option 1' },
          { value: 2, label: 'Option 2' },
          { value: 3, label: 'Option 3' },
          { value: 4, label: 'Option 4' }
        ]
      }
    },
    {
      key: 'select_multi',
      type: 'select',
      templateOptions: {
        label: 'Select Multiple',
        placeholder: 'Placeholder',
        description: 'Description',
        required: true,
        multiple: true,
        selectAllOption: 'Select All',
        options: [
          { value: 1, label: 'Option 1' },
          { value: 2, label: 'Option 2' },
          { value: 3, label: 'Option 3' },
          { value: 4, label: 'Option 4' }
        ]
      }
    },
    {
      key: 'left',
      type: 'input',
      templateOptions: {
        placeholder: 'Formly is terrific!',
        addonLeft: {
          class: 'fas fa-euro'
        },
        label: 'One add-on on the left (icon)'
      }
    },
    {
      key: 'both',
      type: 'input',
      templateOptions: {
        placeholder: 'How great is this?',
        addonLeft: {
          class: 'fas fa-demo'
        },
        addonRight: {
          text: '$'
        },
        label: 'One add-on on both side (left: icon, right: text)'
      }
    },
    {
      key: 'right',
      type: 'input',
      templateOptions: {
        placeholder: "Nice, isn't it??",

        addonRight: {
          class: 'fa fa-heart'
        },
        label: 'One add-on on the right (icon)'
      }
    }
  ];

  constructor() {}

  ngOnInit(): void {}

  submit(): void {
    alert('Model' + JSON.stringify(this.model));
  }
}
