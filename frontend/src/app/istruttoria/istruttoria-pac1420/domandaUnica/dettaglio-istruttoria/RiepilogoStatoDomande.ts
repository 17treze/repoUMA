export class RiepilogoStatoDomande {
    stato: string;
    counter: number;
    order: number;

    constructor(orderIn: number, statoIn: string, counterIn: number) {
        this.order = orderIn;
        this.stato = statoIn;
        this.counter = counterIn;
    }

}
