import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[appValidateInputOtherNumber]'
})
export class ValidateInputOtherNumberDirective {

  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home', '-', 'ArrowLeft', 'ArrowRight'];
  private regex: RegExp = new RegExp(/^\d{0,5}(\.?|(\.\d{0,6}))$/);

  constructor(private el: ElementRef) {
  }
  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    console.log(this.el.nativeElement.value);
    // Allow Backspace, tab, end, and home keys
    if (this.specialKeys.indexOf(event.key) !== -1) {
      return;
    }
    const current: string = this.el.nativeElement.value;
    const position: number = this.el.nativeElement.selectionStart;

    const next: string = [current.slice(0, position), event.key , current.slice(position)].join('');
    if (next && !String(next).match(this.regex) ) {
      event.preventDefault();
    }
  }

}
