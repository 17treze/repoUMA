import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-a4g-show-more-show-less',
  templateUrl: './a4g-show-more-show-less.component.html',
  styleUrls: ['./a4g-show-more-show-less.component.css']
})
export class A4gShowMoreShowLessComponent implements OnInit, OnChanges {

  @Input() fullText: string;
  @Input() limit: number;

  public showMoreButton: boolean;
  public truncatedText: string;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes.fullText && changes.fullText.currentValue) {
      const fullText = changes.fullText.currentValue.trim();
      if (fullText.length > this.limit) {
        this.showMoreButton = true;
        this.truncate(fullText);
      } else {
        this.showMoreButton = null;
      }
    }
  }

  private truncate(fullText: string) {
    this.truncatedText = fullText.slice(0, this.limit + 1);
  }

}
