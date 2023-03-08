package it.tndigitale.a4g.uma.dto.consumi;

import it.tndigitale.a4g.uma.business.persistence.entity.TipoAllegatoConsuntivo;

public class InfoAllegatoConsuntivoDto {

	private Long id;
	private String nome;
	private String descrizione;
	private TipoAllegatoConsuntivo tipoDocumento;

	public Long getId() {
		return id;
	}
	public InfoAllegatoConsuntivoDto setId(Long id) {
		this.id = id;
		return this;
	}
	public String getNome() {
		return nome;
	}
	public InfoAllegatoConsuntivoDto setNome(String nome) {
		this.nome = nome;
		return this;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public InfoAllegatoConsuntivoDto setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}
	public TipoAllegatoConsuntivo getTipoDocumento() {
		return tipoDocumento;
	}
	public InfoAllegatoConsuntivoDto setTipoDocumento(TipoAllegatoConsuntivo tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
		return this;
	}
}
