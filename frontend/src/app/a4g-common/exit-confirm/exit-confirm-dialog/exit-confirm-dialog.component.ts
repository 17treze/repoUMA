import { Component } from "@angular/core";
import { Observable } from "rxjs";
import { ExitConfirmService } from "../exit-confirm.service";

@Component({
    selector: "exitConfirmDialog",
    templateUrl: "./exit-confirm-dialog.component.html",
    styleUrls: ["./exit-confirm-dialog.component.css"],
})
export class ExitConfirmDialogComponent {
    public visible$: Observable<boolean>;

    constructor(private _exitConfirmService: ExitConfirmService) {
        this.visible$ = _exitConfirmService.visible$;
    }

    public onSave(): void {
        this._exitConfirmService.save();
    }
    public onDiscard(): void {
        this._exitConfirmService.discard();
    }
    public onCancel(): void {
        this._exitConfirmService.cancel();
    }
}
