import { Component, OnInit, ViewChild } from '@angular/core';
import { DettaglioAllevamenti } from '../../../domain/dettaglioAllevamenti';
import { Labels } from 'src/app/app.labels';
import { ProduzioneLatte } from '../../../domain/produzione-latte';
import { registrazioneAlpeggio } from '../../../domain/registrazione-alpeggio';
import { EtichettaturaCarne } from '../../../domain/etichettatura-carne';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { EsitoCapiFilter } from '../../../classi/esitoCapiFilter';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

@Component({
  selector: 'app-capi-ammessi',
  templateUrl: './capi-ammessi.component.html',
  styleUrls: ['./capi-ammessi.component.css']
})
export class CapiAmmessiComponent implements OnInit {
  allevamenti: DettaglioAllevamenti[] = [];
  cols: any[] = [];
  idIstruttoria: string;
  intestazioni = Labels;
  elementiPagina: number = 10;
  @ViewChild('table') table;
  interventiBovini: string[] = ["310", "312", "313", "314", "322"];
  interventiMacellati: string[] = ["315", "316", "318"];
  interventiOvicaprini: string[] = ["320", "321"];
  mesi : string[] = ["Gen","Feb","Mar","Apr","Mag","Giu","Lug","Ago","Set","Ott","Nov","Dic"];
  produzioneLatte: ProduzioneLatte;
  alpeggi: registrazioneAlpeggio[];
  etichettature: EtichettaturaCarne[];
  cuaa: string;
  showProduzione : boolean = false;
  noAnalisi : boolean = false;
  sortBy: string;
  sortDirection: SortDirection;
  annoCampagna: string;

  constructor(
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.setCols();
    this.setIdIstruttoria();
    this.setAnnoCampagna();
    this.getAllevamentiImpegnati();
    
  }

  private setCols() {
    this.cols = [
      { field: 'codiceCapo', header: this.intestazioni.marcaAuricolare },
      { field: null, header: this.intestazioni.codiceSpecie },
      { field: null, header: this.intestazioni.razza },
      { field: 'esito', header: this.intestazioni.ESITO_CONTROLLO },
      { field: 'messaggio', header: this.intestazioni.messaggioEsito }
    ];
  }

  private setIdIstruttoria() {
    this.route.paramMap.subscribe(params => {
      this.idIstruttoria = params.get('idIstruttoria');
    });
  }

  private setAnnoCampagna() {
    this.route.paramMap.subscribe(params => {
      this.annoCampagna = params.get('annoCampagna');
    });
  }

  getAllevamentiImpegnati() {
    this.istruttoriaDettaglioService.getAllevamentiImpegnati(this.idIstruttoria)
      .subscribe(
        (dati => {
          if (dati) {
            dati.forEach(element => {
              this.cuaa = element.cuaaIntestatario;
              element.datiAllevamento = JSON.parse(element.datiAllevamento);
              element.datiDetentore = JSON.parse(element.datiDetentore);
              element.datiProprietario = JSON.parse(element.datiProprietario);
              this.allevamenti.push(element);
            })
            this.allevamenti.sort(function (a, b) {
              return a.codiceIntervento - b.codiceIntervento;
            });
          }
          console.log(this.allevamenti);
          this.getProduzioneLatteAndEtichettaturaCarne();
        }
        ),
        (error => {
          console.log(error);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        })
      );
  }

  changePage(event: any, allevamento: DettaglioAllevamenti, start: number = 0, codice: any) {    
    if (event != null) {
      this.sortDirection = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC; 
      this.sortBy = event.sortField || 'id';
      start = event.first;
    }
    let filter = new EsitoCapiFilter();
    filter.idAllevamento = allevamento.id;
    if (codice != null) {
      filter.codiceCapo = codice;
    }
    let paginazione: Paginazione = Paginazione.of(
      Math.round(start/this.elementiPagina), this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
    );
    this.istruttoriaDettaglioService.ricercaEsiticapi(filter, paginazione)
    .subscribe(
      (dati => {
        if (dati) {
            allevamento.count = dati.count;
            allevamento.richiesteAllevamentoDuEsito = dati.risultati;
            allevamento.richiesteAllevamentoDuEsito.forEach(result => {
              result.esito = result.esito.split('_').join(' ');
            })
        }
        console.log(this.allevamenti);
      }
      ),
      (error => {
        console.log(error);
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
      })
    ); 
  }

  isInterventoBovini(intervento: string): boolean {
    return this.interventiBovini.includes(intervento);
  }

  isAllevamentoMontagna(intervento: string): boolean {
    return "311".localeCompare(intervento) == 0;
  }

  isInterventoMacellati(intervento: string): boolean {
    return this.interventiMacellati.includes(intervento);
  }

  isInterventoOvicaprini(intervento: string): boolean {
    return this.interventiOvicaprini.includes(intervento);
  }

  getProduzioneLatteAndEtichettaturaCarne() {
        //Se è presente un intervento tra: 310-311-322
        if(this.isProduzioneLatte()){
          this.getProduzioneLatte();
          this.getRegistrazioneAlpeggio();
        }
        //Se è presente un intervento: 318
        if(this.isEtichettatura()){
          this.getEtichettatura();
        } 
  }

  getRegistrazioneAlpeggio() {
    this.istruttoriaDettaglioService.getAlpeggioZootecnia(this.annoCampagna, this.cuaa).subscribe(
      regAlpeggio => {
        this.alpeggi = regAlpeggio;
      },
      err => {
        console.log(err);
      }
    )
  }

  public isProduzioneLatte(): boolean {
    return this.allevamenti.some(element => {
      if (element.codiceIntervento == 310 || element.codiceIntervento == 311 || element.codiceIntervento == 322) {
        return true;
      }
      else {
        return false;
      }
    });
  }

  public isEtichettatura(): boolean {
    return this.allevamenti.some(element => {
      if (element.codiceIntervento == 318) {
        return true;
      }
      else {
        return false;
      }
    });
  }

  getProduzioneLatte(){
    this.istruttoriaDettaglioService.getProduzioneLatteZootecnia(this.annoCampagna, this.cuaa).subscribe(
      prodLatte => {
        this.produzioneLatte = prodLatte[0];
        this.showProduzione = true;
        this.checkAnalisi();
      },
      err => {
        this.showProduzione = false;
        console.log(err);
      }
    );
  }

  getEtichettatura(){
    this.istruttoriaDettaglioService.getEtichettaturaZootecnia(this.annoCampagna, this.cuaa).subscribe(
      etichettatura => {
        this.etichettature = etichettatura;
      },
      err => {
        console.log(err);
      }
    )
  }

  checkAnalisi(){
    if(this.produzioneLatte.tenoreBatteri == null || this.produzioneLatte.tenoreCellule == null || this.produzioneLatte.contenutoProteine == null){
      this.noAnalisi = true;
    }
    else{
      this.noAnalisi = false;
    }
  }

  onTabOpen(event,allevamento){
    allevamento.tableVisible=true;
  }
  cerca(codice, allevamento: DettaglioAllevamenti){
    console.log(codice);
    let event = null;
    let start = 0;
    this.changePage(event, allevamento, start, codice);
  }
}
