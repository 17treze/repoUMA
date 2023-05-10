import { Component, OnInit } from '@angular/core';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';
import { GruppoColtureDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';

@Component({
  selector: 'app-gruppi-colture',
  templateUrl: './gruppi-colture.component.html',
  styleUrls: ['./gruppi-colture.component.scss']
})
export class GruppiColtureComponent implements OnInit {

  listaGruppi: GruppoColtureDto[] = [];
  cols: any;

  constructor(
    private errorService: ErrorService,
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService
  ) { }

  ngOnInit() {
    this.setCols();
    this.getGruppi();
  }

  private setCols() {
    this.cols = [
      { field: 'codiceSuolo', header: 'Suolo' },
      { field: 'codiceDestUso', header: 'Destinazione uso' },
      { field: 'codiceUso', header: 'Uso' },
      { field: 'codiceQualita', header: 'Qualità' },
      { field: 'codiceVarieta', header: 'Varietà' },
      { field: 'gruppoLavorazione', header: 'Gruppo lavorazione' },
      { field: 'annoInizio', header: 'Anno inizio' },
      { field: 'annoFine', header: 'Anno fine' }
    ];
  }

  private getGruppi() {
    this.httpClientConfigurazioneUmaService.getGruppiColture()
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