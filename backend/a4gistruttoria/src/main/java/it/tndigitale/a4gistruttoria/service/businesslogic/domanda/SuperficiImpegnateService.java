package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.IRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.dto.InformazioniColtivazione;
import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.domandaunica.RichiestaSuperficie;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SuperficiImpegnate;
import it.tndigitale.a4gistruttoria.dto.lavorazione.RiferimentiCartografici;
import it.tndigitale.a4gistruttoria.repository.dao.InterventoDao;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;

@Service
public class SuperficiImpegnateService {

	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;
	@Autowired
	private InterventoDao daoInterventoDu;

	public SuperficiImpegnate superficiImpegnateBPSDomanda(Long idDomanda, Paginazione paginazione, Ordinamento ordinamento) throws Exception {
		
		Pageable pageable = PageableBuilder.build().from(paginazione, Optional.ofNullable(ordinamento).orElse(Ordinamento.DEFAULT_ORDER_BY));

		List<IRichiestaSuperficie> aa = daoRichiestaSuperficie.findByDomandaInterventoPaginata1(
				idDomanda,daoInterventoDu.findByIdentificativoIntervento(CodiceInterventoAgs.BPS).getId(), pageable);

		List<RichiestaSuperficie> richiestaSup = new ArrayList<>();
		SuperficiImpegnate result = new SuperficiImpegnate();
		aa.forEach(d -> {
			RichiestaSuperficie a = new RichiestaSuperficie();
			BeanUtils.copyProperties(d, a);
			Particella infoCat = new Particella();
			BeanUtils.copyProperties(d, infoCat);
			a.setInfoCatastali(infoCat);

			InformazioniColtivazione infoColt = new InformazioniColtivazione();
			BeanUtils.copyProperties(d, infoColt);
			a.setInfoColtivazione(infoColt);

			RiferimentiCartografici riferimentiCartografici = new RiferimentiCartografici();
			BeanUtils.copyProperties(d, riferimentiCartografici);
			a.setRiferimentiCartografici(riferimentiCartografici);

			richiestaSup.add(a);
		});
		RisultatiPaginati<RichiestaSuperficie> rp = RisultatiPaginati.of(richiestaSup, daoRichiestaSuperficie.getCountfindByDomandaInterventoPaginata1(idDomanda, CodiceInterventoAgs.BPS));
		result.setSupImpegnataLorda(getSupImpegnataLordaBPS(idDomanda));
		result.setSupImpegnataNetta(getSupImpegnataNettaBPS(idDomanda));
		result.setPaginaSuperfici(rp);
		return result;
	}

	public Double getSupImpegnataLordaBPS(Long id) {
		return daoRichiestaSuperficie.sumSuperficieRichiestaLordaCompatibile(id, CodiceInterventoAgs.BPS).multiply(new BigDecimal(10000)).doubleValue();

	}

	public Double getSupImpegnataNettaBPS(Long id) {
		return daoRichiestaSuperficie.sumSuperficieRichiestaNettaCompatibile(id, CodiceInterventoAgs.BPS).multiply(new BigDecimal(10000)).doubleValue();
	}
}
