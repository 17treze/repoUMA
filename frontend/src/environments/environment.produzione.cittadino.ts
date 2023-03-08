// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: true,
  backendUrl: 'https://a4g.provincia.tn.it/cittadino/',
  frontendUrl: 'https://a4g.provincia.tn.it/cittadino/a4g/',
  srTrentoUrl: 'https://srt.infotn.it/cittadino/homepage.aspx',
  mobileUrl: "https://myappag.it/cittadino/cruscotto/",
  agsUrl: 'http://www.siap.provincia.tn.it',
  tipoLogin: 'cittadino',
  indexPage: 'https://a4g.provincia.tn.it/',
  annoInizioCampagnaUma: 2022,
  dtAperturaRevocaImmediata: new Date(new Date().getFullYear(), 0, 1),
  dtChiusuraRevocaImmediata: new Date(new Date().getFullYear(), 3, 30, 23, 59, 59),
  TIMEOUT_SPINNER: 120000,
  anniGis: [2022],
  annoCorrenteGis: 2022,
  verificaFirmaFascicolo: true,
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
