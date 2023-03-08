import { Component, OnInit } from '@angular/core';
import { IstruttoriaAntimafiaService } from 'src/app/istruttoria/istruttoria-antimafia/istruttoria-antimafia.service';
import { ActivatedRoute } from '@angular/router';
import { DomandaCollegataFilter } from 'src/app/istruttoria/istruttoria-antimafia/dto/DomandaCollegataFilter';
import { Labels } from 'src/app/app.labels';
import { DomandaCollegata } from 'src/app/istruttoria/istruttoria-antimafia/dto/DomandaCollegata';
import { TipoDomandaEnum } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoDomandaEnum';

@Component({
  selector: 'app-dettaglio-domande-collegate',
  templateUrl: './dettaglio-domande-collegate.component.html',
  styleUrls: ['./dettaglio-domande-collegate.component.css']
})
export class DettaglioDomandeCollegateComponent implements OnInit {

  intestazioni = Labels;
  textTitle = Labels.domandeCollegateAntimafia;
  domandeUnicheView: Array<DomandaCollegata> = new Array();
  domandeStrutturaliView: Array<DomandaCollegata> = new Array();
  domandeSuperficieView: Array<DomandaCollegata> = new Array();
  colsSUPDU: any[];
  colsPSRStr: any[];

  constructor(private istruttoriaAntimafiaService: IstruttoriaAntimafiaService, private route: ActivatedRoute) {


    //colonne ordinamento PSR_SUPERFICIE e DOMANDA_UNICA
    this.colsSUPDU = [
      { field: "idDomanda", header: this.intestazioni.idDomanda },
      { field: "dtDomanda", header: this.intestazioni.dtDomanda },
      { field: "campagna", header: this.intestazioni.campagna },
      { field: "importoRichiesto", header: this.intestazioni.importoRichiesto },
      { field: "dtInizioSilenzioAssenso", header: this.intestazioni.dtInizioSilenzioAssenso },
      { field: "dtFineSilenzioAssenso", header: this.intestazioni.dtFineSilenzioAssenso },
      { field: "dtInizioEsitoNegativo", header: this.intestazioni.dtInizioEsitoNegativo },
      { field: "dtFineEsitoNegativo", header: this.intestazioni.dtFineEsitoNegativo },
      { field: "statoBdna", header: this.intestazioni.statoBdna }
    ];

    //colonne ordinamento PSR_STRUTTURALI
    this.colsPSRStr = [
      { field: "misura", header: this.intestazioni.misura },
      { field: "idDomanda", header: this.intestazioni.idDomanda },
      { field: "dtDomanda", header: this.intestazioni.dtDomanda },
      { field: "importoRichiesto", header: this.intestazioni.importoRichiesto },
      { field: "dtInizioSilenzioAssenso", header: this.intestazioni.dtInizioSilenzioAssenso },
      { field: "dtFineSilenzioAssenso", header: this.intestazioni.dtFineSilenzioAssenso },
      { field: "dtInizioEsitoNegativo", header: this.intestazioni.dtInizioEsitoNegativo },
      { field: "dtFineEsitoNegativo", header: this.intestazioni.dtFineEsitoNegativo },
      { field: "statoBdna", header: this.intestazioni.statoBdna }
    ];
  }

  ngOnInit() {
    this.route.queryParams.subscribe(queryString => {
      let params = JSON.parse(queryString["params"]);
      let domandaCollegataFilter = new DomandaCollegataFilter;
      domandaCollegataFilter.cuaa = params.cuaa;
      this.istruttoriaAntimafiaService.getDomandeCollegateFilter(domandaCollegataFilter).subscribe(ret => {
        if (ret) {
          ret.forEach(domanda => {
            if (domanda.tipoDomanda === TipoDomandaEnum.PSR_SUPERFICIE_EU) {
              this.domandeSuperficieView.push(domanda);
            } else if (domanda.tipoDomanda === TipoDomandaEnum.PSR_STRUTTURALI_EU) {
              this.domandeStrutturaliView.push(domanda);
            } else if (domanda.tipoDomanda === TipoDomandaEnum.DOMANDA_UNICA) {
              this.domandeUnicheView.push(domanda);
            }
          });
          // odinamento in tabella in base a data domanda DESC
          if (this.domandeSuperficieView) {
            this.domandeSuperficieView.sort((a, b) => a.dtDomanda > b.dtDomanda ? -1 : 1);
          }
          if (this.domandeStrutturaliView) {
            this.domandeStrutturaliView.sort((a, b) => a.dtDomanda > b.dtDomanda ? -1 : 1);
          }
          if (this.domandeUnicheView) {
            this.domandeUnicheView.sort((a, b) => a.dtDomanda > b.dtDomanda ? -1 : 1);
          }
        }
      });
    });
  }
}
