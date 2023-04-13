import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-istruttoria-domanda-psr-superficie-costi',
    templateUrl: './istruttoria-domanda-psr-superficie-costi.component.html',
    styleUrls: ['./istruttoria-domanda-psr-superficie-costi.component.css']
})
export class IstruttoriaDomandaPsrSuperficieCostiComponent {

    @Input()
    importoLiquidato: number | undefined;

    @Input()
    showDivider = false;

}
