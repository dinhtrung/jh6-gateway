import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
// + HTTP support
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { plainToFlattenObject } from 'app/common/util/request-util';
// + ng-select
import { Subject, Observable, of, concat, Subscription, combineLatest } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FieldArrayType, FieldType } from '@ngx-formly/core';
import * as _ from 'lodash';
import { JhiParseLinks } from 'ng-jhipster';
import { FormControl } from '@angular/forms';
// + Modal
import { NgbModal, ModalDismissReasons, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EntityService } from 'app/common/model/entity.service';

@Component({
  selector: 'jhi-formly-select-table',
  template: `
    <label *ngIf="to.label && to.hideLabel !== true" [attr.for]="id">
      <span [jhiTranslate]="to.label"></span>
      <span *ngIf="to.required && to.hideRequiredMarker !== true">*</span>
    </label>
    <button *ngIf="to.create" type="button" class="btn btn-primary btn-sm float-right" (click)="create()">
      <fa-icon icon="plus"></fa-icon>
      <span class="d-none d-md-inline">Create New</span>
    </button>

    <div class="table-responsive">
      <table class="table table-striped table-bordered">
        <thead>
          <tr>
            <th *ngFor="let c of columnNames">
              <span>{{ c }}</span>
            </th>
            <th></th>
          </tr>
        </thead>
        <tbody *ngIf="rows?.length > 0">
          <tr *ngFor="let val of rows; let i = index; trackBy: trackId">
            <td *ngFor="let c of columnKeys" [innerHTML]="renderCell(val, c)"></td>
            <td class="text-right">
              <div class="btn-group">
                <button type="button" class="btn btn-success btn-sm" (click)="editItem(val)">
                  <fa-icon icon="pencil-alt"></fa-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </button>
                <button class="btn btn-danger btn-sm" type="button" (click)="delete(val)">
                  <fa-icon icon="ban"></fa-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <ng-template #deleteModal let-delmodal>
      <div class="modal-header">
        <h4 class="modal-title" jhiTranslate="entity.delete.title">Confirm delete operation</h4>
        <button type="button" class="close" (click)="delmodal.dismiss()">&times;</button>
      </div>
      <div class="modal-body">
        <p>Are you sure you want to delete this item?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal" (click)="delmodal.dismiss()">
          <fa-icon [icon]="'ban'"></fa-icon>&nbsp;<span jhiTranslate="entity.action.cancel">Cancel</span>
        </button>
        <button id="jhi-confirm-delete-ticket" type="submit" class="btn btn-danger" (click)="delmodal.close()">
          <fa-icon [icon]="'times'"></fa-icon>&nbsp;<span jhiTranslate="entity.action.delete">Delete</span>
        </button>
      </div>
    </ng-template>
    <ng-template #formModal let-formmodal>
      <form (ngSubmit)="formmodal.close()">
        <div class="modal-header">
          <h4 class="modal-title" jhiTranslate="entity.add.title">Add Modal</h4>
          <button type="button" class="close" (click)="formmodal.dismiss()">&times;</button>
        </div>
        <div class="modal-body">
          <formly-form [model]="modalModel" [fields]="field.fieldArray.fieldGroup" [options]="options"></formly-form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal" (click)="formmodal.dismiss()">
            <fa-icon icon="ban"></fa-icon>&nbsp;<span jhiTranslate="entity.action.cancel">Cancel</span>
          </button>
          <button id="jhi-confirm-delete-ticket" type="submit" class="btn btn-danger" (click)="formmodal.close()">
            <fa-icon icon="save"></fa-icon>&nbsp;<span jhiTranslate="entity.action.save">Save</span>
          </button>
        </div>
      </form>
    </ng-template>
  `
})
export class CrudTableTypeComponent extends FieldArrayType implements OnInit, OnDestroy {
  rows: any[];
  columns: any[];
  columnKeys: string[];
  columnNames: string[];
  prop: string;
  apiEndpoint: string;
  _ = _;
  // pagination
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  // + selected
  hideSelected: boolean;
  // + delete Modal
  @ViewChild('deleteModal', { static: true })
  deleteModal: any;

  @ViewChild('formModal', { static: true })
  formModal: any;

  rowIdx: number;
  modalModel: any;
  // + references
  referenceMap: any = {};
  referenceEndpoint: any = {};

  constructor(
    public httpClient: HttpClient,
    protected parseLinks: JhiParseLinks,
    protected modalService: NgbModal,
    protected dataService: EntityService
  ) {
    super();
    this.rows = [];
    this.columns = [];
    this.columnKeys = [];
    this.columnNames = [];
    this.prop = '';
    this.apiEndpoint = '';
    this.hideSelected = true;
    this.rowIdx = 0;
  }

