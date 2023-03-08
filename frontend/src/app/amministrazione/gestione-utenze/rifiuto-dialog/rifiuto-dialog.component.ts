import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RichiesteAccessoSistema } from '../richieste-accesso-sistema/dto/RichiesteAccessoSistema';
import { GestioneUtenzeService } from '../gestione-utenze.service';
import { DatiAnagrafici } from '../../../utenti/classi/datiAnagrafici';
import { StringSupport } from '../../../a4g-common/utility/string-support';
import { A4gMessages, A4gSeverityMessage } from '../../../a4g-common/a4g-messages';
import { MessageService, SelectItem } from 'primeng/api';
import { RichiestaAccessoSistemaRifiuto } from '../richieste-accesso-sistema/dto/richiesta-accesso-sistema-rifiuto';
import { Router, Params } from '@angular/router';

@Component({
    selector: 'app-rifiuto-dialog',
    templateUrl: './rifiuto-dialog.component.html',
    styleUrls: ['./rifiuto-dialog.component.css']
})
export class RifiutoDialogComponent implements OnInit {

    @Output() displayChange = new EventEmitter();
    @Input() richiestaAccessoCorrente: RichiesteAccessoSistema;

    public display: boolean;
    public displayRifiutoDialog: boolean;
    public testoMail: string;
    public motivazioneRifiuto: string;
    public note: string;
    public possibiliMorivazioni: SelectItem[] = [];

    constructor(
        private gestioneUtenzeSerice: GestioneUtenzeService,
        private messages: MessageService,
        private router: Router
    ) {
        this.displayRifiutoDialog = false;
    }

    ngOnInit() {
        this.initPossibiliMotivazioni();
    }

    public onOpen(datiAnagrafici: DatiAnagrafici) {
        this.display = true;
        this.displayChange.emit(true);
        this.testoMail = "Gentile " +
                        datiAnagrafici.nome + ' ' + datiAnagrafici.cognome +
                        ",\ncon la presente si comunica che la Sua richiesta per accedere al Sistema Informativo A4G non può essere accolta per la seguente motivazione:" +
                        "\n\nCordiali saluti.";
        this.motivazioneRifiuto = null;
        this.note = null;
    }

    public onClose() {
        this.display = false;
        this.displayChange.emit(false);
    }

    public rifiutaDomanda() {
        if (this.validaRifiutoDomanda()) {
            this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.inputRequiredInvalid));
        } else {
            this.callService(this.richiestaAccessoCorrente.id, this.creaRichiestaAccessoSistemaRifiuto());
        }
    }




    private validaRifiutoDomanda(): boolean {
        return StringSupport.isNullOrEmpty(this.testoMail) || StringSupport.isNullOrEmpty(this.motivazioneRifiuto);
    }

    private callService(idDomanda: number, richiestaAccessoSistemaRifiuto: RichiestaAccessoSistemaRifiuto) {
        this.gestioneUtenzeSerice.rifiutaRichiestaAccessoSistema(idDomanda, richiestaAccessoSistemaRifiuto)
            .subscribe(result => {
                this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
                this.onClose();
                const queryParams: Params = { 
                    tabselected: 'rifiutate'
                    //, dialog: 0
                };
                this.router.navigate(
                ['./funzioniPat/gestioneUtenze/'||queryParams],
                {
                    replaceUrl: false,
                    queryParams: queryParams,
                    queryParamsHandling: 'merge'
                });
                // this.router.navigate(['./funzioniPat/gestioneUtenze']);
            }, err => {
                this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
            });
    }

    private creaRichiestaAccessoSistemaRifiuto(): RichiestaAccessoSistemaRifiuto {
        let richiestaAccessoSistemaRifiuto: RichiestaAccessoSistemaRifiuto = new RichiestaAccessoSistemaRifiuto();
        richiestaAccessoSistemaRifiuto.id = this.richiestaAccessoCorrente.id;
        richiestaAccessoSistemaRifiuto.motivazioneRifiuto = this.motivazioneRifiuto;
        richiestaAccessoSistemaRifiuto.note = this.note;
        richiestaAccessoSistemaRifiuto.testoMail = this.testoMail;
        return richiestaAccessoSistemaRifiuto;
    }

    private initPossibiliMotivazioni() {
        this.possibiliMorivazioni.push({ label: 'Seleziona', value: null });
        this.possibiliMorivazioni.push({ label: 'Rifiuto domanda di accesso al sistema', value: 'Rifiuto domanda di accesso al sistema' });
    }

}
