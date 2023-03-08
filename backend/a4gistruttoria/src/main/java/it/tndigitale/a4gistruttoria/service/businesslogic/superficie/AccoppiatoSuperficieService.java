package it.tndigitale.a4gistruttoria.service.businesslogic.superficie;

import it.tndigitale.a4gistruttoria.dto.InterventoSuperficieDto;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccoppiatoSuperficieService {
	
	@Autowired private RichiestaSuperficieDao richiestaSuperficieDao;
	
	public List<InterventoSuperficieDto> getTotaleSuperficiePerIntervento(final Integer annoCampagna) {
		List<Object[]> totaleSuperficiePerIntervento = richiestaSuperficieDao.getTotaleSuperficiePerIntervento(annoCampagna);
		List<InterventoSuperficieDto> collect = totaleSuperficiePerIntervento.stream()
				.map(tupla -> InterventoSuperficieDto.build((String)tupla[0], (BigDecimal)tupla[1]))
				.collect(Collectors.toList());
		return collect;
	}

}
