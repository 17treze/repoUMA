import { RiaperturaFascicoloModule } from './riapertura-fascicolo.module';

describe('RiaperturaFascicoloModule', () => {
  let riaperturaFascicoloModule: RiaperturaFascicoloModule;

  beforeEach(() => {
    riaperturaFascicoloModule = new RiaperturaFascicoloModule();
  });

  it('should create an instance', () => {
    expect(riaperturaFascicoloModule).toBeTruthy();
  });
});
