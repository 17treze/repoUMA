/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

/**
 * @author S.DeLuca
 *
 */
public class ControlliSostegno {

	private List<String> errors;
	private List<String> warnings;
	private List<String> infos;
	private List<String> successes;

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public List<String> getInfos() {
		return infos;
	}

	public void setInfos(List<String> infos) {
		this.infos = infos;
	}

	public List<String> getSuccesses() {
		return successes;
	}

	public void setSuccesses(List<String> successes) {
		this.successes = successes;
	}

}
