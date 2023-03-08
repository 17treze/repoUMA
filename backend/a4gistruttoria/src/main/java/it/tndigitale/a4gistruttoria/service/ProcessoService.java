package it.tndigitale.a4gistruttoria.service;

import it.tndigitale.a4gistruttoria.dto.*;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

import java.util.ArrayList;
import java.util.List;

public interface ProcessoService {

	void avviaProcesso(ProcessoAnnoCampagnaDomandaDto processoDomanda);

	ArrayList<Processo> getListaProcessiAttivi();

	Processo getProcessoById(Long idProcesso);

	List<Processo> getProcessi(ProcessoFilter processoFilter);

	List<Processo> getProcessi(ProcessoFilter processoFilter, List<TipoProcesso> tipoProcessi);

}
