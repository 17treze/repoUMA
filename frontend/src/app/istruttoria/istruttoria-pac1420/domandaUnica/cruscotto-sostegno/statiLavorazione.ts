export class StatiLavorazione {
    stato: string;
    order: number;
    counter: number;

    constructor(orderIn: number, statoIn: string, counterIn: number) {
        this.order = orderIn;
        this.stato = statoIn;
        this.counter = counterIn;
    }
}
