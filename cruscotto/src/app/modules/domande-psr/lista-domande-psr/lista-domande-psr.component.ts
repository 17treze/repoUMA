import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { MessageService } from 'primeng-lts';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute } from '@angular/router';
import { switchMap, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { DomandePsrService } from '../domande-psr.service';
import { DomandaPsr } from '../models/domanda-psr';
import { AziendaAgricolaService } from 'src/app/shared/services/azienda-agricola.service';
import { CodiceMisureIntervento } from '../models/codiceMisureInterventoEnum';
import { Operazione } from '../models/operazione';
import { Router } from '@angular/router';
import { Carousel } from 'primeng-lts/carousel';

@Component({
  selector: 'app-lista-domande-psr',
  templateUrl: './lista-domande-psr.component.html',
  styleUrls: ['./lista-domande-psr.component.css']
})
export class ListaDomandePsrComponent implements OnInit {
  @ViewChild('carousel') carousel: Carousel;

  private cuaaFromUrl: string = "";
  public numVisible: number = 3;

  public listaDomandaPsr: Array<DomandaPsr>;
  public responsiveOptions;
  private componentDestroyed$: Subject<boolean> = new Subject();
  swiping = false;

  constructor(
    private domandePsrService: DomandePsrService,
    private messageService: MessageService,
    private translateService: TranslateService,
    protected route: ActivatedRoute,
    private aziendaAgricolaService: AziendaAgricolaService,
    private router: Router
  ) {
    this.responsiveOptions = [
      {
        breakpoint: '1024px',
        numVisible: 3,
        numScroll: 3
      },
      {
        breakpoint: '768px',
        numVisible: 2,
        numScroll: 2
      },
      {
        breakpoint: '560px',
        numVisible: 1,
        numScroll: 1
      }
    ];
  }

  static sortOperazioniByDescendingCode(operazioni: Array<Operazione>): Array<Operazione> {
    return operazioni.sort((n1, n2) => {
      if (n1.codiceMisureIntervento.toString().substring(0, 3) < n2.codiceMisureIntervento.toString().substring(0, 3)) {
        return 1;
      }
      if (n1.codiceMisureIntervento.toString().substring(0, 3) > n2.codiceMisureIntervento.toString().substring(0, 3)) {
        return -1;
      }
      // Se la prima parte del codice è uguale, ordina dal più piccolo al più grande la seconda parte del codice
      if (n1.codiceMisureIntervento.toString().substring(4, 8) > n2.codiceMisureIntervento.toString().substring(4, 8)) {
        return 1;
      }
      if (n1.codiceMisureIntervento.toString().substring(4, 8) < n2.codiceMisureIntervento.toString().substring(4, 8)) {
        return -1;
      }
      // i codici sono uguali
      return 0;
      }
    );
  }

  ngOnInit() {
    this.caricaUtente();
  }

  // Ritorna una mappa che ha come chiavi gli interventi PSR provenienti dall'enum censito e come valore di default false
  loadOperazioniMap(): Map<string, boolean> {
    let mapOperazioni = new Map<string, boolean>();
    for (let operazione in CodiceMisureIntervento) {
      mapOperazioni.set(operazione, false);
    }
    return mapOperazioni;
  }

  caricaUtente() {
    this.cuaaFromUrl = this.route.parent.snapshot.paramMap.get('cuaa');
    //recupera il CUAA dal service o se non presente lo carica
    //in seguito viene chiamato il servizio per il recupero delle domande
    this.aziendaAgricolaService.getSelectedCuaa(this.cuaaFromUrl).pipe(
      switchMap(fascicolo => {
        const cuaa = fascicolo && fascicolo.cuaa ? fascicolo.cuaa : this.cuaaFromUrl;
        return this.domandePsrService.getDomandePsr(cuaa);
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(domande => {
      this.listaDomandaPsr = domande ? domande : [];
      if (this.listaDomandaPsr.length == 1 || this.listaDomandaPsr.length == 0 ) {
        //aggiunta questa condizione perchè in modalità desktop il componente carousel lancia una eccezione e non fa vedere dati
        this.numVisible = 1;
      }
      this.listaDomandaPsr = this.ordinaXCampagna(this.listaDomandaPsr);
      this.listaDomandaPsr = this.ordinaCodiceMisure(this.listaDomandaPsr);
      // Mapping degli interventi da mostrare
      this.listaDomandaPsr = this.mappingInterventiDaMostrare(this.listaDomandaPsr);
      this.carousel.calculatePosition();
    });
  }

  @HostListener('window:resize')
  onResize() {
    this.carousel.page = this.carousel.firstIndex() >= 0 ? this.carousel.firstIndex() : 0;
  }

  // In base agli interventi richiesti nella domanda, viene settata a true la chiave corrispondente a tali interventi
  private mappingInterventiDaMostrare(domande: Array<DomandaPsr>): Array<DomandaPsr> {
    domande.forEach(domanda => {
      domanda.mapOperazioniDaMostrare = this.loadOperazioniMap();
      domanda.operazioni.forEach(op => {
          domanda.mapOperazioniDaMostrare.set(op.codiceMisureIntervento, true);
      });
    });
    return domande;
  }

  private ordinaCodiceMisure(domande: Array<DomandaPsr>): Array<DomandaPsr> {
    domande.forEach(domanda => {
      domanda.operazioni = this.ordinaXCodiceMisure(domanda.operazioni);
    });
    return domande;
  }

  private ordinaXCodiceMisure(operazioni: Array<Operazione>): Array<Operazione> {
    if (operazioni != null && operazioni.length > 0) {
      return ListaDomandePsrComponent.sortOperazioniByDescendingCode(operazioni);
    } else {
      this.messageService.add(
        A4gMessages.getToast('defaultToast', A4gSeverityMessage.error, this.translateService.instant('errorsMsg.BRCIDU001').replace("$CUAA", this.cuaaFromUrl)));
      return operazioni;
    }
  }

  private ordinaXCampagna(domande: Array<DomandaPsr>): Array<DomandaPsr> {
    if (domande != null && domande.length > 0) {
      return domande.sort((n1, n2) => {
        if (n1.campagna < n2.campagna) {
          return 1;
        }
        if (n1.campagna > n2.campagna) {
          return -1;
        }
        return 0;
      });
    } else {
      this.messageService.add(
        A4gMessages.getToast('defaultToast', A4gSeverityMessage.error, this.translateService.instant('errorsMsg.BRCIDU001').replace("$CUAA", this.cuaaFromUrl)));
      return domande;
    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  openDomandaPsrSuperficie(domanda: DomandaPsr) {
    if (this.swiping) return;
    this.router.navigate(
      ['psr/:idDomanda/dettaglio-psr-superficie'
        .replace(':idDomanda', String(domanda.numeroDomanda))
      ]
    );
  }
  
  onSwipeMove(event: any) {
    this.swiping = true;
  }
  onSwipeEnd(event: any) {
    this.swiping = false;
  }
}
