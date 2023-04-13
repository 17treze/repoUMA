import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MenuItem, MessageService } from 'primeng-lts';
import { catchError } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from '../../shared/a4g-messages';
import { DichiarazioneAntimafiaService } from './dichiarazione-antimafia.service';
import { DichiarazioneAntimafia } from './models/dichiarazione-antimafia';

@Component({
  selector: 'app-dichiarazione-antimafia',
  templateUrl: './dichiarazione-antimafia.component.html',
  styleUrls: ['./dichiarazione-antimafia.component.css'],
})
export class DichiarazioneAntimafiaComponent implements OnInit {
  constructor(
    private translateService: TranslateService,
    private activatedRoute: ActivatedRoute,
    private messageService: MessageService,
    private antimafiaService: DichiarazioneAntimafiaService
  ) {}

  cuaa = '';
  dichiarazione?: DichiarazioneAntimafia;
  note = '';
  expireDate = '';
  public tabs: Array<MenuItem>;
  public selectedTab: number = 1;

  ngOnInit() {
    this.cuaa = this.activatedRoute.snapshot.paramMap.get('cuaa');
    this.fetchDichiarazione();
    this.initTabs();
  }

  fetchDichiarazione() {
    if (!this.cuaa) {
      this.showError();
      return;
    }

    this.antimafiaService
      .getDichiarazioneAntimafia(this.cuaa)
      .pipe(
        catchError(async (error) => {
          this.showError();
          throw error.status;
        })
      )
      .subscribe((dichiarazione) => {
        if (!dichiarazione) {
          this.showError();
          return;
        }
        this.dichiarazione = dichiarazione;
        const status = this.dichiarazione.stato.identificativo;
        if (!status) {
          return;
        }

        if (status === 'RIFIUTATA') {
          this.fetchNotes();
        }
      });
  }

  showError() {
    this.messageService.add(
      A4gMessages.getToast(
        'toast',
        A4gSeverityMessage.error,
        A4gMessages.erroreRecuperoDichiarazioneAntimafia
      )
    );
  }

  fetchNotes() {
    this.antimafiaService
      .getAntimafiaNote(this.dichiarazione.id)
      .pipe(catchError(async (error) => this.setNoteNotFound()))
      .subscribe((note) => {
        if (!note || !note[0]) {
          this.setNoteNotFound();
          return;
        }

        const nota = JSON.parse(note[0].nota);
        if (!nota || !nota.noteDiChiusura) {
          this.setNoteNotFound();
          return;
        }

        this.note = nota.noteDiChiusura;
      });
  }

  setNoteNotFound() {
    this.note = this.translateService.instant('antimafia.noReasonFound');
  }

  private initTabs() {
    this.tabs = new Array<MenuItem>(
      this.createMenuItem('antimafia.applicationStatus', 1),
      this.createMenuItem('antimafia.relatedApplications', 2)
    );
  }


  private createMenuItem(label: string, tabIndex: number): MenuItem {
    return {
      label: this.translateService.instant(label),
      command: () => this.selectedTab = tabIndex
    };
  }
}
