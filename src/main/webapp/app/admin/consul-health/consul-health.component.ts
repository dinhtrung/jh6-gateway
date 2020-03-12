import { Component, OnInit } from '@angular/core';
// + perform health check
import { HttpClient } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { SERVER_API_URL } from 'app/app.constants';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-consul-health',
  templateUrl: './consul-health.component.html',
  styleUrls: ['./consul-health.component.scss']
})
export class ConsulHealthComponent implements OnInit {
  healthIndicators: any[] = [];

  constructor(private httpClient: HttpClient, private jhiAlertService: JhiAlertService) {}

  ngOnInit(): void {
    this.refresh();
  }
  refresh(): void {
    this.httpClient
      .get<any[]>(SERVER_API_URL + 'api/consul/health', { observe: 'response' })
      .pipe(
        filter(res => res.ok),
        map(res => res.body || [])
      )
      .subscribe(
        res => (this.healthIndicators = res),
        err => this.jhiAlertService.error(err.message)
      );
  }
}
