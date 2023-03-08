package it.tndigitale.a4gistruttoria.action.acs;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.RichiestaSuperficiePerCalcoloDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloSostegnoM11LeguminoseConsumer
		extends CalcoloSostegnoAccoppiatoSuperficieConsumer {

	public String getMisura() {
		return "M11";
	}
	
	
	/* M11

"IF (CTRLREGM11 NO OR CTRLCOLTM11 NO) THEN 0
ELSE IF (AZCMP=NO AND CTRLCOORD = SI AND CTRLREGM11 SI AND CTRLCOLTM11 SI) THEN MAX((PSSUPELEGIS-PFSUPANCOORD),0)
ELSE IF (AZCMP=SI AND CTRLCOORD=NO AND CTRLREGM11 SI AND CTRLCOLTM11 SI) THEN PFSUPCTRLLOCO
ELSE IF (AZCMP=SI AND CTRLCOORD=SI AND CTRLREGM11 SI AND CTRLCOLTM11 SI) THEN MAX((PFSUPCTRLLOCO-PFSUPANCOORD),0)
ELSE IF PSSUPELEGIS"
	 */
	@Override
	protected Float calcolaSuperficieDeterminata(RichiestaSuperficiePerCalcoloDto richiesta,
			CalcoloAccoppiatoHandler info, String suffissoMisura) {
		Float result;
		Boolean isAziendaCampione = info.getVariabiliInput().get(TipoVariabile.ISCAMP).getValBoolean();
		boolean isControlloRegioni = super.isControlloRegioni(richiesta, info, suffissoMisura);
		boolean isCompatibilitaColturaIntervento = super.isCompatibilitaColturaIntervento(richiesta, info, suffissoMisura);
		boolean isAnomaliaCoordinamento = super.isAnomaliaCoordinamento(richiesta, info, suffissoMisura);
		Float superficieControlliInLoco = super.getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSPFSUPCTRLLOCO_".concat(suffissoMisura)), Float.class);
		Float superficieEleggibileGis = getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSPFSUPELEGIS_".concat(suffissoMisura)), Float.class);
		Float superficieAnCoord = getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSPFSUPANCOORD_".concat(suffissoMisura)), Float.class);

		if (!isControlloRegioni || !isCompatibilitaColturaIntervento) {
			result = Float.valueOf(0);
		} else if (Boolean.FALSE.equals(isAziendaCampione) && isAnomaliaCoordinamento) {
			result = Math.max(ConversioniCalcoli.sottrazioneSuperfici(superficieEleggibileGis, superficieAnCoord), 0);
		} else if (Boolean.TRUE.equals(isAziendaCampione)) {
			if (!isAnomaliaCoordinamento) {
				result = superficieControlliInLoco;
			} else {
				result = Math.max(ConversioniCalcoli.sottrazioneSuperfici(superficieControlliInLoco, superficieAnCoord), 0);
			}
		} else {
			result = superficieEleggibileGis;
		}
		return result == null ? Float.valueOf(0) : result;
	}
}
