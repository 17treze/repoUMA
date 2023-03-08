-- Persona
INSERT INTO A4GT_PERSONA
(ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE)
VALUES(3592, 0, 0, '00959460221');

-- Persona Giuridica
INSERT INTO A4GT_PERSONA_GIURIDICA
(ID, ID_VALIDAZIONE, VERSIONE, PARTITA_IVA, DENOMINAZIONE, FORMA_GIURIDICA, DATA_COSTITUZIONE, DATA_TERMINE, CAPITALE_SOCIALE_DELIBERATO, CF_RAPPRESENTANTE_LEGALE, DATA_ISCRIZIONE, PROVINCIA_CCIAA, NUMERO_REPERTORIO, CESSATA, NOME_RAPPRESENTANTE_LEGALE, PEC, TELEFONO, SEDE_DESCRIZIONE_ESTESA, SEDE_TOPONIMO, SEDE_VIA, SEDE_NUMERO_CIVICO, SEDE_CAP, SEDE_CODICE_ISTAT, SEDE_PROVINCIA, SEDE_COMUNE, SEDE_FRAZIONE, SEDE_CC_DESCRIZIONE_ESTESA, SEDE_CC_TOPONIMO, SEDE_CC_VIA, SEDE_CC_NUMERO_CIVICO, SEDE_CC_CAP, SEDE_CC_CODICE_ISTAT, SEDE_CC_PROVINCIA, SEDE_CC_COMUNE, SEDE_CC_FRAZIONE, OGGETTO_SOCIALE)
VALUES(3592, 0, 0, '00959460221', 'SEVEN S.P.A.', 'SOCIETA'' PER AZIONI', TIMESTAMP '1982-09-20 00:00:00.000000', TIMESTAMP '2025-12-31 00:00:00.000000', 1500000, 'PLOPLA44H15L821Q', TIMESTAMP '1982-12-17 00:00:00.000000', 'TN', 106862, 0, 'POLI PAOLO', 'AMMINISTRAZIONE@PEC.GRUPPOPOLI.IT', '046197654', 'VIA ALTO ADIGE 242', NULL, NULL, NULL, '38121', NULL, 'TN', 'TRENTO', NULL, NULL, 'VIA', 'ALTO ADIGE', '242', '38121', '022205', 'TRENTO', 'TRENTO', 'GARDOLO', 'OGGETTO SOCIALE');


-- Attivita Ateco
INSERT INTO A4GT_ATTIVITA_ATECO
(ID, VERSIONE, ID_SEDE, SEDE_ID_VALIDAZIONE, CODICE, DESCRIZIONE, IMPORTANZA, FONTE)
VALUES(1811, 0, 3592, 0, '11021', 'Produzione di vini da tavola e v.q.p.r.d.', 'SECONDARIO', 'CAMERA_COMMERCIO');
INSERT INTO A4GT_ATTIVITA_ATECO
(ID, VERSIONE, ID_SEDE, SEDE_ID_VALIDAZIONE, CODICE, DESCRIZIONE, IMPORTANZA, FONTE)
VALUES(1812, 0, 3592, 0, '46341', 'Commercio all''ingrosso di bevande alcoliche', 'PRIMARIO_IMPRESA', 'CAMERA_COMMERCIO');


COMMIT;