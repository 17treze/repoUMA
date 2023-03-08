import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-configurazione-istruttoria',
  templateUrl: './configurazione-istruttoria.component.html',
  styleUrls: ['./configurazione-istruttoria.component.css']
})
export class ConfigurazioneIstruttoriaComponent implements OnInit {

  public annoCampagna: number;
  public funzioni = Array<MenuItem>();

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.annoCampagna = this.route.snapshot.params.annoCampagna;
    this.funzioni = new Array<MenuItem>(
      { routerLink: "ricevibilita", label: "Ricevibilità" },
      //{ routerLink: "istruttorie", label: "Istruttorie" },
      { routerLink: "istruttoriaDisaccoppiato", label: "Istruttoria Disaccoppiato" },
      { routerLink: "istruttoriaAcz", label: "Istruttoria ACZ" },
      { routerLink: "istruttoriaAcs", label: "Istruttoria ACS" }
    );
  }

}
