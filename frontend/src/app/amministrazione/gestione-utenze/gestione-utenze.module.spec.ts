import { GestioneUtenzeModule } from './gestione-utenze.module';

describe('GestioneUtenzeModule', () => {
  let gestioneUtenzeModule: GestioneUtenzeModule;

  beforeEach(() => {
    gestioneUtenzeModule = new GestioneUtenzeModule();
  });

  it('should create an instance', () => {
    expect(gestioneUtenzeModule).toBeTruthy();
  });
});
