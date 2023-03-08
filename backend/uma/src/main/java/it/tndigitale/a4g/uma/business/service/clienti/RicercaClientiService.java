package it.tndigitale.a4g.uma.business.service.clienti;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FatturaClienteModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ClienteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FattureClientiDao;
import it.tndigitale.a4g.uma.dto.clienti.ClienteDto;
import it.tndigitale.a4g.uma.dto.clienti.FatturaClienteDto;
import it.tndigitale.a4g.uma.dto.clienti.builder.ClienteBuilder;
import javassist.NotFoundException;

@Service
public class RicercaClientiService {

	@Autowired
	private ClienteDao clienteDao;
	@Autowired
	private FattureClientiDao fattureClientiDao;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;

	public List<ClienteDto> getClienti(Long id) {
		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Non esiste una dichiarazione consumi"));

		List<ClienteModel> clienti = clienteDao.findByDichiarazioneConsumi(dichiarazioneConsumi);
		List<ClienteDto> clientiDto = new ArrayList<>();

		if (CollectionUtils.isEmpty(clienti)) { return new ArrayList<>(); }

		clienti.forEach(cliente -> 
		clientiDto.add(new ClienteBuilder()
				.from(cliente)
				.build()));
		return clientiDto;
	}

	public List<FatturaClienteDto> getAllegatiCliente(Long idCliente) throws NotFoundException {
		ClienteModel cliente = clienteDao.findById(idCliente).orElseThrow(() -> new NotFoundException("Non esiste un cliente con id " + idCliente.toString()));
		List<FatturaClienteModel> allegati = fattureClientiDao.findByCliente(cliente);
		List<FatturaClienteDto> allegatiDto = new ArrayList<>();

		allegati.forEach(allegato -> 
		allegatiDto.add(new FatturaClienteDto()
				.setId(allegato.getId())
				.setIdCliente(allegato.getCliente().getId())
				.setNomeFile(allegato.getNomeFile())
				.setAllegato(allegato.getDocumento())));

		return allegatiDto;
	}
}
