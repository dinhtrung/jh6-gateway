import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';
import * as _ from 'lodash';

@Component({
  selector: 'jhi-form-json',
  templateUrl: './form-json.component.html'
})
export class FormJsonComponent implements OnInit {
  form = new FormGroup({});
  model = {
    // date: '2019-08-02',
    // accounts: ['user-0']
  };
  options: FormlyFormOptions = {};
  fields: FormlyFieldConfig[] = [
    {
      key: 'upperCase',
      type: 'input',
      templateOptions: {
        label: 'Upper Case',
        required: true,
        change: (field: any): void => field.formControl.setValue(_.upperCase(field.formControl.value))
      }
    },
    {
      key: 'file',
      type: 'file-gridfs',
      wrappers: ['form-field'],
      templateOptions: {
        label: 'File Upload',
        maxFiles: 2,
        isImage: true,
        className: 'img-thumbnail'
      }
    },
    {
      key: 'datetime',
      type: 'datetime',
      wrappers: ['form-field'],
      templateOptions: {
        label: 'Date Time Picker'
      }
    },
    {
      key: 'date',
      type: 'date',
      wrappers: ['form-field'],
      templateOptions: {
        label: 'DatePicker'
      }
      // parsers: [
      //   (value: any) => {
      //     console.log(value);
      //     return value.format(DATE_TIME_FORMAT);
      //   }
      // ]
    },
    {
      key: 'time',
      type: 'time',
      wrappers: ['form-field'],
      templateOptions: {
        label: 'Time'
      }
    },
    {
      type: 'tabset',
      fieldGroup: [
        {
          key: 'basic',
          templateOptions: { label: 'Basic information' },
          fieldGroup: [
            { key: 'basicName', type: 'input', templateOptions: { label: 'Name' } },
            { key: 'basicDesc', type: 'textarea', templateOptions: { label: 'Description' } }
          ]
        },
        {
          key: 'other',
          templateOptions: { label: 'Other information' },
          fieldGroup: [{ key: 'otherAddr', type: 'input', templateOptions: { label: 'Address' } }]
        }
      ]
    },
    {
      type: 'card',
      templateOptions: { label: 'Widget Title' },
      fieldGroup: [{ key: 'name', type: 'input', templateOptions: { label: 'Name' } }]
    },
    {
      type: 'accordion',
      fieldGroup: [
        {
          templateOptions: { label: 'Primary' },
          fieldGroup: [{ key: 'name', type: 'input', templateOptions: { label: 'Name' } }]
        },
        {
          templateOptions: { label: 'Secondary' },
          fieldGroup: [{ key: 'hobby', type: 'input', templateOptions: { label: 'Hobby' } }]
        }
      ]
    },
    {
      key: 'users',
      type: 'select-table',
      templateOptions: {
        label: 'Users',
        apiEndpoint: 'api/users',
        params: { activated: true },
        // Pagination under available table
        itemsPerPage: 2,
        predicate: 'login',
        reverse: false,
        // Attributes on the table
        columns: ['login', 'email', 'firstName', 'lastName'],
        hideSelected: false
      },
      fieldArray: {
        fieldGroup: []
      }
    },
    {
      key: 'accounts',
      type: 'ng-select',
      wrappers: ['form-field'],
      templateOptions: {
        label: 'Accounts',
        apiEndpoint: 'api/users',
        params: { activated: true },
        hideSelected: false,
        key: 'id',
        val: 'login',
        multiple: true
      }
    }
  ];
  constructor() {}

  ngOnInit(): void {
    this.model = {
      date: '2019-08-03',
      accounts: ['user-0', 'user-1']
    };
  }

  submit(): void {
    alert('FIXME: Submit the model' + JSON.stringify(this.model));
  }
}
