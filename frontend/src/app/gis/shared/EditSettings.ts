import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EditSettingsService {
    public modifyPixelTolerance = 8;
    public snapTolerance = 8;
    snapEdge: boolean;
    snapVertex: boolean;
     
  constructor() { }
}