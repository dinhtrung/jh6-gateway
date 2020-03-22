import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EntityService } from 'app/common/model/entity.service';
import { filter, map } from 'rxjs/operators';
import * as _ from 'lodash';

@Component({
  selector: 'jhi-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.scss']
})
export class DataComponent implements OnInit {
  _ = _;
  site: any;
  templateFile: any;
  pagingParams: any;
  contentType: any;
  columns: any[] = [];
  constructor(private activatedRoute: ActivatedRoute, private entityService: EntityService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contentType, pagingParams }) => {
      this.site = contentType.site;
      this.pagingParams = pagingParams;
      this.contentType = contentType;
      this.loadColumns(templateFile);
    });
  }

  loadColumns(contentType: any): void {
    this.entityService
      .query({ type: 'FIELD', tags: contentType.id, state: 200 }, 'api/nodes')
      .pipe(
        filter(res => res.ok),
        map(res => res.body || [])
      )
      .subscribe(columns => (this.columns = columns));
  }
}
