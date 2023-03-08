import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.css']
})
export class ErrorPageComponent implements OnInit {

  error: string;
  messageError: string;

  constructor(
    private route: ActivatedRoute,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.setError();
  }

  private setError() {
    this.error = this.route.snapshot.paramMap.get('error');
    console.log(this.error)
    if (this.error)
      this.messageError = 'Error ' + this.error + ' - ' + this.translateService.instant('PAGINA_ERRORE.' + this.error).toUpperCase();
    else
      this.messageError = 'PAGE NOT FOUND';
  }

}
