import { Component, OnInit, OnDestroy, NgModule } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoaderService } from '../../app/loader.service';
import { LoaderState } from './loader.model';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { PanelEvent } from '../gis/shared/PanelEvent';
@Component({
  selector: 'a4g-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent implements OnInit {
  public show = false;
  private subscription: Subscription;

  constructor(private loaderService: LoaderService, public panelEvent: PanelEvent) { }

  ngOnInit() {
    this.subscription = this.loaderService.loaderState.subscribe((state: LoaderState) => {
      this.show = state.show;
    }, error => console.error(error.error));
  }
}

@NgModule({
  exports: [LoaderComponent],
  declarations: [LoaderComponent],
  imports: [ProgressSpinnerModule],
  providers: [LoaderService]
})
export class LoaderModule { }