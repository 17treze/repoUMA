import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable()
export class GisMessaggiToastCostants {
  // MESSAGGI POLIGONI DICHIARATI
  public successAggiornaDichiarati = 'Poligoni dichiarati aggiornati correttamente';
  // VALIDAZIONE FE POLIGONI ADL
  public erroreValidazioneAdl = 'Poligono Area di lavoro non valido, modificare o ridisegnare il poligono';
  public erroreEsecuzioneAdl = 'Errore in esecuzione ritaglio suolo su ADL ';
  public ritaglioSuoloAdl = 'Ritaglio suolo su ADL';
  public poligoniAdlNonValidi = 'Il calcolo dei poligoni di suolo da ADL ha prodotto geometrie non valide.'
    + "Modificare la geometria dell'area di lavoro disegnata e reinnescare il calcolo dei poligoni di suolo";
  // GENERIC SUCCESS
  public genericSuccess = 'Operazione effettuata con successo';
  // ELIMINAZIONE ADL
  public deleteAdlSuccess = 'Adl cancellata con successo';
  public deleteAdlError = "Errore nella cancellazione dell'Adl";
  // EditToolbar
  public selectFeatureWarning = 'Selezionare almeno una feature con il tool di select';
  // Apertura lavorazione con anno di campagna diverso da quello selezionato in mappa
  public warningAnnoDiCampagna = "L'anno di campagna della lavorazione è diverso da quello impostato nell'applicazione," +
    " l'anno di campagna verrà impostato sul "
  // Cambio anno di campagna nella lavorazione
  public warningCambioAnnoDiCampagna = 'Attenzione, tutti i poligoni di dichiarato e suolo prenotato verranno rimossi. Confermare?';
}
