package it.tndigitale.a4gistruttoria.dto.istruttoria;

import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

import java.util.List;
import java.util.Objects;

public class InputProcessoIstruttorieDto extends Processo {

	private static final long serialVersionUID = -9185960299885849534L;

	private List<Long> idIstruttorie;

	public List<Long> getIdIstruttorie() {
		return idIstruttorie;
	}

	public InputProcessoIstruttorieDto setIdIstruttorie(List<Long> idIstruttorie) {
		this.idIstruttorie = idIstruttorie;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InputProcessoIstruttorieDto that = (InputProcessoIstruttorieDto) o;
		return Objects.equals(idIstruttorie, that.idIstruttorie);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idIstruttorie);
	}
}
