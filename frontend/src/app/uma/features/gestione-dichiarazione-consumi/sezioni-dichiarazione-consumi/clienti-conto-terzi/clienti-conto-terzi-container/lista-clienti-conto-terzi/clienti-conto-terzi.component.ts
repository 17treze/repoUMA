import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { ClienteConsumiDto } from './../../../../../../core-uma/models/dto/ClienteConsumiDto';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PopupRicercaClientiContoterziComponent } from '../../popup-ricerca-clienti-contoterzi/popup-ricerca-clienti-contoterzi.component';
import { PopupCancellaClienteContoterziComponent } from '../../popup-cancella-cliente-contoterzi/popup-cancella-cliente-contoterzi.component';
import { PopupAllegatiClientiContoterziComponent } from '../../popup-allegati-clienti-contoterzi/popup-allegati-clienti-contoterzi.component';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { EMPTY, Subscription, throwError } from 'rxjs';
import { ClienteDto } from 'src/app/uma/core-uma/models/dto/ClienteDto';
import { HttpClientClienteUmaService } from 'src/app/uma/core-uma/services/http-client-cliente-uma.service';
import { RaggruppamentoLavorazioneDto } from 'src/app/uma/core-uma/models/dto/RaggruppamentoDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { catchError, switchMap } from 'rxjs/operators';
import { AllegatiClientiDialogViewModel } from 'src/app/uma/core-uma/models/viewModels/AllegatiClientiDialogViewModel';

@Component({
  selector: 'app-clienti-conto-terzi',
  templateUrl: './clienti-conto-terzi.component.html',
  styleUrls: ['./clienti-conto-terzi.component.scss']
})
export class ClientiContoTerziComponent implements OnInit, OnDestroy {
  listaClientiContoterzi: ClienteDto[];
  READONLY_MODE: boolean;
  idDichiarazione: number;
  cuaaDaInserire: string;
  idFascicoloDaInserire: number;
  idClienteSelezionato: number;

  @ViewChild("popupRicercaClienti", { static: true })
  popupRicercaClienti: PopupRicercaClientiContoterziComponent;

  @ViewChild("popupCancellaCliente", { static: true })
  popupCancellaCliente: PopupCancellaClienteContoterziComponent;

  @ViewChild("popupAllegatiCliente", { static: true })
  popupAllegatiCliente: PopupAllegatiClientiContoterziComponent;

  caricaSubscription: Subscription;
  routerSubscription: Subscription;
  getLavorazioniSubscription: Subscription;
  deleteSubscription: Subscription;

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private indiceUmaService: IndiceUmaService,
    private httpClientClienteUmaService: HttpClientClienteUmaService,
    private location: Location,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private errorService: ErrorService) { }

  ngOnInit() {
    console.log('localStorage.getItem("UMA_RO_DICH"): ' + localStorage.getItem('UMA_RO_DICH'));
    this.READONLY_MODE = this.indiceUmaService.READONLY_MODE_DICHIARAZIONE || (localStorage.getItem('UMA_RO_DICH') == 'true' ? true : false);
    this.routerSubscription = this.route.params
      .subscribe(params => {
        this.idDichiarazione = params['id'];
      });
    this.caricaListaClienti();
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.caricaSubscription) {
      this.caricaSubscription.unsubscribe();
    }
    if (this.getLavorazioniSubscription) {
      this.getLavorazioniSubscription.unsubscribe();
    }
    if (this.deleteSubscription) {
      this.deleteSubscription.unsubscribe();
    }
  }

  goBack() {
    this.location.back();
  }

  openRicercaClienteDialog() {
    this.popupRicercaClienti.open();
  }

  // Schermata clienti conto terzi
  openAllegatiClienteEditaDialog(cliente: ClienteDto) {
    const data: AllegatiClientiDialogViewModel = { modalitaCrea: false, cliente };
    this.popupAllegatiCliente.open(data);
  }

  // Schermata di ricerca dei clienti
  openAllegatiClienteCreaDialog(cliente: ClienteDto) {
    const data: AllegatiClientiDialogViewModel = { modalitaCrea: true, cliente };
    this.popupAllegatiCliente.open(data);
  }

  caricaListaClienti() {
    this.caricaSubscription = this.httpClientClienteUmaService.getClientiContoterzi(this.idDichiarazione).subscribe((result: ClienteDto[]) => {
      this.listaClientiContoterzi = result;
    }, error => this.errorService.showError(error));
  }

  deleteClientiContoterzi(clienteContoTerzi: ClienteConsumiDto) {
    this.popupCancellaCliente.open(clienteContoTerzi);
  }

  cancellaCliente(clienteContoTerzi: ClienteConsumiDto) {
    this.deleteSubscription = this.httpClientClienteUmaService.deleteCliente(this.idDichiarazione, clienteContoTerzi.id)
      .pipe(
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap(() => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.cancellazioneClienteContoTerziOK));
          return this.httpClientClienteUmaService.getClientiContoterzi(this.idDichiarazione)
        })
      ).subscribe((clienti: ClienteDto[]) => {
        this.listaClientiContoterzi = clienti;
      }, error => this.errorService.showError(error));
  }

  onClickButton(tipo: String, cliente: ClienteDto) {
    switch (tipo) {
      case "LAVORAZIONI": {
        this.getLavorazioniSubscription = this.httpClientClienteUmaService.getLavorazioniClientiContoTerzi(this.idDichiarazione.toString(), cliente.id).subscribe((lavorazioni: Array<RaggruppamentoLavorazioneDto>) => {
          if (lavorazioni && lavorazioni.length) {
            this.router.navigate([cliente.id], { relativeTo: this.activeRoute.parent });
          } else {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, UMA_MESSAGES.noLavorazioniDisponibili));
          }
        }, error => this.errorService.showError(error));
        break;
      }
      case "ALLEGATI": {
        this.openAllegatiClienteEditaDialog(cliente);
        break;
      }
      default: {
        break;
      }
    }
  }
}
