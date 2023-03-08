import { ClienteConsumiDto } from './../../../../../core-uma/models/dto/ClienteConsumiDto';
import { Component, ElementRef, EventEmitter, OnInit, Output, ViewChild, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-popup-cancella-cliente-contoterzi',
  templateUrl: './popup-cancella-cliente-contoterzi.component.html',
  styleUrls: ['./popup-cancella-cliente-contoterzi.component.scss']
})
export class PopupCancellaClienteContoterziComponent implements OnInit, OnDestroy {
  display: boolean;
  idDichiarazione: number;

  @ViewChild('dialog', { read: ElementRef, static: true })
  dialogElement: ElementRef;

  @Output() chiudiPopup = new EventEmitter<ClienteConsumiDto>();
  @Input() cliente: ClienteConsumiDto;

  routerSubscription: Subscription;
  clienteSelezionato: ClienteConsumiDto;

  constructor(
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.clienteSelezionato = null;
    this.routerSubscription = this.route.params
      .subscribe(params => {
        this.idDichiarazione = params['id'];
      });
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  open(clienteContoTerzi: ClienteConsumiDto) {
    this.clienteSelezionato = clienteContoTerzi;
    this.display = true;
  }

  onHideDialog() {
  }

  onCloseToastRicercaClienti() {
  }

  chiudiDialog($event: Event) {
    this.chiudiPopup.emit(this.clienteSelezionato);
    this.display = false;
  }

  annulla() {
    this.display = false;
  }

}
