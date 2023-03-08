import { Injectable } from '@angular/core';
import { Fill, Stroke, Style, Text, Circle } from 'ol/style';
import CircleStyle from 'ol/style/Circle';
import MultiPolygon from 'ol/geom/MultiPolygon';
import MultiPoint from 'ol/geom/MultiPoint';
import GeometryType from 'ol/geom/GeometryType';
import { PropertyLayer } from './PropertyLayer.enum';
@Injectable()
export class GisStyles {
  // STILI SELCT
  selectTematismoFillColore = 'rgb(255, 255, 50, 0.5)';
  selectTematismoBordoColore = 'rgb(255, 255, 0, 1)';
  selectVertexSyleColor = 'rgb(255, 255, 0, 1)';
  selectTematismoDimensionePunto = 3;
  selectTematismoDimensioneBordo = 2;

  // STILI SELCT MODIFY
  selectModTematismoFillColore = 'rgb(255, 153, 0, 0.2)';
  selectModTematismoBordoColore = 'rgb(255, 153, 0, 1)';
  selectModVertexSyleColor = 'rgb(255, 153, 0, 1)';
  selectModTematismoDimensionePunto = 5;
  selectModTematismoDimensioneBordo = 2;

  public newPolygonStyle = [
    new Style({
      stroke: new Stroke({
        color: 'rgba(255, 255, 255, 0.75)',
        width: 4,
        zIndex: 0
      }),
      image: new CircleStyle({
        radius: 7,
        zIndex: 0,
        fill: new Fill({
          color: 'rgba(255, 255, 255, 0.75)',
        })
      })
    }),
    new Style({
      stroke: new Stroke({
        color: 'rgba(51, 153, 204, 1)',
        width: 2,
        zIndex: 1
      }),
      image: new CircleStyle({
        radius: 5,
        zIndex: 1,
        fill: new Fill({
          color: 'rgba(51, 153, 204, 1)',
        })
      })
    })
  ];

  public modifyPolygonStyle = [new Style({
    stroke: new Stroke({
      color: this.selectModTematismoBordoColore,
      width: this.selectModTematismoDimensioneBordo
    }),
    zIndex: 100
  }),
  new Style({
    image: new CircleStyle({
      radius: this.selectModTematismoDimensionePunto,
      fill: new Fill({
        color: this.selectModVertexSyleColor,
      }),
      zIndex: 101
    }),
    geometry: function (feature) {
      // return the coordinates of the first ring of the polygon
      const coordinates = [].concat.apply([], (<MultiPolygon> feature.getGeometry()).getCoordinates());
      return new MultiPoint(coordinates);
    },
  })];

  getTextStyle(coloreBordo): Text {
    return new Text({
      text: '',
      scale: 1.3,
      rotation: 0.78,
      fill: new Fill({
        color: 'rgb(0,0,0,1)'
      }),
      backgroundFill: new Fill({
        color: coloreBordo
      })
    });
  }

  getStylePoly(coloreFill, coloreBordo, spessoreBordo): Style {
    return new Style({
      fill: new Fill({
        color: coloreFill
      }),
      stroke: new Stroke({
        color: coloreBordo,
        width: spessoreBordo,
      }),
      text: this.getTextStyle(coloreBordo),
    });
  }

  getStyleNew (coloreFill, coloreBordo, spessoreBordo): Style {
    return new Style({
      fill: new Fill({
        color: coloreFill
      }),
      stroke: new Stroke({
        color: coloreBordo,
        width: spessoreBordo,
      }),
    });
  }

  getStyleLine(coloreBordo, width): Style {
    return new Style({
      stroke: new Stroke({
        color: coloreBordo,
        width: width
      }),
      text: this.getTextStyle(coloreBordo),
    });
  }

  getStylePoint(coloreFill, coloreBordo, width): Style {
    return new Style({
      image: new Circle({
        radius: width,
        fill: new Fill({
          color: coloreFill
        }),
        stroke: new Stroke({
          color: coloreBordo,
          width: 2.2
        })
      }),
      text: this.getTextStyle(coloreBordo),
    });
  }


  getGenericStyle(coloreFill, coloreBordo, spessoreBordo, width): any[] {
    const styles = [];

    styles[GeometryType.POLYGON] = this.getStylePoly(coloreFill, coloreBordo, spessoreBordo);
    styles[GeometryType.LINE_STRING] = [this.getStyleLine(coloreBordo, width)];
    styles[GeometryType.MULTI_LINE_STRING] = styles[GeometryType.LINE_STRING];
    styles[GeometryType.POINT] = [this.getStylePoint(coloreFill, coloreBordo, width)];
    styles[GeometryType.MULTI_POINT] = styles[GeometryType.POINT];
    styles[GeometryType.MULTI_POLYGON] = styles[GeometryType.POLYGON];
    styles[GeometryType.GEOMETRY_COLLECTION] =
      [styles[GeometryType.POLYGON],
      styles[GeometryType.LINE_STRING],
      styles[GeometryType.POINT]
      ];
    return styles;
  }


  setWFSCusyomSTYLE(styles, currentLayer, vectorLayer, campoLabel) {
    console.log('setWFSCusyomSTYLE ', currentLayer.mapProperty.get(PropertyLayer.WFS_STYLE));
    
    if (currentLayer.mapProperty.get(PropertyLayer.WFS_STYLE) &&
    currentLayer.mapProperty.get(PropertyLayer.WFS_STYLE) === PropertyLayer.WFS_STYLE_ANOMALIE) {
      const styleAnomalie = this.getStyleNew('rgb(0,0,255,0.2)', 'rgb(0,0,255,1)', 2.2);
      vectorLayer.setStyle((function () {
        return function (feature, resolution) {
          const geom_name = feature.getGeometry().getType();
          if (feature.get('TIPO_ANOMALIA') && feature.get('TIPO_ANOMALIA') === 'ERRORI_ORACLE_CORRETTI_CON_SCOSTAMENTO_AREA') {
            if (campoLabel) {
              styleAnomalie.getText().setText(feature.get(campoLabel));
            }
            return styleAnomalie;
          } else {
            const style = styles[geom_name];
            if (campoLabel) {
              style.getText().setText(feature.get(campoLabel));
            }
            return style;
          }
        };
      })());
      return;
    }

    if (currentLayer.mapProperty.get(PropertyLayer.WFS_STYLE) &&
      currentLayer.mapProperty.get(PropertyLayer.WFS_STYLE) === PropertyLayer.WFS_STYLE_CLIP_SU_ADL) {
        const styleErroriOra = this.getStyleNew('rgb(255,0,0,0.8)', 'rgb(255,0,0,1)', 2);
        vectorLayer.setStyle((function () {
          return function (feature, resolution) {
            const geom_name = feature.getGeometry().getType();
            if (feature.get('ESITO_VALIDAZIONE') && feature.get('ESITO_VALIDAZIONE') !== 'TRUE') {
              if (campoLabel) {
                styleErroriOra.getText().setText(feature.get(campoLabel));
              }
              return styleErroriOra;
            } else {
              const style = styles[geom_name];
              if (campoLabel) {
                style.getText().setText(feature.get(campoLabel));
              }
              return style;
            }
          };
        })());
        return;
    }
  }
}
