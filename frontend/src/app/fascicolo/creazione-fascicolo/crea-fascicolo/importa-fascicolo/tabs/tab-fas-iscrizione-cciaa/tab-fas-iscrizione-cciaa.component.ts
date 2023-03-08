import { Component, OnInit } from '@angular/core';
import { ImpresaDto } from 'src/app/fascicolo/creazione-fascicolo/dto/ImpresaDto';
import { ActivatedRoute } from '@angular/router';
import { FonteImpresaAgricola } from 'src/app/fascicolo/creazione-fascicolo/dto/FonteImpresaAgricolaEnum';

@Component({
  selector: 'app-tab-fas-iscrizione-cciaa',
  templateUrl: './tab-fas-iscrizione-cciaa.component.html',
  styleUrls: ['./tab-fas-iscrizione-cciaa.component.css']
})
export class TabFasIscrizioneCciaaComponent implements OnInit {

  anagrafica: ImpresaDto;
  fonteEnum = FonteImpresaAgricola;
  checkEsenzioneAnagrafeTributaria: boolean;

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.anagrafica = this.route.snapshot.data['fascicolo'];
    this.fonteEnum.ANAGRAFE_TRIBUTARIA
  }

}
