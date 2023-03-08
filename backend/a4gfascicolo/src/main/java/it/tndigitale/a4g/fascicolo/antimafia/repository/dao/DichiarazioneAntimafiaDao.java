package it.tndigitale.a4g.fascicolo.antimafia.repository.dao;

import java.util.List;
import java.util.Optional;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtDichiarazioneAntimafia;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazioneFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDic;

public interface DichiarazioneAntimafiaDao extends JpaRepository<A4gtDichiarazioneAntimafia, Long>, JpaSpecificationExecutor<A4gtDichiarazioneAntimafia> {

    final class DichiarazioneAntimafiaSpecifications {
		
		private DichiarazioneAntimafiaSpecifications() {}
		
		public static Specification<A4gtDichiarazioneAntimafia> withState(List<String> stati) {
			return (root, query, cb) -> stati == null ? null : root.join("a4gdStatoDicAntimafia").get("identificativo").in(stati);
		}
		public static Specification<A4gtDichiarazioneAntimafia> withState(String campo,Object valore) {
			return (root, query, cb) ->  valore == null ? null : cb.equal(root.join("a4gdStatoDicAntimafia").get(campo), valore);
		}	
		public static Specification<A4gtDichiarazioneAntimafia> withState(StatoDic stato) {
			Specification<A4gtDichiarazioneAntimafia> specs= Specification.where(null);
			return specs
					.and(withState("id",stato.getId()))
					.and(withState("descrizione",stato.getDescrizione()))
					.and(withState("identificativo",stato.getIdentificativo()));
		}	
		public static Specification<A4gtDichiarazioneAntimafia> withAzienda(String cuaa) {
			return (root, query, cb) -> cuaa == null ? null : cb.equal(root.join("a4gtAziendaAgricola").get("cuaa"),cuaa );
		}	
		
		public static Specification<A4gtDichiarazioneAntimafia> withCampoGenerico(String campoGenerico, Object valore) {
			return (root, query, cb) -> valore == null ? null : cb.equal(root.get(campoGenerico),valore );
		}
		
		public static Specification<A4gtDichiarazioneAntimafia>  build(DichiarazioneFilter dichiarazione ){
			Specification<A4gtDichiarazioneAntimafia> specs= Specification.where(null);
			return specs
					.and(withState(dichiarazione.getStatiDichiarazione()))
					.and(withState(dichiarazione.getStato()))
					.and(withAzienda(dichiarazione.getAzienda().getCuaa()))
					.and(withCampoGenerico("dtGenerazionePdf",dichiarazione.getDtGenerazionePdf()))
					.and(withCampoGenerico("dtInizioCompilazione",dichiarazione.getDtInizioCompilazione()))
					.and(withCampoGenerico("dtUltimoAggiornamento",dichiarazione.getDtUltimoAggiornamento()))
					.and(withCampoGenerico("dtUploadPdfFirmato",dichiarazione.getDtUploadPdfFirmato()))
					.and(withCampoGenerico("dtFine",dichiarazione.getDtFine()))
					.and(withCampoGenerico("idProtocollo",dichiarazione.getIdProtocollo()))
					.and(withCampoGenerico("dtProtocollazione",dichiarazione.getDtProtocollazione()))
					.and(withCampoGenerico("assenzaDt",dichiarazione.getAssenzaDt()))
					;
		}

	}
}
