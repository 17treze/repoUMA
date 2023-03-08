import { A4gMessages } from "../a4g-messages";

export class UploadHelper {
  errors: Array<string> = [];
  fileExt: string//.JPG,.GIF,.PNG
  maxSizeMb: number

  public constructor(fileExt: string, maxSizeMb: number) {
    this.fileExt = fileExt; //.JPG,.GIF,.PNG
    this.maxSizeMb = maxSizeMb; // 5
    this.errors = [];
  }

  public isValidFile(file: File) {
    if (file && this.isValidFileExtension(file) && this.isValidFileSize(file)) {
      return true;
    }
    else {
      return false;
    }

  }

  public isValidFileExtension(file: File, errorMessage?: string): boolean {
    const message: string = errorMessage || A4gMessages.FILE_TYPE(this.fileExt.replace(".", ""));

    // Make array of file extensions
    const extensions = (this.fileExt.split(','))
      .map(function (x) { return x.toLocaleUpperCase().trim().replace('.', '') });

    const ext = file.name.toUpperCase().split('.').pop() || file.name;
    const exists = extensions.includes(ext);
    if (!exists) {
      this.errors.push(message);
      return false;
    }
    else
      return true;
  }

  public isValidFileSize(file: File, errorMessage?: string): boolean {
    const message: string = errorMessage || A4gMessages.FILE_MAXSIZE(this.maxSizeMb);

    if (this.maxSizeMb < 0)
      return true;

    const size = this.getFileSize(file);
    if (size > this.maxSizeMb) {
      this.errors.push(message);
      return false;
    }
    else
      return true;
  }

  public getFileSize(file: File): number {
    var fileSizeinMB = file.size / (1024 * 1000);
    var size = Math.round(fileSizeinMB * 100) / 100; // convert upto 2 decimal place
    return size;
  }

  public blobToFile = (theBlob: Blob, fileName: string): File => {
    const b: any = theBlob;
    b.lastModifiedDate = new Date();
    b.name = fileName;
    return <File>theBlob;
  }

  public stringToFile = (byteArray: string, filename: string, type: string): File => {
     // 1. conversione string binario in array byte
     const byteCharacters = atob(byteArray);
     const byteNumbers = new Array(byteCharacters.length);
     for (let i = 0; i < byteCharacters.length; i++) {​​​​​​​​
     byteNumbers[i] = byteCharacters.charCodeAt(i);
               }​​​​​​​​
     const byteArrayOutput = new Uint8Array(byteNumbers);
     // 2. creazione blob del file
     const blob = new Blob([byteArrayOutput], {​​​​​​​​ type: type }​​​​​​​​);
     const file = new File([blob], filename, { type: type });
     return file;
  }

  getErrors() {
    return this.errors;
  }

}
