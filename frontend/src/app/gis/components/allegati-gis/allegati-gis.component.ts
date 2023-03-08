import { AllegatiEvent } from './../../shared/AllegatiEvent';
import { Component, EventEmitter, Input, Output, ViewEncapsulation } from '@angular/core';
import { RichiestaModificaSuoloService } from './../../services/richiesta-modifica-suolo.service';
import { AllegatiGisService } from './../../services/allegati-gis.service';
import * as FileSaver from "file-saver"
import { ToastGisComponent } from './../toast-gis/toast-gis.component';
import { DetailGisComponent } from '../detail-gis/detail-gis.component';
import { ConfirmationService } from 'primeng-lts/api';
import { StatiRichiesta } from '../../shared/StatiRichiesta.enum';
import { Subject } from "rxjs";
import { Detail } from '../../models/searchgis/detail';

@Component({
  selector: 'allegati-gis',
  templateUrl: './allegati-gis.component.html',
  styleUrls: ['./allegati-gis.component.css'],
  encapsulation: ViewEncapsulation.None

})

export class AllegatiGisComponent {
  @Input() idDichiarato: string;
  @Input() detailResults: Detail;
  @Input() profiloUtente: string;
  @Output() public loadPoligoniDichiarati = new EventEmitter();

  allegato: any;
  noOfItemsToShowInitially: number = 5;
  public itemsToShow: any;
  public isFullListDisplayed: boolean = false;
  itemsToLoad: number = 50;  //disattivata paginazione con lo scroll
  searchFormParams: any;
  loadingList: boolean;
  protected componentDestroyed$: Subject<boolean> = new Subject();

  listStatiRichiestaEditAllegatiNotAllowed = [StatiRichiesta.CONCLUSA];

  constructor(public richiestaModificaSuoloService: RichiestaModificaSuoloService,
    public toastComponent: ToastGisComponent, public detailGis: DetailGisComponent, public confirmationService: ConfirmationService,
    private allegatiService: AllegatiGisService, public allegatiEvent: AllegatiEvent) {
      this.searchFormParams = { numeroElementiPagina: this.itemsToLoad, pagina: 0, triggerFromDetail: true };
      this.searchFormParams.pagina = this.searchFormParams.pagina;
      this.searchFormParams.numeroElementiPagina = this.searchFormParams.numeroElementiPagina;
    }

  ngOnChanges() {
    this.loadAllegati();
  }

  loadAllegati() {
    this.allegatiEvent.allegati = [];
    this.allegatiService.getListaAllegati(this.idDichiarato, this.searchFormParams).subscribe((response: any) => {
      let allegati: Array<any> = [];
      if (response) {
        allegati = response;
      }
      this.allegatiEvent.allegati = this.allegatiEvent.allegati.concat(allegati);
    });
}

  onScroll() {   //disattivata paginazione con lo scroll
    // if (this.noOfItemsToShowInitially <= this.allegatiEvent.allegatiCount && !this.allegatiEvent.params.triggerFromDetail) {
    //   this.noOfItemsToShowInitially += this.itemsToLoad;
    //   this.itemsToShow = this.allegatiEvent.allegati.slice(0, this.noOfItemsToShowInitially);
    //   this.searchFormParams.pagina += 1;
    //   this.searchFormParams.numeroElementiPagina = 50; //no paginazione
    //   this.allegatiEvent.params.triggerFromDetail = false;
    //   this.loadAllegati();
    // } else if (this.allegatiEvent.params.triggerFromDetail) {
    //   this.noOfItemsToShowInitially += this.itemsToLoad;
    //   this.itemsToShow = this.allegatiEvent.allegati.slice(0, this.noOfItemsToShowInitially);
    //   this.searchFormParams.pagina = 0;
    //   this.searchFormParams.pagina += 1;
    //   this.searchFormParams.numeroElementiPagina = 50; //no paginazione
    //   this.allegatiEvent.params.triggerFromDetail = false;
    //   this.loadAllegati();
    // } else {
    //   this.isFullListDisplayed = true;
    // }
  }

  downloadAllegato(idDocumento) {
    this.allegatiService.getAllegato(this.idDichiarato, idDocumento)
    .subscribe(response => {
      const fileName = this.getFileNameFromResponseContentDisposition(response);
      FileSaver.saveAs(response.body, fileName);
    }), error => this.toastComponent.showErrorAllegati(),
    () => console.info('File downloaded successfully');
  }

  cancellaAllegato(idDocumento) {
    if (this.detailResults && this.listStatiRichiestaEditAllegatiNotAllowed.includes(this.detailResults['statoRichiestaMappato'])) {
      this.toastComponent.showErrorDelete();
    } else {

      this.confirmationService.confirm({
        message: "Sei sicuro di voler cancellare l'allegato?",
        header: "Allegato",
        key: 'deleteAllegatoConfirm',
        icon: 'pi pi-info-circle',
        accept: () => {
          this.allegatiService.deleteAllegato(this.idDichiarato, idDocumento)
            .subscribe(response => {
              if (response['status'] === 200 || response['status'] === 201) {
                this.toastComponent.showSuccessDeleteAllegato();
                setTimeout(() => {
                  this.loadingList = true;
                  if (this.detailResults) {this.ricaricaDati();}
                  this.loadAllegati();
                });
              } else {
                this.toastComponent.showErrorDelete();
              }
            });
          },
        reject: () => {
        }
      });

    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  getFileNameFromResponseContentDisposition = (res: Response) => {
    const contentDisposition = res.headers.get('Content-Disposition') || '';
    const matches = /filename=([^;]+)/ig.exec(contentDisposition);
    const fileName = (matches[1] || 'untitled').trim();
    return fileName;
  };

  ricaricaDati() {
    this.loadPoligoniDichiarati.emit();
  }
  
  richiestaConclusa() {
    let ret: boolean = false
    if (this.detailResults && this.detailResults.stato === 'CONCLUSA') {
      ret = true;
    }
    return ret;
  }
}
