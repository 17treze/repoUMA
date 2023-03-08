import { Injectable } from "@angular/core";
@Injectable({
  providedIn: "root"
})
export class StepEvent {

  stepIndex = 0;
  // stepper
  next() {
    this.stepIndex++;

  }
  previous() {
    this.stepIndex--;

  }


}
