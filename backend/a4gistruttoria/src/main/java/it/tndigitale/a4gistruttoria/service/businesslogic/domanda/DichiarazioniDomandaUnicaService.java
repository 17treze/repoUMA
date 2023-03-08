package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import it.tndigitale.a4gistruttoria.dto.domandaunica.DichiarazioneDu;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DichiarazioneDomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.Quadro;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class DichiarazioniDomandaUnicaService {

	private final String NO = "NO";
	private final String SI = "SI";

	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	
	public List<DichiarazioneDu> getDichiarazioni(Long idDomanda) {
		DomandaUnicaModel domanda = domandaUnicaDao.getOne(idDomanda);
		List<DichiarazioneDu> dichiarazioni = new ArrayList<>();
		Set<DichiarazioneDomandaUnicaModel> dichiarazioniModel = domanda.getDichiarazioni();

		dichiarazioniModel.forEach(d -> {
			// if (!d.getA4gdTipoDichiarazioneDu().getIdentificativo().equalsIgnoreCase("DICH_RISERVA_NAZIONALE")) {
			if (!Quadro.RISERVA_NAZIONALE.equals(d.getQuadro())) {
				DichiarazioneDu a = new DichiarazioneDu();
				BeanUtils.copyProperties(d, a);

				a.setCodice(d.getCodice());
				a.setValore(getValoreDichiarazioneDu(d));
				a.setOrdine(d.getOrdine());

				// a.setIdTipoDichiarazioneDu(d.getA4gdTipoDichiarazioneDu().getIdentificativo());
				// a.setDescrizioneTipoDichiarazioneDu(d.getA4gdTipoDichiarazioneDu().getDescrizione());
				a.setQuadro(d.getQuadro());

				dichiarazioni.add(a);
			}
		});

		Collections.sort(dichiarazioni);
		return dichiarazioni;
		
	}

	private String getValoreDichiarazioneDu(DichiarazioneDomandaUnicaModel d) {
		String val = "";
		if (d.getValoreBool() != null) {
			val = d.getValoreBool().equals(new BigDecimal(0)) ? NO : SI;
		} else if (d.getValoreData() != null) {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			val = df.format(d.getValoreData());
		} else if (d.getValoreStringa() != null && !d.getValoreStringa().isEmpty()) {
			val = d.getValoreStringa();
		} else if (d.getValoreNumero() != null) {
			val = d.getValoreNumero().toString();
		}
		return val;
	}
}
