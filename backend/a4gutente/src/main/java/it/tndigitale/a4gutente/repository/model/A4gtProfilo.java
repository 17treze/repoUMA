package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4gutente.dto.Responsabilita;


/**
 * The persistent class for the A4GT_PROFILO database table.
 * 
 */
@Entity
@Table(name="A4GT_PROFILO")
@NamedQuery(name="A4gtProfilo.findAll", query="SELECT a FROM A4gtProfilo a")
@NamedQuery(
		name = "A4gtProfilo.findAllUtente",
		query = "SELECT a FROM A4gtProfilo a WHERE a.responsabilita IS NOT 'UTENZA_TECNICA'"
)
public class A4gtProfilo extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = 1L;

	private String descrizione;

	private String identificativo;

	@Enumerated(EnumType.STRING)
	private Responsabilita responsabilita;

	@OneToMany(mappedBy = "profilo", fetch = FetchType.LAZY)
	private List<A4grProfiloRuolo> a4grProfiloRuolos;

	public List<A4grProfiloRuolo> getA4grProfiloRuolos() {
		return a4grProfiloRuolos;
	}

	public A4gtProfilo setA4grProfiloRuolos(List<A4grProfiloRuolo> a4grProfiloRuolos) {
		this.a4grProfiloRuolos = a4grProfiloRuolos;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public A4gtProfilo setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public A4gtProfilo setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public Responsabilita getResponsabilita() {
		return responsabilita;
	}

	public A4gtProfilo setResponsabilita(Responsabilita responsabilita) {
		this.responsabilita = responsabilita;
		return this;
	}
}
