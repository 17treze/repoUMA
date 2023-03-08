package it.tndigitale.a4gutente.service;

import static it.tndigitale.a4gutente.service.builder.ProfiloBuilder.convert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gutente.dto.Profilo;
import it.tndigitale.a4gutente.repository.dao.IProfiloDao;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;

@Service
public class ProfiloService {

	@Autowired
	private IProfiloDao profiloRepository;

	public List<Profilo> ricercaProfili() throws Exception {
		List<A4gtProfilo> profiliEntita = profiloRepository.findAll();
		return convert(profiliEntita);
	}
	
	public List<Profilo> ricercaProfiliUtente() throws Exception {
		List<A4gtProfilo> profiliEntita = profiloRepository.findAllUtente();
		return convert(profiliEntita);
	}

}
