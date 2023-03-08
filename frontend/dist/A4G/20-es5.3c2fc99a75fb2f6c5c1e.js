function _classCallCheck(e,a){if(!(e instanceof a))throw new TypeError("Cannot call a class as a function")}function _defineProperties(e,a){for(var i=0;i<a.length;i++){var t=a[i];t.enumerable=t.enumerable||!1,t.configurable=!0,"value"in t&&(t.writable=!0),Object.defineProperty(e,t.key,t)}}function _createClass(e,a,i){return a&&_defineProperties(e.prototype,a),i&&_defineProperties(e,i),e}(window.webpackJsonp=window.webpackJsonp||[]).push([[20],{SJd6:function(e,a,i){"use strict";i.r(a),i.d(a,"FascicoliDaMigrareModule",(function(){return L}));var t,n=i("im2e"),o=i("R69K"),c=i("ofXK"),r=i("HosL"),s=i("nU6X"),l=i("GtbX"),b=(i("gCMS"),i("69Tm")),u=i("Tn7N"),p=i("fXoL"),m=i("tyNb"),d=i("RtSl"),f=i("sYmb"),g=i("arS9"),h=i("zLRh"),v=i("3Pt+"),S=i("QqKj"),C=i("GLSp"),T=i("Ivbe"),O=i("K7SP"),y=i("A1Yd"),F=function(e){return{"is-invalid":e}},_=((t=function(){function e(a,i){_classCallCheck(this,e),this.fascicoloService=a,this.messageService=i,this.search=new p.n}return _createClass(e,[{key:"ngOnInit",value:function(){this.filtersFormGroup=new v.j({cuaa:new v.g(null),denominazione:new v.g(null)})}},{key:"onSubmit",value:function(e){var a=this;if(e.controls.cuaa.value||e.controls.denominazione.value){var i=new S.a;this.filtersFormGroup.get("cuaa").value&&(i.cuaa=this.filtersFormGroup.get("cuaa").value),this.filtersFormGroup.get("denominazione").value&&(i.denominazione=this.filtersFormGroup.get("denominazione").value),this.fascicoloService.ricercaFascicoli(i).subscribe((function(e){a.search.emit(e)}),(function(e){a.messageService.add(u.a.getToast("tst",u.b.error,u.a.ERRORE_RICERCA_FASCICOLO))})),i.clean()}else this.messageService.add(u.a.getToast("tst",u.b.error,u.a.ERRORE_DATI))}}]),e}()).\u0275fac=function(e){return new(e||t)(p.Nb(h.a),p.Nb(d.d))},t.\u0275cmp=p.Hb({type:t,selectors:[["app-filtro-ricerca-fascicoli-da-migrare"]],outputs:{search:"search"},decls:18,vars:14,consts:[[1,"h5","text-uppercase","font-weight-bold","my-4"],[1,"boxgrey","mb-0","pb-0"],[1,"ui-g","ui-fluid"],[1,"ui-grid-fixed","boxgrey"],[3,"formGroup","ngSubmit"],[1,"row"],[1,"ui-g-12","ui-md-5"],["type","text","formControlName","cuaa","name","cuaa","placeholder","Cuaa","pInputText","","appValidaCuaa","",3,"ngClass"],["cuaa",""],["type","text","formControlName","denominazione","name","denominazione","placeholder","Denominazione","pInputText","","appValidaDenominazione","",3,"ngClass"],["denominazione",""],[1,"ui-g-12","ui-md-2"],[1,"searchbutton","h-100"],["pButton","","type","button","type","submit",1,"ui-button","bg-primary","no-shadow","text-uppercase","w-100","h-100",3,"disabled","label"]],template:function(e,a){if(1&e&&(p.Tb(0,"h1",0),p.Oc(1),p.gc(2,"translate"),p.Sb(),p.Tb(3,"div",1),p.Tb(4,"div",2),p.Tb(5,"div",3),p.Tb(6,"form",4),p.bc("ngSubmit",(function(){return a.onSubmit(a.filtersFormGroup)})),p.Tb(7,"div",5),p.Tb(8,"div",6),p.Ob(9,"input",7,8),p.Sb(),p.Tb(11,"div",6),p.Ob(12,"input",9,10),p.Sb(),p.Tb(14,"div",11),p.Tb(15,"div",12),p.Ob(16,"button",13),p.gc(17,"translate"),p.Sb(),p.Sb(),p.Sb(),p.Sb(),p.Sb(),p.Sb(),p.Sb()),2&e){var i=p.Bc(10),t=p.Bc(13);p.Bb(1),p.Qc(" ",p.hc(2,6,"DOMANDA_UNICA_FILTRI.ricercaFascicolo"),"\n"),p.Bb(5),p.mc("formGroup",a.filtersFormGroup),p.Bb(3),p.mc("ngClass",p.sc(10,F,i.invalid&&i.touched)),p.Bb(3),p.mc("ngClass",p.sc(12,F,t.invalid&&t.touched)),p.Bb(4),p.nc("label",p.hc(17,8,"DOMANDA_UNICA_FILTRI.filter")),p.mc("disabled",a.filtersFormGroup.invalid)}},directives:[v.I,v.t,v.k,v.c,v.s,v.i,C.a,T.a,c.q,O.a,y.b],pipes:[f.d],styles:[""],encapsulation:2}),t),w=i("yWug");function z(e,a){if(1&e&&(p.Tb(0,"th",12),p.Oc(1),p.Ob(2,"p-sortIcon",13),p.Sb()),2&e){var i=a.$implicit;p.mc("pSortableColumn",i.field),p.Bb(1),p.Qc(" ",i.header," "),p.Bb(1),p.mc("field",i.field)}}function I(e,a){if(1&e&&(p.Tb(0,"tr"),p.Mc(1,z,3,3,"th",11),p.Sb()),2&e){var i=a.$implicit;p.Bb(1),p.mc("ngForOf",i)}}function B(e,a){if(1&e){var i=p.Ub();p.Tb(0,"tr",14),p.Tb(1,"td"),p.Oc(2),p.Sb(),p.Tb(3,"td"),p.Oc(4),p.Sb(),p.Tb(5,"td"),p.Oc(6),p.Sb(),p.Tb(7,"td"),p.Oc(8),p.gc(9,"translate"),p.Sb(),p.Tb(10,"td"),p.Oc(11),p.Sb(),p.Tb(12,"td"),p.Tb(13,"button",15),p.bc("click",(function(){p.Ec(i);var e=a.$implicit;return p.fc().migraFascicolo(e)})),p.Sb(),p.Sb(),p.Sb()}if(2&e){var t=a.$implicit;p.mc("pSelectableRow",t),p.Bb(2),p.Pc(t.cuaa),p.Bb(2),p.Pc(t.denominazione),p.Bb(2),p.Pc(t.stato),p.Bb(2),p.Qc(" ",p.hc(9,6,"FASCICOLO_AZIENDALE."+t.tipoDetenzione)," "),p.Bb(3),p.Pc(t.caa)}}function M(e,a){if(1&e&&(p.Tb(0,"tr"),p.Tb(1,"td"),p.Oc(2),p.Sb(),p.Sb()),2&e){var i=a.$implicit,t=p.fc();p.Bb(1),p.Cb("colspan",i.length),p.Bb(1),p.Pc(t.intestazioni.noContent)}}var k,P,N,A=function(){return{marginTop:"70px"}},R=[{path:"",component:(k=function(){function e(a,i,t,n,o,c){_classCallCheck(this,e),this.router=a,this.route=i,this.messageService=t,this.translateService=n,this.anagraficaFascicoloService=o,this.creazioneFascicoloService=c,this.elementiTotali=0,this.intestazioni=b.a}return _createClass(e,[{key:"ngOnInit",value:function(){this.cols=[{field:"cuaa",header:b.a.cuaaSigla},{field:"denominazione",header:b.a.denominazioneImpresa},{field:"stato",header:b.a.stato},{field:"tipoDetenzione",header:b.a.tipoDetenzione},{field:"caa",header:b.a.sportelloMandatario},{field:null,header:b.a.azioni}]}},{key:"onSearch",value:function(e){this.elementiTotali=e?e.length:0,this.fascicoliList=e}},{key:"migraFascicolo",value:function(e){var a=this;this.anagraficaFascicoloService.getCheckMigraFascicolo(e.cuaa,e.stato,e.tipoDetenzione,e.caacodice).subscribe((function(i){a.creazioneFascicoloService.anagraficaTributaria=i,a.router.navigate(["./cuaa/".concat(e.cuaa,"/migra")],{relativeTo:a.route})}),(function(e){a.messageService.add(u.a.getToast("tst",u.b.info,a.translateService.instant("EXC_APRI_FASCICOLO."+e.error.message)))}))}}]),e}(),k.\u0275fac=function(e){return new(e||k)(p.Nb(m.e),p.Nb(m.a),p.Nb(d.d),p.Nb(f.e),p.Nb(l.a),p.Nb(s.a))},k.\u0275cmp=p.Hb({type:k,selectors:[["app-ricerca-fascicoli-da-migrare"]],decls:13,vars:13,consts:[[1,"ui-grid-fixed"],["key","tst"],[3,"search"],[1,"boxgrey","pt-0"],[1,"layout-actionmenu"],["margin-top","30px",1,"table-responsive"],[1,"labelricerca","my-3"],["selectionMode","single","dataKey","cuaa",3,"columns","value","paginator","rows","responsive"],["pTemplate","header"],["ngFor","","pTemplate","body",3,"ngForOf"],["pTemplate","emptymessage"],[3,"pSortableColumn",4,"ngFor","ngForOf"],[3,"pSortableColumn"],[3,"field"],[3,"pSelectableRow"],["pButton","","label","Migra",1,"command-button","no-border-radius","p-button-lg","py-1","px-4","d-block","bg-primary",3,"click"]],template:function(e,a){1&e&&(p.Tb(0,"div",0),p.Ob(1,"p-toast",1),p.Tb(2,"app-filtro-ricerca-fascicoli-da-migrare",2),p.bc("search",(function(e){return a.onSearch(e)})),p.Sb(),p.Tb(3,"div",3),p.Tb(4,"div",4),p.Tb(5,"div",5),p.Tb(6,"label",6),p.Oc(7),p.gc(8,"translate"),p.Sb(),p.Tb(9,"p-table",7),p.Mc(10,I,2,1,"ng-template",8),p.Mc(11,B,14,8,"ng-template",9),p.Mc(12,M,3,2,"ng-template",10),p.Sb(),p.Sb(),p.Sb(),p.Sb(),p.Sb()),2&e&&(p.Bb(1),p.Kc(p.rc(12,A)),p.Bb(6),p.Rc(" ",a.elementiTotali," ",p.hc(8,10,"DOMANDA_UNICA_FILTRI.fascicoli")," "),p.Bb(2),p.mc("columns",a.cols)("value",a.fascicoliList)("paginator",!0)("rows",10)("responsive",!0),p.Bb(2),p.mc("ngForOf",a.fascicoliList))},directives:[g.a,_,w.q,d.e,c.s,w.p,w.o,w.m,y.b],pipes:[f.d],styles:["app-ricerca-fascicoli-da-migrare[_ngcontent-%COMP%]   .command-button.command-button.command-button[_ngcontent-%COMP%]{font-size:22px!important;height:auto!important;width:auto!important;background-color:#4caf50!important}app-ricerca-fascicoli-da-migrare[_ngcontent-%COMP%]   h1[_ngcontent-%COMP%]{color:#0d5f3f}app-ricerca-fascicoli-da-migrare[_ngcontent-%COMP%]   .ui-table[_ngcontent-%COMP%]   .ui-table-tbody[_ngcontent-%COMP%] > tr[_ngcontent-%COMP%]{background-color:transparent}"]}),k),data:{mybreadcrumb:b.a.fascicoliDaMigrare}},{path:"cuaa/:cuaa/migra",component:r.b,data:{mybreadcrumb:b.a.migrazioneFascicolo}}],D=((N=function e(){_classCallCheck(this,e)}).\u0275mod=p.Lb({type:N}),N.\u0275inj=p.Kb({factory:function(e){return new(e||N)},imports:[[m.i.forChild(R)],m.i]}),N),L=((P=function e(){_classCallCheck(this,e)}).\u0275mod=p.Lb({type:P}),P.\u0275inj=p.Kb({factory:function(e){return new(e||P)},imports:[[o.a,c.c,n.a,D]]}),P)}}]);