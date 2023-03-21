import { Directive, HostListener, Input } from '@angular/core';
import { ScrollSupport } from '../utilities/scroll.support';

@Directive({
  selector: '[appScrollToElement]'
})
export class ScrollToElementDirective {

  @Input()
  appScrollToElement: number;

  @HostListener('click') onClick(): void {
    ScrollSupport.scroll(this.appScrollToElement);
  }

}
