import { RichiesteRevocaMandatoModule } from './richieste-revoca-mandato.module';

describe('RichiesteRevocaMandatoModule', () => {
  let richiesteRevocaMandatoModule: RichiesteRevocaMandatoModule;

  beforeEach(() => {
    richiesteRevocaMandatoModule = new RichiesteRevocaMandatoModule();
  });

  it('should create an instance', () => {
    expect(richiesteRevocaMandatoModule).toBeTruthy();
  });
});
