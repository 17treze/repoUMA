import { DisaccoppiatoModule } from './disaccoppiato.module';

describe('DisaccoppiatoModule', () => {
  let disaccoppiatoModule: DisaccoppiatoModule;

  beforeEach(() => {
    disaccoppiatoModule = new DisaccoppiatoModule();
  });

  it('should create an instance', () => {
    expect(disaccoppiatoModule).toBeTruthy();
  });
});
