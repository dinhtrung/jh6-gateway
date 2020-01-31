import { Component, OnInit } from '@angular/core';
import * as jsyaml from 'js-yaml';

@Component({
  selector: 'jhi-js-yaml',
  templateUrl: './js-yaml.component.html'
})
export class JsYamlComponent implements OnInit {
  modelJson = '';
  modelYaml = '';
  constructor() {}

  ngOnInit(): void {}

  jsonToYaml(): void {
    this.modelYaml = jsyaml.dump(JSON.parse(this.modelJson));
  }

  yamlToJson(): void {
    this.modelJson = JSON.stringify(jsyaml.load(this.modelYaml));
  }
}
