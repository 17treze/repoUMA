package it.tndigitale.a4gutente.repository.dao;

import it.tndigitale.a4gutente.dto.csv.UtenteA4gDTO;
import it.tndigitale.a4gutente.dto.csv.UtenteA4gSospesoDTO;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.dto.UtenteProfiloA4gDto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface IUtenteCompletoDao extends IEntitaDominioRepository<A4gtUtente> {
	
	A4gtUtente findByIdentificativo(String identificativo);
	
	
	 @Query(value = "SELECT DISTINCT " + 
	 		" p.NOME as nome," + 
	 		" p.COGNOME as cognome," + 
	 		" p.CODICE_FISCALE as codiceFiscale," + 
	 		" u.IDENTIFICATIVO AS userName," + 
	 		" MIN(i.DATA_TERMINE_ISTRUTTORIA) AS dataAttivazione," + 
	 		" pr.IDENTIFICATIVO as profilo," + 
	 		" pr.DESCRIZIONE as descProfilo," + 
	 		" pr.RESPONSABILITA as responsabilita," + 
	 		" e.caa as caa," + 
	 		" d.denominazione_azienda as denomDistributore," +
	 		" d.comune_distributore as comuneDistributore" + 
	 		" from a4gt_utente u" + 
	 		" LEFT JOIN A4GT_PERSONA p" + 
	 		" ON u.CODICE_FISCALE = p.CODICE_FISCALE" + 
	 		" LEFT JOIN A4GR_UTENTE_PROFILO r" + 
	 		" ON u.id = r.ID_UTENTE " + 
	 		" LEFT JOIN A4GT_PROFILO pr" + 
	 		" ON pr.ID = r.ID_PROFILO" + 
	 		" INNER JOIN A4GT_ISTRUTTORIA i" + 
	 		" ON u.ID = i.ID_UTENTE" + 
	 		" LEFT JOIN a4gr_utente_ente ue" + 
	 		" ON ue.ID_UTENTE = u.ID" + 
	 		" LEFT JOIN a4gt_ente e" + 
	 		" ON ue.ID_ENTE = e.ID" + 
	 		" LEFT JOIN a4gr_utente_distributore ud" + 
	 		" ON ud.ID_UTENTE = u.ID" + 
	 		" LEFT JOIN a4gt_distributore d" + 
	 		" ON ud.ID_DISTRIBUTORE = d.ID" + 
	 		" where (pr.responsabilita is null or pr.responsabilita <> 'UTENZA_TECNICA')" + 
	 		" GROUP BY p.NOME, p.COGNOME, p.CODICE_FISCALE, u.IDENTIFICATIVO, pr.IDENTIFICATIVO, pr.DESCRIZIONE, pr.RESPONSABILITA,e.caa,d.denominazione_azienda,d.comune_distributore" + 
	 		" ORDER BY u.IDENTIFICATIVO, p.COGNOME, p.NOME, p.CODICE_FISCALE, pr.IDENTIFICATIVO, dataAttivazione DESC", nativeQuery = true)
	 List<UtenteA4gDTO> findUtenteA4g();
	    
	 @Query(value = " SELECT DISTINCT" + 
	 		        " u.IDENTIFICATIVO AS username," + 
			 		" MAX(i.DATA_TERMINE_ISTRUTTORIA) AS dataSospensione," + 
			 		" i.MOTIVO_DISATTIVAZIONE as motivoSospensione" + 
			 		" FROM a4gt_utente u" + 
			 		" INNER JOIN A4GT_ISTRUTTORIA i" + 
			 		" ON u.ID = i.ID_UTENTE" + 
			 		" WHERE u.ID NOT IN (SELECT r.ID_UTENTE FROM A4GR_UTENTE_PROFILO r)" + 
			 		" GROUP BY u.IDENTIFICATIVO, i.MOTIVO_DISATTIVAZIONE" + 
			 		" ORDER BY u.IDENTIFICATIVO, dataSospensione DESC", nativeQuery = true)
	 List<UtenteA4gSospesoDTO> findUtenteA4gSospesi();
	 
	 
	 //@Query(value = "SELECT a.* FROM A4gt_Richiesta_Superficie a JOIN a4gd_intervento i on a.id_intervento = i.id " +
	//			"where a.id_domanda = :idDomanda and JSON_VALUE(a.info_catastali, '$.idParticella') = :idParticella and i.identificativo_intervento = :#{#codiceInterventoAgs.name()}", nativeQuery = true)
		

	@Query(value = "select distinct u.codiceFiscale as codiceFiscale," +
			"u.identificativo as userName,pers.nome as nome,pers.cognome as cognome,p.identificativo as profilo," +
			"p.descrizione as descProfilo " +
			"from A4gtProfilo p " +
			"join A4grUtenteProfilo up " +
			"on p.id = up.idProfilo " +
			"join A4gtUtente u " +
			"on up.idUtente = u.id " +
			"join PersonaEntita pers " +
			"on u.codiceFiscale = pers.codiceFiscale " +
			"where p.identificativo in (:profili) ")
	List<UtenteProfiloA4gDto> findAllUtentiA4gProfilo(@Param("profili")List<String> profili);

}
