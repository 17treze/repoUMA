package it.tndigitale.a4g.proxy.services.catasto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import it.taa.regione.catasto.particellatype.schemas.QualitaColturaParticellaType;
import it.taa.regione.catasto.particellatype.schemas.TipologiaQualitaColturaType;
import it.tndigitale.a4g.proxy.dto.catasto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.taa.regione.catasto.comunecatastaletype.schemas.ChiaveComuneCatastaleType;
import it.taa.regione.catasto.particella.schemas.GetParticella;
import it.taa.regione.catasto.particella.schemas.GetParticellaResponse;
import it.taa.regione.catasto.particella.schemas.ObjectFactory;
import it.taa.regione.catasto.particellatype.schemas.ChiaveParticellaType;
import it.taa.regione.catasto.particellatype.schemas.TipoParticellaType;
import it.taa.regione.librofondiario.benetype.schemas.ParticellaTavolareType;
import it.tndigitale.a4g.proxy.config.WSKSTSSupport;

@Service
public class ParticellaService extends WSKSTSSupport {

	private static final Logger log = LoggerFactory.getLogger(ParticellaService.class);

	@Value("${it.tndigit.catasto.uri}")
	private String wsUri;

	@Value("${it.tndigit.catasto.password}")
	private String wsAuthPsw;

	@Value("${it.tndigit.catasto.user}")
	private String wsAuthUsn;

	ObjectFactory factory = new ObjectFactory();

	@PostConstruct
	private void buildWebTemplate() throws Exception {
		super.buildWebTemplate("it.taa.regione.catasto.particella.schemas", wsUri, wsAuthUsn, wsAuthPsw);
	}

	private ParticellaTavolareType getInfoParticellaFromWs(String numeroParticella, TipologiaParticellaCatastale tipologia, Integer codiceComuneCatastale) {
//		TODO in caso di particelle edificiali va utilizzato anche il subalterno per invocare ImmobileService.UnitaImmobiliareType getInfoUnitaFromWs(String numeroParticella, Integer codiceComuneCatastale, BigInteger subalterno)
		GetParticella createGetParticella = factory.createGetParticella();

		ChiaveParticellaType chiaveParticellaType = new ChiaveParticellaType();
		chiaveParticellaType.setNumero(numeroParticella);
		chiaveParticellaType.setTipologia(TipoParticellaType.fromValue(tipologia.name()));

		ChiaveComuneCatastaleType chiaveComuneCatastaleType = new ChiaveComuneCatastaleType();
		chiaveComuneCatastaleType.setCodice(BigInteger.valueOf(codiceComuneCatastale));
		chiaveParticellaType.setComuneCatastale(chiaveComuneCatastaleType);
		createGetParticella.setParticella(chiaveParticellaType);

		GetParticellaResponse marshalSendAndReceive = (GetParticellaResponse) getWebServiceTemplate().marshalSendAndReceive(wsUri, createGetParticella);
		if (marshalSendAndReceive.getParticella() != null) {
			log.debug("chiamata a get info particella {}" , marshalSendAndReceive.getParticella().toString());
		}


		return marshalSendAndReceive.getParticella();
	}

	public InformazioniParticellaDto getInfoParticella(String numeroParticella, TipologiaParticellaCatastale tipologia, Integer codiceComuneCatastale) {
		try {
			ParticellaTavolareType particellaTavolareType = getInfoParticellaFromWs(numeroParticella, tipologia, codiceComuneCatastale);
			if (particellaTavolareType != null) {
				InformazioniParticellaDto informazioniParticellaDto = new InformazioniParticellaDto();
				informazioniParticellaDto.setParticella(numeroParticella);
				informazioniParticellaDto.setStato(EntitaCatastaleStato.valueOf(particellaTavolareType.getStato().value()));
				informazioniParticellaDto.setComuneCatastale(BigInteger.valueOf(codiceComuneCatastale));
				informazioniParticellaDto.setTipologia(TipologiaParticellaCatastale.valueOf(particellaTavolareType.getChiave().getTipologia().value()));
				PartitaTavolareDto partitaTavolareDto = new PartitaTavolareDto();
				partitaTavolareDto.setComuneCatastale(particellaTavolareType.getChiave().getPartitaTavolare().getComuneCatastale().getCodice());
				partitaTavolareDto.setNumero(particellaTavolareType.getChiave().getPartitaTavolare().getNumero());
				partitaTavolareDto.setSezione(TipologiaSezione.fromValue(particellaTavolareType.getChiave().getPartitaTavolare().getSezione().value()));
				informazioniParticellaDto.setPartitaTavolare(partitaTavolareDto);
				if (particellaTavolareType.getListaQualita() != null && particellaTavolareType.getListaQualita().getQualita() != null && !particellaTavolareType.getListaQualita().getQualita().isEmpty()) {
//					la superficie viene calcolata come somma indipendentemente dal tipo di qualita'
					informazioniParticellaDto.setSuperficie(BigInteger.valueOf(0));
					particellaTavolareType.getListaQualita().getQualita().stream()
							.filter(qualitaColturaParticellaType -> qualitaColturaParticellaType.getDescrizione() != null && qualitaColturaParticellaType.getDescrizione().getSuperficie() != null)
							.forEach(qualitaColturaParticellaType -> {
								informazioniParticellaDto.setSuperficie(informazioniParticellaDto.getSuperficie().add(qualitaColturaParticellaType.getDescrizione().getSuperficie()));
							});
//					List<QualitaColturaDto> qualitaColturaDtoList = new ArrayList<>();
//					particellaTavolareType.getListaQualita().getQualita().forEach(qualitaColturaParticellaType -> {
//						QualitaColturaDto qualitaColturaDto = new QualitaColturaDto();
//						InformazioniQualitaColturaDto informazioniQualitaColturaDto = new InformazioniQualitaColturaDto();
//						informazioniQualitaColturaDto.setSuperficie(qualitaColturaParticellaType.getDescrizione().getSuperficie());
//						informazioniQualitaColturaDto.setClasse(qualitaColturaParticellaType.getDescrizione().getClasse());
//						informazioniQualitaColturaDto.setQualita(TipologiaQualitaColtura.fromValue(qualitaColturaParticellaType.getDescrizione().getQualita().value()));
//						qualitaColturaDto.setDettaglio(informazioniQualitaColturaDto);
//						InformazioniRedditoDto informazioniRedditoDto = new InformazioniRedditoDto();
//						informazioniRedditoDto.setAgrario(qualitaColturaParticellaType.getReddito().getAgrario());
//						informazioniRedditoDto.setDominicale(qualitaColturaParticellaType.getReddito().getDominicale());
//						qualitaColturaDto.setReddito(informazioniRedditoDto);
//						qualitaColturaDtoList.add(qualitaColturaDto);
//					});
//					informazioniParticellaDto.setQualita(qualitaColturaDtoList);
				}
				return informazioniParticellaDto;
			}
			return null;
		} catch (Exception e) {
			log.error("Errore nel reperimento dei dati da catasto", e);
			return null;
		}
	}

	@Override
	protected String getAlias() {
		return null;
	}

}
