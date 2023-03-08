import { Component, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Subject } from 'rxjs';
import { FormGroup, FormControl } from '@angular/forms';
import { RichiestaAccessoSistemaApprovazione } from '../../richieste-accesso-sistema/dto/RichiestaAccessoSistemaApprovazione';
import { RichiesteAccessoSistema } from '../../richieste-accesso-sistema/dto/RichiesteAccessoSistema';


@Component({
  selector: 'motivazione-approvazione-dialog',
  templateUrl: './motivazione-approvazione-dialog.component.html',
  styleUrls: ['./motivazione-approvazione-dialog.component.scss']
})
export class MotivazioneApprovazioneDialogComponent implements OnInit {
  public display: boolean = false;
  public domandaApprovataGroup: FormGroup;

  @Output() displayChange = new EventEmitter();
  @Input() richiestaAccessoCorrente: RichiesteAccessoSistema;
  @Input() istruttoriaDomandaCorrente: RichiestaAccessoSistemaApprovazione;

  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService) {

      this.domandaApprovataGroup = new FormGroup({
        'testoMail': new FormControl(),
        'note': new FormControl()
      });

      this.domandaApprovataGroup.disabled;
  }


  ngOnInit() {
    
  }

  onClose() {
    this.display = false;
    this.displayChange.emit(false);
  }

  onOpen(istruttoriaDomandaCorrente: RichiestaAccessoSistemaApprovazione) {
    this.setCampiDialog(istruttoriaDomandaCorrente);
    this.displayChange.emit(true);
    this.display = true;
  }

  private setCampiDialog(istruttoriaDomandaCorrente: RichiestaAccessoSistemaApprovazione) {
    // this.testoMail = istruttoriaDomandaCorrente.testoMail;
    // this.note = istruttoriaDomandaCorrente.note;
    this.domandaApprovataGroup.controls['testoMail'].setValue(istruttoriaDomandaCorrente.testoMail);
    this.domandaApprovataGroup.controls['note'].setValue(istruttoriaDomandaCorrente.note);
    console.log(istruttoriaDomandaCorrente);
  }

  
  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
