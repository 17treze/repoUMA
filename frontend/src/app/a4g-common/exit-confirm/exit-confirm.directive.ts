import {
    Directive,
    EventEmitter,
    Host,
    HostListener,
    Input,
    Output,
} from "@angular/core";
import { NgForm } from "@angular/forms";
import { Dialog } from "primeng-lts";
import { ExitConfirmService } from "./exit-confirm.service";

@Directive({
    selector: "[exitConfirm]",
})
export class ExitConfirmDirective {
    @Input("formToAnalyze") form: NgForm;
    @Input("visibleField") set visible(visible: boolean) {
        this._dialog.visible = visible;
    }

    @Output("visibleFieldChange") visibleChange =
        new EventEmitter<boolean>();

    constructor(
        @Host() private _dialog: Dialog,
        private _exitConfirmService: ExitConfirmService
    ) {}

    @HostListener("visibleChange", ["$event"])
    onVisibleChange(visible: boolean): void {
        if (visible || !this.form.dirty) {
            this.visibleChange.emit(visible);
        } else {
            this._exitConfirmService.ask({
                save: () => this.form.ngSubmit.emit(),
                discard: () => this.visibleChange.emit(visible),
            });
        }
    }
}
