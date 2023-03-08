import { Injectable } from '@angular/core';
import * as jsts from 'jsts';
import Point from 'ol/geom/Point';
import LineString from 'ol/geom/LineString';
import LinearRing from 'ol/geom/LinearRing';
import Polygon from 'ol/geom/Polygon';
import MultiPoint from 'ol/geom/MultiPoint';
import MultiLineString from 'ol/geom/MultiLineString';
import MultiPolygon from 'ol/geom/MultiPolygon';
import Feature from 'ol/Feature';
import { intersects } from 'ol/extent';
import { MapService } from '../map.service';
import { ToastGisComponent } from '../../toast-gis/toast-gis.component';

@Injectable()
export class GisUtils {
    // math utilities

    // coordinates; will return the length of the [a, b] segment
    static getLength(a, b) {
        return Math.sqrt(
            (b[0] - a[0]) * (b[0] - a[0]) + (b[1] - a[1]) * (b[1] - a[1])
        );
    }

    // coordinates; will return true if c is on the [a, b] segment
    static isOnSegment(c, a, b) {
        const lengthAc = GisUtils.getLength(a, c);
        const lengthAb = GisUtils.getLength(a, b);
        const dot =
            ((c[0] - a[0]) * (b[0] - a[0]) + (c[1] - a[1]) * (b[1] - a[1])) / lengthAb;
        return Math.abs(lengthAc - dot) < 1e-6 && lengthAc < lengthAb;
    }

    // modulo for negative values, eg: mod(-1, 4) returns 3
    static mod(a, b) {
        return ((a % b) + b) % b;
    }

    // returns a coordinates array which contains the segments of the feature's
    // outer ring between the start and end points
    // Note: this assumes the base feature is a single polygon
    static getPartialRingCoords(feature, startPoint, endPoint) {
        let polygon = feature.getGeometry();
        if (polygon.getType() === 'MultiPolygon') {
            polygon = polygon.getPolygon(0);
        }
        const ringCoords = polygon.getLinearRing().getCoordinates();

        let i,
            pointA,
            pointB,
            startSegmentIndex = -1;
        for (i = 0; i < ringCoords.length; i++) {
            pointA = ringCoords[i];
            pointB = ringCoords[GisUtils.mod(i + 1, ringCoords.length)];

            // check if this is the start segment dot product
            if (GisUtils.isOnSegment(startPoint, pointA, pointB)) {
                startSegmentIndex = i;
                break;
            }
        }

        const cwCoordinates = [];
        let cwLength = 0;
        const ccwCoordinates = [];
        let ccwLength = 0;

        // build clockwise coordinates
        for (i = 0; i < ringCoords.length; i++) {
            pointA =
                i === 0
                    ? startPoint
                    : ringCoords[GisUtils.mod(i + startSegmentIndex, ringCoords.length)];
            pointB = ringCoords[GisUtils.mod(i + startSegmentIndex + 1, ringCoords.length)];
            cwCoordinates.push(pointA);

            if (GisUtils.isOnSegment(endPoint, pointA, pointB)) {
                cwCoordinates.push(endPoint);
                cwLength += GisUtils.getLength(pointA, endPoint);
                break;
            } else {
                cwLength += GisUtils.getLength(pointA, pointB);
            }
        }

        // build counter-clockwise coordinates
        for (i = 0; i < ringCoords.length; i++) {
            pointA = ringCoords[GisUtils.mod(startSegmentIndex - i, ringCoords.length)];
            pointB =
                i === 0
                    ? startPoint
                    : ringCoords[GisUtils.mod(startSegmentIndex - i + 1, ringCoords.length)];
            ccwCoordinates.push(pointB);

            if (GisUtils.isOnSegment(endPoint, pointA, pointB)) {
                ccwCoordinates.push(endPoint);
                ccwLength += GisUtils.getLength(endPoint, pointB);
                break;
            } else {
                ccwLength += GisUtils.getLength(pointA, pointB);
            }
        }

        // keep the shortest path
        return ccwLength < cwLength ? ccwCoordinates : cwCoordinates;
    }

    static getSinglePolygon(geomMp): any[] {
        const res = [];
        console.log(geomMp.getType());
        if (geomMp.getType() === 'MultiPolygon') {
            for (const el of geomMp.getPolygons()) {
                if (el.getArea() > 0) {
                    res.push(el);
                }
            }
        } else {
            if (geomMp.getArea() > 0) {
                res.push(geomMp);
            }
        }
        return res;
    }

    static getSinglePartFeature(featureMp, outputLayer, mapService: MapService): any[] {
        const geomMp = featureMp.getGeometry();

        const res = [];
        console.log(geomMp.getType());
        if (geomMp.getType() === 'MultiPolygon') {
            for (const el of geomMp.getPolygons()) {
                if (el.getArea() > 0) {
                    console.log('Multipart');
                    const featureSp = new Feature(el);
                    mapService.setNewFeatureattributesFromFeature(featureSp, featureMp, outputLayer);
                    res.push(featureSp);
                }
            }
        } else {
            if (geomMp.getArea() > 0) {
                res.push(featureMp);
            }
        }
        return res;
    }

