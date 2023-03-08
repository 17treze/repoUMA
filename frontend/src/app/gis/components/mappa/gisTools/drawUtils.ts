import { MapBrowserEvent } from 'ol/MapBrowserEvent';
import { Coordinate } from 'ol/coordinate';

export function condition(drawLast: boolean): (event: MapBrowserEvent) => boolean {
    return function(event) {
        if(isAddEvent(event)) {
            return true;
        } 
        if(isEndEvent(event)) {
            if(drawLast) {
                addPoint.call(this, event.coordinate);
            }

            this.finishDrawing();

            return false;
        }
    }
}

function isAddEvent(event: MapBrowserEvent): boolean {
    return event.originalEvent.button === 0 && !event.originalEvent.ctrlKey;
}
function isEndEvent(event: MapBrowserEvent): boolean {
    return event.originalEvent.button === 2;
}

function addPoint(point: Coordinate): void {
    const coordinate: number[] = [];
    
    coordinate.push(point);

    this.appendCoordinates(coordinate);
}