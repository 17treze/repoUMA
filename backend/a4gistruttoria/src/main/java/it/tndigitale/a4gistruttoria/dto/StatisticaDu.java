package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class StatisticaDu {

	private Long idDomanda;
	private StatoIstruttoria stato;
	private Float impAmm;
	private Integer c110;
	private String c109;
	private String c109a;
	private String f100;
	private String f200;
	private String f201;
	private String f202a;
	private Integer f202b;
	private String f202c;
	private String f207;
	private String f300;
	private String c300a;
	private LocalDate f300b;
	private String c300c;
	private String c400;
	private String c401;
	private Float c402;
	private String c403;
	private Float c403a;
	private String c404;
	private String c405;
	private Float c406;
	private String c407;
	private Float c551;
	private Float c552;
	private Float c554;
	private Float c554a;
	private Float c557;
	private Float c558;
	private Float c558a;
	private Float c558b;
	private Float c558c;
	private Float c558d;
	private Float c558e;
	private Float c558f;
	private Float c559;
	private Float c560;
	private Float c560a;
	private Float c561;
	private Float c561a;
	private Float c561b;
	private String c600;
	private Float c605;
	private String c611;
	private String c620;
	private String c621;
	private String c633;
	private Float c640;
	private Float c640a;
	
	private StatisticaDu() {
		// Private empty constructor to prevent initialization without builder
	}
	
	public static StatisticaDu empty() {
		return new StatisticaDu()
				// Default fixed values
				.withF100("IT25")
				.withC633("N");
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public StatoIstruttoria getStato() {
		return stato;
	}

	public Float getImpAmm() {
		return impAmm;
	}

	public Integer getC110() {
		return c110;
	}

	public String getC109() {
		return c109;
	}

	public String getC109a() {
		return c109a;
	}

	public String getF100() {
		return f100;
	}

	public String getF200() {
		return f200;
	}

	public String getF201() {
		return f201;
	}

	public String getF202a() {
		return f202a;
	}

	public Integer getF202b() {
		return f202b;
	}

	public String getF202c() {
		return f202c;
	}

	public String getF207() {
		return f207;
	}

	public String getF300() {
		return f300;
	}

	public String getC300a() {
		return c300a;
	}

	public LocalDate getF300b() {
		return f300b;
	}

	public String getC300c() {
		return c300c;
	}

	public String getC400() {
		return c400;
	}

	public String getC401() {
		return c401;
	}

	public Float getC402() {
		return c402;
	}

	public String getC403() {
		return c403;
	}

	public Float getC403a() {
		return c403a;
	}

	public String getC404() {
		return c404;
	}

	public String getC405() {
		return c405;
	}

	public Float getC406() {
		return c406;
	}

	public String getC407() {
		return c407;
	}

	public Float getC551() {
		return c551;
	}

	public Float getC552() {
		return c552;
	}

	public Float getC554() {
		return c554;
	}

	public Float getC554a() {
		return c554a;
	}

	public Float getC557() {
		return c557;
	}

	public Float getC558() {
		return c558;
	}

	public Float getC558a() {
		return c558a;
	}

	public Float getC558b() {
		return c558b;
	}

	public Float getC558c() {
		return c558c;
	}

	public Float getC558d() {
		return c558d;
	}

	public Float getC558e() {
		return c558e;
	}

	public Float getC558f() {
		return c558f;
	}

	public Float getC559() {
		return c559;
	}

	public Float getC560() {
		return c560;
	}

	public Float getC560a() {
		return c560a;
	}

	public Float getC561() {
		return c561;
	}

	public Float getC561a() {
		return c561a;
	}

	public Float getC561b() {
		return c561b;
	}

	public String getC600() {
		return c600;
	}

	public Float getC605() {
		return c605;
	}

	public String getC611() {
		return c611;
	}

	public String getC620() {
		return c620;
	}

	public String getC621() {
		return c621;
	}

	public String getC633() {
		return c633;
	}

	public Float getC640() {
		return c640;
	}

	public Float getC640a() {
		return c640a;
	}

	public StatisticaDu withIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
		return this;
	}

	public StatisticaDu withStato(StatoIstruttoria stato) {
		this.stato = stato;
		return this;
	}

	public StatisticaDu withImpAmm(Float impAmm) {
		this.impAmm = impAmm;
		return this;
	}

	public StatisticaDu withC110(Integer c110) {
		this.c110 = c110;
		return this;
	}

	public StatisticaDu withC109(String c109) {
		this.c109 = c109;
		return this;
	}

	public StatisticaDu withC109a(String c109a) {
		this.c109a = c109a;
		return this;
	}

	private StatisticaDu withF100(String f100) {
		this.f100 = f100;
		return this;
	}

	public StatisticaDu withF200(String f200) {
		this.f200 = f200;
		return this;
	}

	public StatisticaDu withF201(String f201) {
		this.f201 = f201;
		return this;
	}

	public StatisticaDu withF202a(String f202a) {
		this.f202a = f202a;
		return this;
	}

	public StatisticaDu withF202b(Integer f202b) {
		this.f202b = f202b;
		return this;
	}

	public StatisticaDu withF202c(String f202c) {
		this.f202c = f202c;
		return this;
	}

	public StatisticaDu withF207(String f207) {
		this.f207 = f207;
		return this;
	}

	public StatisticaDu withF300(String f300) {
		this.f300 = f300;
		return this;
	}

	public StatisticaDu withC300a(String c300a) {
		this.c300a = c300a;
		return this;
	}

	public StatisticaDu withF300b(LocalDate f300b) {
		this.f300b = f300b;
		return this;
	}

	public StatisticaDu withC400(String c400) {
		this.c400 = c400;
		return this;
	}

	public StatisticaDu withC401(String c401) {
		this.c401 = c401;
		return this;
	}

	public StatisticaDu withC402(Float c402) {
		this.c402 = c402;
		return this;
	}

	public StatisticaDu withC403(String c403) {
		this.c403 = c403;
		return this;
	}

	public StatisticaDu withC403a(Float c403a) {
		this.c403a = c403a;
		return this;
	}

	public StatisticaDu withC404(String c404) {
		this.c404 = c404;
		return this;
	}

	public StatisticaDu withC405(String c405) {
		this.c405 = c405;
		return this;
	}

	public StatisticaDu withC406(Float c406) {
		this.c406 = c406;
		return this;
	}

	public StatisticaDu withC407(String c407) {
		this.c407 = c407;
		return this;
	}

	public StatisticaDu withC551(Float c551) {
		this.c551 = c551;
		return this;
	}

	public StatisticaDu withC552(Float c552) {
		this.c552 = c552;
		return this;
	}

	public StatisticaDu withC554(Float c554) {
		this.c554 = c554;
		return this;
	}

	public StatisticaDu withC554a(Float c554a) {
		this.c554a = c554a;
		return this;
	}

	public StatisticaDu withC557(Float c557) {
		this.c557 = c557;
		return this;
	}

	public StatisticaDu withC558(Float c558) {
		this.c558 = c558;
		return this;
	}

	public StatisticaDu withC558a(Float c558a) {
		this.c558a = c558a;
		return this;
	}

	public StatisticaDu withC558b(Float c558b) {
		this.c558b = c558b;
		return this;
	}

	public StatisticaDu withC558c(Float c558c) {
		this.c558c = c558c;
		return this;
	}

	public StatisticaDu withC558d(Float c558d) {
		this.c558d = c558d;
		return this;
	}

	public StatisticaDu withC558e(Float c558e) {
		this.c558e = c558e;
		return this;
	}

	public StatisticaDu withC558f(Float c558f) {
		this.c558f = c558f;
		return this;
	}

	public StatisticaDu withC559(Float c559) {
		this.c559 = c559;
		return this;
	}
	
	public StatisticaDu withC560(Float c560) {
		this.c560 = c560;
		return this;
	}

	public StatisticaDu withC560a(Float c560a) {
		this.c560a = c560a;
		return this;
	}

	public StatisticaDu withC561() {
		this.c561 = getImportoNonPagatoASeguitoDiControlli();
		return this;
	}

	public StatisticaDu withC561a(Float c561a) {
		this.c561a = c561a;
		return this;
	}

	public StatisticaDu withC561b(Float c561b) {
		this.c561b = c561b;
		return this;
	}

	public StatisticaDu withC600(String c600) {
		this.c600 = c600;
		return this;
	}

	public StatisticaDu withC605(Float c605) {
		this.c605 = c605;
		return this;
	}

	public StatisticaDu withC611(String c611) {
		this.c611 = c611;
		return this;
	}

	public StatisticaDu withC620(String c620) {
		this.c620 = c620;
		return this;
	}

	public StatisticaDu withC621() {
		this.c621 = getErroreDuranteControlloInLoco();
		return this;
	}

	public StatisticaDu withC633(String c633) {
		this.c633 = c633;
		return this;
	}

	public StatisticaDu withC640(Float c640) {
		this.c640 = c640;
		return this;
	}

	public StatisticaDu withC640a(Float c640a) {
		this.c640a = c640a;
		return this;
	}

	
	private Float getImportoNonPagatoASeguitoDiControlli() {
		if (c554 == null || c559 == null) {
			return null;
		}
		BigDecimal var1 = BigDecimal.valueOf(c554);
		BigDecimal var2 = BigDecimal.valueOf(c559);
		return var1.subtract(var2).setScale(4, RoundingMode.HALF_UP).floatValue();
	}
	
	private String getErroreDuranteControlloInLoco() {
		
		if (c600 != null && c600.equals("N")) {
			return null;
		} 
		else {
			return (c561 != null && c561.compareTo(0f) != 0) ? "1" : "3"; 
		} 
	}

	@Override
	public String toString() {
		return String.format(
				"StatisticaDu [idDomanda=%s, stato=%s, impAmm=%s, c110=%s, c109=%s, c109a=%s, f100=%s, f200=%s, f201=%s, f202a=%s, f202b=%s, f202c=%s, f207=%s, f300=%s, c300a=%s, f300b=%s, c300c=%s, c400=%s, c401=%s, c402=%s, c403=%s, c403a=%s, c404=%s, c405=%s, c406=%s, c407=%s, c551=%s, c552=%s, c554=%s, c554a=%s, c557=%s, c558=%s, c558a=%s, c558b=%s, c558c=%s, c558d=%s, c558e=%s, c558f=%s, c559=%s, c560=%s, c560a=%s, c561=%s, c561a=%s, c561b=%s, c600=%s, c605=%s, c611=%s, c620=%s, c621=%s, c633=%s, c640=%s, c640a=%s]",
				idDomanda, stato, impAmm, c110, c109, c109a, f100, f200, f201, f202a, f202b, f202c, f207, f300, c300a, f300b, c300c, c400, c401, c402, c403, c403a, c404, c405, c406, c407, c551, c552,
				c554, c554a, c557, c558, c558a, c558b, c558c, c558d, c558e, c558f, c559, c560, c560a, c561, c561a, c561b, c600, c605, c611, c620, c621, c633, c640, c640a);
	}
}