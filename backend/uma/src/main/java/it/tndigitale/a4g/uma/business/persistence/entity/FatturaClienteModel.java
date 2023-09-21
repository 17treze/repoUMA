package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "TAB_AGRI_UMAL_FATTURE_CLIENTI")
public class FatturaClienteModel extends EntitaDominio implements Serializable {
	
	private static final long serialVersionUID = -2881061942345072826L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE")
	private ClienteModel cliente;
	
	//	@Lob
	//	@Column(name = "DOCUMENTO", nullable = false)
	//	private byte[] documento;
	
	@Column(name = "NOME_FILE", nullable = false)
	private String nomeFile;
	
	public ClienteModel getCliente() {
		return cliente;
	}
	
	public FatturaClienteModel setCliente(ClienteModel cliente) {
		this.cliente = cliente;
		return this;
	}
	
	//	public byte[] getDocumento() {
	//		return documento;
	//	}
	//	public FatturaClienteModel setDocumento(byte[] documento) {
	//		this.documento = documento;
	//		return this;
	//	}
	public String getNomeFile() {
		return nomeFile;
	}
	
	public FatturaClienteModel setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
		return this;
	}
}
