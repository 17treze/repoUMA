import { Component, Input, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { A4GToastModel, A4GToastSeverityEnum } from './a4g-toast.model';

@Component({
  selector: 'app-a4g-toast',
  templateUrl: './a4g-toast.component.html',
  styleUrls: ['./a4g-toast.component.scss']
})
export class A4gToastComponent implements OnInit {

  @Input() a4gToastModels: Array<A4GToastModel>;

  constructor() {
  }

  ngOnInit() {
  }
 
}