  ngOnInit() {
    this.page = 1;
    this.itemsPerPage = _.get(this.to, 'itemsPerPage', 1000);
    this.predicate = _.get(this.to, 'predicate', 'id');
    this.reverse = _.get(this.to, 'reverse', true);
    this.hideSelected = _.get(this.to, 'hideSelected', true);
    this.columns = _.get(this.to, 'columns', ['id']);
    this.columnKeys = _.map(this.columns, c => _.get(c, 'prop', c));
    this.columnNames = _.map(this.columns, c => _.get(c, 'label', c));
    // + populate references
    _.each(
      _.filter(this.columns, i => _.get(i, 'options')),
      i => _.each(i.options, o => _.set(this.referenceMap, [i.prop, o.value], o.label))
    );
    // + calculate reference for ng-select
    this.referenceEndpoint = {};
    _.each(
      _.filter(this.columns, i => _.get(i, 'apiEndpoint')),
      field => (this.referenceEndpoint[field.prop] = _.pick(field, ['apiEndpoint', 'params', 'key', 'val']))
    );
    console.log('referenceMap', this.referenceMap, 'referenceEndpoint', this.referenceEndpoint);
    this.loadAll();
  }

  loadAll() {
    this.httpClient
      .get<any[]>(SERVER_API_URL + this.to.apiEndpoint, {
        params: createRequestOption(
          _.assign(
            {},
            {
              page: this.page - 1,
              size: this.itemsPerPage,
              sort: this.sort()
            },
            plainToFlattenObject(this.to.params)
          )
        ),
        observe: 'response'
      })
      .subscribe(
        (res: HttpResponse<any[]>) => this.paginate(res.body || [], res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.loadAll();
    }
  }

  ngOnDestroy() {
    // this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: any) {
    return item.id;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  // Paginate the data into table
  protected paginate(data: any[], headers: HttpHeaders) {
    this.links = headers.get('link') ? this.parseLinks.parse(headers.get('link') || '') : '';
    this.totalItems = headers.get('X-Total-Count') ? parseInt(headers.get('X-Total-Count') || '0', 10) : 0;
    this.rows = data;
    this.loadReferences();
  }

  loadReferences() {
    // + load reference remote entities based on apiEndpoint
    _.each(this.referenceEndpoint, (templateOptions, fieldKey) => {
      const ids = _.uniq(
        _.flatMap(
          _.map(this.rows, i => _.get(i, fieldKey)).filter(i => !_.isEmpty(i)),
          values => (_.isArray(values) ? _.values(values) : values)
        )
      );
      const q = _.get(templateOptions, 'params', {});
      _.set(q, templateOptions.key, ids);
      this.dataService
        .query(q, templateOptions.apiEndpoint)
        .subscribe(refData =>
          _.each(refData.body, i => _.set(this.referenceMap, [fieldKey, _.get(i, templateOptions.key)], _.get(i, templateOptions.val)))
        );
    });
  }

  protected onError(errorMessage: string) {
    console.error(errorMessage);
  }

  // Update item
  editItem(model: any) {
    this.modalModel = model;
    this.openUpdateModal();
  }
  // + delete confirm
  delete(model: any) {
    this.modalModel = model;
    this.modalService.open(this.deleteModal).result.then(
      result => this.dataService.delete(model.id, SERVER_API_URL + this.to.apiEndpoint).subscribe(res => this.loadAll()),
      reason => this.modalService.dismissAll()
    );
  }
  // Create
  create() {
    this.modalModel = {};
    _.each(this.to.params, (v, k) => _.set(this.modalModel, k, v));
    this.openUpdateModal();
  }
  // Open the modal
  openUpdateModal() {
    this.modalService.open(this.formModal, { size: 'lg' }).result.then(
      result => {
        (this.modalModel.id
          ? this.dataService.update(this.modalModel, SERVER_API_URL + this.to.apiEndpoint)
          : this.dataService.create(this.modalModel, SERVER_API_URL + this.to.apiEndpoint)
        ).subscribe(res => this.loadAll());
      },
      reason => this.modalService.dismissAll()
    );
  }

  renderCell(row: any, col: any) {
    // {{ _.get(referenceMap, [c, _.get(val, c)], _.get(val, c)) }}
    const val = _.get(row, col);
    if (_.isArray(val)) {
      return _.map(val, v => _.get(this.referenceMap, [col, v], v));
    } else if (_.isPlainObject(val)) {
      return JSON.stringify(val);
    }
    return _.get(this.referenceMap, [col, val], val);
  }
}
