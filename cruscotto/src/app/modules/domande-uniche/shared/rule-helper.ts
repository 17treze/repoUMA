import { IsFalse, IsTrue, RulePolicy, RuleResult, Severity, ValidationContext } from '@angularlicious/rules-engine';
import { EsitoControlli } from '../models/dettaglio-istruttoria';

export class RuleHelper {

  validationContext: ValidationContext = new ValidationContext();

  addRule(listaAnomalie: EsitoControlli[], tranlsateMessage: string, bridu: EsitoControlli){
    const anomalia = listaAnomalie.find(x => this.getCodAnomalia(x.tipoControllo) === bridu.tipoControllo && this.isPresentTranslateMessage(tranlsateMessage));
    if ( anomalia ) {
      if (bridu.esito) {
        const rul = new IsTrue(bridu.tipoControllo, tranlsateMessage, anomalia.esito, true);
        rul.severity =  this.retrieveSeverity(anomalia.livelloControllo);
        this.validationContext.addRule(rul);
      } else {
        const rul = new IsFalse(bridu.tipoControllo, tranlsateMessage, anomalia.esito, true);
        rul.severity =  this.retrieveSeverity(anomalia.livelloControllo);
        this.validationContext.addRule(rul);
      }
    }
  }

  addRuleCheckValString(listaAnomalie: EsitoControlli[], tranlsateMessage: string, bridu: EsitoControlli){
    const anomalia = listaAnomalie.find(x => this.getCodAnomalia(x.tipoControllo) === bridu.tipoControllo && this.isPresentTranslateMessage(tranlsateMessage));
    if ( anomalia ) {
      const rul = new IsTrue(bridu.tipoControllo, tranlsateMessage, anomalia.valString === bridu.valString , true);
      rul.severity =  this.retrieveSeverity(anomalia.livelloControllo);
      this.validationContext.addRule(rul);
    }
  }

  addRuleAnd(listaAnomalie: EsitoControlli[], tranlsateMessage: string, bridu1: EsitoControlli, bridu2: EsitoControlli){
    const an1=listaAnomalie.find(x => this.getCodAnomalia(x.tipoControllo) === bridu1.tipoControllo && this.isPresentTranslateMessage(tranlsateMessage));
    const an2=listaAnomalie.find(x => this.getCodAnomalia(x.tipoControllo) === bridu2.tipoControllo && this.isPresentTranslateMessage(tranlsateMessage));
    if ( an1 && an2) {
        const rul = new IsTrue(tranlsateMessage, tranlsateMessage, (bridu1.esito === an1.esito) && (bridu2.esito === an2.esito), true);
        rul.severity =  this.retrieveSeverity(an1.livelloControllo);
        this.validationContext.addRule(rul);
    }
  }

  addRuleOr(listaAnomalie: EsitoControlli[], tranlsateMessage: string, bridu1: EsitoControlli, bridu2: EsitoControlli){
    const an1=listaAnomalie.find(x => this.getCodAnomalia(x.tipoControllo) === bridu1.tipoControllo && this.isPresentTranslateMessage(tranlsateMessage));
    const an2=listaAnomalie.find(x => this.getCodAnomalia(x.tipoControllo) === bridu2.tipoControllo && this.isPresentTranslateMessage(tranlsateMessage));
    if ( an1 && an2) {
        const rul = new IsTrue(tranlsateMessage, tranlsateMessage, (bridu1.esito === an1.esito) || (bridu2.esito === an2.esito), true);
        rul.severity =  this.retrieveSeverity(an1.livelloControllo);
        this.validationContext.addRule(rul);
    }
  }

  isPresentTranslateMessage(tranlsateMessage: string) {
      return tranlsateMessage ? true : false;
  }

  retrieveSeverity(livelloControllo: string){
    switch (livelloControllo) {
      case  "WARNING" : return Severity.Warning;
      case  "ERROR"   : return Severity.Exception;
      default         : return Severity.Information;
    }
  }

  addCustomRule(rule: RulePolicy){
    if (rule)
        this.validationContext.addRule(rule);
  }

  //attualmente il codice anomalia si prensenta in questo modo: BRIDUSDC009_infoAgricoltoreAttivo
  //il metodo estrapola solo la prima parte
  getCodAnomalia(tipoControllo: string): string {
    return tipoControllo.split('_')[0];
  }

  evaluateRules(): RuleResult[] {
    this.validationContext.renderRules();
    console.log(this.validationContext.results);
    return this.validationContext.results.filter(rule => rule.isValid).sort((a,b) => {
      if (a.rulePolicy.severity === b.rulePolicy.severity) return 0;
      if (a.rulePolicy.severity < b.rulePolicy.severity) return -1;
      if (a.rulePolicy.severity > b.rulePolicy.severity) return 1;
    });
  }

}
