import { Component, OnInit,Input } from '@angular/core';

@Component({
  selector: 'gis-upload-attachments-progress',
  templateUrl: './upload-attachments-progress.component.html',
  styleUrls: ['./upload-attachments-progress.component.css']
})
export class UploadAttachmentsProgressComponent implements OnInit {

  @Input() progress = 0;
  constructor() {}

  ngOnInit() {
  }

}
