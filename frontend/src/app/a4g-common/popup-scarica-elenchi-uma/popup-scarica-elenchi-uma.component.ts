import { ErrorService } from '../services/error.service';
import { TipoElencoEnum } from '../classi/enums/uma/TipoElenco.enum';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { environment } from 'src/environments/environment';
import { MessageService, SelectItem } from 'primeng-lts';
import { TipoElencoItemVM } from 'src/app/a4g-common/classi/viewModels/TipoElencoItemVM';
import { HttpClientElenchiService } from '../services/http-client-elenchi.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { LoaderService } from 'src/app/loader.service';
import * as FileSaver from 'file-saver';
import { DateUtilService } from '../services/date-util.service';

@Component({
  selector: 'app-popup-scarica-elenchi-uma',
  templateUrl: './popup-scarica-elenchi-uma.component.html',
  styleUrls: ['./popup-scarica-elenchi-uma.component.scss']
})
export class PopupScaricaElenchiUmaComponent implements OnInit, OnDestroy {
  display: boolean;
  downloadForm: FormGroup;
  anni: Array<SelectItem>;
  cols: Array<any>;
  listaElenchi: Array<TipoElencoItemVM>;
  loadingDomande: boolean;
  loadingInadempienti: boolean;
  loadingAccise: boolean;
  loadingLavorazioni: boolean;

  // Subscription
  getElencoSubscription: Subscription;

  constructor(
    private dateUtilService: DateUtilService,
    private httpClientElenchiService: HttpClientElenchiService,
    private errorService: ErrorService,
    private messageService: MessageService,
    private loaderService: LoaderService
  ) { }

  ngOnDestroy(): void {
    if (this.getElencoSubscription) {
      this.getElencoSubscription.unsubscribe();
    }
  }

  ngOnInit() {
    this.loadData();
    this.initForm();
  }

  private loadData() {
    this.listaElenchi = [{ nome: 'Elenco domande', tipo: TipoElencoEnum[TipoElencoEnum.DOMANDE], loading: this.loadingDomande }, { nome: 'Elenco inadempienti', tipo: TipoElencoEnum[TipoElencoEnum.INADEMPIENTI], loading: this.loadingInadempienti }];
    if (localStorage.getItem('selectedRole') === AuthService.roleIstruttoreUMA) {
      this.listaElenchi.push({ nome: 'Elenco accise', tipo: TipoElencoEnum[TipoElencoEnum.ACCISE], loading: this.loadingAccise });
      this.listaElenchi.push({ nome: 'Elenco lavorazioni clienti conto terzi', tipo: TipoElencoEnum[TipoElencoEnum.LAVORAZIONI_CLIENTI_CONTO_TERZI], loading: this.loadingLavorazioni });
    }
    this.cols = [
      { field: 'nome', header: 'Elenco', width: "75%" },
      { field: '', header: '', width: "25%" }
    ];
    this.anni = [];
    /** creo una lista di anni a partire dal 2021 per popolare la select in TEST e a partire dal 2022 per quality e prod*/
    this.dateUtilService.getListYearsFrom(environment.annoInizioCampagnaUma).forEach((year: number) => {
      this.anni.push({ label: year.toString(), value: year });
    });
  }

  private initForm() {
    this.downloadForm = new FormGroup({
      anni: new FormControl(this.dateUtilService.getCurrentYear()),
    });
  }

  open() {
    this.display = true;
  }

  chiudi() {
    this.display = false;
  }

  downloadElenco(elenco: TipoElencoItemVM) {
    elenco.loading = true;
    this.getElencoSubscription = this.httpClientElenchiService.getElenco(this.downloadForm.get('anni').value, elenco.tipo).subscribe((response: any) => {
      if (response && response.headers && response.body) {
        const contentDisposition = response.headers.get('content-disposition') || '';
        const matches = /filename=([^;]+)/ig.exec(contentDisposition);
        const fileName = (matches[1] || 'untitled').trim();
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.downloadElenchiOk));
        FileSaver.saveAs(response.body, fileName);
      }
      elenco.loading = false;
    }, error => {
      this.errorService.showError(error, 'tst-scarica-elenchi-uma');
      elenco.loading = false;
    });

    // cancello lo spinner centralizzato
    this.loaderService.hide();
  }

} 