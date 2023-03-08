import { WindowsEvent } from './../shared/WindowsEvent';
import { LavorazioneWorkspaceComponent } from './../components/lavorazioneWorkspace/lavorazioneWorkspace.component';
import { ComponentPortal, DomPortalOutlet, PortalInjector } from '@angular/cdk/portal';
import { ApplicationRef, ComponentFactoryResolver, ComponentRef, Injectable, Injector, OnDestroy } from '@angular/core';
import {POPOUT_MODAL_DATA, POPOUT_MODALS, PopoutData, PopoutModalName} from './popout.token.service';

@Injectable()
export class PopoutService implements OnDestroy {
  styleSheetElement;

  constructor(
    private injector: Injector,
    private componentFactoryResolver: ComponentFactoryResolver,
    private applicationRef: ApplicationRef,
    public windowsEvent: WindowsEvent
  ) {
  }

  ngOnDestroy() {}

  openPopoutModal(data) {
    const windowInstance = this.openOnce(
      // '../components/gis/external-window/external-window-rendering.html',
      '../../../assets/pages/external-window/external-window-rendering.html',
      'MODAL_POPOUT'
    );
      console.log(windowInstance)
    // window instance
    setTimeout(() => {
      this.createCDKPortal(data, windowInstance);
    }, 1000);
  }

  openOnce(url, target) {
    // Open window
    const winRef = window.open('', target, 'location=no,width=1000,height=780,left=150,top=200');
    if (winRef.location.href === 'about:blank') {
      winRef.location.href = url;
    }
    return winRef;
  }

  createCDKPortal(data, windowInstance) {
    console.log(windowInstance)
    if (windowInstance) {
      // Crea un PortalOutlet con html per la nuova finestra
      const outlet = new DomPortalOutlet(windowInstance.document.body, this.componentFactoryResolver, this.applicationRef, this.injector);

      // Copia i link di css esterni
      this.styleSheetElement = this.getStyleSheetElement();
      windowInstance.document.head.appendChild(this.styleSheetElement);

      // Copia i Css di gis
      document.querySelectorAll('style').forEach(htmlElement => {
        windowInstance.document.head.appendChild(htmlElement.cloneNode(true));
      });

      this.styleSheetElement.onload = () => {
        // Pulisce il contenuto
        windowInstance.document.body.innerText = '';

        // Aggiungo un ID al body per differenziare la tabella
        windowInstance.document.body.id = 'body-external-window';

        // Inject
        const injector = this.createInjector(data);

        // Attach the portal
        let componentInstance;

       windowInstance.document.title = 'Estensione di Poligoni ADL';
       componentInstance = this.attachLavorazioneWorkspaceComponent(outlet, injector);

        POPOUT_MODALS['windowInstance'] = windowInstance;
        POPOUT_MODALS['outlet'] = outlet;
        POPOUT_MODALS['componentInstance'] = componentInstance;
        this.windowsEvent.element = POPOUT_MODALS;
        // this.setScriptClosingWindow(windowInstance)
      };
    }
  }

  isPopoutWindowOpen() {
    return POPOUT_MODALS['windowInstance'] && !POPOUT_MODALS['windowInstance'].closed;
  }

  focusPopoutWindow() {
    POPOUT_MODALS['windowInstance'].focus();
  }

  closePopoutModal() {
    console.log( POPOUT_MODALS['windowInstance'])
    Object.keys(POPOUT_MODALS).forEach(modalName => {
      if (POPOUT_MODALS['windowInstance']) {
        POPOUT_MODALS['windowInstance'].close();
      }
    });
  }


  attachLavorazioneWorkspaceComponent(outlet, injector) {
    const containerPortal = new ComponentPortal(LavorazioneWorkspaceComponent, null, injector);
    const containerRef: ComponentRef<LavorazioneWorkspaceComponent> = outlet.attach(containerPortal);
    return containerRef.instance;
  }

  createInjector(data): PortalInjector {
    const injectionTokens = new WeakMap();
    injectionTokens.set(POPOUT_MODAL_DATA, data);
    return new PortalInjector(this.injector, injectionTokens);
  }

  getStyleSheetElement() {
    const styleSheetElement = document.createElement('link');
    document.querySelectorAll('link').forEach(htmlElement => {
      if (htmlElement.rel === 'stylesheet') {
        const absoluteUrl = new URL(htmlElement.href).href;
        styleSheetElement.rel = 'stylesheet';
        styleSheetElement.href = absoluteUrl;
      }
    });
    console.log(styleSheetElement.sheet);
    return styleSheetElement;
  }

 /* setScriptClosingWindow(istance) {
    const script = document.createElement('script');
    script.type = `application/javascript`;
    script.text = `window.addEventListener('beforeunload', function (e) {
      e.preventDefault();
      e.returnValue = '';
      });`;
    istance.document.body.appendChild(
    script);
    return script;
  }*/

}
