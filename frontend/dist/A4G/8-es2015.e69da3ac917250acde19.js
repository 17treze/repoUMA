(window.webpackJsonp=window.webpackJsonp||[]).push([[8],{"4SxZ":function(t,o,e){"use strict";e.d(o,"i",(function(){return i})),e.d(o,"h",(function(){return n})),e.d(o,"f",(function(){return a})),e.d(o,"e",(function(){return r})),e.d(o,"d",(function(){return l})),e.d(o,"b",(function(){return s})),e.d(o,"c",(function(){return I})),e.d(o,"a",(function(){return c})),e.d(o,"g",(function(){return O}));var i=function(t){return t.YES="YES",t.NO="NO",t}({}),n=function(t){return t.A_GIOVANE_AGRICOLTORE="A_GIOVANE_AGRICOLTORE",t.B_NUOVO_AGRICOLTORE="B_NUOVO_AGRICOLTORE",t.C_ABBANDONO_TERRE="C_ABBANDONO_TERRE",t.D_COMPENSAZIONE_SVANTAGGI_SPECIFICI="D_COMPENSAZIONE_SVANTAGGI_SPECIFICI",t.F_PROVVEDIMENTI_AMMINISTRATIVI_DECISIONI_GIUDIZIARIE="F_PROVVEDIMENTI_AMMINISTRATIVI_DECISIONI_GIUDIZIARIE",t.NON_RICHIESTA="NON_RICHIESTA",t}({}),a=function(t){return t.BPS_RIDUZIONI="BRIDUSDC020",t.BPS_SANZIONI_INF_10="BRIDUSDC021_INF_10",t.BPS_SANZIONI_SUP_10="BRIDUSDC021_SUP_10",t.GREENING_RIDUZIONI="BRIDUSDC026",t.GREENING_SANZIONI="BRIDUSDC055",t.GIOVANE_NO_REQUISITI="BRIDUSDC028",t.GIOVANE_RIDUZIONI="BRIDUSDC059",t.GIOVANE_SANZIONI="BRIDUSDC060",t.RIDUZIONE_DA_CAPPING="BRIDUSDC043",t.RIDUZIONE_PER_RITARDO="BRIDUSDC036",t}({}),r=function(t){return t.IMPORTO_DA_RECUPERARE="BRIDUSDL134",t.IMPORTO_NULLO="BRIDUSDL135",t.IMPORTO_IRRILEVANTE="BRIDUSDL135_IRR",t}({}),l=function(t){return t.BPS_SUPERFICE_MINIMA="BRIDUSDC035",t.BPS_PRESENZA_MAN="BRIDUSDC019",t.BPS_ANOMALIE_COORDINAMENTO="BRIDUSDC135",t.BPS_SCONTO_SANZIONE="BRIDUSDC032",t.BPS_RECIDIVA="BRIDUSDC029",t.BPS_RECUPERO_SCONTO="BRIDUSDC030",t.GREENING_RINUNCIA="BRIDUSDC023",t.GREENING_BIOLOGICA="BRIDUSDC024",t.GREENING_NO_IMPEGNI="BRIDUSDC025_NO_IMPEGNI",t.GREENING_DIV="BRIDUSDC025_DIV",t.GREENING_DIV_EFA="BRIDUSDC025_DIV_EFA",t.GREENING_ESENTE="BRIDUSDC033",t.GREENING_ESENTE_EFA="BRIDUSDC117",t.GIOVANE_SCONTO_SANZIONE="BRIDUSDC057",t.GIOVANE_RECIDIVA="BRIDUSDC056",t.GIOVANE_RECUPERO_SCONTO="BRIDUSDC058",t.RIDUZIONI_SENZA_SANZIONI="BRIDUSDC021",t}({}),s=function(t){return t.NO_INFO_AGRICOLTORE_ATTIVO="BRIDUSDC009",t.AGRICOLTORE_NON_ATTIVO="BRIDUSDC010",t.NESSUN_TITOLO_PRESENTE="BRIDUSDC011",t.SUPERFICIE_MINIMA_RICHIESTA="BRIDUSDC012",t.CONTROLLO_IN_LOCO_APERTO="BRIDUSDC034",t.INSERIRE_INFORMAZIONI_MANTENIMENTO="BRIDUSDC067",t.DOMANDA_ANNO_SCORSO_NON_LIQUIDATA="BRIDUSDC109",t.IMPORTO_DA_RECUPERARE="BRIDUSDC031",t}({}),I=function(t){return t.IBAN_NON_VALIDO="BRIDUSDL037",t.AGRICOLTORE_DECEDUTO="BRIDUSDL038",t.AZIENDA_SOSPESA_DAI_PAGAMENTI="BRIDUSDL039",t.DATI_EREDE_NON_CERTIFICATI="BRIDUNVL129",t}({}),c=function(t){return t.NO_IMPORTO_MINIMO_ERROR="BRIDUSDS040",t.IMPORTO_MAGGIORE_DI_25000_ERROR="BRIDUSDS049",t.IMPORTO_MINORE_DI_25000_INFO="BRIDUSDS049",t.NO_DICHIARAZIONE_ANTIMAFIA_INFO="BRIDUSDS050",t}({});class O{}},AodI:function(t,o,e){"use strict";e.d(o,"a",(function(){return f}));var i=e("fXoL"),n=e("3Pt+"),a=e("XNiG"),r=e("6b9K"),l=e("ngg1"),s=e("RIRQ"),I=(e("ovm2"),e("4SxZ")),c=e("+NW7"),O=e("Q/4e"),m=e("tyNb"),N=e("sYmb"),u=e("3Kf3"),A=e("GLSp"),S=e("1SSY"),C=e("ofXK"),R=e("A1Yd"),_=e("T8KO");function E(t,o){if(1&t&&(i.Tb(0,"div",2),i.Tb(1,"label",7),i.Oc(2,"Domande bloccate"),i.Sb(),i.Ob(3,"p-dropdown",16),i.Sb()),2&t){const t=i.fc();i.Bb(3),i.mc("options",t.yesNo)}}function d(t,o){if(1&t&&(i.Tb(0,"div",2),i.Tb(1,"label",7),i.Oc(2,"Anomalie"),i.Sb(),i.Ob(3,"p-dropdown",17),i.Sb()),2&t){const t=i.fc();i.Bb(3),i.mc("options",t.yesNo)}}function b(t,o){if(1&t&&(i.Tb(0,"div",2),i.Tb(1,"label",7),i.Oc(2,"Anomalie Warning"),i.Sb(),i.Ob(3,"p-multiSelect",18),i.Sb()),2&t){const t=i.fc();i.Bb(3),i.mc("options",t.getAnomalieWarningControlliOption())("filter",!1)}}function p(t,o){if(1&t&&(i.Tb(0,"div",2),i.Tb(1,"label",7),i.Oc(2,"Anomalie Info"),i.Sb(),i.Ob(3,"p-multiSelect",19),i.Sb()),2&t){const t=i.fc();i.Bb(3),i.mc("filter",!1)("options",t.anomalieInfo)}}function D(t,o){if(1&t&&(i.Tb(0,"div",2),i.Tb(1,"label",7),i.Oc(2,"Anomalie Error"),i.Sb(),i.Ob(3,"p-multiSelect",20),i.Sb()),2&t){const t=i.fc();i.Bb(3),i.mc("filter",!1)("options",t.getAnomalieErrorControlliOptions())}}function h(t,o){if(1&t&&(i.Tb(0,"div",2),i.Tb(1,"label",7),i.Oc(2,"Anomalie Intersostegno"),i.Sb(),i.Ob(3,"p-multiSelect",21),i.Sb()),2&t){const t=i.fc();i.Bb(3),i.mc("filter",!1)("options",t.anomalieInterSostegno)}}function T(t,o){if(1&t&&(i.Tb(0,"div",2),i.Tb(1,"label",7),i.Oc(2,"Errori di Calcolo"),i.Sb(),i.Ob(3,"p-dropdown",22),i.Sb()),2&t){const t=i.fc();i.Bb(3),i.mc("options",t.yesNo)}}function g(t,o){if(1&t&&(i.Tb(0,"div",2),i.Tb(1,"label",7),i.Oc(2,"Integrazione"),i.Sb(),i.Ob(3,"p-dropdown",23),i.Sb()),2&t){const t=i.fc();i.Bb(3),i.mc("options",t.yesNoIntegrazione)}}let f=(()=>{class t{constructor(t,o,e){this.route=t,this.elencoDomandeService=o,this.translateService=e,this.selectedFiltersSubmit=new i.n,this.componentDestroyed$=new a.a,this.anomalieWarn=[{title:"BPS: Riduzioni",id:I.f.BPS_RIDUZIONI},{title:"BPS: Sanzioni <10%",id:I.f.BPS_SANZIONI_INF_10},{title:"BPS: Sanzioni >10%",id:I.f.BPS_SANZIONI_SUP_10},{title:"Greening: Riduzioni",id:I.f.GREENING_RIDUZIONI},{title:"Greening: Sanzioni",id:I.f.GREENING_SANZIONI},{title:"Giovane: No requisiti",id:I.f.GIOVANE_NO_REQUISITI},{title:"Giovane: Riduzioni",id:I.f.GIOVANE_RIDUZIONI},{title:"Giovane: Sanzioni",id:I.f.GIOVANE_SANZIONI},{title:"Riduzione da capping",id:I.f.RIDUZIONE_DA_CAPPING},{title:"Riduzione per ritardo",id:I.f.RIDUZIONE_PER_RITARDO}],this.anomalieWarnDebiti=[{title:"Importo da recuperare",id:I.e.IMPORTO_DA_RECUPERARE},{title:"Importo nullo",id:I.e.IMPORTO_NULLO},{title:"Importo irrilevante",id:I.e.IMPORTO_IRRILEVANTE}],this.anomalieInfo=[{title:"BPS: Anomalie coordinamento",id:I.d.BPS_ANOMALIE_COORDINAMENTO},{title:"BPS: Presenza MAN",id:I.d.BPS_PRESENZA_MAN},{title:"BPS: Superficie minima",id:I.d.BPS_SUPERFICE_MINIMA},{title:"BPS: sconto sanzione",id:I.d.BPS_SCONTO_SANZIONE},{title:"BPS: recidiva",id:I.d.BPS_RECIDIVA},{title:"BPS: recupero sconto",id:I.d.BPS_RECUPERO_SCONTO},{title:"Greening: rinuncia",id:I.d.GREENING_RINUNCIA},{title:"Greening: biologica",id:I.d.GREENING_BIOLOGICA},{title:"Greening: No impegni",id:I.d.GREENING_NO_IMPEGNI},{title:"Greening: DIV",id:I.d.GREENING_DIV},{title:"Greening: DIV+EFA",id:I.d.GREENING_DIV_EFA},{title:"Greening: esente",id:I.d.GREENING_ESENTE},{title:"Greening: esente EFA",id:I.d.GREENING_ESENTE_EFA},{title:"Giovane: sconto sanzione",id:I.d.GIOVANE_SCONTO_SANZIONE},{title:"Giovane: recidiva",id:I.d.GIOVANE_RECIDIVA},{title:"Giovane: recupero sconto",id:I.d.GIOVANE_RECUPERO_SCONTO},{title:"Riduzioni senza sanzioni",id:I.d.RIDUZIONI_SENZA_SANZIONI}],this.anomalieErrorControlliCalcoloKoNonAmmissibile=[{title:"No info agricoltore attivo",id:I.b.NO_INFO_AGRICOLTORE_ATTIVO},{title:"Agricoltore non attivo",id:I.b.AGRICOLTORE_NON_ATTIVO},{title:"Nessun titolo presente",id:I.b.NESSUN_TITOLO_PRESENTE},{title:"Superficie minima richiesta",id:I.b.SUPERFICIE_MINIMA_RICHIESTA},{title:"Controllo in loco aperto",id:I.b.CONTROLLO_IN_LOCO_APERTO},{title:"Inserire informazioni mantenimento",id:I.b.INSERIRE_INFORMAZIONI_MANTENIMENTO},{title:"Domanda anno scorso non liquidata",id:I.b.DOMANDA_ANNO_SCORSO_NON_LIQUIDATA},{title:"Importo da recuperare",id:I.b.IMPORTO_DA_RECUPERARE}],this.anomalieErrorControlliLiquidabileKo=[{title:"IBAN non valido",id:I.c.IBAN_NON_VALIDO},{title:"Agricoltore deceduto",id:I.c.AGRICOLTORE_DECEDUTO},{title:"Azienda sospesa dai pagamenti",id:I.c.AZIENDA_SOSPESA_DAI_PAGAMENTI},{title:"Dati erede non certificati",id:I.c.DATI_EREDE_NON_CERTIFICATI}],this.anomalieWarningDebiti=[{title:"Importo da recuperare",id:I.e.IMPORTO_DA_RECUPERARE},{title:"Importo nullo",id:I.e.IMPORTO_NULLO},{title:"Importo Irrilevante",id:I.e.IMPORTO_IRRILEVANTE}],this.anomalieInterSostegno=[{title:"No Importo minimo",id:I.a.NO_IMPORTO_MINIMO_ERROR,livello:"ERROR"},{title:"Importo maggiore di 25000",id:I.a.IMPORTO_MAGGIORE_DI_25000_ERROR,livello:"ERROR"},{title:"Importo minore di 25000",id:I.a.IMPORTO_MINORE_DI_25000_INFO,livello:"INFO"},{title:"No dichiarazione antimafia",id:I.a.NO_DICHIARAZIONE_ANTIMAFIA_INFO,livello:"INFO"}],this.yesNo=[{label:this.translateService.instant("DOMANDA_UNICA_FILTRI.all"),value:null},{label:this.translateService.instant("DOMANDA_UNICA_FILTRI.yes"),value:I.i.YES},{label:this.translateService.instant("DOMANDA_UNICA_FILTRI.no"),value:I.i.NO}],this.yesNoIntegrazione=[{label:this.translateService.instant("DOMANDA_UNICA_FILTRI.all"),value:null},{label:this.translateService.instant("DOMANDA_UNICA_FILTRI.integrazioneYes"),value:I.i.YES},{label:this.translateService.instant("DOMANDA_UNICA_FILTRI.integrazioneNo"),value:I.i.NO}],this.riservaLabel=[{label:this.translateService.instant("DOMANDA_UNICA_FILTRI.all"),value:null},{label:"Fattispecie A",value:I.h.A_GIOVANE_AGRICOLTORE},{label:"Fattispecie B",value:I.h.B_NUOVO_AGRICOLTORE},{label:"Fattispecie C",value:I.h.C_ABBANDONO_TERRE},{label:"Fattispecie D",value:I.h.D_COMPENSAZIONE_SVANTAGGI_SPECIFICI},{label:"Fattispecie F",value:I.h.F_PROVVEDIMENTI_AMMINISTRATIVI_DECISIONI_GIUDIZIARIE},{label:this.translateService.instant("DOMANDA_UNICA_FILTRI.no"),value:I.h.NON_RICHIESTA}];let r={cuaaFormControl:new n.g,denominazioneFormControl:new n.g,numeroDomandaFormControl:new n.g,selectedCampioneFormControl:new n.g(null),selectedGiovaneFormControl:new n.g(null),selectedPascoliFormControl:new n.g(null),selectedBloccataBoolFormControl:new n.g(null),selectedAnomalieFormControl:new n.g(null),selectedRiservaFormControl:new n.g(null),selectedAnomalieWarnFormControl:new n.g,selectedAnomalieInfoFormControl:new n.g,selectedAnomalieErrorFormControl:new n.g,selectedAnomalieInterSostegnoFormControl:new n.g([]),selectedErroreCalcoloFormControl:new n.g(null),selectedIntegrazioneFormControl:new n.g(null)};this.filtersFormGroup=new n.j(r)}isShowAnomalieErrorControlliCalcoloKoNonAmmissibile(){return this.autoCompleteParams.statoSostegno===s.a.CONTROLLI_CALCOLO_KO||this.autoCompleteParams.statoSostegno===s.a.NON_AMMISSIBILE}isShowAnomalieInterSostegno(){return this.autoCompleteParams.statoSostegno===s.a.PAGAMENTO_NON_AUTORIZZATO}isShowAnomalieErrorControlliLiquidabileKo(){return this.autoCompleteParams.statoSostegno===s.a.CONTROLLI_LIQUIDABILE_KO}isShowAnomalieWarningDebiti(){return this.autoCompleteParams.statoSostegno===s.a.DEBITI}getAnomalieErrorControlliOptions(){return this.isShowAnomalieErrorControlliCalcoloKoNonAmmissibile()?this.anomalieErrorControlliCalcoloKoNonAmmissibile:this.isShowAnomalieErrorControlliLiquidabileKo()?this.anomalieErrorControlliLiquidabileKo:void 0}getAnomalieWarningControlliOption(){return this.isShowAnomalieWarningDebiti()?this.anomalieWarnDebiti:this.anomalieWarn}isShowAnomalieErrorControlli(){return this.isShowAnomalieErrorControlliCalcoloKoNonAmmissibile()||this.isShowAnomalieErrorControlliLiquidabileKo()}isShowAnomalieWarningControlli(){return this.isShowControlliCalcolo()||this.isShowAnomalieWarningDebiti()}isShowControlliCalcolo(){return this.autoCompleteParams.statoSostegno===s.a.CONTROLLI_CALCOLO_OK}isShowBloccato(){return O.a.isShowBloccatoByState(s.a[this.autoCompleteParams.statoSostegno])}isShowErroriDiCalcolo(){return O.a.isShowErroriByState(s.a[this.autoCompleteParams.statoSostegno])}isShowIntegrazione(){return this.autoCompleteParams.statoSostegno===s.a.PAGAMENTO_AUTORIZZATO&&this.autoCompleteParams.tipo===l.a.SALDO}ngOnInit(){this.autoCompleteParams.statoSostegno=this.route.routeConfig.path}onSubmit(){let t=new I.g;t.cuaa=this.filtersFormGroup.controls.cuaaFormControl.value;let o=this.filtersFormGroup.controls.numeroDomandaFormControl.value;if(t.numero_domanda=o?Number.parseInt(o):null,t.denominazione=this.filtersFormGroup.controls.denominazioneFormControl.value,t.campione=this.filtersFormGroup.controls.selectedCampioneFormControl.value,t.giovane=this.filtersFormGroup.controls.selectedGiovaneFormControl.value,t.pascoli=this.filtersFormGroup.controls.selectedPascoliFormControl.value,t.bloccataBool=this.filtersFormGroup.controls.selectedBloccataBoolFormControl.value,t.erroreCalcolo=this.filtersFormGroup.controls.selectedErroreCalcoloFormControl.value,t.anomalie=this.filtersFormGroup.controls.selectedAnomalieFormControl.value,t.integrazione=this.filtersFormGroup.controls.selectedIntegrazioneFormControl.value,this.autoCompleteParams.statoSostegno===s.a.PAGAMENTO_NON_AUTORIZZATO)t.anomalieWARNING=[],t.anomalieINFO=[],t.anomalieERROR=[],this.filtersFormGroup.controls.selectedAnomalieInterSostegnoFormControl.value.map(o=>("ERROR"===o.livello&&t.anomalieERROR.push(o.id.toString()),"INFO"===o.livello&&t.anomalieINFO.push(o.id.toString()),o));else{let o=this.filtersFormGroup.controls.selectedAnomalieWarnFormControl.value,e=this.filtersFormGroup.controls.selectedAnomalieInfoFormControl.value,i=this.filtersFormGroup.controls.selectedAnomalieErrorFormControl.value;t.anomalieWARNING=o?o.map(t=>t.id):[],t.anomalieINFO=e?e.map(t=>t.id):[],t.anomalieERROR=i?i.map(t=>t.id):[]}t.riservaNazionale=this.filtersFormGroup.controls.selectedRiservaFormControl.value,this.selectedFiltersSubmit.emit(t)}inputFilterCuaaUpdate(t){let o=r.a.of(0,50,"id",r.b.ASC);this.elencoDomandeService.getCuaaSuggestionsAutocomplete(this.autoCompleteParams.statoSostegno,this.autoCompleteParams.sostegno,this.autoCompleteParams.annoCampagna,t.query,this.autoCompleteParams.tipo,o).subscribe(t=>this.cuaaS=t.risultati)}inputFilterRagioneSocialeUpdate(t){let o=r.a.of(0,50,"id",r.b.ASC);this.elencoDomandeService.getRagioneSocialeSuggestionsAutocomplete(this.autoCompleteParams.statoSostegno,this.autoCompleteParams.sostegno,this.autoCompleteParams.annoCampagna,t.query,this.autoCompleteParams.tipo,o).subscribe(t=>this.denominazioneS=t.risultati)}ngOnDestroy(){this.componentDestroyed$.next(!0),this.componentDestroyed$.complete()}}return t.\u0275fac=function(o){return new(o||t)(i.Nb(m.a),i.Nb(c.a),i.Nb(N.e))},t.\u0275cmp=i.Hb({type:t,selectors:[["app-filtri-disaccoppiato"]],inputs:{autoCompleteParams:"autoCompleteParams",resultsNumber:"resultsNumber"},outputs:{selectedFiltersSubmit:"selectedFiltersSubmit"},decls:48,vars:26,consts:[[2,"padding-right","40px"],[3,"formGroup","ngSubmit"],[1,"form-group"],["formControlName","cuaaFormControl","minLength","3","placeholder","CUAA","name","cuaa",3,"suggestions","completeMethod"],["formControlName","denominazioneFormControl","minLength","3","placeholder","Descrizione Impresa","name","denominazione",3,"suggestions","completeMethod"],["placeholder","Numero Domanda","type","number","minLength","3","formControlName","numeroDomandaFormControl","name","numero_domanda","pInputText",""],["denominazioneInput",""],[1,"labelricerca"],["formControlName","selectedCampioneFormControl","name","selectedCampione",3,"options"],["formControlName","selectedGiovaneFormControl","name","selectedGiovane",3,"options"],["formControlName","selectedPascoliFormControl","name","selectedPascoli",3,"options"],["formControlName","selectedRiservaFormControl","name","selectedRiserva",3,"options"],["class","form-group",4,"ngIf"],[1,"colori-per-numeri"],[1,"searchbutton"],["pButton","","type","button","type","submit",1,"ui-button-success",2,"background","#0e5f3f",3,"disabled"],["formControlName","selectedBloccataBoolFormControl","name","selectedBloccataBool",3,"options"],["formControlName","selectedAnomalieFormControl","name","selectedAnomalie",3,"options"],["formControlName","selectedAnomalieWarnFormControl","name","selectedAnomalieWARN","multiple","multiple","checkbox","checkbox","optionLabel","title","defaultLabel","Seleziona anomalia/e",3,"options","filter"],["formControlName","selectedAnomalieInfoFormControl","name","selectedAnomalieInfo","multiple","multiple","checkbox","checkbox","optionLabel","title","defaultLabel","Seleziona anomalia/e",3,"filter","options"],["formControlName","selectedAnomalieErrorFormControl","name","selectedAnomalieError","multiple","multiple","checkbox","checkbox","optionLabel","title","defaultLabel","Seleziona anomalia/e",3,"filter","options"],["formControlName","selectedAnomalieInterSostegnoFormControl","name","selectedAnomalieInterSostegno","multiple","multiple","checkbox","checkbox","optionLabel","title","defaultLabel","Seleziona anomalia/e",3,"filter","options"],["formControlName","selectedErroreCalcoloFormControl","name","selectedErroreCalcolo",3,"options"],["formControlName","selectedIntegrazioneFormControl","name","selectedIntegrazione",3,"options"]],template:function(t,o){1&t&&(i.Tb(0,"div",0),i.Tb(1,"h3"),i.Oc(2),i.gc(3,"translate"),i.Sb(),i.Tb(4,"div"),i.Tb(5,"form",1),i.bc("ngSubmit",(function(){return o.onSubmit()})),i.Tb(6,"div",2),i.Tb(7,"p-autoComplete",3),i.bc("completeMethod",(function(t){return o.inputFilterCuaaUpdate(t)})),i.Sb(),i.Sb(),i.Tb(8,"div",2),i.Tb(9,"p-autoComplete",4),i.bc("completeMethod",(function(t){return o.inputFilterRagioneSocialeUpdate(t)})),i.Sb(),i.Sb(),i.Tb(10,"div",2),i.Ob(11,"input",5,6),i.Sb(),i.Tb(13,"div",2),i.Tb(14,"label",7),i.Oc(15),i.gc(16,"translate"),i.Sb(),i.Ob(17,"p-dropdown",8),i.Sb(),i.Tb(18,"div",2),i.Tb(19,"label",7),i.Oc(20,"Giovane"),i.Sb(),i.Ob(21,"p-dropdown",9),i.Sb(),i.Tb(22,"div",2),i.Tb(23,"label",7),i.Oc(24,"Pascoli"),i.Sb(),i.Ob(25,"p-dropdown",10),i.Sb(),i.Tb(26,"div",2),i.Tb(27,"label",7),i.Oc(28,"Riserva Nazionale"),i.Sb(),i.Ob(29,"p-dropdown",11),i.Sb(),i.Mc(30,E,4,1,"div",12),i.Mc(31,d,4,1,"div",12),i.Mc(32,b,4,2,"div",12),i.Mc(33,p,4,2,"div",12),i.Mc(34,D,4,2,"div",12),i.Mc(35,h,4,2,"div",12),i.Mc(36,T,4,1,"div",12),i.Mc(37,g,4,1,"div",12),i.Tb(38,"div",2),i.Tb(39,"label",7),i.Oc(40,"Risultati: "),i.Tb(41,"span",13),i.Oc(42),i.Sb(),i.Sb(),i.Sb(),i.Tb(43,"div"),i.Tb(44,"div",14),i.Tb(45,"button",15),i.Oc(46),i.gc(47,"translate"),i.Sb(),i.Sb(),i.Sb(),i.Sb(),i.Sb(),i.Sb()),2&t&&(i.Bb(2),i.Qc(" ",i.hc(3,20,"DOMANDA_UNICA_FILTRI.filtraPer")," "),i.Bb(3),i.mc("formGroup",o.filtersFormGroup),i.Bb(2),i.mc("suggestions",o.cuaaS),i.Bb(2),i.mc("suggestions",o.denominazioneS),i.Bb(6),i.Pc(i.hc(16,22,"DOMANDA_UNICA_FILTRI.campione")),i.Bb(2),i.mc("options",o.yesNo),i.Bb(4),i.mc("options",o.yesNo),i.Bb(4),i.mc("options",o.yesNo),i.Bb(4),i.mc("options",o.riservaLabel),i.Bb(1),i.mc("ngIf",o.isShowBloccato()),i.Bb(1),i.mc("ngIf",o.isShowControlliCalcolo()),i.Bb(1),i.mc("ngIf",o.isShowAnomalieWarningControlli()),i.Bb(1),i.mc("ngIf",o.isShowControlliCalcolo()),i.Bb(1),i.mc("ngIf",o.isShowAnomalieErrorControlli()),i.Bb(1),i.mc("ngIf",o.isShowAnomalieInterSostegno()),i.Bb(1),i.mc("ngIf",o.isShowErroriDiCalcolo()),i.Bb(1),i.mc("ngIf",o.isShowIntegrazione()),i.Bb(5),i.Qc(" ",o.resultsNumber," "),i.Bb(3),i.mc("disabled",!o.filtersFormGroup.valid),i.Bb(1),i.Pc(i.hc(47,24,"DOMANDA_UNICA_FILTRI.applyFilters")))},directives:[n.I,n.t,n.k,u.a,n.s,n.i,n.y,n.c,A.a,S.a,C.t,R.b,_.a],pipes:[N.d],styles:[".colori-per-numeri[_ngcontent-%COMP%]{background:#00985a;line-height:0;color:#fff;font-size:13px;border-radius:30px;display:inline;padding:0 7px}"]}),t})()},LOZX:function(t,o,e){"use strict";e.d(o,"a",(function(){return A}));var i=e("XNiG"),n=e("Cfvw"),a=e("1G5W"),r=e("pLZG"),l=e("lJxs"),s=e("5+tZ"),I=e("tyNb"),c=e("903C"),O=e("fXoL"),m=e("iU1L"),N=e("ngg1"),u=e("MvQ6");let A=(()=>{class t{constructor(t,o,e,n,l){this.sostegno=t,this.tipoIstruttoriaEnum=o,this.activatedRoute=e,this.istruttoriaService=n,this.router=l,this.timeout=3e4,this.itemBadgesCount={},this.componentDestroyed$=new i.a,this.statoIstruttoria=void 0,this.router.events.pipe(Object(a.a)(this.componentDestroyed$),Object(r.a)(t=>t instanceof I.b)).subscribe(t=>{this.activeTab=this.getActiveTabMenuItem(t.url)})}changeTab(t){this.activeTab=this.menu2[t.index],this.router.navigate([this.menu2[t.index].id],{relativeTo:this.activatedRoute}),this.aggiornaContatori()}aggiornaContatori(){Object(n.a)(this.getListaStati()).pipe(Object(l.a)(t=>{let o=new c.a;return o.campagna=Number(this.activatedRoute.snapshot.paramMap.get("annoCampagna")),o.sostegno=this.sostegno,o.tipo=this.tipoIstruttoriaEnum,o.stato=t,o}),Object(s.a)(t=>this.istruttoriaService.countIstruttorieDU(t).pipe(Object(l.a)(o=>{this.itemBadgesCount[t.stato]=o})))).subscribe()}getChildUrlSegment(t){let o=this.router.parseUrl(t).root.children.primary.segments[6];return o?o.path:""}getActiveTabMenuItem(t){let o=this.getChildUrlSegment(t);return o.length>0?this.menu2.find(t=>t.id===o):this.menu2[0]}getListaStati(){return this.menu2.map(t=>t.id)}ngOnInit(){this.datiIstruttoriaCorrente=this.activatedRoute.snapshot.data.istruttoria,this.aggiornaContatori()}ngOnDestroy(){this.componentDestroyed$.next(!0),this.componentDestroyed$.complete(),this.processoDiControllo&&this.processoDiControllo.unsubscribe()}}return t.\u0275fac=function(o){return new(o||t)(O.Nb(m.a),O.Nb(N.a),O.Nb(I.a),O.Nb(u.a),O.Nb(I.e))},t.\u0275dir=O.Ib({type:t}),t})()},"Q/4e":function(t,o,e){"use strict";e.d(o,"a",(function(){return S}));var i=e("8k9x"),n=e("5gIc"),a=e("u+P9"),r=e("iU1L"),l=e("ngg1"),s=e("RIRQ"),I=e("MvQ6"),c=e("+NW7"),O=e("VrO5"),m=e("KMZK"),N=e("fXoL"),u=e("tyNb"),A=e("RtSl");let S=(()=>{class t extends m.a{constructor(o,e,i,n,a,l,s,I,c,O){super(o,e,i,n,a,l,s,c,r.a.DISACCOPPIATO,t.getTipoIstruttoriaFromString(o.snapshot.paramMap.get("tipo")),I,O),this.items=this.menuActionPagamentoAutorizzato()}getVerbale(t){this.overrideGetVerbale(t)}ngOnInit(){super.ngOnInit()}ngOnDestroy(){super.ngOnDestroy()}static getTipoIstruttoriaFromString(t){return"saldo"===t?l.a.SALDO:"anticipo"===t?l.a.ANTICIPO:"integrazione"===t?l.a.INTEGRAZIONE:void 0}isEnableCalcoloDisaccoppiato(){return m.a.anticipiAttivi(this.tipoIstruttoria,this.annoCampagna)&&this.flowIstruttoria===a.a.CALCOLO_PREMIO&&this.statoIstruttoria!==s.a.NON_AMMISSIBILE}isEnableControlliLiquidabilita(){return m.a.anticipiAttivi(this.tipoIstruttoria,this.annoCampagna)&&this.flowIstruttoria===a.a.CONTROLLI_LIQUIDABILITA}isEnableProcessoAmmissibilita(){return this.isEnableCalcoloDisaccoppiato()&&this.statoIstruttoria===s.a.CONTROLLI_CALCOLO_KO}avviaProcessoCalcoloDisaccoppiato(){this.avviaCalcolo(i.a.CALCOLO_DISACCOPPIATO_ISTRUTTORIA)}isShowDettagli(){return!this.isShowAzioni()}isShowAzioni(){return this.statoIstruttoria===s.a.NON_AMMISSIBILE||this.statoIstruttoria===s.a.NON_LIQUIDABILE||this.statoIstruttoria===s.a.PAGAMENTO_AUTORIZZATO||this.statoIstruttoria===s.a.DEBITI}isEnableBloccoIstruttoria(){return m.a.anticipiAttivi(this.tipoIstruttoria,this.annoCampagna)&&this.sostegno===r.a.DISACCOPPIATO&&this.statoIstruttoria!==s.a.NON_LIQUIDABILE&&this.statoIstruttoria!==s.a.NON_AMMISSIBILE&&this.statoIstruttoria!==s.a.PAGAMENTO_AUTORIZZATO&&this.statoIstruttoria!==s.a.DEBITI}static isShowBloccatoByState(t){return t===s.a.RICHIESTO||t===s.a.CONTROLLI_CALCOLO_OK||t===s.a.CONTROLLI_CALCOLO_KO||t===s.a.LIQUIDABILE||t===s.a.PAGAMENTO_NON_AUTORIZZATO||t===s.a.CONTROLLI_LIQUIDABILE_KO||t===s.a.CONTROLLI_INTERSOSTEGNO_OK}static isShowErroriByState(o){return t.isShowBloccatoByState(o)}isShowBloccato(){return t.isShowBloccatoByState(this.statoIstruttoria)}isShowErroriCalcolo(){return t.isShowErroriByState(this.statoIstruttoria)}onFiltersSubmit(t){this.initDefaultFilterValuesIstruttorie(),this.setFilterValuesIstruttorie(t),this.getElencoIstruttoria(this.istruttoriaDomandaUnicaFilter),this.tabella.pTable.reset()}isEnableResetIstruttoria(){return m.a.anticipiAttivi(this.tipoIstruttoria,this.annoCampagna)&&(this.statoIstruttoria===s.a.NON_AMMISSIBILE||this.statoIstruttoria===s.a.LIQUIDABILE||this.statoIstruttoria===s.a.CONTROLLI_LIQUIDABILE_KO||this.statoIstruttoria===s.a.NON_LIQUIDABILE||this.statoIstruttoria===s.a.PAGAMENTO_NON_AUTORIZZATO||this.statoIstruttoria===s.a.DEBITI)}resetIstruttoria(){this.eseguiProcessoIstruttoria(i.a.RESET_ISTRUTTORIA,[i.a.NON_AMMISSIBILITA,i.a.LIQUIDAZIONE,i.a.CALCOLO_DISACCOPPIATO_ISTRUTTORIA,i.a.SBLOCCO_ISTRUTTORIE,i.a.BLOCCO_ISTRUTTORIE,i.a.CONTROLLO_LIQUIDABILITA_ISTRUTTORIA,i.a.INTEGRAZIONE_ISTRUTTORIE,i.a.RESET_ISTRUTTORIA])}}return t.\u0275fac=function(o){return new(o||t)(N.Nb(u.a),N.Nb(I.a),N.Nb(c.a),N.Nb(u.e),N.Nb(A.d),N.Nb(n.a),N.Nb(s.a),N.Nb(N.h),N.Nb(O.a),N.Nb(a.a))},t.\u0275dir=N.Ib({type:t,features:[N.yb]}),t})()}}]);