package it.tndigitale.a4g.ags.dto;

public class DatiComune {

	private String codNazionale;
	private String nome;
	private String denoProv;
	private String siglaProv;
	private String denoReg;
	private String codiceIstat;

	public DatiComune(String codNazionale, String nome, String denoProv, String siglaProv, String denoReg, String codiceIstat) {
		super();
		this.codNazionale = codNazionale;
		this.nome = nome;
		this.denoProv = denoProv;
		this.siglaProv = siglaProv;
		this.denoReg = denoReg;
		this.codiceIstat = codiceIstat;
	}

	public DatiComune(String denoReg, String siglaProv, String denoProv, String codiceIstat) {
		super();
		this.denoProv = denoProv;
		this.siglaProv = siglaProv;
		this.denoReg = denoReg;
		this.codiceIstat = codiceIstat;
	}

	/**
	 * @return the codNazionale
	 */
	public String getCodNazionale() {
		return codNazionale;
	}

	/**
	 * @param codNazionale
	 *            the codNazionale to set
	 */
	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the denoProv
	 */
	public String getDenoProv() {
		return denoProv;
	}

	/**
	 * @param denoProv
	 *            the denoProv to set
	 */
	public void setDenoProv(String denoProv) {
		this.denoProv = denoProv;
	}

	/**
	 * @return the siglaProv
	 */
	public String getSiglaProv() {
		return siglaProv;
	}

	/**
	 * @param siglaProv
	 *            the siglaProv to set
	 */
	public void setSiglaProv(String siglaProv) {
		this.siglaProv = siglaProv;
	}

	/**
	 * @return the denoReg
	 */
	public String getDenoReg() {
		return denoReg;
	}

	/**
	 * @param denoReg
	 *            the denoReg to set
	 */
	public void setDenoReg(String denoReg) {
		this.denoReg = denoReg;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

}
