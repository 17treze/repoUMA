import { Component, OnInit } from '@angular/core';
import { Control, defaults as defaultControls } from 'ol/control';
import Draw from 'ol/interaction/Draw';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import GeometryType from 'ol/geom/GeometryType';
import { MapService } from '../map.service';
import OlMap from 'ol/Map';
import { MapidService } from '../mapid.service';
import { GisTool } from './gisTool';
import { enumTool } from './enumTools';
import Transform from 'ol-ext/interaction/Transform';
import { shiftKeyOnly, always } from 'ol/events/condition';
import { RegularShape, Fill, Stroke, Style, Text } from 'ol/style';
import { ToolBarService } from '../../../shared/ToolBar/toolBar.service';

export class GisTransform extends GisTool {
    toolName = enumTool.transformEl;
    constructor(private mapService: MapService, private toolBarService1: ToolBarService) {
        super([enumTool.transformEl], null, false, true, true, toolBarService1.editToolGroup, toolBarService1);
    }

    declareTool(): any[] {
        function setHandleStyle(interaction: any) {
            interaction.setDefaultStyle();
            // Refresh
            interaction.set('translate', interaction.get('translate'));
        }

        let transform = new Transform({
            enableRotatedTransform: false,
            addCondition: shiftKeyOnly,
            layers: [this.mapService.editLayer],
            hitTolerance: 2,
            translateFeature: true,
            translate: true,
            scale: true,
            rotate: true,
            keepAspectRatio: (true) ? always : undefined,
            stretch: true
        });
        setHandleStyle(transform);
        return [transform];
    }

    public get canUseTool(): boolean {
        return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
    }
}
