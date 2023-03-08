package it.tndigitale.a4gistruttoria.action.cup;

import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.cup.dto.CONCESSIONEINCENTIVIUNITAPRODUTTIVE;
import it.tndigitale.a4gistruttoria.cup.dto.CUPGENERAZIONE;
import it.tndigitale.a4gistruttoria.cup.dto.DESCRIZIONE;
import it.tndigitale.a4gistruttoria.cup.dto.ObjectFactory;
import it.tndigitale.a4gistruttoria.dto.AnagraficaAzienda;

@Component
public class PopolaConcessioneIncentiviUnitaProduttiveConsumer implements BiConsumer<CupHandler, CUPGENERAZIONE> {

	@Override
	public void accept(CupHandler handler, CUPGENERAZIONE generazione) {
		AnagraficaAzienda anagraficaAzienda = handler.getAnagraficaAzienda();
		ObjectFactory of = new ObjectFactory();
		CONCESSIONEINCENTIVIUNITAPRODUTTIVE ciup = of.createCONCESSIONEINCENTIVIUNITAPRODUTTIVE();
		DESCRIZIONE descrizione = of.createDESCRIZIONE();
		descrizione.getLAVORIPUBBLICIOrCONCESSIONEINCENTIVIUNITAPRODUTTIVEOrREALIZZACQUISTOSERVIZIRICERCAOrREALIZZACQUISTOSERVIZIFORMAZIONEOrREALIZZACQUISTOSERVIZINOFORMAZIONERICERCAOrACQUISTOBENIOrPARTECIPAZIONARIECONFERIMCAPITALEOrCONCESSIONECONTRIBUTINOUNITAPRODUTTIVEOrCUPCUMULATIVO().add(ciup);
		generazione.setDESCRIZIONE(descrizione);
		ciup.setDenominazioneImpresaStabilimento(anagraficaAzienda.getDenominazione());
		ciup.setPartitaIva(anagraficaAzienda.getPartitaIva());
		String indirizzo = anagraficaAzienda.getIndirizzoResidenza();
		if (indirizzo == null) indirizzo = "";
		String codiceInd = "05";
		if (indirizzo.startsWith("VIALE")) {
			codiceInd = "02";
			indirizzo = indirizzo.substring(indirizzo.indexOf(" ") + 1);
		} else if (indirizzo.startsWith("VIA")) {
			codiceInd = "01";
			indirizzo = indirizzo.substring(indirizzo.indexOf(" ") + 1);
		} else if (indirizzo.startsWith("PIAZZA")) {
			codiceInd = "03";
			indirizzo = indirizzo.substring(indirizzo.indexOf(" ") + 1);
		} else if (indirizzo.startsWith("CORSO")) {
			codiceInd = "04";
			indirizzo = indirizzo.substring(indirizzo.indexOf(" ") + 1);
		}
		ciup.setTipoIndAreaRifer(codiceInd);
		ciup.setIndAreaRifer(indirizzo);
		ciup.setStrumProgr("99");
		ciup.setDescStrumProgr("P.S.R. 2014-2020 OPERAZIONE 4.1.1.  sostegno a investimenti nelle aziende agricole (Reg (UE) n. 1305/2013");
		ciup.setAltreInformazioni("intervento finanziato ai sensi degli articolo 17 del Reg. (UE) 1305/2013");
		//ciup.setDescrizioneIntervento("P.S.R. 2014-2020 OPERAZIONE 4.1.1.  sostegno a investimenti nelle aziende agricole (Reg (UE) n. 1305/2013");
		ciup.setDescrizioneIntervento(handler.getDatiCUP().getDescrizioneCodice());
	} 

}
