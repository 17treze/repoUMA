// This file can be replaced during build by using the `fileReplacements` array.
// `ng build (or serve) -c test` replaces `environment.ts` with `environment.test.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  // WSO2
  discoveryDocumentEndpoint: 'https://10.206.193.173:9443/oauth2/oidcdiscovery',
  redirectUri: 'http://localhost:4200',
  logoutUrl: 'http://localhost:4200',
  clientId: 'fJO0a2U007kMEQCWV7iuregCDVMa',
  dummyClientSecret: 'hNGtXaR1QWEaO9Wo8HccEhayokwa',
  scope: 'openid',
  responseType: 'code',
  sessionChecksEnabled: false,
  // begin: richiesto perche' il documento non e' completamente conforme
  strictDiscoveryDocumentValidation: false,
  skipIssuerCheck: true,
  // end: richiesto perche' il documento non e' completamente conforme
  requireHttps: false,
  showDebugInformation: true,
  disablePKCE: false,
  useHttpBasicAuth: true,

  // legacy
  production: false,
  backendUrl: 'http://localhost:8080',
  frontendUrl: 'http://localhost:4200',
  TIMEOUT_SPINNER: 120000,
  indexPage: 'http://localhost:4200',
  annoInizioCampagnaUma: 2020,
  dtAperturaRevocaImmediata: new Date(new Date().getFullYear(), 0, 1),
  dtChiusuraRevocaImmediata: new Date(new Date().getFullYear(), 11, 31, 23, 59, 59),
  anniGis: [2022, 2023],
  annoCorrenteGis: 2023,
  verificaFirmaFascicolo: false,

  agsUrl: 'http://siap-svil.infotn.it',
  srTrentoUrl: 'https://srt-test.infotn.it/private/welcome.aspx',
  tipoLogin: 'cittadino',
  mobileUrl: 'http://localhost:4200/',

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
