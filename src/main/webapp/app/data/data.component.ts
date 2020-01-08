import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { EntityService } from 'app/common/model/entity.service';
import { AccountService } from 'app/core/auth/account.service';
import { LanguageHelper } from 'app/core/language/language.helper';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
// + Modal
import { NgbModal, ModalDismissReasons, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

// + search
import * as _ from 'lodash';
import * as jsyaml from 'js-yaml';

@Component({
  selector: 'jhi-data',
  templateUrl: './data.component.html'
})
export class DataComponent implements OnInit, OnDestroy {
  _ = _;
  isReady = false;
  currentAccount: any;
  // + data
  tasks: any[] = []; // List of available task from this UI
  actions: any[] = []; // List of available options for each row
  fields: any;
  rows: any[] = [];
  columns: any[] = [];
  columnKeys: string[] = [];
  columnNames: string[] = [];
  displayColumns: any = {};
  // How to display the table
  title: string = ''; // page title
  prop: string = ''; // entity namespace
  svc: string = ''; // service namespace
  apiEndpoint: string = '';
  queryParams: any;
  // + states
  error: any;
  success: any;
  eventSubscriber: any;
  // + pagination
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  // + search support
  filterOperators: string[] = [];
  searchModel: any;
  // + delete Modal
  @ViewChild('deleteModal', { static: true }) deleteModal: any;
  // + references
  referenceMap: any = {};
  referenceEndpoint: any = {};

  constructor(
    private languageHelper: LanguageHelper,
    protected dataService: EntityService,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
  }

  loadAll() {
    this.dataService
      .query(
        _.assign(
          this.queryParams,
          {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
          },
          this.searchModel
        ),
        this.apiEndpoint
      )
      .subscribe(
        (res: HttpResponse<any[]>) => this.paginate(res.body || [], res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate([], {
      queryParams: _.assign(
        {},
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        },
        this.searchModel
      )
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.isReady = false;
    combineLatest(
      this.activatedRoute.data.pipe(
        map(data => {
          // + pagination parameters
          this.page = data.pagingParams.page;
          this.previousPage = data.pagingParams.page;
          this.reverse = data.pagingParams.ascending;
          this.predicate = data.pagingParams.predicate;
          // + prop and yaml
          this.prop = data.templateFile.prop;
          this.svc = data.templateFile.svc;
          this.title = _.get(data.templateFile, 'config.title', 'app.title.' + this.prop);
          // this.languageHelper.updateTitle(this.title);
          // + apiEndpoint and params
          this.tasks = _.get(data.templateFile, 'config.tasks', []);
          this.actions = _.get(data.templateFile, 'config.actions', []);
          this.apiEndpoint = _.get(data.templateFile, 'config.apiEndpoint', data.templateFile.apiEndpoint);
          this.queryParams = _.get(data.templateFile, 'config.queryParams', {});
          // + fields
          this.fields = _.get(data.templateFile, 'config.fields', []);
          console.log('Fields', JSON.stringify(this.fields));
          this.columns = _.get(data.templateFile, 'config.columns', ['id']);
          this.columnKeys = _.map(this.columns, c => _.get(c, 'prop', c));
          this.columnNames = _.map(this.columns, c => _.get(c, 'label', c));
          // TODO: Store the list of display columns under account preferences or session storage
          _.each(this.columnKeys, c => _.set(this.displayColumns, c, true));
          this.filterOperators = _.get(data.templateFile, 'config.filterOperators');
          // + calculate filtering map for select field, which annotated with `options`
          this.referenceMap = {};
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
        })
      ),
      this.activatedRoute.queryParams.pipe(map(params => (this.searchModel = _.omit(params, ['size', 'sort', 'page']))))
    ).subscribe(() => this.loadAll());
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.registerChangeInData();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: any) {
    return item.id;
  }

  registerChangeInData() {
    this.eventSubscriber = this.eventManager.subscribe('dataListModification', () => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginate(data: any[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link') || '');
    this.totalItems = parseInt(headers.get('X-Total-Count') || '0', 10);
    this.rows = data;
    this.loadReferences();
    this.isReady = true;
  }

  protected loadReferences() {
    // + load reference remote entities based on apiEndpoint
    _.each(this.referenceEndpoint, (templateOptions, fieldKey) => {
      const ids = _.uniq(
        _.flatMap(
          _.map(this.rows, i => _.get(i, fieldKey)).filter(i => !_.isEmpty(i)),
          values => (_.isArray(values) ? _.values(values) : values)
        )
      );
      console.log('Gotta find all related info for key = ' + fieldKey, ids);
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
    this.jhiAlertService.error(errorMessage);
  }

  // Add search modifier
  setSearchOperator(field: string, operator: string) {
    _.set(this.searchModel, field, `${operator}(${_.get(this.searchModel, field)})`);
  }
  toggleView(column: string) {
    this.displayColumns[column] = !this.displayColumns[column];
  }
  // + delete confirm
  delete(t: any) {
    this.modalService.open(this.deleteModal).result.then(
      result => {
        this.dataService.delete(t.id, this.apiEndpoint).subscribe(
          res => this.loadAll(),
          err => this.onError(err.error.title)
        );
      },
      reason => {
        this.modalService.dismissAll();
      }
    );
  }

  // Render cell value based on current reference map
  renderCell(row: any, col: string) {
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
