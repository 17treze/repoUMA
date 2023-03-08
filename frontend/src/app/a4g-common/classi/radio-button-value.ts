export class RadioButtonValue {
    public value: any;
    public name: string;
    public label: string;

    constructor(value: any, name: string, label: string) {
        this.label = label;
        this.name = name;
        this.value = value;
    }
}
