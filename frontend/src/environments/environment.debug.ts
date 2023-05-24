export const environment = {
  // WSO2
  discoveryDocumentEndpoint: 'https://10.206.193.173:9443/oauth2/oidcdiscovery',
  redirectUri: 'http://localhost:4200/home',
  logoutUrl: 'http://localhost:4200',
  clientId: 'IMmZukAFkU1q57oTlO4402Sy0Loa',
  dummyClientSecret: 'nSBQ41ZYYbikBX428ejUL0_cYf0a',
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
  backendUrl: 'http://localhost:8080/',
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

};
