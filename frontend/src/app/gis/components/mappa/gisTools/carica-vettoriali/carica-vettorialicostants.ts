import { Injectable } from '@angular/core';
import Style from 'ol/style/Style';
import CircleStyle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';

@Injectable()
export class CaricaVettorialiCostants {
  public NoFileMsg = 'Nessun file selezionato';
  public ErrFileExt = 'Formato non corretto, estensioni accettate: JSON, GEOJSON';

  color0 = '#00FF00';
  color1 = '#00FFB9';
  color2 = '#FFFFFF';
  color3 = '#C8FF00';
  color4 = '#FFBA00';
  strokeW = 0.8;

  public styleArray = [CaricaVettorialiCostants.getStyle(this.color0, this.strokeW),
    CaricaVettorialiCostants.getStyle(this.color1, this.strokeW),
    CaricaVettorialiCostants.getStyle(this.color2, this.strokeW),
    CaricaVettorialiCostants.getStyle(this.color3, this.strokeW),
    CaricaVettorialiCostants.getStyle(this.color4, this.strokeW),
    ];

   static getStyle(colorStyle, strokeW): Style {
    return {
      'Point': new Style({
        image: new CircleStyle({
          radius: 5,
          fill: null,
          stroke: new Stroke({ color: colorStyle, width: strokeW }),
        })
      }),
      'LineString': new Style({
        stroke: new Stroke({
          color: colorStyle,
          width: strokeW,
        }),
      }),
      'MultiLineString': new Style({
        stroke: new Stroke({
          color: colorStyle,
          width: strokeW,
        }),
      }),
      'MultiPoint': new Style({
        image: new CircleStyle({
          radius: 5,
          fill: null,
          stroke: new Stroke({ color: colorStyle, width: strokeW }),
        })
      }),
      'MultiPolygon': new Style({
        stroke: new Stroke({
          color: colorStyle,
          width: strokeW
        }),
        fill: new Fill({
          color: 'rgba(0, 0, 0, 0)',
        }),
      }),
      'Polygon': new Style({
        stroke: new Stroke({
          color: colorStyle,
          width: strokeW,
        }),
        fill: new Fill({
          color: 'rgba(0, 0, 0, 0)',
        }),
      }),
      'GeometryCollection': new Style({
        stroke: new Stroke({
          color: colorStyle,
          width: strokeW
        }),
        fill: new Fill({
          color: 'rgba(0, 0, 0, 0)',
        }),
        image: new CircleStyle({
          radius: 10,
          fill: null,
          stroke: new Stroke({
            color: colorStyle,
          }),
        }),
      }),
      'Circle': new Style({
        stroke: new Stroke({
          color: colorStyle,
          width: strokeW,
        })
      }),
    };
  }
}
