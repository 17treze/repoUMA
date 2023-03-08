package it.tndigitale.a4g.uma.dto.clienti;

public class FatturaClienteDto {
	private Long id;
	private Long idCliente;
	private String nomeFile;
	private byte[] allegato;

	public Long getId() {
		return id;
	}
	public FatturaClienteDto setId(Long id) {
		this.id = id;
		return this;
	}
	public Long getIdCliente() {
		return idCliente;
	}
	public FatturaClienteDto setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
		return this;
	}
	public byte[] getAllegato() {
		return allegato;
	}
	public FatturaClienteDto setAllegato(byte[] allegato) {
		this.allegato = allegato;
		return this;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public FatturaClienteDto setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
		return this;
	}
}
