package it.tndigitale.a4g.uma.dto.protocollo;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;

public class ProtocollaDocumentoUmaDto {
	private Long id;
	private String nome;
	private String cognome;
	private Integer anno;
	private String pec;
	private String cuaa;
	private String descrizioneImpresa;
	private ByteArrayResource documento;
	private List<ByteArrayResource> allegati;
	private TipoDocumentoUma tipoDocumentoUma;

	public ByteArrayResource getDocumento() {
		return documento;
	}
	public ProtocollaDocumentoUmaDto setDocumento(ByteArrayResource documento) {
		this.documento = documento;
		return this;
	}
	public Long getId() {
		return id;
	}
	public ProtocollaDocumentoUmaDto setId(Long id) {
		this.id = id;
		return this;
	}
	public String getNome() {
		return nome;
	}
	public ProtocollaDocumentoUmaDto setNome(String nome) {
		this.nome = nome;
		return this;
	}
	public String getCognome() {
		return cognome;
	}
	public ProtocollaDocumentoUmaDto setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public ProtocollaDocumentoUmaDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDescrizioneImpresa() {
		return descrizioneImpresa;
	}
	public ProtocollaDocumentoUmaDto setDescrizioneImpresa(String descrizioneImpresa) {
		this.descrizioneImpresa = descrizioneImpresa;
		return this;
	}
	public Integer getAnno() {
		return anno;
	}
	public ProtocollaDocumentoUmaDto setAnno(Integer anno) {
		this.anno = anno;
		return this;
	}
	public String getPec() {
		return pec;
	}
	public ProtocollaDocumentoUmaDto setPec(String pec) {
		this.pec = pec;
		return this;
	}
	public TipoDocumentoUma getTipoDocumentoUma() {
		return tipoDocumentoUma;
	}
	public ProtocollaDocumentoUmaDto setTipoDocumentoUma(TipoDocumentoUma tipoDocumentoUma) {
		this.tipoDocumentoUma = tipoDocumentoUma;
		return this;
	}
	public List<ByteArrayResource> getAllegati() {
		return allegati;
	}
	public ProtocollaDocumentoUmaDto setAllegati(List<ByteArrayResource> allegati) {
		this.allegati = allegati;
		return this;
	}
}
