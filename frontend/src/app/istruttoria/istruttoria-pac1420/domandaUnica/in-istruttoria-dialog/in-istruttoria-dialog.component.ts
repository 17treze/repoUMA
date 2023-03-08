import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router, Route } from '@angular/router';
import { DettaglioIstruttoriaComponent } from '../dettaglio-istruttoria/dettaglio-istruttoria.component';
import { Costanti } from '../Costanti';
import { IstruttoriaService } from '../istruttoria.service';
import { SostegnoDu } from '../classi/SostegnoDu';

@Component({
  selector: 'app-in-istruttoria-dialog',
  templateUrl: './in-istruttoria-dialog.component.html',
  styleUrls: ['./in-istruttoria-dialog.component.css']
})
export class InIstruttoriaDialogComponent implements OnInit {
  @Input() display: boolean;
  @Input() dettaglioIstruttoria: DettaglioIstruttoriaComponent;
  @Output() displayChange = new EventEmitter();

  constructor(private router: Router, private route: ActivatedRoute, private istruttoriaService: IstruttoriaService) { }

  ngOnInit() { }

  onC() { // chiudo dialog
    this.display = false;
    this.displayChange.emit(false);
  }

  naviga() {
    this.router.navigate(['']);
  }

  avviaAccoppiatoZootecnia() {
    this.router.navigate(['./' + Costanti.ACC_ZOOTECNIA], { relativeTo: this.route });
  }

  avviaAccoppiatoSuperficie() {
    this.router.navigate(['./' + Costanti.cruscottoAccoppiatoSuperficie], { relativeTo: this.route });
  }
}
