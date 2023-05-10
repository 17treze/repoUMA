import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EMPTY, Subscription } from 'rxjs';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { MessageService } from 'primeng/api';
import { GruppoColtureDto } from 'src/app/uma/core-uma/models/dto/GruppoColtureDto';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';

@Component({
  selector: 'app-gruppi-colture',
  templateUrl: './gruppi-colture.component.html',
  styleUrls: ['./gruppi-colture.component.scss']
})
export class GruppiColtureComponent implements OnInit {

  listaGruppi: Array<GruppoColtureDto>;

  // Subscriptions
  getGruppiSubscription: Subscription;

  constructor(
    private errorService: ErrorService,
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.listaGruppi = [];
    this.getGruppi();
  }

  private getGruppi() {
    this.getGruppiSubscription = this.httpClientConfigurazioneUmaService.getGruppiColture()
      .subscribe((gruppi: Array<GruppoColtureDto>) => {
        if (gruppi && gruppi.length) {
          this.listaGruppi = [];
          gruppi.forEach(gruppoColt => {
            this.listaGruppi.push(gruppoColt);
          })
        } else {
          this.listaGruppi = [];
        }
      }, error => this.errorService.showError(error, 'tst-macchine'));
  }

}