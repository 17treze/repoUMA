import { Component, OnInit, ViewChild, ElementRef, HostListener, Input, Output } from '@angular/core';
import { UploadService } from '../upload.service';
import { Observable } from 'rxjs';
import { NgForm, FormControl, ReactiveFormsModule } from '@angular/forms';
import { EventEmitter } from 'events';
import { onErrorResumeNext } from 'rxjs/operators';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  errors: Array<string> = [];
  @Input() fileExt: string = ".PDF"; //.JPG,.GIF,.PNG
  @Input() maxSize: number = 1; // 5MB
  @Input() serviceUrl: string;
  @Input() id;
  @Input() title: string;

  @Output() uploadStatus = new EventEmitter();
  @ViewChild('fileInput') fileInput: ElementRef;

  uploadedFile: File;
  loading: boolean;
  uploadResult: Observable<any>;

  constructor(private uploadService: UploadService) { }

  ngOnInit() {
    this.loading = true;
  }

  onFileChange(event) {
    console.log("ChangeFile");
    if (event.target.files && event.target.files.length > 0) {
      this.loading = false;
      this.uploadedFile = event.target.files[0];
      console.log("FileName " + this.uploadedFile.name);
      console.log("FileSize " + this.getFileSize(this.uploadedFile));
    }
  }

  clearFile() {
    this.uploadedFile = null;
    this.fileInput.nativeElement.value = '';
    this.loading = true;
  }

  onSubmit() {
    this.loading = true;
    this.errors = [];
    console.log("SubmitFile");
    if (this.isValidFile(this.uploadedFile)) {
      this.uploadService.uploadFile(this.serviceUrl, this.uploadedFile, this.id)
        .subscribe((next) => this.uploadResult = next, () => console.log(this.uploadResult), () => this.loading = false);
    }
    else {
      console.log("File non valido");
      this.loading = false;
    }
  }

  private isValidFile(file) {
    if (file && this.isValidFileExtension(file) && this.isValidFileSize(file)) {
      console.log("Ok (File): " + file.name);
      return true;
    }
    else {
      return false;
    }

  }

  private isValidFileExtension(file): boolean {
    // Make array of file extensions
    var extensions = (this.fileExt.split(','))
      .map(function (x) { return x.toLocaleUpperCase().trim().replace('.', '') });

    var ext = file.name.toUpperCase().split('.').pop() || file.name;
    var exists = extensions.includes(ext);
    if (!exists) {
      console.log("Error (File Extension): " + file.name);
      this.errors.push("Error (Extension): " + file.name);
      return false;
    }
    else
      return true;
  }

  private isValidFileSize(file): boolean {
    if (this.maxSize < 0)
      return true;

    var size = this.getFileSize(file);
    if (size > this.maxSize) {
      this.errors.push("Error (File Size): " + file.name + ": exceed file size limit of " + this.maxSize + "MB ( " + size + "MB )");
      console.log("Error (File Size): " + file.name);
      return false;
    }
    else
      return true;
  }

  private getFileSize(file): number {
    var fileSizeinMB = file.size / (1024 * 1000);
    var size = Math.round(fileSizeinMB * 100) / 100; // convert upto 2 decimal place
    return size;
  }
}
