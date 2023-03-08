import {Component, OnInit} from '@angular/core';
import {IstruttoriaService} from '../../istruttoria.service';
import {ActivatedRoute} from '@angular/router';
import {PascoloDao} from '../../domain/pascolo-dao';

@Component({
  selector: 'app-dati-per-pascolo',
  templateUrl: './dati-per-pascolo.component.html',
  styleUrls: ['./dati-per-pascolo.component.css']
})
export class DatiPerPascoloComponent implements OnInit {
  idDomandaCorrente;
  cols: any[] = [{ header: 'Dati in entrata', width: '70%' }];
  cols2: any[] = [{ header: 'Dati in uscita', width: '70%' }];
  cols3: any[] = [{header: 'Anomalie' , width: '70%'}];
  arrayPascoli: Array<PascoloDao> = [];
  dati: Array<any> = [{}];
  constructor(private istruttoriaService: IstruttoriaService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params
      .subscribe(params => {
        this.idDomandaCorrente = params['idDomanda'];
      });
    this.istruttoriaService.getDatiperPascoloByID_Domanda(this.idDomandaCorrente)
      .subscribe((dati) => {
        this.arrayPascoli = dati;
        if(this.arrayPascoli){
          this.arrayPascoli.sort((one, two) => (one.descPascolo < two.descPascolo ? -1 : 1));
        }
      });

  }


}
