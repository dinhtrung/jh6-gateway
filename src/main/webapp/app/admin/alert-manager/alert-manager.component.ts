import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

// + Modal
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { plainToFlattenObject } from 'app/common/util/request-util';
import { EntityService } from 'app/common/model/entity.service';

@Component({
  selector: 'jhi-alert-manager',
  templateUrl: './alert-manager.component.html'
})
export class AlertManagerComponent implements OnInit {
  alerts?: any[];
  fromDate = '';
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  previousPage!: number;
  ascending!: boolean;
  toDate = '';
  totalItems = 0;

  private dateFormat = 'yyyy-MM-dd';
  // + details modal
  @ViewChild('detailModal', { static: true }) detailModal: any;
  model = '';
  details: any;

  constructor(
    private entityService: EntityService,
    private activatedRoute: ActivatedRoute,
    private datePipe: DatePipe,
    // + detail view
    protected modalService: NgbModal,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.toDate = this.today();
    this.fromDate = this.previousMonth();
    this.activatedRoute.data.subscribe(data => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.ascending = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
      this.loadData();
    });
  }

  loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  canLoad(): boolean {
    return this.fromDate !== '' && this.toDate !== '';
  }

  transition(): void {
    if (this.canLoad()) {
      this.router.navigate(['/admin/alert-manager'], {
        queryParams: {
          page: this.page,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
        }
      });
      this.loadData();
    }
  }

  private previousMonth(): string {
    let date = new Date();
    if (date.getMonth() === 0) {
      date = new Date(date.getFullYear() - 1, 11, date.getDate());
    } else {
      date = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate());
    }
    return this.datePipe.transform(date, this.dateFormat)!;
  }

  private today(): string {
    // Today + 1 day - needed if the current day must be included
    const date = new Date();
    date.setDate(date.getDate() + 1);
    return this.datePipe.transform(date, this.dateFormat)!;
  }

  private loadData(): void {
    this.entityService
      .query(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          fromDate: this.fromDate,
          toDate: this.toDate
        },
        'services/alert-manager/api/v2/alerts'
      )
      .subscribe((res: HttpResponse<any[]>) => this.onSuccess(res.body, res.headers));
  }

  private sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(alerts: any[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.alerts = alerts || [];
  }
  // + detail view
  viewDetail(item: string): void {
    this.model = item;
    try {
      this.details = plainToFlattenObject(JSON.parse(item));
    } catch (Error) {
      this.details = undefined;
    }
    this.modalService.open(this.detailModal, { size: 'lg' }).result.then(
      () => this.modalService.dismissAll(),
      () => this.modalService.dismissAll()
    );
  }

  // + render the badge class
  getBadgeClass(statusState: string): string {
    if (['CRITICAL', 'DANGER', 'ERROR', 'FIRING', 'ACTIVE'].includes(statusState.toUpperCase())) {
      return 'badge-danger blinking';
    } else if (['UP', 'PASSING', 'RESOLVED'].includes(statusState.toUpperCase())) {
      return 'badge-success';
    } else if (['WARNING', 'ALARM'].includes(statusState.toUpperCase())) {
      return 'badge-warning blinking';
    } else {
      return 'badge-dark';
    }
  }
}
