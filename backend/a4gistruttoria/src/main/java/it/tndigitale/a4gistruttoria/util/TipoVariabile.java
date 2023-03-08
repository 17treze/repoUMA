package it.tndigitale.a4gistruttoria.util;

import java.util.HashMap;
import java.util.stream.Stream;

public enum TipoVariabile {
	GREPERC("Greening - Percentuale di incremento del premio", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, FonteDati.CONFIG_ISTRUTTORIA_ANNO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 10), new OrdineVariabile(TipologiaPassoTransizione.GREENING, 10))), 10),
	GREFPAZOTO("Greening - Fattore di ponderazione colture azotofissatrici", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.NULL,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 20))), 20),
	GIOPERC("Giovane - Percentuale di incremento del premio", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, FonteDati.CONFIG_ISTRUTTORIA_ANNO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 20), new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 10))), 11),
	GIOLIMITE("Giovane - Limite ai diritti all'aiuto attivabili", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, FonteDati.CONFIG_ISTRUTTORIA_ANNO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 30))), 12),
	TITONUM("Numero Titoli", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 50))), 14),
	TITSUP("Superficie totale titoli", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 60))), 15),
	TITVAL("Valore medio dei titoli per ettaro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 70))), 16),
	TITVALGIO("Giovane - Valore medio dei titoli per ettaro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 80))),
			17),
	BPSSUPIMP200("BPS - Superficie Impegnata (parcelle < 200 mq)", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 100))), 90),
	INFOAGRATT("Info agricoltore in attività", FormatoVariabile.BOOL, UnitaMisura.NULL, 69),
	AGRATT("Agricoltore in attività", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 90))), 18),
	BPSSUPIMP("BPS - Superficie Impegnata", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 110))), 19),
	GRERIC("Greening - Intervento Richiesto", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 120))), 20),
	GIORIC("Giovane - Intervento Richiesto", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 130))), 21),
	BPSSUPRIC("BPS - Superficie richiesta", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 160), new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 20))), 32),
	BPSIMPRIC("BPS - Importo richiesto", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 170), new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 20))), 33),
	BPSSUPAMM("BPS - Superficie ammissibile", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 180), new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 80),
					new OrdineVariabile(TipologiaPassoTransizione.GREENING, 50), new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 60))),
			34),
	BPSIMPAMM("BPS - Importo ammissibile", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 190), new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 90),
					new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 30), new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 10), new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 10))),
			35),
	GRESUPRIC("Greening - Superficie richiesta", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 200))), 36),
	GREIMPRIC("Greening - importo richiesto", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 210))), 37),
	GIOSUPRIC("Giovane - Superficie richiesta", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 240), new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 40))), 40),
	GIOIMPRIC("Giovane - Importo richiesto", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 250), new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 50))), 41),
	GIOSUPAMM("Giovane - Superficie ammissibile", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 260), new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 90))), 42),
	GIOIMPAMM("Giovane - Importo ammissibile", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 270), new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 100),
					new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 30), new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 30))),
			43),
	PERCRIDLINTIT("Riduzione lineare titoli", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, FonteDati.CONFIG_ISTRUTTORIA_ANNO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 40))), 13),
	TITVALRID("Valore medio dei titoli per ettaro con riduzione lineare", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 140), new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 10),
					new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 10), new OrdineVariabile(TipologiaPassoTransizione.GREENING, 30))),
			30),
	TITVALGIORID("Giovane - Valore medio dei titoli per ettaro con riduzione lineare", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 150), new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 20))), 31),

	BPSSUPDET("BPS - Superficie determinata", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 70), new OrdineVariabile(TipologiaPassoTransizione.GREENING, 40))), 9), // greening
	GREAZIBIO("Greening - Esenzione: Azienda totalmente biologica", FormatoVariabile.BOOL, UnitaMisura.NULL, 210),
	GRESUPRIDIST("Greening - Sup non ammissibile mancato Rispetto Inverdimento Istruttore", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 60))), 220),
	GRESUPDET("Greening - Superficie Determinata totale", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 70))), 230),
	GRESUPARB("Greening - Superficie Determinata Colture Arboree Permanenti", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 80))), 240),
	GRESUPPP("Greening - Superficie Determinata Prati Permanenti", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 90))),
			250),
	GRESUPSEM("Greening - Superficie Determinata Seminativo", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 100))),
			260),
	GRESUPERBAI("Greening - Superficie Determinata Erbai", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 110))), 270),
	GRESUPLEGUM("Greening - Superficie Determinata Leguminose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 120))),
			280),
	GRESUPRIPOSO("Greening - Superficie Determinata Terreni a riposo", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 130))), 290),
	GRESUPSOMM("Greening - Superficie Determinata Coltivazioni sommerse", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 140))), 300),
	GREDIVSUP1COL("Greening - Superficie Coltura Principale", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 150))),
			310),
	GREDIVFGS("Greening - Codici Famiglia-Genere-Specie della Coltura principale", FormatoVariabile.STRING, UnitaMisura.NULL,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 160))), 320),
	GREDIV1COL("Greening - Coltura Principale", FormatoVariabile.STRING, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 170))), 330),
	GREDIVSUP2COL("Greening - Superficie Seconda Coltura", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 180))), 340),
	GREDIV2FGS("Greening - Codici Famiglia-Genere-Specie della Seconda Coltura", FormatoVariabile.STRING, UnitaMisura.NULL,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 190))), 350),
	GREDIV2COL("Greening - Seconda Coltura Rappresentativa", FormatoVariabile.STRING, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 200))), 360),
	GRESUPAZOTODIC("Greening - Superficie Richiesta Colture Azotofissatrici", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 210))), 370),
	GRESUPAZOTODET("Greening - Superficie Determinata Colture Azotofissatrici", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 220))), 380),
	GRESUPAZOTOPOND("Greening - Superficie Colture Azotofissatrici ponderata", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 230))), 390),
	GREESEBIO("Greening - Esenzione: Azienda totalmente biologica", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 240))), 340),
	GREESESEM("Greening - Esenzione: Seminativi < 10 ha", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 250))), 350),
	GREPERCPES("Greening - % PES su Superfice agricola determinata", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 260))), 360),
	GREPERCELR("Greening - % ELR su seminativi", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 270))), 370),
	GREESEDIV("Greening - Esenzione diversificazione", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 280))), 380),
	GREESEEFA("Greening - Esenzione Aree di interesse ecologico (EFA)", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 290))), 390),
	GREDIVPERC1COL("Greening - % Prima Coltura", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 300))), 400),
	GREDIVPERC2COL("Greening - % Prima più Seconda Coltura", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 310))),
			410),
	GREDIVSUPRID("Greening - Riduzione di superficie per mancato rispetto diversificazione", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 320))), 410),
	GREEFAPERCAZOTO("Greening - % Coltura Azotofissatrici", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 330))),
			420),
	GREEFASUPRID("Greening - Riduzione di superficie per mancato rispetto EFA", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 340))), 430),
	GRESUPBASE("Greening - Superficie base di calcolo (ammissibile BPS)", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 350))), 440),
	GREIMPBASE("Greening - Importo base di calcolo", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 360))), 450),
	GRESUPRID("Greening - Sup non ammissibile mancato Rispetto Inverdimento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 370))), 460),
	GREIMPRID("Greening - Importo riduzione per Mancato Rispetto Inverdimento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 380))), 440),
	GRESUPAMM("Greening - Superficie ammissibile", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 220), new OrdineVariabile(TipologiaPassoTransizione.GREENING, 390))), 38),
	GREIMPAMM("Greening - Importo ammissibile", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.AMMISSIBILITA, 230), new OrdineVariabile(TipologiaPassoTransizione.GREENING, 400),
					new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 20), new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 20))),
			39),
	GREPERCSCOST("Greening - Percentuale Scostamento", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 410))), 470),
	GREIMPSANZ("Greening - Importo sanzione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GREENING, 420))), 480),

	PFSUPDET("Superficie determinata", FormatoVariabile.LISTA, UnitaMisura.NULL, false, 490), // greening particellaColtura
	PFSUPDETARB("Coltura permanente", FormatoVariabile.BOOL, UnitaMisura.NULL, 500), // greening particellaColtura
	PFSUPDETPP("Prato Permanente", FormatoVariabile.BOOL, UnitaMisura.NULL, 510), // greening particellaColtura
	PFSUPDETSEM("Seminativo", FormatoVariabile.BOOL, UnitaMisura.NULL, 520), // greening particellaColtura
	PFSUPDETERBAI("Erbaio", FormatoVariabile.BOOL, UnitaMisura.NULL, 530), // greening particellaColtura
	PFSUPDETLEGUM("Leguminose", FormatoVariabile.BOOL, UnitaMisura.NULL, 540), // greening particellaColtura
	PFSUPDETRIPOSO("Terreni lasciati a riposo", FormatoVariabile.BOOL, UnitaMisura.NULL, 550), // greening particellaColtura
	PFSUPDETSOMM("Coltivazioni sommerse", FormatoVariabile.BOOL, UnitaMisura.NULL, 560), // greening particellaColtura
	PFSUPDETPRIMA("Coltura Principale", FormatoVariabile.BOOL, UnitaMisura.NULL, 570), // greening particellaColtura
	PFSUPDETSECONDA("Seconda Coltura", FormatoVariabile.BOOL, UnitaMisura.NULL, 571), // greening particellaColtura
	PFAZOTO("Coltura Azotofissatrice", FormatoVariabile.BOOL, UnitaMisura.NULL, 572), // greening particellaColtura
	PFAZOTOIMP("Coltura Azotofissatrice su superifici Impegnate", FormatoVariabile.BOOL, UnitaMisura.NULL, 573), // greening particellaColtura

	PFSUPIMP("RiduzioneBPS - Superficie impegnata netta", FormatoVariabile.LISTA, UnitaMisura.NULL, false, 580),
	PFSUPELE("Superficie eleggibile GIS", FormatoVariabile.LISTA, UnitaMisura.NULL, false, 590),
	PFSUPSIGECO("Superficie eleggibile SIGECO", FormatoVariabile.LISTA, UnitaMisura.NULL, false, 600),
	PFANOMMAN("Presenza anomalie di mantenimento", FormatoVariabile.LISTA, UnitaMisura.NULL, false, 610),
	PFANOMCOORD("Presenza anomalie di coordinamento", FormatoVariabile.LISTA, UnitaMisura.NULL, false, 620),
	
	PFSUPANCOORD("Superficie Anomalie di Coordinamento", FormatoVariabile.LISTA, UnitaMisura.NULL, false, 630),

	BPSSUPELE("BPS - Superficie eleggibile(GIS)", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 30))), 111),
	BPSSUPSCOSTMAN("BPS - Superficie non ammissibile per anomalie di mantenimento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 40), new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 40))), 115),
	BPSSUPSCOSTCOO("BPS - Superficie non ammissibile per anomalie di coordinamento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 50))), 116),
	BPSSUPSCOST("BPS - Superficie in scostamento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 100))), 207),
	BPSPERCSCOST("BPS - Percentuale scostamento", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 110), new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 50))), 208),
	ISCAMP("BPS - Domanda estratta a campione", FormatoVariabile.BOOL, UnitaMisura.NULL, 112),
	BPSSUPDETIST("BPS - Superficie eleggibile determinata dall'istruttore", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 60))), 690),
	BPSIMPRID("BPS - Importo della riduzione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIDUZIONI_BPS, 120))), 209),
	BPSSUPSIGECO("BPS - Superficie accertata nel controllo in loco", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 114),
	DOMSIGECOCHIUSA("BPS - Controllo in loco chiuso", FormatoVariabile.BOOL, UnitaMisura.ETTARI, 113),
	BPSRECIDIVA("BPS - Presenza di recidiva", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 80))), 730),
	BPSYELLOWCARD("BPS - Applicazione \"yellow card\"", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 90))), 740),
	BPSYCSANZANNIPREC("BPS - Presenza di sanzioni anni precedenti", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 60))), 760),
	BPSYCIMPSANZAPREC("BPS - Sanzione ridotta anno precedente da recuperare in caso di recidiva", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 70))), 770),
	BPSIMPSANZ("BPS - Importo sanzione da sovradichiarazione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 100), new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 40))), 780),
	BPSIMPSANZREC("BPS - Importo recupero sanzione scontata anno precedente", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.SANZIONI_BPS, 110), new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 50))), 790),
	BPSDOMLIQANNOPREC("BPS - Domanda anno precedente non liquidata", FormatoVariabile.BOOL, UnitaMisura.NULL, false, 791),

	// giovane
	REQGIOVANE("Giovane - Presenza dei requisiti del Giovane Agricoltore", FormatoVariabile.BOOL, UnitaMisura.NULL,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 30))), 22),
	GIOYCSANZANNIPREC("Giovane - Presenza di sanzioni anni precedenti", FormatoVariabile.BOOL, UnitaMisura.NULL,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 70))), 26),
	GIOYCIMPSANZAPREC("Giovane - Sanzione ridotta anno precedente da recuperare in caso di recidiva", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 80))), 27),
	GIOSUPSCOST("Giovane - Superficie in scostamento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 110))),
			830),
	GIOPERCSCOST("Giovane - Percentuale Scostamento", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 120))),
			840),
	GIOIMPRID("Giovane - importo della riduzione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 130))), 850),
	GIORECIDIVA("Giovane - Presenza di recidiva", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 140))), 850),
	GIOYELLOWCARD("Giovane - Applicazione \"yellow card\"", FormatoVariabile.BOOL, UnitaMisura.NULL, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 150))), 860),
	GIOIMPSANZ("Giovane - Importo sanzione da sovradichiarazione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 160), new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 60))), 870),
	GIOIMPSANZREC("Giovane - Importo recupero sanzione scontata anno precedente", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, 170), new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 70))), 880),

	// riepilogo sanzioni
	SANZTOT("Importo Sanzioni Totale Disaccoppiato", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 150))),
			890),
	IMPSANZNORISC("Importo Sanzione non riscossa", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 160))), 900),
	BPSIMPCALCINT("BPS - importo calcolato con sanzioni", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 80))),
			901),
	GREIMPCALCINT("Greening - Importo calcolato con sanzioni", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 90))), 902),
	GIOIMPCALCINT("Giovane - importo calcolato con sanzioni", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 100))), 903),
	IMPSANZINTERINT("Importo Sanzioni Interintervento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 110))),
			904),
	BPSIMPCALC("BPS - importo calcolato con eventuale compensazione sanzioni ", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 120), new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 40))), 905),
	GREIMPCALC("Greening - importo calcolato con eventuale compensazione sanzioni ", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 130), new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 50))), 906),
	GIOIMPCALC("Giovane - importo calcolato con eventuale compensazione sanzioni ", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, 140), new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 60))), 907),

	// controlli finali
	DTPROTDOM("Data di protocollazione della domanda", FormatoVariabile.DATE, UnitaMisura.NULL, 910),
	IMPSALARI("Importo Salari Azienda", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 110))), 920),
	PERCRIDLIN1("Riduzione lineare giovane (art.51, par.2, reg. 1307/2013)", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 80))), 930),
	PERCRIDLIN2("Riduzione lineare giovane (art.51, par.3, reg. 1307/2013)", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 90))), 940),
	PERCRIDLIN3("Riduzione lineare massimale netto (art. 7, reg. 1307/2013)", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 100))), 940),
	PERCPAGAMENTO("Percentuale pagamento", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 105))), 945),
	PERCRIT("Percentuale riduzione ritardata presentazione", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 70))), 950),
	IMPCALCNORIT("Importo Calcolato (prima dell'applicazione dei giorni di ritardo)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 960),
	IMPRIDRIT("Importo Riduzione per ritardata presentazione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, false, 970),
	BPSIMPRIDRIT("BPS - Importo Riduzione per ritardata presentazione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 120))), 980),
	GREIMPRIDRIT("Greening - Importo Riduzione per ritardata presentazione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 190))), 990),
	GIOIMPRIDRIT("Giovane - Importo Riduzione per ritardata presentazione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 220))), 1000),
	IMPCALCNOLIN1("Importo Calcolato (prima dell'applicazione delle riduzioni lineari art.51 e 65)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1010),
	IMPRIDLIN1("Importo Riduzione lineare (art.51 e 65 giovani + piccoli)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1020),
	IMPCALCNOCAP("Importo Calcolato (prima dell'applicazione del capping)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1030),
	IMPBCCAP("Importo base di calcolo del capping", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1040),
	IMPRIDCAP("Importo Riduzione capping", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, false, 1050),
	IMPCALCNOLIN2("Importo Calcolato (prima dell'applicazione delle riduzioni lineari art.7)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1060),
	IMPRIDLIN2("Importo Riduzione lineare (art. 7 massimali netti)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1070),
	IMPCALC("Importo Calcolato dopo l'applicazione delle sanzioni", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 260))), 1080),
	RIDTOT("Importo totale delle riduzioni applicate alla domanda", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1090),

	// controlli intersostegno
	HASRICBPS("Esiste l'istruttoria di disaccoppiato", FormatoVariabile.BOOL, UnitaMisura.NULL, 3100),
	HASRICACS("Esiste l'istruttoria di accoppiato superficie", FormatoVariabile.BOOL, UnitaMisura.NULL, 3110),
	HASRICACZ("Esiste l'istruttoria di accoppiato zootecnia", FormatoVariabile.BOOL, UnitaMisura.NULL, 3120),
	STATOBPS("Stato della lavorazione del sostegno disaccoppiato", FormatoVariabile.STRING, UnitaMisura.NULL, 3124),
	STATOACS("Stato della lavorazione del sostegno accoppiato superficie", FormatoVariabile.STRING, UnitaMisura.NULL, 3125),
	STATOACZ("Stato della lavorazione del sostegno accoppiato zootecnia", FormatoVariabile.STRING, UnitaMisura.NULL, 3126),
	IMPMINIMOLIQ("Soglia minima di importo per consentire la liquidazione", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3130),
	DISIMPCALC("Importo Calcolato finale sostegno disaccoppiato", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3490),
	DISIMPCALCLORDO("Importo Calcolato finale sostegno disaccoppiato lordo", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,3506),
	ACSIMPCALC("Importo Calcolato finale sostegno accoppiato superficie", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3492),
	ACZIMPCALC("Importo Calcolato finale sostegno accoppiato zootecnia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3493),
	ACZIMPCALCLORDO("ACZ Importo Calcolato al lordo degli importi già pagati Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3494),

	// disciplina finanziaria
	// input
	DFPERC("Disciplina - Percentuale disciplina finanziaria", FormatoVariabile.PERCENTUALE6DECIMALI, UnitaMisura.PERCENTUALE, 1200),
	DFFR("Disciplina - Soglia della franchigia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1210),
	DFIMPLIPAGBPS("BPS - Importo già erogato per la domanda al netto della df", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,3507),
	DFIMPLIPAGGRE("Greening - Importo già erogato per la domanda al netto della df", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,3508),
	DFIMPLIPAGGIO("Giovane - Importo già erogato per la domanda al netto della df", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,3509),
	DFFRAPPDIS("Disciplina - Franchigia già applicata sostegno disaccoppiato", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3510),
	DFFRAPPDISBPS("Disciplina - BPS - Franchigia già applicata", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3511),
	DFFRAPPDISGRE("Disciplina - Greening - Franchigia già applicata", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3512),
	DFFRAPPDISGIO("Disciplina -  Giovane - Franchigia già applicata", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3513),
	DFFRAPPACZ("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3520),
	DFFRAPPACS("Disciplina - Franchigia già applicata sostegno accoppiato superficie", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3530),
	
	//input disciplna finanaziaria ACZ
	DFFRAPPACZ_310("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intervento Vacche da Latte",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3521),
	DFFRAPPACZ_311("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intevento Vacche Da Latte in zona di montagna",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3522),
	DFFRAPPACZ_313("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intevento Vacche Nutrici",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3523),
	DFFRAPPACZ_322("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intervento Vacche nutrici non iscritte",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3524),
	DFFRAPPACZ_315("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intervento Bovini macellati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3525),
	DFFRAPPACZ_316("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intervento Capi bovini macellati e allevati almeno 12 mesi",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3526),
	DFFRAPPACZ_318("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intervento Capi bovini macellati - Etichettatura",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3527),
	DFFRAPPACZ_320("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intervento Agnelle",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3528),
	DFFRAPPACZ_321("Disciplina - Franchigia già applicata sostegno accoppiato zootecnia intervento Ovicaprini macellati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3529),

	// output
	DFIMPLIQDISLORDO("Disciplina - Importo liquidabile sostegno disaccoppiato al lordo degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1240),
	DFFRPAGLORDIS("Disciplina - Franchigia sostegno disaccoppiato al lordo degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1242),
	DFFRPAGLORDISBPS("Disciplina - Franchigia sostegno disaccoppiato BPS al lordo degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1244),
	DFFRPAGLORDISGRE("Disciplina - Franchigia sostegno disaccoppiato Greening al lordo degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1246),
	DFFRPAGLORDISGIO("Disciplina - Franchigia sostegno disaccoppiato Giovane al lordo degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1248),	
	DFFRPAGDIS("Disciplina - Franchigia sostegno disaccoppiato", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1250),
	DFFRPAGDISBPS("Disciplina - Franchigia sostegno disaccoppiato BPS", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1280),
	DFFRPAGDISGRE("Disciplina - Franchigia sostegno disaccoppiato Greening", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1290),
	DFFRPAGDISGIO("Disciplina - Franchigia sostegno disaccoppiato Giovane", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1300),
	DFIMPDFDISBPS("Disciplina - Importo oltre franchigia sostegno disaccopppiato BPS con disciplina finanziaria", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1310),
	DFIMPDFDISGRE("Disciplina - Importo oltre franchigia sostegno disaccopppiato GRE con disciplina finanziaria", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1320),
	DFIMPDFDISGIO("Disciplina - Importo oltre franchigia sostegno disaccopppiato GIO con disciplina finanziaria", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1330),
	DFFRPAGACZ("Disciplina - Franchigia sostegno accoppiato zootecnia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1280),
	DFFRPAGLORACZ("Disciplina - Franchigia sostegno Accoppiato Zootecnia al lordo degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1282),
	DFIMPRIDACZ("Disciplina - Importo riduzione sostegno accoppiato zootecnia al netto degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1290),
	DFIMPLIQACZ("Disciplina - Importo liquidato sostegno accoppiato zootecnia al netto degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1300),
	DFIMPLIQACZLORDO("Disciplina - Importo liquidabile sostegno accoppiato zootecnia al lordo degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1305),
	DFFRPAGACS("Disciplina - Franchigia sostegno accoppiato superficie", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1310),
	DFIMPRIDACS("Disciplina - Importo riduzione sostegno accoppiato superficie al netto degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1320),
	DFIMPLIQACS("Disciplina - Importo liquidato sostegno accoppiato superficie al netto degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1330),
	DFIMPLIQDIS("Disciplina - Importo liquidato sostegno disaccopppiato al netto degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1335),
	DFIMPRIDDIS("Disciplina - Importo riduzione sostegno disaccopppiato sull'importo al netto degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1339),

	//output discliplina franchigia accoppiato superficie
	DFFRPAGLORACS("Disciplina - Franchigia sostegno accoppiato superficie al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770), // MIN((DFFR - (DFFRAPPDIS+DFFRAPPACZ));ACSIMPCALCLORDO)
	DFFRPAGLORACS_M8("Disciplina - Franchigia sostegno accoppiato superficie Soia al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5771), // min(DFFRPAGLORACS;ACSIMPCALCLORDO-M8)
	DFFRPAGLORACS_M9("Disciplina - Franchigia sostegno accoppiato superficie Frumento al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5772), // min(DFFRPAGLORACS-DFFRPAGLORACS-M8;ACSIMPCALC-M9)
	DFFRPAGLORACS_M10("Disciplina - Franchigia sostegno accoppiato superficie Proteaginose al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5773), // min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9;ACSIMPCALC-M10)
	DFFRPAGLORACS_M11("Disciplina - Franchigia sostegno accoppiato superficie Leguminose al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5774), // min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10;ACSIMPCALC-M11)
	DFFRPAGLORACS_M14("Disciplina - Franchigia sostegno accoppiato superficie Pomodoro al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5775), // min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10-DFFRPAGLORACS-M11;ACSIMPCALC-M14)
	DFFRPAGLORACS_M15("Disciplina - Franchigia sostegno accoppiato superficie Olivo Standard al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5776), // min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10-DFFRPAGLORACS-M11-DFFRPAGLORACS-M14;ACSIMPCALC-M15)
	DFFRPAGLORACS_M16("Disciplina - Franchigia sostegno accoppiato superficie Olivo 7.5% al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5777), // min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10-DFFRPAGLORACS-M11-DFFRPAGLORACS-M14-DFFRPAGLORACS-M15;ACSIMPCALC-M16)
	DFFRPAGLORACS_M17("Disciplina - Franchigia sostegno accoppiato superficie Olivo Qualità al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5778), // min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10-DFFRPAGLORACS-M11-DFFRPAGLORACS-M14-DFFRPAGLORACS-M15-DFFRPAGLORACS-M16;ACSIMPCALC-M17)
	DFIMPLIQACSLORDO("Disciplina - Importo liquidabile sostegno accoppiato superficie al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5779), // DFIMPLIQACS+SOMMA(DFIMPLIPAGACS-MXX)

	// Pascolo
	PASUBABOVINI("Pascolo - UBA (Bovini e Equini) pascolate", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 1340),
	PASUBAOVINI("Pascolo - UBA (Ovini e Caprini) pascolate", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 1350),
	PASUBAOVINIPARTITA("Pascolo - UBA (Ovini e Caprini) pascolate caricate per partita", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 1360),
	PASSUPELE("Pascolo - Superficie Eleggibile (GIS)", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1370),
	PASSUPSIGECO("Pascolo - Superficie accertata nel controllo in loco", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1380),
	PASSUPDETIST("Pascolo - Superficie eleggibile determinata dall’istruttore", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1390),
	PASSUPDET("Pascolo - Superficie determinata", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1400),
	PASUBATOT("Pascolo - UBA pascolate totali", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 1410),
	PASRUH("Pascolo - Carico di bestiame", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA_HA, 1420),
	PASALLLIM("Pascolo - Allevamenti limitrofi", FormatoVariabile.STRING, UnitaMisura.NULL, 1421),
	PASUBAALLLIM("Pascolo - UBA allevamenti limitrofi", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 1422),
	PASALLLON("Pascolo - Allevamenti non limitrofi", FormatoVariabile.STRING, UnitaMisura.NULL, 1423),
	PASUBAALLLON("Pascolo - UBA allevamenti non limitrofi", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 1424),
	PASSUPELEMAP("Map Pascoli - Superfici eleggibli delle particelle associate al pascolo", FormatoVariabile.MAP, UnitaMisura.NULL, false, 1425),
	PASSUPSIGEMAP("Map Pascoli - Superfici sigeco delle particelle associate al pascolo", FormatoVariabile.MAP, UnitaMisura.NULL, false, 1426),
	PASRUHMAN2("Pascolo - Carico di bestiame MAN2 (Allevamenti limitrofi)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA_HA, 1427),
	PASRUHMAN3("Pascolo - Carico di bestiame MAN3 (Tutti Gli allevamenti)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA_HA, 1428),
	PFPASCOLO("Pascolo associato", FormatoVariabile.LISTA, UnitaMisura.NULL, false, 1429),
	PASTRASPORTO("Pascolo - Verifica documentazione di trasporto", FormatoVariabile.STRING, UnitaMisura.NULL, 1430),

	// Impegni di domanda
	ACSSUPIMP_M8("ACS - Superficie Impegnata a premio Soia", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1500),
	ACSSUPIMP_M9("ACS - Superficie Impegnata Frumento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1510),
	ACSSUPIMP_M10("ACS - Superficie Impegnata Proteaginose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1520),
	ACSSUPIMP_M11("ACS - Superficie Impegnata Leguminose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1530),
	ACSSUPIMP_M14("ACS -Superficie Impegnata Pomodoro", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1540),
	ACSSUPIMP_M15("ACS - Superficie Impegnata a premio ACS Misura 15", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1550),
	ACSSUPIMP_M16("ACS - Superficie Impegnata a premio ACS Misura 16", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1560),
	ACSSUPIMP_M17("ACS - Superficie Impegnata a premio ACS Misura 17", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1570),
	ACSSUPDETIST_M8("ACS - Superficie Determinata Istruttore premio Soia", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1600),
	ACSSUPDETIST_M9("ACS - Superficie Determinata Istruttore Frumento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1610),
	ACSSUPDETIST_M10("ACS - Superficie Determinata Istruttore Proteaginose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1620),
	ACSSUPDETIST_M11("ACS - Superficie Determinata Istruttore Leguminose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1630),
	ACSSUPDETIST_M14("ACS - Superficie Determinata Istruttore Pomodoro", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1640),
	ACSSUPDETIST_M15("ACS - Superficie Determinata Istruttore Olivo Standard", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1650),
	ACSSUPDETIST_M16("ACS - Superficie Determinata Istruttore Olivo 7,5%", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1660),
	ACSSUPDETIST_M17("ACS - Superficie Determinata Istruttore Olivo Qualità", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1670),

	// Valori unitari
	ACSVAL_M8("Valore Unitario Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1700),
	ACSVAL_M9("Valore Unitario Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1710),
	ACSVAL_M10("Valore Unitario Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1720),
	ACSVAL_M11("Valore Unitario Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1730),
	ACSVAL_M14("Valore Unitario Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1740),
	ACSVAL_M15("Valore Unitario Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1750),
	ACSVAL_M16("Valore Unitario Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1760),
	ACSVAL_M17("Valore Unitario Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 1770),

	// Variabili di particella input
	PFSUPIMP_M8("Variabili di particella Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 1780),
	PFSUPIMP_M9("Variabili di particella Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 1781),
	PFSUPIMP_M10("Variabili di particella Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 1782),
	PFSUPIMP_M11("Variabili di particella Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 1783),
	PFSUPIMP_M14("Variabili di particella Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 1784),
	PFSUPIMP_M15("Variabili di particella Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 1785),
	PFSUPIMP_M16("Variabili di particella Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 1786),
	PFSUPIMP_M17("Variabili di particella Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 1787),

	// Olivo
	OLIONAZ("Registro nazionale olivo", FormatoVariabile.BOOL, UnitaMisura.NULL, 1800),
	OLIOQUAL("Registro olivo qualità", FormatoVariabile.BOOL, UnitaMisura.NULL, 1810),

	// Azienda
	SIGECO("Controllo SIGECO", FormatoVariabile.BOOL, UnitaMisura.NULL, 1820),

	// Premio Accoppiato Superficie: variabili output
	ACSSUPRIC_M8("ACS - Superficie Richiesta Soia", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1900),
	ACSSUPRIC_M9("ACS - Superficie Richiesta Frumento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1910),
	ACSSUPRIC_M10("ACS - Superficie Richiesta Proteaginose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1920),
	ACSSUPRIC_M11("ACS - Superficie Richiesta Leguminose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1930),
	ACSSUPRIC_M14("ACS - Superficie Richiesta Pomodoro", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1940),
	ACSSUPRIC_M15("ACS - Superficie Richiesta Olivo Standard", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1950),
	ACSSUPRIC_M16("ACS - Superficie Richiesta Olivo 7,5%", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1960),
	ACSSUPRIC_M17("ACS - Superficie Richiesta Olivo Qualità", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1970),
	ACSSUPRICTOT("ACS - Superficie Richiesta Totale", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 1980),

	ACSSUPDET_M8("ACS - Superficie Determinata Soia", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2000),
	ACSSUPDET_M9("ACS - Superficie Determinata Frumento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2010),
	ACSSUPDET_M10("ACS - Superficie Determinata Proteaginose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2020),
	ACSSUPDET_M11("ACS - Superficie Determinata Leguminose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2030),
	ACSSUPDET_M14("ACS - Superficie Determinata Pomodoro", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2040),
	ACSSUPDET_M15("ACS - Superficie Determinata Olivo Standard", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2050),
	ACSSUPDET_M16("ACS - Superficie Determinata Olivo 7,5%", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2060),
	ACSSUPDET_M17("ACS - Superficie Determinata Olivo Qualità", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2070),
	ACSSUPDETTOT("ACS - Superficie Determinata Totale", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2080),

	ACSSUPAMM_M8("ACS - Superficie Ammessa Soia", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2100),
	ACSSUPAMM_M9("ACS - Superficie Ammessa Frumento", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2110),
	ACSSUPAMM_M10("ACS - Superficie Ammessa Proteaginose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2120),
	ACSSUPAMM_M11("ACS - Superficie Ammessa Leguminose", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2130),
	ACSSUPAMM_M14("ACS - Superficie Ammessa Pomodoro", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2140),
	ACSSUPAMM_M15("ACS - Superficie Ammessa Olivo Standard", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2150),
	ACSSUPAMM_M16("ACS - Superficie Ammessa Olivo 7,5%", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2160),
	ACSSUPAMM_M17("ACS - Superficie Ammessa Olivo Qualità", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2170),
	ACSSUPAMMTOT("ACS - Superficie Ammessa Totale", FormatoVariabile.NUMERO4DECIMALI, UnitaMisura.ETTARI, 2180),

	ACSIMPRIC_M8("ACS - Importo Richiesto Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2200),
	ACSIMPRIC_M9("ACS - Importo Richiesto Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2210),
	ACSIMPRIC_M10("ACS - Importo Richiesto Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2220),
	ACSIMPRIC_M11("ACS - Importo Richiesto Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2230),
	ACSIMPRIC_M14("ACS - Importo Richiesto Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2240),
	ACSIMPRIC_M15("ACS - Importo Richiesto Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2250),
	ACSIMPRIC_M16("ACS - Importo Richiesto Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2260),
	ACSIMPRIC_M17("ACS - Importo Richiesto Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2270),
	ACSIMPRICTOT("ACS - Importo Richiesto Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2280),

	ACSIMPRID_M8("ACS - Importo Riduzione Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2300),
	ACSIMPRID_M9("ACS - Importo Riduzione Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2310),
	ACSIMPRID_M10("ACS - Importo Riduzione Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2320),
	ACSIMPRID_M11("ACS - Importo Riduzione Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2330),
	ACSIMPRID_M14("ACS - Importo Riduzione Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2340),
	ACSIMPRID_M15("ACS - Importo Riduzione Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2350),
	ACSIMPRID_M16("ACS - Importo Riduzione Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2360),
	ACSIMPRID_M17("ACS - Importo Riduzione Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2370),
	ACSIMPRIDTOT("ACS - Importo Riduzione Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2380),

	ACSIMPAMM_M8("ACS - Importo Ammesso Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2400),
	ACSIMPAMM_M9("ACS - Importo Ammesso Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2410),
	ACSIMPAMM_M10("ACS - Importo Ammesso Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2420),
	ACSIMPAMM_M11("ACS - Importo Ammesso Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2430),
	ACSIMPAMM_M14("ACS - Importo Ammesso Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2440),
	ACSIMPAMM_M15("ACS - Importo Ammesso Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2450),
	ACSIMPAMM_M16("ACS - Importo Ammesso Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2460),
	ACSIMPAMM_M17("ACS - Importo Ammesso Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2470),
	ACSIMPAMMTOT("ACS - Importo Ammesso Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2480),

	ACSIMPRIDRIT_M8("ACS - Importo Riduzione Ritardo Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2500),
	ACSIMPRIDRIT_M9("ACS - Importo Riduzione Ritardo Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2510),
	ACSIMPRIDRIT_M10("ACS - Importo Riduzione Ritardo Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2520),
	ACSIMPRIDRIT_M11("ACS - Importo Riduzione Ritardo Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2530),
	ACSIMPRIDRIT_M14("ACS - Importo Riduzione Ritardo Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2540),
	ACSIMPRIDRIT_M15("ACS - Importo Riduzione Ritardo Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2550),
	ACSIMPRIDRIT_M16("ACS - Importo Riduzione Ritardo Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2560),
	ACSIMPRIDRIT_M17("ACS - Importo Riduzione Ritardo Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2570),
	ACSIMPRIDRITTOT("ACS - Importo Riduzione Ritardo Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2580),

	ACSIMPCALC_M8("Importo Calcolato finale sostegno accoppiato superficie Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2600),
	ACSIMPCALC_M9("Importo Calcolato finale sostegno accoppiato superficie Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2610),
	ACSIMPCALC_M10("Importo Calcolato finale sostegno accoppiato superficie Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2620),
	ACSIMPCALC_M11("Importo Calcolato finale sostegno accoppiato superficie Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2630),
	ACSIMPCALC_M14("Importo Calcolato finale sostegno accoppiato superficie Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2640),
	ACSIMPCALC_M15("Importo Calcolato finale sostegno accoppiato superficie Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2650),
	ACSIMPCALC_M16("Importo Calcolato finale sostegno accoppiato superficie Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2660),
	ACSIMPCALC_M17("Importo Calcolato finale sostegno accoppiato superficie Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2670),
	ACSIMPCALCTOT("ACS - Importo Calcolato Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2680),

	// Variabili relative agli importi lordi
	ACSIMPCALCLORDO_M8("ACS - Importo Calcolato al lordo degli importi già pagati Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2600),
	ACSIMPCALCLORDO_M9("ACS - Importo Calcolato al lordo degli importi già pagati Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2610),
	ACSIMPCALCLORDO_M10("ACS - Importo Calcolato al lordo degli importi già pagati Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2620),
	ACSIMPCALCLORDO_M11("ACS - Importo Calcolato al lordo degli importi già pagati Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2630),
	ACSIMPCALCLORDO_M14("ACS - Importo Calcolato al lordo degli importi già pagati Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2640),
	ACSIMPCALCLORDO_M15("ACS - Importo Calcolato al lordo degli importi già pagati Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2650),
	ACSIMPCALCLORDO_M16("ACS - Importo Calcolato al lordo degli importi già pagati Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2660),
	ACSIMPCALCLORDO_M17("ACS - Importo Calcolato al lordo degli importi già pagati Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2670),
	ACSIMPCALCLORDOTOT("ACS - Importo Calcolato al lordo degli importi già pagati Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2680),

	// Variabili relative agli importi gia erogati per sostegno acs
	ACSIMPERO_M8("ACS - Importo erogato in pagamenti precedenti Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2600),
	ACSIMPERO_M9("ACS - Importo erogato in pagamenti precedenti Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2610),
	ACSIMPERO_M10("ACS - Importo erogato in pagamenti precedenti Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2620),
	ACSIMPERO_M11("ACS - Importo erogato in pagamenti precedenti Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2630),
	ACSIMPERO_M14("ACS - Importo erogato in pagamenti precedenti Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2640),
	ACSIMPERO_M15("ACS - Importo erogato in pagamenti precedenti Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2650),
	ACSIMPERO_M16("ACS - Importo erogato in pagamenti precedenti Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2660),
	ACSIMPERO_M17("ACS - Importo erogato in pagamenti precedenti Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2670),
	ACSIMPEROTOT("ACS - Importo erogato in pagamenti precedenti Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 2680),

	// Variabili di particella output
	ACSCTRLREG_M8("ACS - Controllo Regioni Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 2700),
	ACSCTRLREG_M9("ACS - Controllo Regioni Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 2710),
	ACSCTRLREG_M10("ACS - Controllo Regioni Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 2720),
	ACSCTRLREG_M11("ACS - Controllo Regioni Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 2730),
	ACSCTRLREG_M14("ACS - Controllo Regioni Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 2740),
	ACSCTRLREG_M15("ACS - Controllo Regioni Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 2750),
	ACSCTRLREG_M16("ACS - Controllo Regioni Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 2760),
	ACSCTRLREG_M17("ACS - Controllo Regioni Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 2770),

	ACSCTRLCOLT_M8("ACS - Controllo Colture Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 2800),
	ACSCTRLCOLT_M9("ACS - Controllo Colture Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 2810),
	ACSCTRLCOLT_M10("ACS - Controllo Colture Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 2820),
	ACSCTRLCOLT_M11("ACS - Controllo Colture Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 2830),
	ACSCTRLCOLT_M14("ACS - Controllo Colture Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 2840),
	ACSCTRLCOLT_M15("ACS - Controllo Colture Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 2850),
	ACSCTRLCOLT_M16("ACS - Controllo Colture Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 2860),
	ACSCTRLCOLT_M17("ACS - Controllo Colture Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 2870),

	ACSCTRLCOORD_M8("ACS - Controllo Anomalie Coordinamento Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 2900),
	ACSCTRLCOORD_M9("ACS - Controllo Anomalie Coordinamento Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 2910),
	ACSCTRLCOORD_M10("ACS - Controllo Anomalie Coordinamento Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 2920),
	ACSCTRLCOORD_M11("ACS - Controllo Anomalie Coordinamento Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 2930),
	ACSCTRLCOORD_M14("ACS - Controllo Anomalie Coordinamento Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 2940),
	ACSCTRLCOORD_M15("ACS - Controllo Anomalie Coordinamento Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 2950),
	ACSCTRLCOORD_M16("ACS - Controllo Anomalie Coordinamento Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 2960),
	ACSCTRLCOORD_M17("ACS - Controllo Anomalie Coordinamento Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 2970),

	ACSPFSUPRIC_M8("ACS - Superficie Richiesta Particella Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 3000),
	ACSPFSUPRIC_M9("ACS - Superficie Richiesta Particella Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 3010),
	ACSPFSUPRIC_M10("ACS - Superficie Richiesta Particella Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3020),
	ACSPFSUPRIC_M11("ACS - Superficie Richiesta Particella Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3030),
	ACSPFSUPRIC_M14("ACS - Superficie Richiesta Particella Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 3040),
	ACSPFSUPRIC_M15("ACS - Superficie Richiesta Particella Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 3050),
	ACSPFSUPRIC_M16("ACS - Superficie Richiesta Particella Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 3060),
	ACSPFSUPRIC_M17("ACS - Superficie Richiesta Particella Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 3070),

	ACSPFSUPCTRLLOCO_M8("ACS - Superficie da Controllo in Loco Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 3100),
	ACSPFSUPCTRLLOCO_M9("ACS - Superficie da Controllo in Loco Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 3110),
	ACSPFSUPCTRLLOCO_M10("ACS - Superficie da Controllo in Loco Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3120),
	ACSPFSUPCTRLLOCO_M11("ACS - Superficie da Controllo in Loco Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3130),
	ACSPFSUPCTRLLOCO_M14("ACS - Superficie da Controllo in Loco Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 3140),
	ACSPFSUPCTRLLOCO_M15("ACS - Superficie da Controllo in Loco Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 3150),
	ACSPFSUPCTRLLOCO_M16("ACS - Superficie da Controllo in Loco Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 3160),
	ACSPFSUPCTRLLOCO_M17("ACS - Superficie da Controllo in Loco Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 3170),

	ACSPFSUPELEGIS_M8("ACS - Superficie Eleggibile GIS Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 3200),
	ACSPFSUPELEGIS_M9("ACS - Superficie Eleggibile GIS Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 3210),
	ACSPFSUPELEGIS_M10("ACS - Superficie Eleggibile GIS Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3220),
	ACSPFSUPELEGIS_M11("ACS - Superficie Eleggibile GIS Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3230),
	ACSPFSUPELEGIS_M14("ACS - Superficie Eleggibile GIS Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 3240),
	ACSPFSUPELEGIS_M15("ACS - Superficie Eleggibile GIS Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 3250),
	ACSPFSUPELEGIS_M16("ACS - Superficie Eleggibile GIS Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 3260),
	ACSPFSUPELEGIS_M17("ACS - Superficie Eleggibile GIS Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 3270),

	ACSPFSUPDET_M8("ACS - Superficie Determinata Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 3300),
	ACSPFSUPDET_M9("ACS - Superficie Determinata Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 3310),
	ACSPFSUPDET_M10("ACS - Superficie Determinata Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3320),
	ACSPFSUPDET_M11("ACS - Superficie Determinata Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3330),
	ACSPFSUPDET_M14("ACS - Superficie Determinata Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 3340),
	ACSPFSUPDET_M15("ACS - Superficie Determinata Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 3350),
	ACSPFSUPDET_M16("ACS - Superficie Determinata Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 3360),
	ACSPFSUPDET_M17("ACS - Superficie Determinata Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 3370),
	
	ACSPFSUPANCOORD_M8("ACS - Superficie Anomalie di Coordinamento Soia", FormatoVariabile.LISTA, UnitaMisura.NULL, 3371),
	ACSPFSUPANCOORD_M9("ACS - Superficie Anomalie di Coordinamento Frumento", FormatoVariabile.LISTA, UnitaMisura.NULL, 3372),
	ACSPFSUPANCOORD_M10("ACS - Superficie Anomalie di Coordinamento Proteaginose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3373),
	ACSPFSUPANCOORD_M11("ACS - Superficie Anomalie di Coordinamento Leguminose", FormatoVariabile.LISTA, UnitaMisura.NULL, 3374),
	ACSPFSUPANCOORD_M14("ACS - Superficie Anomalie di Coordinamento Pomodoro", FormatoVariabile.LISTA, UnitaMisura.NULL, 3375),
	ACSPFSUPANCOORD_M15("ACS - Superficie Anomalie di Coordinamento Olivo Standard", FormatoVariabile.LISTA, UnitaMisura.NULL, 3376),
	ACSPFSUPANCOORD_M16("ACS - Superficie Anomalie di Coordinamento Olivo 7,5%", FormatoVariabile.LISTA, UnitaMisura.NULL, 3377),
	ACSPFSUPANCOORD_M17("ACS - Superficie Anomalie di Coordinamento Olivo Qualità", FormatoVariabile.LISTA, UnitaMisura.NULL, 3378),

	BPSIMPRIDLIN1("BPS - Importo riduzione lineare giovane (art.51, par.2, reg. 1307/2013)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 130))), 3380),
	BPSIMPBCCAP("BPS - Importo base di calcolo capping", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 140))),
			3390),
	BPSIMPRIDCAP50("BPS - Importo Riduzione capping 50%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 150))),
			3400),
	BPSIMPRIDCAP100("BPS - Importo Riduzione capping 100%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 160))),
			3410),
	BPSIMPRIDLIN3("BPS - Importo riduzione lineare massimale netto (art. 7, reg. 1307/2013)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 170))), 3420),
	
	BPSIMPCALCFINLORDO("BPS - Importo Calcolato finale lordo", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 175))), //ordinamento all'interno del passo 
			3503),
	
	BPSIMPCALCFIN("BPS - Importo Calcolato finale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 180))), 3430),
	GREIMPRIDLIN3("Greening - Importo riduzione lineare massimale netto (art. 7, reg. 1307/2013)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 200))), 3440),
	GREIMPCALCFINLORDO("Greening - Importo Calcolato finale lordo", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 205))),
			3504),
	GREIMPCALCFIN("Greening - Importo Calcolato finale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 210))),
			3450),
	GIOIMPRIDLIN2("Giovane - Riduzione lineare giovane (art.51, par.3, reg. 1307/2013)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 230))), 3460),
	GIOIMPRIDLIN3("Giovane - Riduzione lineare massimale netto (art. 7, reg. 1307/2013)", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO,
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 240))), 3470),
	GIOIMPCALCFINLORDO("Giovane - Importo Calcolato finale lordo", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 245))),
			3505),
	GIOIMPCALCFIN("Giovane - Importo Calcolato finale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 250))),
			3480),
	IMPCALCFIN("Importo Calcolato finale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 291))), 3501),
	BPSIMPEROGATO("BPS - Importo già erogato per la domanda", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 275))), 3485),
	GREIMPEROGATO("Greening - Importo già erogato per la domanda", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 280))), 3490),
	GIOIMPEROGATO("Giovane - Importo già erogato per la domanda", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 285))), 3495),
	IMPCALCFINLORDO("Importo Calcolato finale al lordo degli importi già pagati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 290))), 3500),

	// Accoppiato zootecnia
	// Variabili input
	ACZCAPIRIC_310("ACZ - Numero Capi Richiesti Lordi Intervento 310", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4000),
	ACZCAPIRIC_311("ACZ - Numero Capi Richiesti Lordi Intervento 311", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4010),
	ACZCAPIRIC_313("ACZ - Numero Capi Richiesti Lordi Intervento 313", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4020),
	ACZCAPIRIC_322("ACZ - Numero Capi Richiesti Lordi Intervento 322", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4030),
	ACZCAPIRIC_315("ACZ - Numero Capi Richiesti Lordi Intervento 315", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4040),
	ACZCAPIRIC_316("ACZ - Numero Capi Richiesti Lordi Intervento 316", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4050),
	ACZCAPIRIC_318("ACZ - Numero Capi Richiesti Lordi Intervento 318", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4060),
	ACZCAPIRIC_320("ACZ - Numero Capi Richiesti Lordi Intervento 320", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4070),
	ACZCAPIRIC_321("ACZ - Numero Capi Richiesti Lordi Intervento 321", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4080),
	ACZCAPIRICTOT("ACZ - Numero Capi Richiesti Lordi Totali", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4090),
	
	// numero di ovini adulti necessario per calcolo importo richiesto netto del 320 
	ACZOVIADULTI_320("ACZ - Numero Ovini Adulti Intevento 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.NULL, 4095),

	ACZCAPIDUP_310("ACZ - Numero Capi Duplicati Intervento 310", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4100),
	ACZCAPIDUP_311("ACZ - Numero Capi Duplicati Intervento 311", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4110),
	ACZCAPIDUP_313("ACZ - Numero Capi Duplicati Intervento 313", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4120),
	ACZCAPIDUP_322("ACZ - Numero Capi Duplicati Intervento 322", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4130),
	ACZCAPIDUP_315("ACZ - Numero Capi Duplicati Intervento 315", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4140),
	ACZCAPIDUP_316("ACZ - Numero Capi Duplicati Intervento 316", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4150),
	ACZCAPIDUP_318("ACZ - Numero Capi Duplicati Intervento 318", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4160),
	ACZCAPIDUP_320("ACZ - Numero Capi Duplicati Intervento 320", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4170),
	ACZCAPIDUP_321("ACZ - Numero Capi Duplicati Intervento 321", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4180),
	ACZCAPIDUPTOT("ACZ - Numero Capi Duplicati Totali", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4190),

	ACZCAPIRICNET_310("ACZ - Numero Capi Richiesti Netti Intervento 310", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4200),
	ACZCAPIRICNET_311("ACZ - Numero Capi Richiesti Netti Intervento 311", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4210),
	ACZCAPIRICNET_313("ACZ - Numero Capi Richiesti Netti Intervento 313", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4220),
	ACZCAPIRICNET_322("ACZ - Numero Capi Richiesti Netti Intervento 322", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4230),
	ACZCAPIRICNET_315("ACZ - Numero Capi Richiesti Netti Intervento 315", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4240),
	ACZCAPIRICNET_316("ACZ - Numero Capi Richiesti Netti Intervento 316", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4250),
	ACZCAPIRICNET_318("ACZ - Numero Capi Richiesti Netti Intervento 318", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4260),
	ACZCAPIRICNET_320("ACZ - Numero Capi Richiesti Netti Intervento 320", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4270),
	ACZCAPIRICNET_321("ACZ - Numero Capi Richiesti Netti Intervento 321", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4280),
	ACZCAPIRICNETTOT("ACZ - Numero Capi Richiesti Netti Totali", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4290),

	ACZCAPISANZ_310("ACZ - Numero Capi Ammessi con Sanzione Intervento 310", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4300),
	ACZCAPISANZ_311("ACZ - Numero Capi Ammessi con Sanzione Intervento 311", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4310),
	ACZCAPISANZ_313("ACZ - Numero Capi Ammessi con Sanzione Intervento 313", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4320),
	ACZCAPISANZ_322("ACZ - Numero Capi Ammessi con Sanzione Intervento 322", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4330),
	ACZCAPISANZ_315("ACZ - Numero Capi Ammessi con Sanzione Intervento 315", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4340),
	ACZCAPISANZ_316("ACZ - Numero Capi Ammessi con Sanzione Intervento 316", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4350),
	ACZCAPISANZ_318("ACZ - Numero Capi Ammessi con Sanzione Intervento 318", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4360),
	ACZCAPISANZ_320("ACZ - Numero Capi Ammessi con Sanzione Intervento 320", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4370),
	ACZCAPISANZ_321("ACZ - Numero Capi Ammessi con Sanzione Intervento 321", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4380),
	ACZCAPISANZTOT("ACZ - Numero Capi Ammessi con Sanzione Totali", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4390),

	ACZCAPIACC_310("ACZ - Numero Capi Accertati Intervento 310", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4400),
	ACZCAPIACC_311("ACZ - Numero Capi Accertati Intervento 311", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4410),
	ACZCAPIACC_313("ACZ - Numero Capi Accertati Intervento 313", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4420),
	ACZCAPIACC_322("ACZ - Numero Capi Accertati Intervento 322", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4430),
	ACZCAPIACC_315("ACZ - Numero Capi Accertati Intervento 315", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4440),
	ACZCAPIACC_316("ACZ - Numero Capi Accertati Intervento 316", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4450),
	ACZCAPIACC_318("ACZ - Numero Capi Accertati Intervento 318", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4460),
	ACZCAPIACC_320("ACZ - Numero Capi Accertati Intervento 320", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4470),
	ACZCAPIACC_321("ACZ - Numero Capi Accertati Intervento 321", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4480),
	ACZCAPIACCTOT("ACZ - Numero Capi Accertati Totali", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 4490),

	ACZUBA_LAT("ACZ - Numero UBA Ammessi Intervento 310-311-313-322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 4500),
	ACZUBA_MAC("ACZ - Numero UBA Ammessi Intervento 315-316-318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 4510),
	ACZUBA_OVI("ACZ - Numero UBA Ammessi Intervento 320-321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 4520),
	ACZUBATOT("ACZ - Numero UBA Ammessi Totali", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.UBA, 4530),

	ACZVAL_310("ACZ - Valore Unitario Intervento 310", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4600),
	ACZVAL_311("ACZ - Valore Unitario Intervento 311", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4610),
	ACZVAL_313("ACZ - Valore Unitario Intervento 313", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4620),
	ACZVAL_322("ACZ - Valore Unitario Intervento 322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4630),
	ACZVAL_315("ACZ - Valore Unitario Intervento 315", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4640),
	ACZVAL_316("ACZ - Valore Unitario Intervento 316", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4650),
	ACZVAL_318("ACZ - Valore Unitario Intervento 318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4660),
	ACZVAL_320("ACZ - Valore Unitario Intervento 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4670),
	ACZVAL_321("ACZ - Valore Unitario Intervento 321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 4680),

	AZCMPBOV("Azienda Campione Bovini", FormatoVariabile.BOOL, UnitaMisura.NULL, 4700),
	AZCMPOVI("Azienda Campione Ovicaprini", FormatoVariabile.BOOL, UnitaMisura.NULL, 4710),
	ACZCONTROLLILOCO("ACZ - Esito controlli in loco", FormatoVariabile.BOOL, UnitaMisura.NULL, 4120),

	PERCSANZDET_310("ACZ - Percentuale Sanzione Determinata Intervento 310", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4800),
	PERCSANZDET_311("ACZ - Percentuale Sanzione Determinata Intervento 311", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4810),
	PERCSANZDET_313("ACZ - Percentuale Sanzione Determinata Intervento 313", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4820),
	PERCSANZDET_322("ACZ - Percentuale Sanzione Determinata Intervento 322", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4830),
	PERCSANZDET_315("ACZ - Percentuale Sanzione Determinata Intervento 315", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4840),
	PERCSANZDET_316("ACZ - Percentuale Sanzione Determinata Intervento 316", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4850),
	PERCSANZDET_318("ACZ - Percentuale Sanzione Determinata Intervento 318", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4860),
	PERCSANZDET_320("ACZ - Percentuale Sanzione Determinata Intervento 320", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4870),
	PERCSANZDET_321("ACZ - Percentuale Sanzione Determinata Intervento 321", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4880),

	PERCSANZ_310("ACZ - Percentuale Sanzione Applicata Intervento 310", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4900),
	PERCSANZ_311("ACZ - Percentuale Sanzione Applicata Intervento 311", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4910),
	PERCSANZ_313("ACZ - Percentuale Sanzione Applicata Intervento 313", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4920),
	PERCSANZ_322("ACZ - Percentuale Sanzione Applicata Intervento 322", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4930),
	PERCSANZ_315("ACZ - Percentuale Sanzione Applicata Intervento 315", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4940),
	PERCSANZ_316("ACZ - Percentuale Sanzione Applicata Intervento 316", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4950),
	PERCSANZ_318("ACZ - Percentuale Sanzione Applicata Intervento 318", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4960),
	PERCSANZ_320("ACZ - Percentuale Sanzione Applicata Intervento 320", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4970),
	PERCSANZ_321("ACZ - Percentuale Sanzione Applicata Intervento 321", FormatoVariabile.PERCENTUALE, UnitaMisura.PERCENTUALE, 4980),

	// Variabili output
	ACZIMPRICNET_310("ACZ - Importo Richiesto Netto Intervento 310", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5000),
	ACZIMPRICNET_311("ACZ - Importo Richiesto Netto Intervento 311", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5010),
	ACZIMPRICNET_313("ACZ - Importo Richiesto Netto Intervento 313", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5020),
	ACZIMPRICNET_322("ACZ - Importo Richiesto Netto Intervento 322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5030),
	ACZIMPRICNET_315("ACZ - Importo Richiesto Netto Intervento 315", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5040),
	ACZIMPRICNET_316("ACZ - Importo Richiesto Netto Intervento 316", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5050),
	ACZIMPRICNET_318("ACZ - Importo Richiesto Netto Intervento 318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5060),
	ACZIMPRICNET_320("ACZ - Importo Richiesto Netto Intervento 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5070),
	ACZIMPRICNET_321("ACZ - Importo Richiesto Netto Intervento 321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5080),
	ACZIMPRICNETTOT("ACZ - Importo Richiesto Netto Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5090),

	ACZIMPRID_310("ACZ - Importo Riduzione Intervento 310", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5100),
	ACZIMPRID_311("ACZ - Importo Riduzione Intervento 311", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5110),
	ACZIMPRID_313("ACZ - Importo Riduzione Intervento 313", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5120),
	ACZIMPRID_322("ACZ - Importo Riduzione Intervento 322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5130),
	ACZIMPRID_315("ACZ - Importo Riduzione Intervento 315", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5140),
	ACZIMPRID_316("ACZ - Importo Riduzione Intervento 316", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5150),
	ACZIMPRID_318("ACZ - Importo Riduzione Intervento 318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5160),
	ACZIMPRID_320("ACZ - Importo Riduzione Intervento 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5170),
	ACZIMPRID_321("ACZ - Importo Riduzione Intervento 321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5180),
	ACZIMPRIDTOT("ACZ - Importo Riduzione Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5190),

	ACZIMPACC_310("ACZ - Importo Accertato Intervento 310", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5200),
	ACZIMPACC_311("ACZ - Importo Accertato Intervento 311", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5210),
	ACZIMPACC_313("ACZ - Importo Accertato Intervento 313", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5220),
	ACZIMPACC_322("ACZ - Importo Accertato Intervento 322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5230),
	ACZIMPACC_315("ACZ - Importo Accertato Intervento 315", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5240),
	ACZIMPACC_316("ACZ - Importo Accertato Intervento 316", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5250),
	ACZIMPACC_318("ACZ - Importo Accertato Intervento 318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5260),
	ACZIMPACC_320("ACZ - Importo Accertato Intervento 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5270),
	ACZIMPACC_321("ACZ - Importo Accertato Intervento 321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5280),
	ACZIMPACCTOT("ACZ - Importo Accertato Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5290),

	ACZIMPRIDSANZ_310("ACZ - Importo Riduzione Sanzione Intervento 310", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5300),
	ACZIMPRIDSANZ_311("ACZ - Importo Riduzione Sanzione Intervento 311", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5310),
	ACZIMPRIDSANZ_313("ACZ - Importo Riduzione Sanzione Intervento 313", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5320),
	ACZIMPRIDSANZ_322("ACZ - Importo Riduzione Sanzione Intervento 322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5330),
	ACZIMPRIDSANZ_315("ACZ - Importo Riduzione Sanzione Intervento 315", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5340),
	ACZIMPRIDSANZ_316("ACZ - Importo Riduzione Sanzione Intervento 316", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5350),
	ACZIMPRIDSANZ_318("ACZ - Importo Riduzione Sanzione Intervento 318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5360),
	ACZIMPRIDSANZ_320("ACZ - Importo Riduzione Sanzione Intervento 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5370),
	ACZIMPRIDSANZ_321("ACZ - Importo Riduzione Sanzione Intervento 321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5380),
	ACZIMPRIDSANZTOT("ACZ - Importo Riduzione Sanzione Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5390),

	ACZIMPRIDRIT_310("ACZ - Importo Riduzione Ritardo Intervento 310", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5400),
	ACZIMPRIDRIT_311("ACZ - Importo Riduzione Ritardo Intervento 311", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5410),
	ACZIMPRIDRIT_313("ACZ - Importo Riduzione Ritardo Intervento 313", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5420),
	ACZIMPRIDRIT_322("ACZ - Importo Riduzione Ritardo Intervento 322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5430),
	ACZIMPRIDRIT_315("ACZ - Importo Riduzione Ritardo Intervento 315", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5440),
	ACZIMPRIDRIT_316("ACZ - Importo Riduzione Ritardo Intervento 316", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5450),
	ACZIMPRIDRIT_318("ACZ - Importo Riduzione Ritardo Intervento 318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5460),
	ACZIMPRIDRIT_320("ACZ - Importo Riduzione Ritardo Intervento 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5470),
	ACZIMPRIDRIT_321("ACZ - Importo Riduzione Ritardo Intervento 321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5480),
	ACZIMPRIDRITTOT("ACZ - Importo Riduzione Ritardo Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5490),

	// Importi lordi
	ACZIMPCALCLORDO_310("Importo Calcolato al lordo degli importi già pagati Intervento Vacche da Latte", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5500),
	ACZIMPCALCLORDO_311("Importo Calcolato al lordo degli importi già pagati Intervento Vacche Da Latte in zona di montagna", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5510),
	ACZIMPCALCLORDO_313("Importo Calcolato al lordo degli importi già pagati Intervento Vacche Nutrici", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5520),
	ACZIMPCALCLORDO_322("Importo Calcolato al lordo degli importi già pagati Intervento Vacche nutrici non iscritte", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5530),
	ACZIMPCALCLORDO_315("Importo Calcolato al lordo degli importi già pagati Intervento Bovini macellati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5540),
	ACZIMPCALCLORDO_316("Importo Calcolato al lordo degli importi già pagati Intervento Capi bovini macellati e allevati almeno 12 mesi", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5550),
	ACZIMPCALCLORDO_318("Importo Calcolato al lordo degli importi già pagati Intervento Capi bovini macellati - Etichettatura", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5560),
	ACZIMPCALCLORDO_320("Importo Calcolato al lordo degli importi già pagati Intervento Agnelle", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5570),
	ACZIMPCALCLORDO_321("Importo Calcolato al lordo degli importi già pagati Intervento Ovicaprini macellati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5580),
	ACZIMPCALCLORDOTOT("Importo Calcolato al lordo degli importi già pagati Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5590),

	ACZIMPCALC_310("Importo Calcolato finale sostegno accoppiato zootecnia Vacche da Latte", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5500),
	ACZIMPCALC_311("Importo Calcolato finale sostegno accoppiato zootecnia Vacche Da Latte in zona di montagna", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5510),
	ACZIMPCALC_313("Importo Calcolato finale sostegno accoppiato zootecnia Vacche Nutrici", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5520),
	ACZIMPCALC_322("Importo Calcolato finale sostegno accoppiato zootecnia Vacche nutrici non iscritte", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5530),
	ACZIMPCALC_315("Importo Calcolato finale sostegno accoppiato zootecnia Bovini macellati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5540),
	ACZIMPCALC_316("Importo Calcolato finale sostegno accoppiato zootecnia Capi bovini macellati e allevati almeno 12 mesi", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5550),
	ACZIMPCALC_318("Importo Calcolato finale sostegno accoppiato zootecnia Capi bovini macellati - Etichettatura", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5560),
	ACZIMPCALC_320("Importo Calcolato finale sostegno accoppiato zootecnia Agnelle", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5570),
	ACZIMPCALC_321("Importo Calcolato finale sostegno accoppiato zootecnia Ovicaprini macellati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5580),
	ACZIMPCALCTOT("ACZ - Importo Calcolato Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5590),

	// Importi erogati
	ACZIMPERO_310("ACZ - Importo erogato in pagamenti precedenti 310", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5500),
	ACZIMPERO_311("ACZ - Importo erogato in pagamenti precedenti 311", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5510),
	ACZIMPERO_313("ACZ - Importo erogato in pagamenti precedenti 313", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5520),
	ACZIMPERO_322("ACZ - Importo erogato in pagamenti precedenti 322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5530),
	ACZIMPERO_315("ACZ - Importo erogato in pagamenti precedenti 315", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5540),
	ACZIMPERO_316("ACZ - Importo erogato in pagamenti precedenti 316", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5550),
	ACZIMPERO_318("ACZ - Importo erogato in pagamenti precedenti 318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5560),
	ACZIMPERO_320("ACZ - Importo erogato in pagamenti precedenti 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5570),
	ACZIMPERO_321("ACZ - Importo erogato in pagamenti precedenti 321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5580),
	ACZIMPEROTOT("ACZ - Importo erogato in pagamenti precedenti Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5590),

	// Disciplina finanziaria Zootecnia
	DFFRPAGACZ_310("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Vacche da Latte", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5600),
	DFFRPAGACZ_311("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Vacche Da Latte in zona di montagna", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5610),
	DFFRPAGACZ_313("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Vacche Nutrici", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5620),
	DFFRPAGACZ_322("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Vacche nutrici non iscritte", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5630),
	DFFRPAGACZ_315("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Bovini macellati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5640),
	DFFRPAGACZ_316("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Capi bovini macellati e allevati almeno 12 mesi", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5650),
	DFFRPAGACZ_318("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Capi bovini macellati - Etichettatura", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5660),
	DFFRPAGACZ_320("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Agnelle", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5670),
	DFFRPAGACZ_321("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Ovicaprini macellati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5680),

	DFIMPDFDISACZ_310("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Vacche da Latte", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5690),
	DFIMPDFDISACZ_311("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Vacche Da Latte in zona di montagna", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5700),
	DFIMPDFDISACZ_313("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Vacche Nutrici", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5710),
	DFIMPDFDISACZ_322("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Vacche nutrici non iscritte", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5720),
	DFIMPDFDISACZ_315("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Bovini macellati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5730),
	DFIMPDFDISACZ_316("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Capi bovini macellati e allevati almeno 12 mesi", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5740),
	DFIMPDFDISACZ_318("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Capi bovini macellati - Etichettatura", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5750),
	DFIMPDFDISACZ_320("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Agnelle", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5760),
	DFIMPDFDISACZ_321("Disciplina - Importo oltre franchigia sostegno accoppiato zootecnia con disciplina finanziaria interventi Ovicaprini macellati", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),

	// Pagamenti già liquidati - Accoppiato Superficie
	DFIMPLIPAGACS_M8("Importo già erogato per la domanda al netto della df Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3379),
	DFIMPLIPAGACS_M9("Importo già erogato per la domanda al netto della df Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3380),
	DFIMPLIPAGACS_M10("Importo già erogato per la domanda al netto della df Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3381),
	DFIMPLIPAGACS_M11("Importo già erogato per la domanda al netto della df Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3382),
	DFIMPLIPAGACS_M14("Importo già erogato per la domanda al netto della df Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3383),
	DFIMPLIPAGACS_M15("Importo già erogato per la domanda al netto della df Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3384),
	DFIMPLIPAGACS_M16("Importo già erogato per la domanda al netto della df Olivo 7,5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3385),
	DFIMPLIPAGACS_M17("Importo già erogato per la domanda al netto della df Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 3386),

	//importi liquidati
	DFIMPLIPAGACZ_310("ACZ - Importo già erogato per la domanda al netto della df intervento Vacche da Latte",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFIMPLIPAGACZ_311("ACZ - Importo già erogato per la domanda al netto della df intervento Vacche Da Latte in zona di montagna",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFIMPLIPAGACZ_313("ACZ - Importo già erogato per la domanda al netto della df intervento Vacche Nutrici",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFIMPLIPAGACZ_322("ACZ - Importo già erogato per la domanda al netto della df intervento Vacche nutrici non iscritte",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFIMPLIPAGACZ_315("ACZ - Importo già erogato per la domanda al netto della df intervento Bovini macellati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFIMPLIPAGACZ_316("ACZ - Importo già erogato per la domanda al netto della df intervento Capi bovini macellati e allevati almeno 12 mesi",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFIMPLIPAGACZ_318("ACZ - Importo già erogato per la domanda al netto della df intervento Capi bovini macellati - Etichettatura",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFIMPLIPAGACZ_320("ACZ - Importo già erogato per la domanda al netto della df intervento Agnelle",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFIMPLIPAGACZ_321("ACZ - Importo già erogato per la domanda al netto della df intervento Ovicaprini macellati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),

	//output discliplina franchigia accoppiato zootecnia
	DFFRPAGLORACZ_310("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Vacche da Latte al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFFRPAGLORACZ_311("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Vacche Da Latte in zona di montagna al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFFRPAGLORACZ_313("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Vacche Nutrici al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFFRPAGLORACZ_322("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Vacche nutrici non iscritte al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFFRPAGLORACZ_315("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Bovini macellati al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFFRPAGLORACZ_316("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Capi bovini macellati e allevati almeno 12 mesi al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFFRPAGLORACZ_318("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Capi bovini macellati - Etichettatura al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFFRPAGLORACZ_320("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Agnelle al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),
	DFFRPAGLORACZ_321("Disciplina - Franchigia sostegno accoppiato zootecnia intervento Ovicaprini macellati al lordo degli importi già pagati",FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5770),

	// Disciplina finanziaria Superficie
	DFFRPAGACS_M8("Disciplina - Franchigia sostegno accoppiato superficie Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5780),
	DFFRPAGACS_M9("Disciplina - Franchigia sostegno accoppiato superficie Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5790),
	DFFRPAGACS_M10("Disciplina - Franchigia sostegno accoppiato superficie Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5800),
	DFFRPAGACS_M11("Disciplina - Franchigia sostegno accoppiato superficie Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5810),
	DFFRPAGACS_M14("Disciplina - Franchigia sostegno accoppiato superficie Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5820),
	DFFRPAGACS_M15("Disciplina - Franchigia sostegno accoppiato superficie Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5830),
	DFFRPAGACS_M16("Disciplina - Franchigia sostegno accoppiato superficie Olivo 7.5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5840),
	DFFRPAGACS_M17("Disciplina - Franchigia sostegno accoppiato superficie Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5850),
	
	//come ordine di output per la disciplina finanaziaria le variabili devono avere un valore > 3530 per essere visualizzate in coda a DFFRAPPACS come variabili di input 
	DFFRAPPACS_M8("Disciplina - Franchigia già applicata sostegno accoppiato superficie Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5853),
	DFFRAPPACS_M9("Disciplina - Franchigia già applicata sostegno accoppiato superficie Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5855),
	DFFRAPPACS_M10("Disciplina - Franchigia già applicata sostegno accoppiato superficie Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5857),
	DFFRAPPACS_M11("Disciplina - Franchigia già applicata sostegno accoppiato superficie Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5859),
	DFFRAPPACS_M14("Disciplina - Franchigia già applicata sostegno accoppiato superficie Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5861),
	DFFRAPPACS_M15("Disciplina - Franchigia già applicata sostegno accoppiato superficie Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5863),
	DFFRAPPACS_M16("Disciplina - Franchigia già applicata sostegno accoppiato superficie Olivo 7.5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5865),
	DFFRAPPACS_M17("Disciplina - Franchigia già applicata sostegno accoppiato superficie Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5867),
	
	DFIMPDFDISACS_M8("Disciplina - Importo oltre franchigia sostegno accoppiato superficie con disciplina finanziaria Soia", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5860),
	DFIMPDFDISACS_M9("Disciplina - Importo oltre franchigia sostegno accoppiato superficie con disciplina finanziaria Frumento", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5870),
	DFIMPDFDISACS_M10("Disciplina - Importo oltre franchigia sostegno accoppiato superficie con disciplina finanziaria Proteaginose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5880),
	DFIMPDFDISACS_M11("Disciplina - Importo oltre franchigia sostegno accoppiato superficie con disciplina finanziaria Leguminose", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5890),
	DFIMPDFDISACS_M14("Disciplina - Importo oltre franchigia sostegno accoppiato superficie con disciplina finanziaria Pomodoro", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5900),
	DFIMPDFDISACS_M15("Disciplina - Importo oltre franchigia sostegno accoppiato superficie con disciplina finanziaria Olivo Standard", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5910),
	DFIMPDFDISACS_M16("Disciplina - Importo oltre franchigia sostegno accoppiato superficie con disciplina finanziaria Olivo 7.5%", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5920),
	DFIMPDFDISACS_M17("Disciplina - Importo oltre franchigia sostegno accoppiato superficie con disciplina finanziaria Olivo Qualità", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5930),
	

	
	
	

	// Capi Da iscrivere a registro debitori
	ACZCAPIDEB_310("ACZ - Numero Capi Da iscrivere a registro debitori Intervento 310", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5930),
	ACZCAPIDEB_311("ACZ - Numero Capi Da iscrivere a registro debitori  Intervento 311", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5940),
	ACZCAPIDEB_313("ACZ - Numero Capi Da iscrivere a registro debitori Intervento 313", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5950),
	ACZCAPIDEB_322("ACZ - Numero Capi Da iscrivere a registro debitori Intervento 322", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5960),
	ACZCAPIDEB_315("ACZ - Numero Capi Da iscrivere a registro debitori  Intervento 315", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5970),
	ACZCAPIDEB_316("ACZ - Numero Capi Da iscrivere a registro debitori    Intervento 316", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5980),
	ACZCAPIDEB_318("ACZ - Numero Capi Da iscrivere a registro debitori   Intervento 318", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5990),
	ACZCAPIDEB_320("ACZ - Numero Capi Da iscrivere a registro debitori  Intervento 320", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5600),
	ACZCAPIDEB_321("ACZ - Numero Capi Da iscrivere a registro debitori  Intervento 321", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5610),
	ACZCAPIDEBTOT("ACZ - Numero Capi Da iscrivere a registro debitori Totale", FormatoVariabile.NUMEROINTERO, UnitaMisura.NULL, 5620),

	// Importo Da iscrivere Registro debitori intervento
	ACZIMPDEB_310("ACZ - ACZ Importo Da iscrivere Registro debitori intervento Intervento 310", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5930),
	ACZIMPDEB_311("ACZ - ACZ Importo Da iscrivere Registro debitori intervento  Intervento 311", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5940),
	ACZIMPDEB_313("ACZ - ACZ Importo Da iscrivere Registro debitori intervento Intervento 313", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5950),
	ACZIMPDEB_322("ACZ - ACZ Importo Da iscrivere Registro debitori intervento Intervento 322", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5960),
	ACZIMPDEB_315("ACZ - ACZ Importo Da iscrivere Registro debitori intervento  Intervento 315", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5970),
	ACZIMPDEB_316("ACZ - ACZ Importo Da iscrivere Registro debitori intervento    Intervento 316", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5980),
	ACZIMPDEB_318("ACZ - ACZ Importo Da iscrivere Registro debitori intervento   Intervento 318", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5990),
	ACZIMPDEB_320("ACZ - ACZ Importo Da iscrivere Registro debitori intervento  Intervento 320", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5600),
	ACZIMPDEB_321("ACZ - ACZ Importo Da iscrivere Registro debitori intervento  Intervento 321", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5610),
	ACZIMPDEBCTOT("ACZ - ACZ Importo Da iscrivere Registro debitori intervento Totale", FormatoVariabile.NUMERO2DECIMALI, UnitaMisura.EURO, 5620),

	// Percentuale di riduzione per le domande di modifica prsentate in ritardo applicata dall' istruttore
	PERCRITISTR("Verifica istruttoria: la domanda di modifica in ritardo è in riduzione", FormatoVariabile.BOOL, UnitaMisura.NULL, 
			getMapOrdineVar(Stream.of(new OrdineVariabile(TipologiaPassoTransizione.CONTROLLI_FINALI, 71))), 951);
	
	
	
	
	
	
	
	
	
	
	
	
	

	private String descrizione;
	private FormatoVariabile formato;
	private UnitaMisura unitaMisura;
	// Per definire se è una variabile da persistere/stampare o meno
	private Boolean stampa;
	private FonteDati fonteDati;
	private HashMap<TipologiaPassoTransizione, Integer> ordinePasso;

	public Integer getOrdine() {
		return ordine;
	}

	private Integer ordine;
	private boolean sintesi;
	private boolean controllo;

	private TipoVariabile(String descrizione, FormatoVariabile formato, UnitaMisura unitaMisura, FonteDati fonteDati, Integer ordine) {
		this.descrizione = descrizione;
		this.formato = formato;
		this.unitaMisura = unitaMisura;
		this.fonteDati = fonteDati;
		this.stampa = true;
		this.ordine = ordine;
	}

	private TipoVariabile(String descrizione, FormatoVariabile formato, UnitaMisura unitaMisura, Integer ordine) {
		this.descrizione = descrizione;
		this.formato = formato;
		this.unitaMisura = unitaMisura;
		this.fonteDati = FonteDati.DOMANDA;
		this.stampa = true;
		this.ordine = ordine;
	}

	private TipoVariabile(String descrizione, FormatoVariabile formato, UnitaMisura unitaMisura, boolean stampa, Integer ordine) {
		this.descrizione = descrizione;
		this.formato = formato;
		this.unitaMisura = unitaMisura;
		this.fonteDati = FonteDati.DOMANDA;
		this.stampa = stampa;
		this.ordine = ordine;
	}

	private TipoVariabile(String descrizione, FormatoVariabile formato, UnitaMisura unitaMisura, FonteDati fonteDati, HashMap<TipologiaPassoTransizione, Integer> ordinePasso, int a) {
		this.descrizione = descrizione;
		this.formato = formato;
		this.unitaMisura = unitaMisura;
		this.fonteDati = fonteDati;
		this.stampa = true;
		this.ordinePasso = ordinePasso;
		this.ordine = a;
	}

	private TipoVariabile(String descrizione, FormatoVariabile formato, UnitaMisura unitaMisura, HashMap<TipologiaPassoTransizione, Integer> ordinePasso) {
		this.descrizione = descrizione;
		this.formato = formato;
		this.unitaMisura = unitaMisura;
		this.fonteDati = FonteDati.DOMANDA;
		this.stampa = true;
		this.ordinePasso = ordinePasso;
	}

	private TipoVariabile(String descrizione, FormatoVariabile formato, UnitaMisura unitaMisura, HashMap<TipologiaPassoTransizione, Integer> ordinePasso, Integer ordine) {
		this.descrizione = descrizione;
		this.formato = formato;
		this.unitaMisura = unitaMisura;
		this.fonteDati = FonteDati.DOMANDA;
		this.stampa = true;
		this.ordinePasso = ordinePasso;
		this.ordine = ordine;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public FormatoVariabile getFormato() {
		return formato;
	}

	public UnitaMisura getUnitaMisura() {
		return unitaMisura;
	}

	public Boolean getStampa() {
		return stampa;
	}

	public FonteDati getFonteDati() {
		return fonteDati;
	}

	public void setStampa(Boolean stampa) {
		this.stampa = stampa;
	}

	public boolean isSintesi() {
		return sintesi;
	}

	public boolean isControllo() {
		return controllo;
	}

	public HashMap<TipologiaPassoTransizione, Integer> getOrdinePasso() {
		return ordinePasso;
	}

	public void setOrdinePasso(HashMap<TipologiaPassoTransizione, Integer> ordinePasso) {
		this.ordinePasso = ordinePasso;
	}

	private static HashMap<TipologiaPassoTransizione, Integer> getMapOrdineVar(Stream<OrdineVariabile> listaOrdineVariabili) {
		HashMap<TipologiaPassoTransizione, Integer> map = new HashMap<>();

		listaOrdineVariabili.forEach(p -> {
			map.put(p.getPasso(), p.getOrdine());
		});

		return map;

	}

}
