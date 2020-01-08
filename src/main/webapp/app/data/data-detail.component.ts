import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as _ from 'lodash';
import * as moment from 'moment';
import { AccountService } from 'app/core/auth/account.service';
import { combineLatest, from } from 'rxjs';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'jhi-data-detail',
  templateUrl: './data-detail.component.html'
})
export class DataDetailComponent implements OnInit {
  _ = _;
  isReady = false;
  model: any;
  columns: any[] = [];
  prop: string = '';
  svc: string = '';
  account: any;
  fields: any[] = [];
  options: any;

  constructor(protected activatedRoute: ActivatedRoute, private accountService: AccountService) {}

  ngOnInit() {
    this.isReady = false;
    combineLatest(
      this.activatedRoute.data.pipe(
        tap(({ templateFile, model }) => {
          this.model = model;
          this.fields = _.get(templateFile, 'config.details', this.generateDefaultFields());
          console.log('Fields', JSON.stringify(this.fields));
        })
      ),
      this.accountService.identity().pipe(
        tap(
          account =>
            (this.options = {
              formState: {
                mainModel: this.model
              },
              account,
              moment
            })
        )
      )
    ).subscribe(res => (this.isReady = true));
  }

  previousState() {
    window.history.back();
  }
  // Generate default fields based on model keys
  generateDefaultFields() {
    return _.map(this.model, (val, key) => {
      return {
        template: `<div class="d-flex justify-content-between"><strong>${key}</strong><em>${val}</em></div>`
      };
    });
  }
}
