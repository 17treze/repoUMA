import { RevocaImmediataAcquisizioneMandatoModule } from './revoca-immediata-acquisizione-mandato.module';

describe('RevocaImmediataRiaperturaFascicoloModule', () => {
  let revocaImmediataRiaperturaFascicoloModule: RevocaImmediataAcquisizioneMandatoModule;

  beforeEach(() => {
    revocaImmediataRiaperturaFascicoloModule = new RevocaImmediataAcquisizioneMandatoModule();
  });

  it('should create an instance', () => {
    expect(revocaImmediataRiaperturaFascicoloModule).toBeTruthy();
  });
});
