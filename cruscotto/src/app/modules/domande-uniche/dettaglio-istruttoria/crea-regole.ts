import { Injectable } from '@angular/core';
import { IsTrue, Severity } from '@angularlicious/rules-engine';
import { TranslateService } from '@ngx-translate/core';
import { EsitoControlli, Istruttorie } from '../models/dettaglio-istruttoria';
import { RuleHelper } from '../shared/rule-helper';

@Injectable({
  providedIn: 'root'
})
export class CreaRegole {

  //ordine stati
    // richiesto -
    // controllo SUPERATI
    // controlli non SUPERATI -
    // non AMMISSIBILE -
    // controlli SUPERATI
    // LIQUIDABILE
    // controlli LIQUIDABILITÀ’ non SUPERATI
    // non LIQUIDABILE
    // pagamento non AUTORIZZATO
    // controlli INTERSOSTEGNO SUPERATI
    // pagamento AUTORIZZATO
  constructor(protected translate: TranslateService) { }

  //ordine stati
    // richiesto -
    // controllo SUPERATI
    // controlli non SUPERATI -
    // non AMMISSIBILE -
    // controlli SUPERATI
    // LIQUIDABILE
    // controlli LIQUIDABILITÀ’ non SUPERATI
    // non LIQUIDABILE
    // pagamento non AUTORIZZATO
    // controlli INTERSOSTEGNO SUPERATI
    // pagamento AUTORIZZATO


