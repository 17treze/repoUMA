export const environment = {
  // WSO2
  discoveryDocumentEndpoint: 'https://10.206.193.173:9443/oauth2/oidcdiscovery',
  redirectUri: 'http://localhost:4200/homepage',
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
  backendUrl: 'http://localhost:4200/',
  frontendUrl: 'http://localhost:4200/',
  srTrentoUrl: 'https://srt-test.infotn.it/private/welcome.aspx',
  mobileUrl: "http://localhost:4200/",
  agsUrl: 'http://siap-test.infotn.it',
  tipoLogin: 'cittadino',
  indexPage: 'http://localhost:4201',
  annoInizioCampagnaUma: 2021,
  dtAperturaRevocaImmediata: new Date(new Date().getFullYear(), 0, 1),
  dtChiusuraRevocaImmediata: new Date(new Date().getFullYear(), 11, 31, 23, 59, 59),
  TIMEOUT_SPINNER: 120000,
  anniGis: [2022],
  annoCorrenteGis: 2022,
  verificaFirmaFascicolo: false,
};
