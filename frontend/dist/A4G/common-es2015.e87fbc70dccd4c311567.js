(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{"2yZY":function(t,n,i){"use strict";i.d(n,"a",(function(){return r}));var e=i("fXoL");let r=(()=>{class t{constructor(){}loadConfig(){return{fileExt:".p7m, .pdf",maxSize:2}}loadConfigPdf(){return{fileExt:".pdf",maxSize:2}}}return t.\u0275fac=function(n){return new(n||t)},t.\u0275prov=e.Jb({token:t,factory:t.\u0275fac,providedIn:"root"}),t})()},Es4X:function(t,n,i){"use strict";i.d(n,"a",(function(){return e}));class e{}},GJmQ:function(t,n,i){"use strict";i.d(n,"a",(function(){return r}));var e=i("7o/Q");function r(t,n=!1){return i=>i.lift(new s(t,n))}class s{constructor(t,n){this.predicate=t,this.inclusive=n}call(t,n){return n.subscribe(new c(t,this.predicate,this.inclusive))}}class c extends e.a{constructor(t,n,i){super(t),this.predicate=n,this.inclusive=i,this.index=0}_next(t){const n=this.destination;let i;try{i=this.predicate(t,this.index++)}catch(e){return void n.error(e)}this.nextOrComplete(t,i)}nextOrComplete(t,n){const i=this.destination;Boolean(n)?i.next(t):(this.inclusive&&i.next(t),i.complete())}}},Lsrs:function(t,n,i){"use strict";i.d(n,"a",(function(){return e}));class e{}},MgAl:function(t,n,i){"use strict";i.d(n,"a",(function(){return e}));var e=function(t){return t.PAC_2014_2020="PAC_2014_2020",t}({})},VhzV:function(t,n,i){"use strict";i.d(n,"a",(function(){return e}));class e{}},jLKh:function(t,n,i){"use strict";i.d(n,"a",(function(){return e}));class e{constructor(t,n){this.codiceFiscale=t,this.dati=n}}},"q1/g":function(t,n,i){"use strict";i.d(n,"a",(function(){return e}));var e=function(t){return t.START="START",t.OK="OK",t.KO="KO",t.RUN="RUN",t}({})}}]);