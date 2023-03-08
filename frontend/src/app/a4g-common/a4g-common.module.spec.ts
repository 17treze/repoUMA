import { A4gCommonModule } from './a4g-common.module';

describe('A4gCommonModule', () => {
  let a4gCommonModule: A4gCommonModule;

  beforeEach(() => {
    a4gCommonModule = new A4gCommonModule();
  });

  it('should create an instance', () => {
    expect(a4gCommonModule).toBeTruthy();
  });
});
