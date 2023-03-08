import { FascicoliDaMigrareModule } from './fascicoli-da-migrare.module';

describe('FascicoliDaMigrareModule', () => {
  let fascicoliDaMigrareModule: FascicoliDaMigrareModule;

  beforeEach(() => {
    fascicoliDaMigrareModule = new FascicoliDaMigrareModule();
  });

  it('should create an instance', () => {
    expect(fascicoliDaMigrareModule).toBeTruthy();
  });
});
