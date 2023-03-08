package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;

@Entity
@Table(name="A4GT_STATISTICA_DU")
@NamedQuery(name="A4gtStatisticaDu.findAll", query="SELECT a FROM A4gtStatisticaDu a")
public class A4gtStatisticaDu extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="TIPO_STATISTICA")
	@Enumerated(EnumType.STRING)
	private TipologiaStatistica tipoStatistica;
	
	@Column(name="ID_DOMANDA")
	private Long idDomanda;
	
	@Column(name="STATO")
	@Enumerated(EnumType.STRING)
	private StatoIstruttoria stato;
	
	@Column(name="IMP_AMM")
	private Float impAmm;
	
	@Column(name="C110")
	private Integer c110;
	
	@Column(name="C109")
	private String c109;
	
	@Column(name="C109A")
	private String c109a;
	
	@Column(name="F100")
	private String f100;
	
	@Column(name="F200")
	private String f200;
	
	@Column(name="F201")
	private String f201;
	
	@Column(name="F202A")
	private String f202a;
	
	@Column(name="F202B")
	private Integer f202b;
	
	@Column(name="F202C")
	private String f202c;
	
	@Column(name="F207")
	private String f207;
	
	@Column(name="F300")
	private String f300;
	
	@Column(name="C300A")
	private String c300a;
	
	@Temporal(TemporalType.DATE)
	@Column(name="F300B")
	private Date f300b;
	
	@Column(name="C300C")
	private String c300c;
	
	@Column(name="C400")
	private String c400;
	
	@Column(name="C401")
	private String c401;
	
	@Column(name="C402")
	private Float c402;
	
	@Column(name="C403")
	private String c403;
	
	@Column(name="C403A")
	private Float c403a;
	
	@Column(name="C404")
	private String c404;
	
	@Column(name="C405")
	private String c405;
	
	@Column(name="C406")
	private Float c406;
	
	@Column(name="C407")
	private String c407;
	
	@Column(name="C551")
	private Float c551;
	
	@Column(name="C552")
	private Float c552;
	
	@Column(name="C554")
	private Float c554;
	
	@Column(name="C554A")
	private Float c554a;
	
	@Column(name="C557")
	private Float c557;
	
	@Column(name="C558")
	private Float c558;
	
	@Column(name="C558A")
	private Float c558a;
	
	@Column(name="C558B")
	private Float c558b;
	
	@Column(name="C558C")
	private Float c558c;
	
	@Column(name="C558D")
	private Float c558d;
	
	@Column(name="C558E")
	private Float c558e;
	
	@Column(name="C558F")
	private Float c558f;
	
	@Column(name="C559")
	private Float c559;
	
	@Column(name="C560")
	private Float c560;
	
	@Column(name="C560A")
	private Float c560a;
	
	@Column(name="C561")
	private Float c561;
	
	@Column(name="C561A")
	private Float c561a;
	
	@Column(name="C561B")
	private Float c561b;
	
	@Column(name="C600")
	private String c600;
	
	@Column(name="C605")
	private Float c605;
	
	@Column(name="C611")
	private String c611;
	
	@Column(name="C620")
	private String c620;
	
	@Column(name="C621")
	private String c621;
	
	@Column(name="C633")
	private String c633;
	
	@Column(name="C640")
	private Float c640;
	
	@Column(name="C640A")
	private Float c640a;

	@Column(name="DATA_ELABORAZIONE")
	private LocalDateTime dataElaborazione;
	
	public TipologiaStatistica getTipoStatistica() {
		return tipoStatistica;
	}

	public void setTipoStatistica(TipologiaStatistica tipoStatistica) {
		this.tipoStatistica = tipoStatistica;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public StatoIstruttoria getStato() {
		return stato;
	}

	public void setStato(StatoIstruttoria stato) {
		this.stato = stato;
	}

	public Float getImpAmm() {
		return impAmm;
	}

	public void setImpAmm(Float impAmm) {
		this.impAmm = impAmm;
	}

	public Integer getC110() {
		return c110;
	}

	public void setC110(Integer c110) {
		this.c110 = c110;
	}

	public String getC109() {
		return c109;
	}

	public void setC109(String c109) {
		this.c109 = c109;
	}

	public String getC109a() {
		return c109a;
	}

	public void setC109a(String c109a) {
		this.c109a = c109a;
	}

	public String getF100() {
		return f100;
	}

	public void setF100(String f100) {
		this.f100 = f100;
	}

	public String getF200() {
		return f200;
	}

	public void setF200(String f200) {
		this.f200 = f200;
	}

	public String getF201() {
		return f201;
	}

	public void setF201(String f201) {
		this.f201 = f201;
	}

	public String getF202a() {
		return f202a;
	}

	public void setF202a(String f202a) {
		this.f202a = f202a;
	}

	public Integer getF202b() {
		return f202b;
	}

	public void setF202b(Integer f202b) {
		this.f202b = f202b;
	}

	public String getF202c() {
		return f202c;
	}

	public void setF202c(String f202c) {
		this.f202c = f202c;
	}

	public String getF207() {
		return f207;
	}

	public void setF207(String f207) {
		this.f207 = f207;
	}

	public String getF300() {
		return f300;
	}

	public void setF300(String f300) {
		this.f300 = f300;
	}

	public String getC300a() {
		return c300a;
	}

	public void setC300a(String c300a) {
		this.c300a = c300a;
	}

	public Date getF300b() {
		return f300b;
	}

	public void setF300b(Date f300b) {
		this.f300b = f300b;
	}

	public String getC300c() {
		return c300c;
	}

	public void setC300c(String c300c) {
		this.c300c = c300c;
	}

	public String getC400() {
		return c400;
	}

	public void setC400(String c400) {
		this.c400 = c400;
	}

	public String getC401() {
		return c401;
	}

	public void setC401(String c401) {
		this.c401 = c401;
	}

	public Float getC402() {
		return c402;
	}

	public void setC402(Float c402) {
		this.c402 = c402;
	}

	public String getC403() {
		return c403;
	}

	public void setC403(String c403) {
		this.c403 = c403;
	}

	public Float getC403a() {
		return c403a;
	}

	public void setC403a(Float c403a) {
		this.c403a = c403a;
	}

	public String getC404() {
		return c404;
	}

	public void setC404(String c404) {
		this.c404 = c404;
	}

	public String getC405() {
		return c405;
	}

	public void setC405(String c405) {
		this.c405 = c405;
	}

	public Float getC406() {
		return c406;
	}

	public void setC406(Float c406) {
		this.c406 = c406;
	}

	public String getC407() {
		return c407;
	}

	public void setC407(String c407) {
		this.c407 = c407;
	}

	public Float getC551() {
		return c551;
	}

	public void setC551(Float c551) {
		this.c551 = c551;
	}

	public Float getC552() {
		return c552;
	}

	public void setC552(Float c552) {
		this.c552 = c552;
	}

	public Float getC554() {
		return c554;
	}

	public void setC554(Float c554) {
		this.c554 = c554;
	}

	public Float getC554a() {
		return c554a;
	}

	public void setC554a(Float c554a) {
		this.c554a = c554a;
	}

	public Float getC557() {
		return c557;
	}

	public void setC557(Float c557) {
		this.c557 = c557;
	}

	public Float getC558() {
		return c558;
	}

	public void setC558(Float c558) {
		this.c558 = c558;
	}

	public Float getC558a() {
		return c558a;
	}

	public void setC558a(Float c558a) {
		this.c558a = c558a;
	}

	public Float getC558b() {
		return c558b;
	}

	public void setC558b(Float c558b) {
		this.c558b = c558b;
	}

	public Float getC558c() {
		return c558c;
	}

	public void setC558c(Float c558c) {
		this.c558c = c558c;
	}

	public Float getC558d() {
		return c558d;
	}

	public void setC558d(Float c558d) {
		this.c558d = c558d;
	}

	public Float getC558e() {
		return c558e;
	}

	public void setC558e(Float c558e) {
		this.c558e = c558e;
	}

	public Float getC558f() {
		return c558f;
	}

	public void setC558f(Float c558f) {
		this.c558f = c558f;
	}

	public Float getC559() {
		return c559;
	}

	public void setC559(Float c559) {
		this.c559 = c559;
	}

	public Float getC560() {
		return c560;
	}

	public void setC560(Float c560) {
		this.c560 = c560;
	}

	public Float getC560a() {
		return c560a;
	}

	public void setC560a(Float c560a) {
		this.c560a = c560a;
	}

	public Float getC561() {
		return c561;
	}

	public void setC561(Float c561) {
		this.c561 = c561;
	}

	public Float getC561a() {
		return c561a;
	}

	public void setC561a(Float c561a) {
		this.c561a = c561a;
	}

	public Float getC561b() {
		return c561b;
	}

	public void setC561b(Float c561b) {
		this.c561b = c561b;
	}

	public String getC600() {
		return c600;
	}

	public void setC600(String c600) {
		this.c600 = c600;
	}

	public Float getC605() {
		return c605;
	}

	public void setC605(Float c605) {
		this.c605 = c605;
	}

	public String getC611() {
		return c611;
	}

	public void setC611(String c611) {
		this.c611 = c611;
	}

	public String getC620() {
		return c620;
	}

	public void setC620(String c620) {
		this.c620 = c620;
	}

	public String getC621() {
		return c621;
	}

	public void setC621(String c621) {
		this.c621 = c621;
	}

	public String getC633() {
		return c633;
	}

	public void setC633(String c633) {
		this.c633 = c633;
	}

	public Float getC640() {
		return c640;
	}

	public void setC640(Float c640) {
		this.c640 = c640;
	}

	public Float getC640a() {
		return c640a;
	}

	public void setC640a(Float c640a) {
		this.c640a = c640a;
	}

	public LocalDateTime getDataElaborazione() {
		return dataElaborazione;
	}

	public void setDataElaborazione(LocalDateTime dataElaborazione) {
		this.dataElaborazione = dataElaborazione;
	}
	
	@PrePersist
	@PreUpdate
	void updateDataElaborazione() {
		  this.dataElaborazione = LocalDateTime.now();
	}
	
}
