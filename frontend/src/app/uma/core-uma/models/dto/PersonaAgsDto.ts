export class PersonaAgsDto { // su BE CaricaAgsDto
    codiceFiscale: string;
	carica: string;
	tipo: string; // Nella response da swagger non trovo questo tipo. Inoltre sembra che nessuno acceda a questo campo (refuso?)
	cuaa: string;
    denominazione: string;
    nome: string;
    cognome: string;
}