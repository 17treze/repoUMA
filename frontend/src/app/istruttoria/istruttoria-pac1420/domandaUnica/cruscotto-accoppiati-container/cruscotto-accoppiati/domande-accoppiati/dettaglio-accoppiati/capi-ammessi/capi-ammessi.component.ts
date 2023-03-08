import { stringify } from '@angular/compiler/src/util';
import { ProduzioneLatte } from './../../../../../domain/produzione-latte';
import { Component, OnInit, ViewChild } from '@angular/core';
import { DettaglioAllevamenti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/domain/dettaglioAllevamenti';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { ActivatedRoute } from '@angular/router';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { Labels } from 'src/app/app.labels';
import { registrazioneAlpeggio } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/domain/registrazione-alpeggio';
import { EtichettaturaCarne } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/domain/etichettatura-carne';

@Component({
  selector: 'app-capi-ammessi',
  templateUrl: './capi-ammessi.component.html',
  styleUrls: ['./capi-ammessi.component.css']
})
export class CapiAmmessiComponent implements OnInit {

  allevamenti: DettaglioAllevamenti[] = [];
  cols: any[] = [];
  idDomanda: string;
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

  constructor(
    private istruttoriaService: IstruttoriaService,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.setCols();
    this.setIdDomanda();
    this.setIdIstruttoria();
    this.changePage();
    
  }

  private setCols() {
    this.cols = [
      { field: 'codiceCapo', header: this.intestazioni.marcaAuricolare },
      { field: 'codiceSpecie', header: this.intestazioni.codiceSpecie },
      { field: 'esito', header: this.intestazioni.ESITO_CONTROLLO },
      { field: 'messaggio', header: this.intestazioni.messaggioEsito }
    ];
  }

  private setIdDomanda() {
    this.route.paramMap.subscribe(params => {
      this.idDomanda = params.get(Costanti.dettaglioDomandaParam);
    });
  }

  private setIdIstruttoria() {
    this.route.paramMap.subscribe(params => {
      this.idIstruttoria = params.get('idIstruttoria');
    });
  }

  changePage() {
    let jsonStato: any = { tipo: "AMMESSO" };
    this.istruttoriaService.getCapi(this.idDomanda, JSON.stringify(jsonStato))
      .subscribe(
        (dati => {
          if (dati) {
            dati.forEach(element => {
              element.datiAllevamento = JSON.parse(element.datiAllevamento);
              element.datiDetentore = JSON.parse(element.datiDetentore);
              element.datiProprietario = JSON.parse(element.datiProprietario);
              this.allevamenti.push(element);
            })
            this.allevamenti.forEach(element => {
              element.richiesteAllevamentoDuEsito.forEach(result => {
                result.esito = result.esito.split('_').join(' ');
              })
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
    this.istruttoriaService.getDettaglioDomanda(this.idDomanda, null, null).subscribe(
      data => {
        this.cuaa = data.infoGeneraliDomanda.cuaaIntestatario;
        let jsonInput: any = { "cuaa": this.cuaa };
        //Se è presente un intervento tra: 310-311-322
        if(this.isProduzioneLatte()){
          this.getProduzioneLatte(jsonInput);
          this.getRegistrazioneAlpeggio(jsonInput);
        }
        //Se è presente un intervento: 318
        if(this.isEtichettatura()){
          this.getEtichettatura(jsonInput);
        }
      },
      error => {
        console.log(error);
      }
    )
  }

  getRegistrazioneAlpeggio(jsonInput: any) {
    this.istruttoriaService.getAlpeggio(this.idIstruttoria, JSON.stringify(jsonInput)).subscribe(
      regAlpeggio => {
        this.alpeggi = regAlpeggio;
      },
      err => {
        console.log(err);
      }
    )
  }

  isProduzioneLatte(): boolean {
    return this.allevamenti.some(element => {
      if (element.codiceIntervento == 310 || element.codiceIntervento == 311 || element.codiceIntervento == 322) {
        return true;
      }
      else {
        return false;
      }
    });
  }

  isEtichettatura(): boolean {
    return this.allevamenti.some(element => {
      if (element.codiceIntervento == 318) {
        return true;
      }
      else {
        return false;
      }
    });
  }

  getProduzioneLatte(jsonInput: any){
    this.istruttoriaService.getProduzioneLatte(this.idIstruttoria, JSON.stringify(jsonInput)).subscribe(
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

  getEtichettatura(jsonInput: any){
    this.istruttoriaService.getEtichettatura(JSON.stringify(jsonInput)).subscribe(
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
}