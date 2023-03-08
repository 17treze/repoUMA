import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { CertificazioniAntimafiaComponent } from "./certificazioni-antimafia/certificazioni-antimafia.component";
import { DettaglioDomandeCollegateComponent } from "./certificazioni-antimafia/tabs/cert-amf-controllate/dettaglio-domande-collegate/dettaglio-domande-collegate.component";

const routes: Routes = [
  {
    path: "",
    component: CertificazioniAntimafiaComponent,
    pathMatch: "full"
  },
  {
    path: "dettaglioDomandeCollegate",
    component: DettaglioDomandeCollegateComponent,
    pathMatch: "full",
    data: {
      mybreadcrumb: "Domande Collegate"
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class IstruttoriaAntimafiaRoutingModule { }
