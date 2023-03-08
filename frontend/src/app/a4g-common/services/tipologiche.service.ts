import { CoperturaEnum } from './../classi/enums/dotazione.-tecnica/Copertura.enum';
import { TipoConduzione } from './../classi/enums/dotazione.-tecnica/TipoConduzione.enum';
import { Injectable } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { SelectItemTipoDocumentoViewModel } from 'src/app/a4g-common/classi/viewModels/SelectItemTipoDocumentoViewModel';
import { StatoDichiarazioneConsumiEnum } from '../classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { StatoDomandaUma } from '../classi/enums/uma/StatoDomandaUma.enum';
import { TipoAllegatoConsuntivo } from '../classi/enums/uma/TipoAllegatoConsuntivo.enum';
import { TipoIntestazioneUma } from '../classi/enums/uma/TipoDocumentoUma.enum';
import * as _ from 'lodash';
@Injectable({
  providedIn: 'root'
})
export class TipologicheService {
  motiviAccisa: Array<SelectItem>;
  tipiDocumento: Array<SelectItemTipoDocumentoViewModel>;
  documentiUma: Array<SelectItem>;
  tipiDomande: Array<SelectItem>;
  statiDichiarazioneConsumiUma: Array<SelectItem>;
  statiRichiestaCarburanteUma: Array<SelectItem>;
  statiRichiestaEDichiarazioneUma: Array<SelectItem>;
  statiRettificaUma: Array<SelectItem>;
  tipologiaMacchinario: Array<SelectItem>;
  classeFunzionaleMacchinario: Array<SelectItem>;
  sottoTipologiaMacchinario: Array<SelectItem>;
  alimentazioneMotore: Array<SelectItem>;
  tipologiaDiPossesso: Array<SelectItem>;
  tipologiaFabbricato: Array<SelectItem>;
  sottoTipologiaFabbricato: Array<SelectItem>;
  sottoTipoFabbricato: Array<SelectItem>;
  tipoConduzione: Array<SelectItem>;
  copertura: Array<SelectItem>;
  tipologiaCatasto: Array<SelectItem>;

  constructor() { }

  setMotiviAccisa() {
    this.motiviAccisa = [
      { label: 'Seleziona un motivo per l\'accisa', value: null },
      { label: 'Consumo di carburante agevolato eccedente il massimo ammesso', value: 'Consumo di carburante agevolato eccedente il massimo ammesso' },
      { label: 'Errato prelevamento di carburante agevolato', value: 'Errato prelevamento di carburante agevolato' },
      { label: 'Residuo non trasferito', value: 'Residuo non trasferito' }
    ];
  }

  setTipiDocumento() {
    this.tipiDocumento = [
      { label: "Autocertificazione", value: 0, enum: TipoAllegatoConsuntivo.AUTOCERTIFICAZIONE },
      { label: "Autorizzazione UMA", value: 1, enum: TipoAllegatoConsuntivo.AUTORIZZAZIONE_UMA },
      { label: "Altro", value: 2, enum: TipoAllegatoConsuntivo.ALTRO }
    ];
  }

  setDocumentiUma() {
    this.documentiUma = [
      { label: "Richiesta", value: TipoIntestazioneUma.RICHIESTA },
      { label: "Rettifica", value: TipoIntestazioneUma.RETTIFICA },
      { label: "Dichiarazione Consumi", value: TipoIntestazioneUma.DICHIARAZIONE_CONSUMI }
    ];
  }

  setTipiDomandeUma() {
    this.tipiDomande = [
      { label: 'RICHIESTA', value: 'RICHIESTA' },
      { label: 'RETTIFICA', value: 'RETTIFICA' },
      { label: 'DICHIARAZIONE CONSUMI', value: 'DICHIARAZIONE CONSUMI' }
    ];
  }

  setStatiDichiarazioneConsumiUma() {
    this.statiDichiarazioneConsumiUma = [
      { label: "In Compilazione", value: StatoDichiarazioneConsumiEnum.IN_COMPILAZIONE },
      { label: "Protocollata", value: StatoDichiarazioneConsumiEnum.PROTOCOLLATA }
    ];
  }

  setStatiRichiestaCarburanteUma() {
    this.statiRichiestaCarburanteUma = [
      { label: "In Compilazione", value: StatoDomandaUma.IN_COMPILAZIONE },
      { label: "Autorizzata", value: StatoDomandaUma.AUTORIZZATA },
      { label: "Rettificata", value: StatoDomandaUma.RETTIFICATA }
    ];
  }

  setStatiRichiestaEDichiarazioneUma() {
    this.statiRichiestaEDichiarazioneUma = [
      { label: "In Compilazione", value: StatoDomandaUma.IN_COMPILAZIONE },
      { label: "Autorizzata", value: StatoDomandaUma.AUTORIZZATA },
      { label: "Rettificata", value: StatoDomandaUma.RETTIFICATA },
      { label: "Protocollata", value: StatoDichiarazioneConsumiEnum.PROTOCOLLATA }
    ];
  }

