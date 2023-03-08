package it.tndigitale.a4gistruttoria.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4gistruttoria.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACZ;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaPascoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoreDisPascoliBuilder;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoriaAcsBuilder;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoriaAczBuilder;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoriaBuilder;
import it.tndigitale.a4gistruttoria.repository.dao.DatiIstruttoreDisDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiIstruttoreSupDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiIstruttoreZooDao;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;

@Service
public class DatiIstruttoreService {
	
	private static Logger logger = LoggerFactory.getLogger(DatiIstruttoreService.class);
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	@Autowired
	private DatiIstruttoreDisDao datiIstruttoreDisDao;
	@Autowired
	private DatiIstruttoreSupDao datiIstruttoreSupDao;
	@Autowired
	private DatiIstruttoreZooDao datiIstruttoreZooDao;
	
	public DatiIstruttoria getDatiIstruttoreDisaccoppiato(Long idIstruttoria) {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		return DatiIstruttoriaBuilder.from(istruttoria.getDatiIstruttoreDisModel());
	}
	
	public List<DatiIstruttoriaPascoli> getDatiIstruttoreDisaccoppiatoPascoli(Long idIstruttoria) {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		return DatiIstruttoreDisPascoliBuilder.from(istruttoria.getDatiIstruttoreDisPascoli());
	}

	public DatiIstruttoriaACS getDatiIstruttoreSuperficie(Long idIstruttoria) {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		return DatiIstruttoriaAcsBuilder.from(istruttoria.getDatiIstruttoreSuperficie());
	}
	
	public DatiIstruttoriaACZ getDatiIstruttoreZootecnia(Long idIstruttoria) {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		return DatiIstruttoriaAczBuilder.from(istruttoria.getDatiIstruttoreZootecnia());
	}
	
	public List<DatiIstruttoriaPascoli> saveOrUpdateDatiIstruttoriaPascoli(Long idIstruttoria, List<DatiIstruttoriaPascoli> datiIstruttoriaPascoli) {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		//convert to model
		Set<DatiIstruttoreDisPascoliModel> listaPascoliModel= DatiIstruttoreDisPascoliBuilder.to(datiIstruttoriaPascoli);
		//ripulisco lista delle istruttoria per poi reinserirle
		//in questo modo si preoccupa JPA di fare insert/update
		Optional
			.ofNullable(istruttoria.getDatiIstruttoreDisPascoli())
			.ifPresent(Set::clear);
		istruttoria.addAllDatiIstruttoreDisPascoli(listaPascoliModel);
		listaPascoliModel = istruttoriaDao.save(istruttoria).getDatiIstruttoreDisPascoli();
		//convert from model
		return DatiIstruttoreDisPascoliBuilder.from(listaPascoliModel);
	}
	
	
	/**
	 * Salva o aggiorna i dati Istruttore contenuti nel dettaglio DISACCOPPIATO 
	 * @param idIstruttoria
	 * @param datiIstruttoria
	 * @return
	 */
	public DatiIstruttoria saveOrUpdateDatiIstruttore(Long idIstruttoria, DatiIstruttoria datiIstruttoria) {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		DatiIstruttoreDisModel datiIstruttoreDisModel = istruttoria.getDatiIstruttoreDisModel();
		if (datiIstruttoreDisModel == null ) {
			//crea
			datiIstruttoreDisModel = DatiIstruttoriaBuilder.from(datiIstruttoria);
		} else {
			//update
			BeanUtils.copyProperties(DatiIstruttoriaBuilder.from(datiIstruttoria), datiIstruttoreDisModel);
		}
		datiIstruttoreDisModel.setIstruttoria(istruttoria);
		istruttoria.setDatiIstruttoreDisModel(datiIstruttoreDisModel);
		return DatiIstruttoriaBuilder.from(datiIstruttoreDisDao.save(datiIstruttoreDisModel));
	}
	
	/**
	 * Salva o aggiorna i dati Istruttore contenuti nel dettaglio SUPERFICIE 
	 * @param idIstruttoria
	 * @param datiIstruttoriaAcs
	 * @return
	 */
	public DatiIstruttoriaACS saveOrUpdateDatiIstruttore(Long idIstruttoria, DatiIstruttoriaACS datiIstruttoriaAcs) {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		DatiIstruttoreSuperficieModel datiIstruttoreSupModel = istruttoria.getDatiIstruttoreSuperficie();
		if (datiIstruttoreSupModel == null ) {
			//crea
			datiIstruttoreSupModel = DatiIstruttoriaAcsBuilder.to(datiIstruttoriaAcs);
		} else {
			//update
			BeanUtils.copyProperties(DatiIstruttoriaAcsBuilder.to(datiIstruttoriaAcs), datiIstruttoreSupModel);
		}
		datiIstruttoreSupModel.setIstruttoria(istruttoria);
		istruttoria.setDatiIstruttoreSuperficie(datiIstruttoreSupModel);
		return DatiIstruttoriaAcsBuilder.from(datiIstruttoreSupDao.save(datiIstruttoreSupModel));
	}
	
	/**
	 * Salva o aggiorna i dati Istruttore contenuti nel dettaglio ZOOTECNIA 
	 * @param idIstruttoria
	 * @param datiIstruttoriaAcz
	 * @return
	 */
	public DatiIstruttoriaACZ saveOrUpdateDatiIstruttore(Long idIstruttoria, DatiIstruttoriaACZ datiIstruttoriaAcz) {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		DatiIstruttoreZootecniaModel datiIstruttoreZooModel = istruttoria.getDatiIstruttoreZootecnia();
		if (datiIstruttoreZooModel == null ) {
			//crea
			datiIstruttoreZooModel = DatiIstruttoriaAczBuilder.to(datiIstruttoriaAcz);
		} else {
			//update
			BeanUtils.copyProperties(DatiIstruttoriaAczBuilder.to(datiIstruttoriaAcz), datiIstruttoreZooModel);
		}
		datiIstruttoreZooModel.setIstruttoria(istruttoria);
		istruttoria.setDatiIstruttoreZootecnia(datiIstruttoreZooModel);
		return DatiIstruttoriaAczBuilder.from(datiIstruttoreZooDao.save(datiIstruttoreZooModel));
	}
}
