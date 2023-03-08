import { ButtonSpinnerTemplateComponent } from './button-spinner-template/button-spinner-template.component';
import { Directive, OnChanges, SimpleChanges, ElementRef, ComponentFactory, ComponentRef, ComponentFactoryResolver, Renderer2, ViewContainerRef, Input, TemplateRef } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Directive({
  selector: '[loading]'
})
export class ButtonLoadingDirective implements OnChanges {
  private spinnerFactory: ComponentFactory<ButtonSpinnerTemplateComponent>;
  private spinner: ComponentRef<ButtonSpinnerTemplateComponent>;
  private button: HTMLButtonElement;

  @Input('loading') loading: boolean;

  constructor(
    private templateRef: TemplateRef<any>,
    private elementRef: ElementRef,
    private componentFactoryResolver: ComponentFactoryResolver,
    private viewContainerRef: ViewContainerRef,
    private renderer: Renderer2
  ) {
    this.spinnerFactory = this.componentFactoryResolver.resolveComponentFactory(ButtonSpinnerTemplateComponent);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!changes.loading) {
      return;
    }

    if (changes.loading.currentValue) {
      this.createSpinner();
    } else if (!changes.loading.firstChange) {
      this.destroySpinner();
    }
  }

  private createSpinner(): void {
    if (!this.spinner) {
      this.viewContainerRef.clear(); /** rimuove precedenti embedded view nel caso di più passaggi di loading true/false per evitare doppie visualizzazioni */
      this.viewContainerRef.createEmbeddedView(this.templateRef);
      this.spinner = this.viewContainerRef.createComponent(this.spinnerFactory);
      this.button = this.elementRef.nativeElement.parentElement && this.elementRef.nativeElement.parentElement.children[0];
      // this.renderer.addClass(this.button, 'd-none');
      // this.renderer.setProperty(this.button, 'disabled', true);
      this.renderer.appendChild(this.button, this.spinner.location.nativeElement);
    }
  }

  private destroySpinner(): void {
    if (this.spinner) {
      // this.renderer.removeClass(this.button, 'd-none');
      // this.renderer.setProperty(this.button, 'disabled', false);
      this.spinner.destroy();
      this.spinner = null;
      this.button = null;
    }
  }
}
