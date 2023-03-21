import { Component, Input } from '@angular/core';
import { DichiarazioneAntimafia } from '../models/dichiarazione-antimafia';

@Component({
  selector: 'app-stato-dichiarazione',
  templateUrl: './stato-dichiarazione.component.html',
  styleUrls: ['./stato-dichiarazione.component.css']
})
export class StatoDichiarazioneComponent {
  @Input()
  dichiarazione?: DichiarazioneAntimafia;

  @Input()
  note?: String;

  constructor() { }
}
