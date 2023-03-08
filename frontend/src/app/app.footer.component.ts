import { Component } from '@angular/core';
import { Configuration } from './app.constants';
import { HttpClient } from '@angular/common/http';
import * as FileSaver from 'file-saver';

@Component({
    selector: 'app-footer',
    templateUrl: './app.footer.component.html'
})

export class AppFooterComponent {

    constructor(
        private _configuration: Configuration,
        private http: HttpClient,
    ) { }

    downloadInformativaPrivacy(): any {
        this.http.get(this._configuration.UrlGetInfoPrivacy, {
            responseType: "blob"
        }).subscribe(response => {
            FileSaver.saveAs(response, 'informativa_privacy.pdf')
        })
    }
}
