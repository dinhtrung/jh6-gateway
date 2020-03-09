import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { JhiDataUtils, JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-data-import',
  templateUrl: './data-import.component.html'
})
export class DataImportComponent implements OnInit {
  dataFile: any = {};
  jsonString = '';
  jsonData: any[] = [];
  isSaving = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private alertService: JhiAlertService,
    private dataUtils: JhiDataUtils,
    private httpClient: HttpClient
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ templateFile }) => this.apiEndpoint = `api/import/nodes{}`);
  }

  setFileData(event: any): void {
    const file: File = event.target.files[0];
    const fileReader: FileReader = new FileReader();
    fileReader.onload = () => (this.jsonString = fileReader.result as string);
    fileReader.readAsText(file);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.jsonData = JSON.parse(this.jsonString);
    this.httpClient.put(SERVER_API_URL + 'api/import/nodes', this.jsonData, { observe: 'response' }).subscribe(
      () => this.onSaveSuccess(),
      (res: HttpErrorResponse) => this.onSaveError(res)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.alertService.success('xlsx-import.accepted');
  }

  protected onSaveError(error: any): void {
    this.isSaving = false;
    this.alertService.error(error.error, error.message);
  }
}
