import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng-lts';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { DomandePsrStrutturaliService } from '../domande-psr-strutturali.service';
import { DomandaPsrStrutturale } from '../models/domanda-psr-strutturale';
import { AziendaAgricolaService } from 'src/app/shared/services/azienda-agricola.service';

@Component({
  selector: 'app-lista-domande-psr-strutturali',
  templateUrl: './lista-domande-psr-strutturali.component.html',
  styleUrls: ['./lista-domande-psr-strutturali.component.css']
})
export class ListaDomandePsrStrutturaliComponent implements OnInit {

  private cuaaFromUrl: string = "";
  public numVisible: number = 3;
  public esempioCodicePSRStrutturale: string = "7.5.1";

  public listaDomandaPsrStrutturale: Array<DomandaPsrStrutturale>;
  public responsiveOptions;
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private domandePsrStrutturaliService: DomandePsrStrutturaliService,
    private messageService: MessageService,
    private router: Router,
    protected route: ActivatedRoute,
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
  swiping = false;

  ngOnInit() {
    this.caricaUtente();
  }

  caricaUtente() {
    this.cuaaFromUrl = this.route.parent.snapshot.paramMap.get('cuaa');
    //recupera il CUAA dal service o se non presente lo carica
    //in seguito viene chiamato il servizio per il recupero delle domande
    this.aziendaAgricolaService.getSelectedCuaa(this.cuaaFromUrl).pipe(
      switchMap(fascicolo => {
        const cuaa = fascicolo && fascicolo.cuaa ? fascicolo.cuaa : this.cuaaFromUrl;
        return this.domandePsrStrutturaliService.getDomandePsrStrutturali(cuaa);
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(domande => {
      this.listaDomandaPsrStrutturale = domande ? domande : [];
      if (this.listaDomandaPsrStrutturale.length == 0 ) {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.noDomandePSRStr));
      } else {
       console.log(this.listaDomandaPsrStrutturale);
      }
      if (this.listaDomandaPsrStrutturale.length == 1 || this.listaDomandaPsrStrutturale.length == 0 ) {
        //aggiunta questa condizione perchè in modalità desktop il componente carousel lancia una eccezione e non fa vedere dati
        this.numVisible = 1;
      }
    },
    error => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDomandePSRStr));
    });
  }

  mostraDataTermine(stato: string): boolean {
    let mostraData: boolean = false;
    let controllo: string = stato.toUpperCase();
    if (controllo == "F" || controllo == "V" || controllo == "H"
        || controllo == "S" || controllo == "O") {
      mostraData = true;
    }
    return mostraData;
  }

  vaiAllaDomanda(domanda): any {
    if (this.swiping) return;
    this.router.navigate(
      ['./:idDomanda/dettaglioPsrStrutturali'
          .replace(':idDomanda', domanda.idProgetto)
      ],
      { relativeTo: this.route.parent.parent }
    );
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
