import { Component, OnInit, AfterContentChecked } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as _ from 'lodash';
import * as moment from 'moment';
import { AccountService } from 'app/core/auth/account.service';
import { combineLatest } from 'rxjs';
import { tap } from 'rxjs/operators';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-data-detail',
  templateUrl: './data-detail.component.html'
})
export class DataDetailComponent implements OnInit, AfterContentChecked {
  _ = _;
  isReady = false;
  model: any;
  columns: any[] = [];
  prop = '';
  svc = '';
  account: any;
  fields: any[] = [];
  options: any = {
    formState: {
      moment
    }
  };
  form = new FormGroup({});

  constructor(protected activatedRoute: ActivatedRoute, private accountService: AccountService) {}

  ngOnInit(): void {
    this.isReady = false;
    combineLatest(
      this.accountService.identity().pipe(tap(account => (this.options.formState.account = account))),
      this.activatedRoute.data.pipe(
        tap(({ templateFile, model }) => {
          this.svc = templateFile.svc;
          this.prop = templateFile.prop;
          this.fields = _.get(templateFile, 'config.details', _.get(templateFile, 'config.fields', this.generateDefaultFields()));
          // eslint-disable-next-line no-console
          console.log('done processing field');
          this.model = model;
          this.options.formState.mainModel = this.model;
        })
      )
    ).subscribe(() => (this.isReady = true));
  }

  previousState(): void {
    window.history.back();
  }
  ngAfterContentChecked(): void {
    this.form.disable();
  }
  // Generate default fields based on model keys
  generateDefaultFields(): any {
    return _.map(this.model, (val, key) => {
      return {
        template: `<div class="d-flex justify-content-between"><strong>${key}</strong><em>${val}</em></div>`
      };
    });
  }
}
