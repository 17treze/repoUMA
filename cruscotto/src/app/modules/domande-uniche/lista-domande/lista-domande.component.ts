import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { Subject } from 'rxjs';
import { switchMap, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { RegisterService } from '../../register/register.service';
import { DomandeUnicheService } from '../domande-uniche.service';
import { DomandaUnica } from '../models/domanda-unica';
import { DomandaUnicaFilter } from '../models/domanda-unica-filter';
import { StatoDomandaUnicaEnum } from '../models/stato-domanda-unica-enum';
import { AziendaAgricolaService } from 'src/app/shared/services/azienda-agricola.service';
import { Carousel } from 'primeng-lts/carousel';

@Component({
  selector: 'app-lista-domande',
  templateUrl: './lista-domande.component.html',
  styleUrls: ['./lista-domande.component.css']
})
export class ListaDomandeComponent implements OnInit {
  @ViewChild('carousel') carousel: Carousel;

  private cuaaFromUrl: string = "";

  public domandaUnica: DomandaUnica;
  public listaDomandaUnica: Array<DomandaUnica>;
  public responsiveOptions;
  public numVisible: number = 3;
  private componentDestroyed$: Subject<boolean> = new Subject();
  swiping = false;

  constructor(
    private registerService: RegisterService,
    private domandeUnicheService: DomandeUnicheService,
    private messageService: MessageService,
    private translateService: TranslateService,
    protected route: ActivatedRoute,
    private router: Router,
    private aziendaAgricolaService: AziendaAgricolaService
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

  ngOnInit() { 
    this.caricaUtente();
  }

  caricaUtente() {
    this.cuaaFromUrl = this.route.parent.snapshot.paramMap.get('cuaa');
    //recupera il CUAA dal service o se non presente lo carica
    //in seguito viene chiamato il servizio per il recupero delle domande
    this.aziendaAgricolaService.getSelectedCuaa(this.cuaaFromUrl).pipe(
        switchMap(fascicolo => {
            const domandaUnicaFilter: DomandaUnicaFilter = new DomandaUnicaFilter();
            domandaUnicaFilter.expand = 'RICHIESTE';
            domandaUnicaFilter.stati = [StatoDomandaUnicaEnum.IN_ISTRUTTORIA,
              StatoDomandaUnicaEnum.RICEVIBILE,
              StatoDomandaUnicaEnum.NON_RICEVIBILE,
              StatoDomandaUnicaEnum.PROTOCOLLATA];
            domandaUnicaFilter.cuaa = fascicolo.cuaa;
            return this.domandeUnicheService.getDomandaUnica(domandaUnicaFilter);
          }),
          takeUntil(this.componentDestroyed$)
      ).subscribe(domande => {
          this.listaDomandaUnica = domande ? domande : [];
          this.listaDomandaUnica = this.escludiAnniPrecedenti(this.listaDomandaUnica);
          this.listaDomandaUnica = this.ordinaXCampagna(this.listaDomandaUnica);
          if (this.listaDomandaUnica && this.listaDomandaUnica.length < 3){
            this.numVisible = 1;
          }
          this.carousel.calculatePosition();
      });
  }

  private ordinaXCampagna(domande: Array<DomandaUnica>): Array<DomandaUnica> {
    if (domande && domande.length >0) {
      return domande.sort((n1,n2) => {
        if (n1.infoGeneraliDomanda.campagna < n2.infoGeneraliDomanda.campagna) {
            return 1;
        }
        if (n1.infoGeneraliDomanda.campagna > n2.infoGeneraliDomanda.campagna) {
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

  vaiAllaDomanda(domanda): any {
    if (this.swiping) return;
    this.router.navigate(
      ['./:idDomanda/dettaglioDomandaUnica'
          .replace(':idDomanda', domanda.infoGeneraliDomanda.numeroDomanda)
      ],
      { relativeTo: this.route.parent.parent }
    );
  }

  private escludiAnniPrecedenti(domande: Array<DomandaUnica>): Array<DomandaUnica>  {
    if (domande && domande.length >0) {
      var posizione = domande.length-1;
      var annoRiferimento = this.translateService.instant("common.annoRiferimento");
      for(posizione; posizione >= 0; posizione--){
        if ( domande[posizione].infoGeneraliDomanda.campagna < annoRiferimento) {
          domande.splice(posizione, 1);
        }
     }
      return domande;
    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  onSwipeMove(event: any) {
    this.swiping = true;
  }
  onSwipeEnd(event: any) {
    this.swiping = false;
  }
}
