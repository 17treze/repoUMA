package it.tndigitale.a4gistruttoria.action.acs;

import it.tndigitale.a4gistruttoria.dto.lavorazione.RichiestaSuperficiePerCalcoloDto;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloSostegnoM17OlivoQualitaConsumer extends CalcoloSostegnoAccoppiatoSuperficieConsumer {

	public String getMisura() {
		return "M17";
	}

	@Override
	protected boolean isSuperficieValida(RichiestaSuperficiePerCalcoloDto richiesta, CalcoloAccoppiatoHandler info,
			String suffissoMisura) {
		Boolean isOlioNazionale = info.getVariabiliInput().get(TipoVariabile.OLIONAZ).getValBoolean();
		Boolean isOlioQualita = info.getVariabiliInput().get(TipoVariabile.OLIOQUAL).getValBoolean();
		return Boolean.TRUE.equals(isOlioNazionale) && Boolean.TRUE.equals(isOlioQualita);
	}

	/* M17

"IF (CTRLCOLTM17 NO  OR OLIONAZ=NO OR OLIOQUAL=NO) THEN 0
ELSE IF  (AZCMP =NO AND CTRLCOORD=SI AND CTRLCOLTM17 SI  AND OLIONAZ=SI AND OLIOQUAL=SI) THEN MAX((PSSUPELEGIS-PFSUPANCOORD),0) 
ELSE IF (AZCMP=SI AND CTRLCOORD=NO AND CTRLCOLTM17 SI  AND OLIONAZ=SI AND OLIOQUAL=SI) THEN PFSUPCTRLLOCO
ELSE IF (AZCMP=SI AND CTRLCOORD=SI AND CTRLCOLTM17 SI  AND OLIONAZ=SI AND OLIOQUAL=SI)  THEN MAX((PFSUPCTRLLOCO-PFSUPANCOORD),0)
ELSE IF PSSUPELEGIS"
	 */
	@Override
	protected Float calcolaSuperficieDeterminata(RichiestaSuperficiePerCalcoloDto richiesta,
			CalcoloAccoppiatoHandler info, String suffissoMisura) {
		Float result;
		boolean isAziendaCampione = info.getVariabiliInput().get(TipoVariabile.ISCAMP).getValBoolean();
		boolean isCompatibilitaColturaIntervento = super.isCompatibilitaColturaIntervento(richiesta, info, suffissoMisura);
		boolean isAnomaliaCoordinamento = isAnomaliaCoordinamento(richiesta, info, suffissoMisura);
		Float superficieControlliInLoco = getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSPFSUPCTRLLOCO_".concat(suffissoMisura)), Float.class);
		Float superficieEleggibileGis = getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSPFSUPELEGIS_".concat(suffissoMisura)), Float.class);
		Float superficieAnCoord = getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSPFSUPANCOORD_".concat(suffissoMisura)), Float.class);
		boolean superficieValida = isSuperficieValida(richiesta, info, suffissoMisura);
		
		if (!isCompatibilitaColturaIntervento || !superficieValida) {
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
