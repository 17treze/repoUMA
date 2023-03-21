import { Component, OnInit, OnDestroy, NgModule } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoaderService } from './loader.service';
import { ProgressSpinnerModule } from 'primeng-lts/progressspinner';


@Component({
  selector: 'a4g-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent implements OnInit {
  public show = false;
  public subscription: Subscription;

  constructor(private loaderService: LoaderService) { }

  ngOnInit() {
    this.subscription = this.loaderService.toShowOB.subscribe((toShow: boolean) => {
      this.show = toShow;
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