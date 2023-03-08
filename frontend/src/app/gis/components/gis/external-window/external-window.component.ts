import { LavorazioniEvent } from './../../../shared/LavorazioniEvent';
import { Router } from '@angular/router';
import { ToolBarService } from './../../../shared/ToolBar/toolBar.service';
import { WindowsEvent } from './../../../shared/WindowsEvent';
import {
  Component,
  ViewChild,
  OnDestroy,
  HostListener,
  SimpleChanges,
  ViewEncapsulation,
  ComponentRef,
  Input,
  OnChanges
} from '@angular/core';
import {POPOUT_MODAL_DATA, POPOUT_MODALS, PopoutData, PopoutModalName} from './../../../services/popout.token.service';
import {PopoutService} from './../../../services/PopoutService.service';
import { CdkPortal, DomPortalHost, DomPortalOutlet } from '@angular/cdk/portal';

/**
 * This component template wrap the projected content
 * with a 'cdkPortal'.
 */

@Component({
  selector: 'gis-externalWindow',
  styleUrls: ['./external-window.component.css'],
  templateUrl: './external-window.component.html',
  encapsulation: ViewEncapsulation.None
})
export class ExternalWindowComponent implements OnChanges, OnDestroy{
  @ViewChild(CdkPortal) portal: CdkPortal;
  @Input() elementiWorkspace: any[];

  editaCelle: boolean;
  componentRef: ComponentRef<any>;

  constructor(
    private popoutService: PopoutService, public windowsEvent: WindowsEvent, private router: Router,
     public lavorazioniEvent: LavorazioniEvent
  ) {}
  @HostListener('window:beforeunload', ['$event'])
  onWindowClose(event: Event) {
    this.windowsEvent.fromWindow = false;
    this.popoutService.closePopoutModal();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.lavorazioniEvent.stato === 'IN_CORSO') {
      this.lavorazioniEvent.editaCelleWorkSpace = true;
    }
  }

  ngOnDestroy(): void {
    //
  }
  newWin(name: string) {
    console.log(this.router.url);
    this.windowsEvent.fromWindow = true;
    const modalData = {
      elementiWorkspace: this.elementiWorkspace
    };

    if (!this.popoutService.isPopoutWindowOpen()) {
      this.popoutService.openPopoutModal(modalData);
    } else {
      const sameData = POPOUT_MODALS['componentInstance'].name === name;
      // When popout modal is open and there is no change in data, focus on popout modal
      if (sameData && POPOUT_MODALS['elementiWorkspace'] === this.elementiWorkspace) {
        this.popoutService.focusPopoutWindow();
      } else {
        POPOUT_MODALS['outlet'].detach();
        const injector = this.popoutService.createInjector(modalData);
        const componentInstance = this.popoutService.attachLavorazioneWorkspaceComponent(POPOUT_MODALS['outlet'], injector);
        POPOUT_MODALS['componentInstance'] = componentInstance;
        this.popoutService.focusPopoutWindow();
      }
    }
  }

}
