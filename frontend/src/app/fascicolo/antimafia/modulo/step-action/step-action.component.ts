import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { StepEvent } from '../../classi/stepEvent';
import { MessageService } from 'primeng/api';
import { DichiarazioneAntimafiaService } from '../../dichiarazione-antimafia.service';
import { AntimafiaService } from '../../antimafia.service';
import { DatiDichiarazione, Richiedente } from '../../classi/datiDichiarazione';
import { DichiarazioneAntimafia } from '../../classi/dichiarazioneAntimafia';
import { Azienda } from '../../classi/azienda';
import { Router, ActivatedRoute } from '@angular/router';
import { A4gMessages } from 'src/app/a4g-common/a4g-messages';
import { Location } from "@angular/common";
import { LoaderService } from 'src/app/loader.service';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-step-action',
  templateUrl: './step-action.component.html',
  styleUrls: ['./step-action.component.css']
})
export class StepActionComponent implements OnInit {

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    public stepEvent: StepEvent,
    private messages: MessageService,
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private antimafiaService: AntimafiaService,
    private loader: LoaderService
  ) { }

  @Output() next = new EventEmitter<boolean>();
  @Input() isProtocollaReady: boolean;
  ngOnInit() {
    this.loader.setTimeout(480000); //otto minuti
  }

  //emette l'evento intercettato dal componente parent che effettua le sue validazioni e poi invoca il metodo goNext()
  procedi() {
    this.next.emit(true);
  }
  //metodo richiamato dopo il conferma ha avuto successo ed è effettivamente possibile passare allo step successivo
  goNext() {
    this.stepEvent.next();
  }
  indietro() {
    this.stepEvent.previous();
  }
  aggiorna() {
    this.messages.add({ key: 'aggiornaDichiarazione', sticky: true, severity: 'warn', summary: 'Tutti i dettagli della dichiarazione saranno eliminati', detail: 'Sei sicuro di voler continuare?' });
  }
  cancella() {
    this.messages.add({ key: 'cancellaDichiarazione', sticky: true, severity: 'warn', summary: 'Tutti i dettagli della dichiarazione saranno eliminati', detail: 'Sei sicuro di voler continuare?' });
  }
  onConfirmAggiornaDichiarazione() {
    const dichiarazioneAntimafia = this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia();
    const streamModifica = this.antimafiaService.eliminaDichiarazioneAntimafia(dichiarazioneAntimafia.id.toString()).pipe(
      switchMap(deleteResponse => {
        const dichiarazioneAntimafiaCreate = new DichiarazioneAntimafia();
        const datiDichiarazione = new DatiDichiarazione();
        datiDichiarazione.richiedente = new Richiedente();
        datiDichiarazione.richiedente.codiceFiscale = dichiarazioneAntimafia.datiDichiarazione.richiedente.codiceFiscale;
        dichiarazioneAntimafiaCreate.datiDichiarazione = datiDichiarazione;
        const azienda = new Azienda();
        azienda.cuaa = dichiarazioneAntimafia.azienda.cuaa;
        dichiarazioneAntimafiaCreate.azienda = azienda;
        return this.antimafiaService.creaDichiarazioneAntimafia(dichiarazioneAntimafiaCreate);
      })
    );
    streamModifica.subscribe(
      data => {
        console.log('Dichiarazione cancellata: ' + data);
        this.messages.clear('aggiornaDichiarazione');
        this.router.navigate(['./' + data], { relativeTo: this.route.parent }).then(() => location.reload());
      },
      error => {
        console.log('Error', error);
        A4gMessages.handleError(this.messages, error, A4gMessages.SERVIZIO_NON_DISPONIBILE);
      }
    );
  }
  onConfirmCancellaDichiarazione() {

    const dichiarazioneAntimafia = this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia();
    this.antimafiaService.eliminaDichiarazioneAntimafia(dichiarazioneAntimafia.id.toString()).subscribe(
      deleteResponse => {
        console.log('Dichiarazione cancellata: ' + deleteResponse);
        this.router.navigate(['./'], { relativeTo: this.route.parent.parent });
      },
      error => {
        console.log('Error', error);
        A4gMessages.handleError(this.messages, error, A4gMessages.SERVIZIO_NON_DISPONIBILE);
      }
    );

  }
  onRejectAggiornaDichiarazione() {
    this.messages.clear('aggiornaDichiarazione');
  }
  onRejectCancellaDichiarazione() {
    this.messages.clear('cancellaDichiarazione');
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
  }

}
