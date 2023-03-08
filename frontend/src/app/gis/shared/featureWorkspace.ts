export class FeatureWorkspace {
    constructor(
        public featureID: string,
        public id: string,
        public codUso: CodUso,
        public statoColt: StatoColt,
        public sincronizzatoInMappa: boolean,
        public area: string,
        public selectedFeature: boolean,
        public extent: any,
        public multiSelected: any,
        public selectedAndMultiselected: boolean,
        public note: string) {
    }
}
export class CodUso {
    constructor(public codUso: string, public codUsoDesc: string) { }
}
export class StatoColt {
    constructor(public statoColt: string, public statoColtDesc: string) { }
}