  setStatiRichiestaCarburanteUmaOperatoreDogane() {
    this.statiRichiestaCarburanteUma = [
      { label: "Autorizzata", value: StatoDomandaUma.AUTORIZZATA },
      { label: "Rettificata", value: StatoDomandaUma.RETTIFICATA }
    ];
  }

  setStatiRettificaUma() {
    this.statiRettificaUma = [
      { label: "In Compilazione", value: StatoDomandaUma.IN_COMPILAZIONE },
      { label: "Autorizzata", value: StatoDomandaUma.AUTORIZZATA }
    ];
  }

  setTipologiaMacchinario(tipologie?: Array<SelectItem>) {
    this.tipologiaMacchinario = [...tipologie];
    this.tipologiaMacchinario = _.sortBy(this.tipologiaMacchinario, 'value');
    this.tipologiaMacchinario.unshift({ label: "Seleziona una tipologia", value: null });
  }

  setClasseFunzionaleMacchinario(classeFunzionale?: Array<SelectItem>) {
    this.classeFunzionaleMacchinario = [...classeFunzionale];
    this.classeFunzionaleMacchinario = _.sortBy(this.classeFunzionaleMacchinario, 'label');
    if(classeFunzionale.length!=1)
      this.classeFunzionaleMacchinario.unshift({ label: "Seleziona una classe funzionale", value: null });
  }

  setSottoTipologiaMacchinario(sottoTipologie?: Array<SelectItem>) {
    this.sottoTipologiaMacchinario = [...sottoTipologie];
    this.sottoTipologiaMacchinario = _.sortBy(this.sottoTipologiaMacchinario, 'label');
    if(sottoTipologie.length!=1)
      this.sottoTipologiaMacchinario.unshift({ label: "Seleziona un tipo macchina", value: null });
  }
  
  resetClassiFunzionaliSottoTipologieMacchinario() {
    this.sottoTipologiaMacchinario = [];
    this.classeFunzionaleMacchinario = [];
  }

  setTipologiaFabbricato(tipologie?: Array<SelectItem>) {
    this.tipologiaFabbricato = [...tipologie];
    this.tipologiaFabbricato = _.sortBy(this.tipologiaFabbricato, 'label');
    this.tipologiaFabbricato.unshift({ label: "Seleziona un ambito", value: null });
  }

  setSottoTipologiaFabbricato(sottoTipologie?: Array<SelectItem>) {
    this.sottoTipologiaFabbricato = [...sottoTipologie];
    this.sottoTipologiaFabbricato = _.sortBy(this.sottoTipologiaFabbricato, 'label');
    this.sottoTipologiaFabbricato.unshift({ label: "Seleziona una tipologia", value: null });
  }

  setSottoTipoFabbricato(sottoTipi?: Array<SelectItem>) {
    this.sottoTipoFabbricato = [...sottoTipi];
    this.sottoTipoFabbricato = _.sortBy(this.sottoTipoFabbricato, 'label');
    this.sottoTipoFabbricato.unshift({ label: "Seleziona una sotto tipologia", value: null });
  }

  setTipoConduzioneFabbricato(tipi?: Array<SelectItem>) {
    this.tipoConduzione = [
      { label: 'Proprietà o Comproprietà', value: TipoConduzione.PROPRIETA_O_COMPROPRIETA },
      { label: 'Affitto', value: TipoConduzione.AFFITTO },
      { label: 'Mezzadria', value: TipoConduzione.MEZZADRIA },
      { label: 'Altra forma', value: TipoConduzione.ALTRA_FORMA },
      { label: 'EST.INF. 5.000mq comune montano (DL.24/06/2014 N.91)', value: TipoConduzione.DECRETO_LEGGE }
    ];
    this.tipoConduzione.unshift({ label: "Seleziona un tipo di conduzione", value: null });
  }

  setCopertura(coperture?: Array<SelectItem>) {
    this.copertura = [
      { label: CoperturaEnum.SI, value: CoperturaEnum.SI },
      { label: CoperturaEnum.NO, value: CoperturaEnum.NO },
      { label: CoperturaEnum.PARZIALE, value: CoperturaEnum.PARZIALE }
    ];
    this.copertura.unshift({ label: "Seleziona una copertura", value: null });
  }

  setAlimentazione() {
    this.alimentazioneMotore = [
      { label: "Seleziona alimentazione", value: null },
      { label: "Gasolio", value: "GASOLIO" },
      { label: "Benzina", value: "BENZINA" },
      { label: "Elettrico", value: "ELETTRICO" }
    ];
  }

  setTipologiaDiPossesso() {
    this.tipologiaDiPossesso = [
      { label: "Seleziona tipologia", value: null },
      { label: "Proprietà", value: "PROPRIETA" },
      { label: "Comodato", value: "COMODATO" },
      { label: "Comproprietà", value: "COMPROPRIETA" },
      { label: "In uso", value: "IN_USO" },
      { label: "Leasing", value: "LEASING" },
      { label: "Noleggio", value: "NOLEGGIO" }
    ];
  }

  setTipologiaCatasto() {
    this.tipologiaCatasto = [
      { label: "Seleziona tipologia", value: null },
      { label: "EDIFICIALE", value: 'EDIFICIALE' },
      { label: "FONDIARIA", value: 'FONDIARIA' }
    ];
  }
}
