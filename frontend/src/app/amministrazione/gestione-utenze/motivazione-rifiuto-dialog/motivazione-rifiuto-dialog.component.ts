import { Component, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { RichiesteAccessoSistema } from '../richieste-accesso-sistema/dto/RichiesteAccessoSistema';
import { RichiestaAccessoSistemaRifiuto } from '../richieste-accesso-sistema/dto/richiesta-accesso-sistema-rifiuto';
import { Labels } from 'src/app/app.labels';

@Component({
  selector: 'app-motivazione-rifiuto-dialog',
  templateUrl: './motivazione-rifiuto-dialog.component.html',
  styleUrls: ['./motivazione-rifiuto-dialog.component.css']
})
export class MotivazioneRifiutoDialogComponent implements OnInit {

  public display: boolean;
  public motivazioneRifiuto: String;
  public testoMail: String;
  public note: String;
  public labels = Labels;

  @Output() displayChange = new EventEmitter();
  @Input() richiestaAccessoCorrente: RichiesteAccessoSistema;
  @Input() istruttoriaDomandaCorrente: RichiestaAccessoSistemaRifiuto;


  constructor() { }

  ngOnInit() {
  }

  public onOpen(istruttoriaDomandaCorrente: RichiestaAccessoSistemaRifiuto) {
    this.display = true;
    this.displayChange.emit(true);
    this.setCampiDialog(istruttoriaDomandaCorrente);
  }

  private setCampiDialog(istruttoriaDomandaCorrente: RichiestaAccessoSistemaRifiuto) {
    this.motivazioneRifiuto = istruttoriaDomandaCorrente.motivazioneRifiuto;
    this.testoMail = istruttoriaDomandaCorrente.testoMail;
    this.note = istruttoriaDomandaCorrente.note;
    console.log(istruttoriaDomandaCorrente);
  }

  public onClose() {
    this.display = false;
    this.displayChange.emit(false);
  }

}
