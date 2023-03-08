import { Injectable } from '@angular/core';
import RegularShape from 'ol/style/RegularShape';
import Text from 'ol/style/Text';
import Style from 'ol/style/Style';
import CircleStyle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import { LineString, Point } from 'ol/geom';
@Injectable()
export class MisuraUtils {
  tipPoint: any;
  unitLine = 'm';
  unitPolygon = 'mq';
  firstClick = true;
  geometry: any ;
  enableLabelLine = true;
  enableLabelArea = true;

  public style = new Style({
    fill: new Fill({
      color: 'rgba(255, 255, 255, 0.2)',
    }),
    stroke: new Stroke({
      color: 'rgba(1, 193, 145, 1)',
      lineDash: [10, 10],
      width: 2,
    }),
    image: new CircleStyle({
      radius: 5,
      stroke: new Stroke({
        color: 'rgba(1, 193, 145, 1)',
      }),
      fill: new Fill({
        color: 'rgba(255, 255, 255, 0.2)',
      }),
    }),
  });

  public labelStyle = new Style({
    text: new Text({
      font: '14px Calibri,sans-serif',
      fill: new Fill({
        color: 'rgba(255, 255, 255, 1)',
      }),
      backgroundFill: new Fill({
        color: 'rgba(0, 0, 0, 0.7)',
      }),
      padding: [3, 3, 3, 3],
      textBaseline: 'bottom',
      offsetY: -15,
    }),
    image: new RegularShape({
      radius: 8,
      points: 3,
      angle: Math.PI,
      displacement: [0, 10],
      fill: new Fill({
        color: 'rgba(0, 0, 0, 0.7)',
      }),
    }),
  });

  public tipStyle = new Style({
    text: new Text({
      font: '12px Calibri,sans-serif',
      fill: new Fill({
        color: 'rgba(255, 255, 255, 1)',
      }),
      backgroundFill: new Fill({
        color: 'rgba(0, 0, 0, 0.4)',
      }),
      padding: [2, 2, 2, 2],
      textAlign: 'left',
      offsetX: 15,
    }),
  });

  public segmentStyle = new Style({
    text: new Text({
      font: '12px Calibri,sans-serif',
      fill: new Fill({
        color: 'rgba(255, 255, 255, 1)',
      }),
      stroke: new Stroke({ color: 'rgba(0, 0, 0, 0.5)', width: 5 }),
      placement: 'line',
      maxAngle: 360,
    })
  });

  segmentStyles = [this.segmentStyle];

  formatLength(line): string {
    const length = line.getLength();
    let output;
    if (this.unitLine === 'km') {
      output = Math.round((length / 1000) * 100) / 100 + ' ' + this.unitLine;
    } else { // deafult è metri
      output = Math.round(length * 100) / 100 + ' ' + this.unitLine;
    }
    return output;
  }

  formatArea(polygon): string {
    const area = polygon.getArea();
    let output;
    if (this.unitPolygon === 'ha') {
      output = Math.round((area / 10000) * 100) / 100 + ' ' + this.unitPolygon;
    } else { // deafult è m2
      output = Math.round(area * 100) / 100 + ' ' + this.unitPolygon;
    }
    return output;
  }

  styleFunction(feature, showSegments, drawType, misuraStyle, otherStyle, sogliaSegmenti: number = null, resetDefault: boolean= true) {
    const _self2 = this;
    let styles: any;

    if (feature) {
      if (resetDefault) {
        _self2.unitLine = 'm';
        _self2.unitPolygon = 'mq';
        styles = Object.assign([], otherStyle);
      } else {
        styles = otherStyle;
      }

      if (misuraStyle) {
        const geometry = feature.getGeometry();
        _self2.geometry = geometry;
        const type = geometry.getType();
        let geometryLabel, label, line;
        if (!drawType || drawType === type) {
          if (type === 'Polygon') {
            geometryLabel = geometry.getInteriorPoint();
            if (_self2.enableLabelArea) {
              label = _self2.formatArea(geometry);
            }
            line = new LineString(geometry.getCoordinates()[0]);
          } else if (type === 'LineString') {
            geometryLabel = new Point(geometry.getLastCoordinate());
            label = _self2.formatLength(geometry);
            line = geometry;
          }
        }
        if (showSegments && line && _self2.enableLabelLine) {
          let count = 0;
          line.forEachSegment(function (a, b) {
            const segment = new LineString([a, b]);
            if (sogliaSegmenti &&  segment.getLength() < sogliaSegmenti) {
              return;
            }
            const label2 = _self2.formatLength(segment);
            if (_self2.segmentStyles.length - 1 < count) {
              _self2.segmentStyles.push(_self2.segmentStyle.clone());
            }

            const segmentPoint = new Point(segment.getCoordinateAt(0.5));
            let textBaseline, xInner, yInner: any;

            // verifico se il punto medio spostato a dx è dentro o fuori dal poligono originale
            const pointx = segmentPoint.clone();
            pointx.translate(0.008, 0); // via di mezzo fra tolleranza 0.005 e cifra visulizzata 0.01
            xInner = _self2.geometry.intersectsCoordinate(pointx.getCoordinates());

            // verifico se il punto medio spostato in alto è dentro o fuori dal poligono originale
            const pointy = segmentPoint.clone();
            pointy.translate(0, 0.008); // via di mezzo fra tolleranza 0.005 e cifra visulizzata 0.01
            yInner = _self2.geometry.intersectsCoordinate(pointy.getCoordinates());

            // se y a 90 trattamento particolare
            if (segment.intersectsCoordinate(pointy.getCoordinates())) {
              if (xInner && a[1] < b[1]) {
                textBaseline = 'top';
              } else if (!xInner && a[1] < b[1]) {
                textBaseline = 'bottom';
              } else if (xInner && a[1] > b[1]) {
                textBaseline = 'bottom';
              } else if (!xInner && a[1] > b[1]) {
                textBaseline = 'top';
              }
            } else {
              if (xInner && yInner) {
                textBaseline = 'bottom';
              } else if (!xInner && yInner) {
                textBaseline = 'bottom';
              } else if (xInner && !yInner) {
                textBaseline = 'top';
              } else if (!xInner && !yInner) {
                textBaseline = 'top';
              }
            }

            _self2.segmentStyles[count].getText().setTextBaseline(textBaseline);
            _self2.segmentStyles[count].setGeometry(segment);
            _self2.segmentStyles[count].getText().setText(label2);
            styles.push(_self2.segmentStyles[count]);
            count++;
          });
        }
        if (label) {
          _self2.labelStyle.setGeometry(geometryLabel);
          _self2.labelStyle.getText().setText(label);
          styles.push(_self2.labelStyle);
        }

        if (
          type === 'Point' // &&!_self.modify.getOverlay().getSource().getFeatures().length
        ) {
          _self2.tipPoint = geometry;
          styles.push(_self2.tipStyle);
        }
        return styles;
      } else {
        return otherStyle;
      }
    }
  }
}

