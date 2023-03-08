import { ZootecniaModule } from './zootecnia.module';

describe('ZootecniaModule', () => {
  let zootecniaModule: ZootecniaModule;

  beforeEach(() => {
    zootecniaModule = new ZootecniaModule();
  });

  it('should create an instance', () => {
    expect(zootecniaModule).toBeTruthy();
  });
});
