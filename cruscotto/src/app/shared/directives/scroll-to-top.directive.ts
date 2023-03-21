import { Directive, HostListener } from '@angular/core';
import { ScrollSupport } from '../utilities/scroll.support';

@Directive({
  selector: '[appScrollToTop]'
})
export class ScrollToTopDirective {

  @HostListener('click') onClick(): void {
    ScrollSupport.scrollToTop();
  }
}
