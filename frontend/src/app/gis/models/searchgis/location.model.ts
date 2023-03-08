export interface Search {
    results: Result[];
}

export interface Result {
    id: number;
    weight: number;
    attrs: Attributes;
}

export interface Attributes {
    num: number;
    origin: string;
    layerBodId: string;
    lon: string;
    label: string;
    geom_st_box2d: string;
    x: number;
    geom_quadindex: string;
    y: number;
    rank: number;
    detail: string;
    featureId: string;
    lat: string;
}

export type Location = Pick<Attributes, 'label' | 'x' | 'y'>;