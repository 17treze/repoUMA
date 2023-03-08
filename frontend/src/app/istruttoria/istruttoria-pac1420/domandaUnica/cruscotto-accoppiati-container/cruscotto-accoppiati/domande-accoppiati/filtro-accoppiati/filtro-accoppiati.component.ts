import { Component, OnInit, Input } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { NgForm } from '@angular/forms';
import { InputFascicolo } from 'src/app/a4g-common/classi/InputFascicolo';
import { FiltroAccoppiatiService } from '../../../../filtro-accoppiati.service';
import { SostegnoDu } from '../../../../classi/SostegnoDu';
import { Costanti } from '../../../../Costanti';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-filtro-accoppiati',
  templateUrl: './filtro-accoppiati.component.html',
  styleUrls: ['./filtro-accoppiati.component.css']
})
export class FiltroAccoppiatiComponent implements OnInit {

  settori: MenuItem[];
  selectedSettori: MenuItem[];
  campioni: MenuItem[];
  selectedCampione: MenuItem;
  public inputRicerca: InputFascicolo = new InputFascicolo();
  @Input() cuaaSuggestions: string[];
  @Input() denominazioneSuggestions: string[];
  cuaaS: string[];
  denominazioneS: string[];
  selectedSostegno: string;

  constructor(
    public filtroAccoppiati: FiltroAccoppiatiService,
    private router: Router,
    private translateService: TranslateService) {}

  ngOnInit() {
    if (this.router.url.split('/').filter(url => url === Costanti.cruscottoAccoppiatoZootecnia).length > 0) {
      this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    } else {
      this.selectedSostegno = SostegnoDu.SUPERFICIE;
    }
    this.initFiltroSettori();
    this.initFiltroCampioni();
  }

  private initFiltroCampioni() {
    this.campioni = [
      { label: 'SI', title: 'Sì' },
      { label: 'NO', title: 'No' },
      { label: 'TUTTI', title: 'Tutti' }
    ];
    this.selectedCampione = this.campioni[2];
  }

  // Id: Codice Agea
  private initFiltroSettori() {
    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA) {
      this.settori = [
        { id: '310', label: 'M1', title: 'M1 - Vacche da latte (310)' },
        { id: '311', label: 'M2', title: 'M2 - Vacche da latte in zone di montagna (311)' },
        { id: '313', label: 'M4', title: 'M4 - Vacche nutrici (313)' },
        { id: '315', label: 'M5', title: 'M5- Bovini macellati (315)' },
        { id: '320', label: 'M6', title: 'M6 - Agnelle (320)' },
        { id: '321', label: 'M7', title: 'M7 - Ovicaprini macellati (321)' },
        { id: '316', label: 'M19', title: 'M19 - Bovini macellati - Allevati 12 mesi (316)' },
        { id: '318', label: 'M19', title: 'M19 - Bovini macellati - Etichettatura (318)' },
        { id: '322', label: 'M20', title: 'M20 - Vacche nutrici non iscritte (322)' }
      ]
    } else { // 'ACC_SUPERFICI'
      this.settori = [
        { id: '122', label: 'M8', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.soia") },
        { id: '124', label: 'M9', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.frumento")},
        { id: '123', label: 'M10', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.proteaginose")},
        { id: '125', label: 'M11', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.leguminose")},
        { id: '128', label: 'M14', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.pomodoro")},
        { id: '129', label: 'M15', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.oliviStandard")},
        { id: '132', label: 'M16', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.olivi75")},
        { id: '138', label: 'M17', title: this.translateService.instant("FILTRI_INTERVENTI_SUPERFICI.oliviQualita")}
      ]
    }
    this.selectedSettori = this.settori;
  }

  filterCUAA(event: any) {
    let filtered: string[] = [];
    let query = event.query;
    for (let i = 0; i < this.cuaaSuggestions.length; i++) {
      let item = this.cuaaSuggestions[i];
      if (item.toLowerCase().indexOf(query.toLowerCase()) >= 0) {
        filtered.push(item);
      }
    }
    this.cuaaS = filtered;
  }

  filterDenominazione(event: any) {
    let filtered: string[] = [];
    let query = event.query;
    for (let i = 0; i < this.denominazioneSuggestions.length; i++) {
      let item = this.denominazioneSuggestions[i];
      if (item.toLowerCase().indexOf(query.toLowerCase()) >= 0) {
        filtered.push(item);
      }
    }
    this.denominazioneS = filtered;
  }

  filtroSettoreDisabled() {
    return false;
  }

  onSubmit(f: NgForm) {
    this.filtroAccoppiati.filtro.cuaa = f.value.cuaa;
    this.filtroAccoppiati.filtro.denominazione = f.value.denominazione;
    this.filtroAccoppiati.filtro.numero_domanda = f.value.numero_domanda;
    // da riattivare dopo implementazione filtro per gli accoppiati
    // this.filtroAccoppiati.filtro.campione = f.value.selectedCampione.label;
    this.filtroAccoppiati.filtro.interventi = [];
    if (f.value.selectedSettori.length == 0)
      for (let i = 0; i < this.settori.length; i++) {
        this.filtroAccoppiati.filtro.interventi.push(this.settori[i].id);
      }
    else
      for (let i = 0; i < f.value.selectedSettori.length; i++) {
        this.filtroAccoppiati.filtro.interventi.push(f.value.selectedSettori[i].id);
      }
    console.log(this.filtroAccoppiati);
    this.filtroAccoppiati.elencoDomande.loadData(this.filtroAccoppiati.elencoDomande.table);
  }
}