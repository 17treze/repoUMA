package it.tndigitale.a4gistruttoria.action.acs;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.RichiestaSuperficiePerCalcoloDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloSostegnoM9GranoConsumer
		extends CalcoloSostegnoAccoppiatoSuperficieConsumer {

	public String getMisura() {
		return "M9";
	}
	
	/* M9

"IF (CTRLREGM9 NO OR CTRLCOLTM9 NO) THEN 0
ELSE IF (AZCMP=NO CTRLCOORD = SI AND CTRLREGM9 SI AND CTRLCOLTM9 SI) THEN MAX((PSSUPELEGIS-PFSUPANCOORD),0)
ELSE IF (AZCMP=SI AND CTRLCOORD =NO AND CTRLREGM9 SI AND CTRLCOLTM9 SI) THEN PFSUPCTRLLOCO
ELSE IF (AZCMP=SI AND CTRLCOORD =SI AND CTRLREGM9 SI AND CTRLCOLTM9 SI) THEN THEN MAX((PFSUPCTRLLOCO-PFSUPANCOORD),0)
ELSE PSSUPELEGIS"
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
