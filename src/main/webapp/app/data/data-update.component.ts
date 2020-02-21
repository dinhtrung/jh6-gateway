import { Component, OnInit } from '@angular/core';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, combineLatest, from } from 'rxjs';
import { tap, map, filter } from 'rxjs/operators';
// + Form Builder
import * as _ from 'lodash';
import * as moment from 'moment';
import { AccountService } from 'app/core/auth/account.service';
import { EntityService } from 'app/common/model/entity.service';
import { LanguageHelper } from 'app/core/language/language.helper';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

@Component({
  selector: 'jhi-data-update',
  templateUrl: './data-update.component.html'
})
export class DataUpdateComponent implements OnInit {
  _ = _;
  title: string;
  isReady = false;
  isSaving: boolean;
  model: any = {};
  fields: FormlyFieldConfig[];
  prop: string;
  svc: string;
  debug = DEBUG_INFO_ENABLED;
  apiEndpoint: string;
  editForm = new FormGroup({});
  options: any;

  constructor(
    private languageHelper: LanguageHelper,
    protected accountService: AccountService,
    protected dataService: EntityService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
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
          this.languageHelper.updateTitle(this.title);
          this.svc = templateFile.svc;
          this.prop = templateFile.prop;
          // + apiEndpoint and params
          this.apiEndpoint = _.get(templateFile, 'config.apiEndpoint', templateFile.apiEndpoint);
          // + form rendering
          this.fields = _.get(templateFile, 'config.fields', []);
          this.model = model;
        })
      )
    ).subscribe(res => (this.isReady = true));
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (_.get(this.model, 'id') !== undefined) {
      this.subscribeToSaveResponse(this.dataService.update(this.model, this.apiEndpoint));
    } else {
      this.subscribeToSaveResponse(this.dataService.create(this.model, this.apiEndpoint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
