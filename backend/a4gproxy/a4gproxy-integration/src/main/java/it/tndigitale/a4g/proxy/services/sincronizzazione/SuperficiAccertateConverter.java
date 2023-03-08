package it.tndigitale.a4g.proxy.services.sincronizzazione;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import it.tndigitale.a4g.proxy.dto.SuperficiAccertateDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AppoSupeAccert;

@Component
class SuperficiAccertateConverter implements Converter<SuperficiAccertateDto, AppoSupeAccert> {

	@Override
	public AppoSupeAccert convert(SuperficiAccertateDto source) {

		AppoSupeAccert target = new AppoSupeAccert();
		target.setCuaa(source.getCuaa());
		target.setNumeCamp(source.getAnnoCampagna());
		target.setIdUffiOrpa(CostantiSincronizzazione.CODICE_APPAG);
		target.setCodiAttoOpr(source.getIdentificativoDomanda());
		target.setCodiInte(CostantiSincronizzazione.CODICE_AGEA_PREMIO_BASE); // Codice AGEA intervento premio base
		target.setSupeAcceAmmi(BigDecimal.valueOf(source.getSuperficieAccertata()));
		target.setSupeDete(BigDecimal.valueOf(source.getSuperficieDeterminata()));
		target.setMotivazioneA1(source.getMotivazioneA1());
		target.setMotivazioneA2(source.getMotivazioneA2());
		target.setMotivazioneA3(source.getMotivazioneA3());
		target.setMotivazioneB0(source.getMotivazioneB0());
		target.setMotivazioneB1(source.getMotivazioneB1());
		target.setDataInizioVal(new Date()); // Default SYSDATE
		target.setDataFineVal(Date.from(LocalDate.of(9999, 12, 31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())); // Default 31/12/9999
		target.setFonte(CostantiSincronizzazione.FONTE_SINCRONIZZAZIONE);
		return target;
	}
}
