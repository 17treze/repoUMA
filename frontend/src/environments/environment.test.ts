// This file can be replaced during build by using the `fileReplacements` array.
// `ng build (or serve) -c test` replaces `environment.ts` with `environment.test.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  backendUrl: 'https://a4g-test.infotn.it/',
  frontendUrl: 'http://localhost:4200/',
  srTrentoUrl: 'https://srt-test.infotn.it/private/welcome.aspx',
  mobileUrl: 'https://myappag-test.tndigit.it/cittadino/cruscotto/',
  agsUrl: 'http://siap-test.infotn.it',
  tipoLogin: 'cittadino',
  indexPage: 'https://a4g-test.infotn.it/',
  annoInizioCampagnaUma: 2021,
  dtAperturaRevocaImmediata: new Date(new Date().getFullYear(), 0, 1),
  dtChiusuraRevocaImmediata: new Date(new Date().getFullYear(), 11, 31, 23, 59, 59),
  TIMEOUT_SPINNER: 120000,
  anniGis: [2022, 2023],
  annoCorrenteGis: 2022,
  verificaFirmaFascicolo: false,
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