  retrieveTranslation(bridu: string, istruttoria: Istruttorie) {
    //regola specifica per stato e sostegno
    const livello1 = 'ANOMALIE' +'.'+istruttoria.sostegno +'.'+ istruttoria.statoLavorazioneSostegno;// +'.'+ anomalia.rulePolicy.message;
    //regola comune ma specifica per stato
    const livello2 = 'ANOMALIE.COMMON.'+ istruttoria.statoLavorazioneSostegno;
    //regola comune
    const livello3 = 'ANOMALIE.COMMON';
    let codMessaggio = livello1+'.'+bridu;
    if (this.translate.instant(codMessaggio) !== codMessaggio) return this.translate.instant(codMessaggio);
    codMessaggio = livello2+'.'+bridu;
    if (this.translate.instant(codMessaggio) !== codMessaggio) return this.translate.instant(codMessaggio);
    codMessaggio = livello3+'.'+bridu;
    if (this.translate.instant(codMessaggio) !== codMessaggio) return this.translate.instant(codMessaggio);
    return undefined;
  }
  perDisaccoppiato(istruttoria: Istruttorie) {

    const ruleHelper = new RuleHelper();
    //CONTROLLI NON SUPERATI & NON AMMISSIBILE
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC009',istruttoria),{tipoControllo:'BRIDUSDC009', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC010',istruttoria),{tipoControllo:'BRIDUSDC010', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC011',istruttoria),{tipoControllo:'BRIDUSDC011', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC012',istruttoria),{tipoControllo:'BRIDUSDC012', esito:false});
    //CONTROLLI MANTENIMENTO PASCOLI
    //CONTROLLI NON SUPERATI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC067',istruttoria),{tipoControllo:'BRIDUSDC067', esito:false});
    //CONTROLLI SUPERATI e SUCCESSIVI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC019',istruttoria),{tipoControllo:'BRIDUSDC019', esito:true});
    //PREMIO BASE - RIDUZIONI e SANZIONI
    //CONTROLLI NON SUPERATI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC034',istruttoria),{tipoControllo:'BRIDUSDC034', esito:false});
    ruleHelper.addRuleAnd(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC109&BRIDUSDC128',istruttoria),{tipoControllo: 'BRIDUSDC109',esito: false},{tipoControllo: 'BRIDUSDC128',esito: false});
    //CONTROLLI SUPERATI e SUCCESSIVI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC020',istruttoria),{tipoControllo:'BRIDUSDC020', esito:true});
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC021inf_10',istruttoria),{tipoControllo:'BRIDUSDC021', valString: 'inf_10'});
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC021sup_10',istruttoria),{tipoControllo:'BRIDUSDC021', valString: 'sup_10'});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC035',istruttoria),{tipoControllo:'BRIDUSDC035', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC135_isAnomalieCoordinamento',istruttoria),{tipoControllo:'BRIDUSDC135', esito:true});
    // 028
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDC028', istruttoria), {tipoControllo: 'BRIDUSDC028', esito: false})
    //INTERVENTO GREENING  - RIDUZIONI e SANZIONI
    //CONTROLLI SUPERATI e SUCCESSIVI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC026',istruttoria),{tipoControllo:'BRIDUSDC026', esito:true});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC055',istruttoria),{tipoControllo:'BRIDUSDC055', esito:true});
    //INTERVENTO GIOVANE AGRICOLTORE  - RIDUZIONI e SANZIONI
    //CONTROLLI SUPERATI e SUCCESSIVI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC059',istruttoria),{tipoControllo:'BRIDUSDC059', esito:true});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC060',istruttoria),{tipoControllo:'BRIDUSDC060', esito:true});
    //RIEPILOGO SANZIONI
    //CONTROLLI NON SUPERATI & NON AMMISSIBILE
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC031',istruttoria),{tipoControllo:'BRIDUSDC031', esito:false});
    //CONTROLLI FINALI
    //CONTROLLI SUPERATI e SUCCESSIVI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC036',istruttoria),{tipoControllo:'BRIDUSDC036', esito:true});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC043',istruttoria),{tipoControllo:'BRIDUSDC043', esito:true});
    //CONTROLLI DI LIQUIDABILITÀ’
    //CONTROLLI LIQUIDABILITÀ’ NON SUPERATI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL037',istruttoria),{tipoControllo:'BRIDUSDL037', esito:false});
    ruleHelper.addRuleAnd(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL038&BRIDUNVL129',istruttoria),{tipoControllo:'BRIDUSDL038', esito:true},{tipoControllo:'BRIDUNVL139', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL039',istruttoria),{tipoControllo:'BRIDUSDL039', esito:true});
    // 133
    //ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL133', istruttoria), {tipoControllo: 'BRIDUSDL133', esito: false});
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL135', istruttoria), {tipoControllo: 'BRIDUSDL135', esito: true});
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL135_IRR', istruttoria), {tipoControllo: 'BRIDUSDL135_IRR', esito: true});
    //DEBITI
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL134', istruttoria), {tipoControllo: 'BRIDUSDL134', esito: true});//stato debiti
    //CONTROLLI INTERSOSTEGNO
    //PAGAMENTO NON AUTORIZZATO & NON LIQUIDABILE
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDS040',istruttoria),{tipoControllo:'BRIDUSDS040', valString: 'NON_DETERMINABILE'});
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDS040',istruttoria),{tipoControllo:'BRIDUSDS040', valString: 'NON_RAGGIUNTO'});
    //PAGAMENTO NON AUTORIZZATO
    this.addRuleAntimafia(ruleHelper,istruttoria);
    istruttoria.ruleResult = ruleHelper.evaluateRules();
  }

  addRuleAntimafia(ruleHelper: RuleHelper, istruttoria: Istruttorie){
    ruleHelper.addCustomRule(this.createCustomRuleAntimafia(istruttoria.esitiControlli,'RAGGIUNTO', istruttoria));
    ruleHelper.addCustomRule(this.createCustomRuleAntimafia(istruttoria.esitiControlli,'NON_DETERMINABILE', istruttoria ));
  }

  createCustomRuleAntimafia(listaAnomalie: EsitoControlli[], valString: string, istruttoria: Istruttorie) {
    const an1=listaAnomalie.find(x => x.tipoControllo.includes("BRIDUSDS050"));
    const an2=listaAnomalie.find(x => x.tipoControllo.includes("importoMinimoAntimafia"));
    if ( an1 && an2) {
      const rul = new IsTrue("BRIDUSDS050&IMPMINANT", this.retrieveTranslation("BRIDUSDS050&IMPMINANT"+valString,istruttoria) , (an1.esito === false) && (an2.valString === valString ), true);
      rul.severity =  Severity.Warning;
      return rul;
    }
  }

  perZootecnia(istruttoria: Istruttorie) {
    const ruleHelper = new RuleHelper();
    //CONTROLLI ACCOPPIATO ZOOTECNIA
    //CONTROLLI NON SUPERATI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC009',istruttoria),{tipoControllo:'BRIDUSDC009', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC010',istruttoria),{tipoControllo:'BRIDUSDC010', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACZ123',istruttoria),{tipoControllo:'BRIDUACZ123', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACZ107',istruttoria),{tipoControllo:'BRIDUACZ107', esito:false});
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACZ126',istruttoria),{tipoControllo:'BRIDUACZ126', valString: 'TUTTI_SANZIONATI'});
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACZ126',istruttoria),{tipoControllo:'BRIDUACZ126', valString: 'CON_SANZIONI'});
    //CONTROLLI SUPERATI e SUCCESSIVI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACZ127',istruttoria),{tipoControllo:'BRIDUACZ127', esito:true});
    //NON AMMISSIBILE
    // TODO: BRIDUACZ107 + domanda integrativa non presentata (BRIDUACZ118_DomandaIntegrativa) - nessuno la setta. non esiste sul db
    ruleHelper.addRuleOr(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACZ107|BRIDUACZ118',istruttoria),{tipoControllo:'BRIDUACZ107', esito:false},{tipoControllo:'BRIDUACZ118', esito:false});
    //CONTROLLI DI LIQUIDABILITÀ’
    //CONTROLLI LIQUIDABILITÀ’ NON SUPERATI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL037',istruttoria),{tipoControllo:'BRIDUSDL037', esito:false});
    ruleHelper.addRuleAnd(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL038&BRIDUNVL129',istruttoria),{tipoControllo:'BRIDUSDL038', esito:true},{tipoControllo:'BRIDUNVL139', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL039',istruttoria),{tipoControllo:'BRIDUSDL039', esito:true});
    // 133
    //ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL133', istruttoria), {tipoControllo: 'BRIDUSDL133', esito: false})
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL135', istruttoria), {tipoControllo: 'BRIDUSDL135', esito: true});
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL135_IRR', istruttoria), {tipoControllo: 'BRIDUSDL135_IRR', esito: true});
    //DEBITI
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL134', istruttoria), {tipoControllo: 'BRIDUSDL134', esito: true});//stato debiti
    //CONTROLLI INTERSOSTEGNO
    //PAGAMENTO NON AUTORIZZATO & NON LIQUIDABILE
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDS040',istruttoria),{tipoControllo:'BRIDUSDS040', valString: 'NON_DETERMINABILE'});
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDS040',istruttoria),{tipoControllo:'BRIDUSDS040', valString: 'NON_RAGGIUNTO'});
    //PAGAMENTO NON AUTORIZZATO
    this.addRuleAntimafia(ruleHelper,istruttoria);
    istruttoria.ruleResult = ruleHelper.evaluateRules();
  }


  perSuperfici(istruttoria: Istruttorie) {

    const ruleHelper = new RuleHelper();
    //CONTROLLI NON SUPERATI & NON AMMISSIBILE
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC009',istruttoria),{tipoControllo:'BRIDUSDC009', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC010',istruttoria),{tipoControllo:'BRIDUSDC010', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDC034',istruttoria),{tipoControllo:'BRIDUSDC034', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACS083',istruttoria),{tipoControllo:'BRIDUACS083', esito:false});

    //CONTROLLI NON SUPERATI
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDC135', istruttoria), {tipoControllo:'BRIDUSDC135', esito: true});
    //CONTROLLI SUPERATI e SUCCESSIVI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACS091',istruttoria),{tipoControllo:'BRIDUACS091', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUACS092',istruttoria),{tipoControllo:'BRIDUACS092', esito:false});

    //CONTROLLI DI LIQUIDABILITÀ’
    //CONTROLLI LIQUIDABILITÀ’ NON SUPERATI
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL037',istruttoria),{tipoControllo:'BRIDUSDL037', esito:false});
    ruleHelper.addRuleAnd(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL038&BRIDUNVL129',istruttoria),{tipoControllo:'BRIDUSDL038', esito:true},{tipoControllo:'BRIDUNVL139', esito:false});
    ruleHelper.addRule(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDL039',istruttoria),{tipoControllo:'BRIDUSDL039', esito:true});

    //ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL133', istruttoria), {tipoControllo: 'BRIDUSDL133', esito: false})
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL135', istruttoria), {tipoControllo: 'BRIDUSDL135', esito: true});
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL135_IRR', istruttoria), {tipoControllo: 'BRIDUSDL135_IRR', esito: true});
    //DEBITI
    ruleHelper.addRule(istruttoria.esitiControlli, this.retrieveTranslation('BRIDUSDL134', istruttoria), {tipoControllo: 'BRIDUSDL134', esito: true});//stato debiti
    //CONTROLLI INTERSOSTEGNO
    //PAGAMENTO NON AUTORIZZATO & NON LIQUIDABILE
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDS040',istruttoria),{tipoControllo:'BRIDUSDS040', valString: 'NON_DETERMINABILE'});
    ruleHelper.addRuleCheckValString(istruttoria.esitiControlli,this.retrieveTranslation('BRIDUSDS040',istruttoria),{tipoControllo:'BRIDUSDS040', valString: 'NON_RAGGIUNTO'});
    //PAGAMENTO NON AUTORIZZATO
    this.addRuleAntimafia(ruleHelper,istruttoria);
    istruttoria.ruleResult = ruleHelper.evaluateRules().filter(rule => rule.isValid);
  }


}
