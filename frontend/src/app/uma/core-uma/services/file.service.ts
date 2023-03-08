import { Injectable } from '@angular/core';
import { FILE_CONFIG, FILE_CONFIG_PDF } from '../config/file-config';
import { FileConfigModel } from '../models/config/file-config.model';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor() { }

  loadConfig(): FileConfigModel {
    return {
      fileExt: FILE_CONFIG.fileExt,
      maxSize: FILE_CONFIG.maxSize
    }
  }

  loadConfigPdf(): FileConfigModel {
    return {
      fileExt: FILE_CONFIG_PDF.fileExt,
      maxSize: FILE_CONFIG_PDF.maxSize
    }
  }

}
