import { Component, OnInit } from '@angular/core';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaDettaglioService } from '../istruttoria-dettaglio.service';
import { Istruttorie } from '../dettaglio-istruttoria';

@Component({
  selector: 'app-dati-liquidazione',
  templateUrl: './dati-liquidazione.component.html',
  styleUrls: ['./dati-liquidazione.component.css']
})
export class DatiLiquidazioneComponent implements OnInit {

  public istruttoriaDUCorrente: IstruttoriaDomandaUnica;
  public istruttoriaInfo: Istruttorie;
  public totaleDebitoRecuperato: number[] = [];
  public totaleImportoLiquidato: number[] = [];
  
  constructor(
    private route: ActivatedRoute, 
    private istruttoriaService: IstruttoriaDettaglioService
  ) { }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.istruttoriaService.getIstruttorie(this.istruttoriaDUCorrente.domanda.numeroDomanda)
      .subscribe(istruttorie => {
        this.istruttoriaInfo = istruttorie.find( ist => ist.sostegno == this.istruttoriaDUCorrente.sostegno && ist.tipoIstruttoria == this.istruttoriaDUCorrente.tipo);
        this.calcolaTotaleDebiti();
        this.calcolaImportoLiquidato();
      });
  }

  private calcolaTotaleDebiti(): any {
      this.totaleDebitoRecuperato[0] = 0;
      if (this.istruttoriaInfo.importiIstruttoria && this.istruttoriaInfo.importiIstruttoria.debitiRecuperati != null) {
        for (let debitoRecuperato of this.istruttoriaInfo.importiIstruttoria.debitiRecuperati) {
          this.totaleDebitoRecuperato[0] = (this.totaleDebitoRecuperato[0] * 100 + debitoRecuperato.importo * 100) / 100;
        }
      }
  }

  private calcolaImportoLiquidato(): any {
      this.totaleImportoLiquidato[0] = 0;
      if (this.istruttoriaInfo.importiIstruttoria && this.istruttoriaInfo.importiIstruttoria.importoAutorizzato != null) {
        this.totaleImportoLiquidato[0] = (this.istruttoriaInfo.importiIstruttoria.importoAutorizzato * 100 - this.totaleDebitoRecuperato[0] * 100) / 100;
      }
  }

}
