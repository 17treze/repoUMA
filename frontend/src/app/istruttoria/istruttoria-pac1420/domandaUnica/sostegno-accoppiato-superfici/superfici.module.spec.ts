import { SuperficiModule } from './superfici.module';

describe('SuperficiModule', () => {
  let superficiModule: SuperficiModule;

  beforeEach(() => {
    superficiModule = new SuperficiModule();
  });

  it('should create an instance', () => {
    expect(superficiModule).toBeTruthy();
  });
});
