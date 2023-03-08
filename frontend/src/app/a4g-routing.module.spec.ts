import { A4gRoutingModule } from './a4g-routing.module';

describe('A4gRoutingModule', () => {
  let a4gRoutingModule: A4gRoutingModule;

  beforeEach(() => {
    a4gRoutingModule = new A4gRoutingModule();
  });

  it('should create an instance', () => {
    expect(a4gRoutingModule).toBeTruthy();
  });
});
