import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as _ from 'lodash';
import * as moment from 'moment';
import { AccountService } from 'app/core/auth/account.service';
import { combineLatest } from 'rxjs';
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
  prop = '';
  svc = '';
  account: any;
  fields: any[] = [];
  options: any;

  constructor(protected activatedRoute: ActivatedRoute, private accountService: AccountService) {}

  ngOnInit(): void {
    this.isReady = false;
    combineLatest(
      this.activatedRoute.data.pipe(
        tap(({ templateFile, model }) => {
          this.model = model;
          this.fields = _.get(templateFile, 'config.details', this.generateDefaultFields());
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
    ).subscribe(() => (this.isReady = true));
  }

  previousState(): void {
    window.history.back();
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