    static doAllIntersection(inputFeature: Feature, selectedFeatures: Array<Feature>) {
        let done = false;
        const res = new Array();

        try {
            const parser = GisUtils.getJstsparser();

            let jstsBouduaryFeatures = parser.read(inputFeature.getGeometry()).getBoundary();

            for (const featurePoly of selectedFeatures) {
                const jstsPoly = parser.read(featurePoly.getGeometry());
                jstsBouduaryFeatures = jstsPoly.getBoundary().union(res);
            }

            const polygonizer = new jsts.operation.polygonize.Polygonizer();
            polygonizer.add(jstsBouduaryFeatures);

            const polygons = polygonizer.getPolygons();

            const splitted_polygon_parts = [];

            for (const jstsPolygon of polygons.array) {
                for (const featurePoly of selectedFeatures) {
                    if (featurePoly.getGeometry()) {
                        const jstsPoly = parser.read(featurePoly.getGeometry());
                        if (jstsPoly.contains(jstsPolygon.getInteriorPoint())) {
                            const splitted_polygon = new Feature(parser.write(jstsPolygon));
                            // attributi copiati dal primo elemento selezionato
                            splitted_polygon_parts.push(splitted_polygon);
                            break;
                        }
                    }
                }
            }

            if (splitted_polygon_parts.length > 1) {
                for (const polygon of splitted_polygon_parts) {
                    for (const el of GisUtils.getSinglePolygon(polygon.getGeometry())) {
                        done = true;

                        const splitted_polygon = new Feature(el);
                        res.push(splitted_polygon);
                    }
                }
            }

            /*
            for (const featurePoly of selectedFeatures) {
                console.log('remove feature');
                this.mapService.editLayer.getSource().removeFeature(featurePoly);
            } */


        } catch (error) {
            console.log(error);
        } finally {
            /*undoInteraction.blockEnd();
            if (done) {
                this.mapService.modifyFeatureEmitter.emit(this.mapService.editLayer.get(PropertyLayer.CODICE));
                toolBarService.rimuoviListaInteraction(null, true, true);
            }*/
        }
    }

    static carotaggioOnInput(inputFeature: Feature, mapService: MapService,
        listToAdd: Feature[], listToRemove: Feature[], toastComponent: ToastGisComponent): boolean {
        const parser = GisUtils.getJstsparser();

        const jstInputGEom = parser.read(inputFeature.getGeometry());
        const extent1 = inputFeature.getGeometry().getExtent();
        const features = mapService.editLayer.getSource().getFeatures();

        let allOk = true;
        let error: any;

        features.forEach(function (featureOri) {
            let done = false;
            if (!intersects(extent1, featureOri.getGeometry().getExtent())) {
                return;
            }

            const jstsPoly = parser.read(featureOri.getGeometry());
            try {
                if (!jstInputGEom.intersects(jstsPoly)) {
                    return;
                }
                const difference = jstsPoly.difference(jstInputGEom);
                // in realtà codice potrebbe essere corretto, ma se fa validazione và in errore (approfondisci librerie jsts/src/org/locationtech/jts/precision/)

                if (!jstsPoly.equalsTopo(difference)) {
                    done = true; // dovrò cancellare il poligono originale

                    for (const el of GisUtils.getSinglePolygon(parser.write(difference))) {
                        const splitted_polygon = new Feature(el);
                        mapService.setNewFeatureattributesFromFeature(splitted_polygon, featureOri, mapService.editLayer);
                        listToAdd.push(splitted_polygon);
                        // mapService.editLayer.getSource().addFeature(splitted_polygon);
                    }
                    listToRemove.push(featureOri);
                    // mapService.editLayer.getSource().removeFeature(featureOri);
                }
            } catch (e) {
                console.log('catch1', e);
                allOk = false;
                error = e;
            }
        });
        return allOk;
    }

    static getJstsparser(): jsts.io.OL3Parser {
        const parser = new jsts.io.OL3Parser();
        parser.inject(
            Point,
            LineString,
            LinearRing,
            Polygon,
            MultiPoint,
            MultiLineString,
            MultiPolygon
        );
        return parser;
    }

    static showValidateMessage(toastComponent: ToastGisComponent, poly: any, msg: string, poly2: any, force: boolean) {
        let validMassage = GisUtils.getValidatationMessage(poly);
        if (!validMassage) {
            validMassage = GisUtils.getValidatationMessage(poly);
            if (!validMassage) {
                if (force) {
                    toastComponent.showErrorGenerico(msg);
                }
            } else {
                msg = msg + 'Errore in geometria selezionata: ' + validMassage;
                toastComponent.showErrorGenerico(msg);
            }
        } else {
            msg = msg + 'Errore in geometria selezionata: ' + validMassage;
            toastComponent.showErrorGenerico(msg);
        }
    }

    static getValidatationMessage(poly1: any): string | undefined {
        let res;
        if (!poly1) {
            return res;
        }

        const isValidOp = new jsts.operation.valid.IsValidOp(poly1);
        const valid = isValidOp.isValid();
        if (!valid) {
            if (isValidOp.getValidationError() && isValidOp.getValidationError().getMessage()) {
                res = isValidOp.getValidationError().getMessage();
            }
            return res;
        } else {
            return res;
        }
    }

    static checkValid(poly: any): boolean {
        const isValidOp = new jsts.operation.valid.IsValidOp(poly);
        return isValidOp.isValid();
    }
}


