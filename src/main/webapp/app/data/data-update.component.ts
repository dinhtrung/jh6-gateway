import { Component, OnInit } from '@angular/core';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { HttpResponse } from '@angular/common/http';

import { FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, combineLatest, from } from 'rxjs';
import { tap } from 'rxjs/operators';
// + Form Builder
import * as _ from 'lodash';
import * as moment from 'moment';
import { AccountService } from 'app/core/auth/account.service';
import { EntityService } from 'app/common/model/entity.service';
import { Title } from '@angular/platform-browser';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

@Component({
  selector: 'jhi-data-update',
  templateUrl: './data-update.component.html'
})
export class DataUpdateComponent implements OnInit {
  _ = _;
  title = '';
  isReady = false;
  isSaving = false;
  model: any = {};
  fields: FormlyFieldConfig[] = [];
  prop = '';
  svc = '';
  debug = DEBUG_INFO_ENABLED;
  apiEndpoint = '';
  editForm = new FormGroup({});
  options: any;

  constructor(
    private titleService: Title,
    protected accountService: AccountService,
    protected dataService: EntityService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    combineLatest(
      from(this.accountService.identity()).pipe(
        tap(
          account =>
            (this.options = {
              formState: {
                mainModel: this.model,
                account,
                moment
              }
            })
        )
      ),
      this.activatedRoute.data.pipe(
        tap(({ templateFile, model }) => {
          this.title = _.get(templateFile, 'title', 'createOrEditData');
          this.titleService.updateTitle(this.title);
          // this.languageHelper.updateTitle(this.title);
          this.svc = templateFile.svc;
          this.prop = templateFile.prop;
          // + apiEndpoint and params
          this.apiEndpoint = _.get(templateFile, 'config.apiEndpoint', templateFile.apiEndpoint);
          // + form rendering
          this.fields = _.get(templateFile, 'config.fields', []);
          this.model = model;
        })
      )
    ).subscribe(() => (this.isReady = true));
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (_.get(this.model, 'id') !== undefined) {
      this.subscribeToSaveResponse(this.dataService.update(this.model, this.apiEndpoint));
    } else {
      this.subscribeToSaveResponse(this.dataService.create(this.model, this.apiEndpoint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
