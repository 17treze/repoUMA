-- scegli cuaa azienda es 'xxx'

select PKID, PK_CUAA, CUAA, COD_FISCALE, CODI_FISC, PART_IVA, CODI_PIVA, RAGI_SOCI, COGNOME, NOME, SESSO, D_NASCITA, D_COSTITUZIONE, DATA_NASC, D_MORTE, FKCOD_COMU_RESI, FKCOD_COMU_NASC, COMU_NASC, FKCIVICO_RESI, FKCOD_COMU_RECAPITO, FKCIVICO_RECAPITO, FLAG_PERS_FISICA, FKRAPP_LEGALE, DATA_FINE_VAL, DATA_INIZIO_VAL, DATA_LAV, UTENTE_INIZIO, UTENTE_FINE, VALIDO_INIZIO, VALIDO_FINE, ATTO_INIZIO, ATTO_FINE, NATURA_GIURIDICA, RESI_COMU, CAP, CAP_RECAP, TELEFONO, FAX, MAIL, INDIRIZZO_EST, INDIRIZZO_EST_RECAP, FL_PART_IVA, LOCALITA, CELLULARE, FONTE_INIZIO, FONTE_FINE, RIFERIMENTO, DENOMINAZIONE_INT, LOCALITA_INT, INDIRIZZO_EST_INT, INDIRIZZO_EST_RECAP_INT
from cons_sogg_viw c where CUAA = 'xxx'
and sysdate between c.data_inizio_val and c.data_fine_val;

select ID_FASCICOLO, ID_SOGGETTO, ID_MODELLO, PROTOCOLLO, ID_ENTE, DT_INIZIO, DT_FINE, COD_OP, SCO_OP, COD_STATO, SCO_STATO, DT_AGGIORNAMENTO 
from tfascicolo
where id_soggetto = (select distinct PK_CUAA from cons_sogg_viw c where CUAA = 'xxx' and sysdate between c.data_inizio_val and c.data_fine_val)
and sysdate between DT_INIZIO and DT_FINE and SCO_STATO not in ('CESSAT', 'CHIUSO');

select PK_CUAA, DATA_INIZIO, DATA_FINE, COD_ENTE, DATA_RICHIESTA, ID_ATTO_INIZIO, ID_ATTO_FINE, ID_ASSOCIAZIONE, TIPO_ASSOCIAZIONE, FLAG_GESTIONE, NOTE, DETENTORE, FL_DETENTORE
 from CONS_CUAA_ENTE ce where PK_CUAA in (select distinct PK_CUAA from cons_sogg_viw c where CUAA = 'xxx' and sysdate between c.data_inizio_val and c.data_fine_val)
 and sysdate between ce.DATA_INIZIO and ce.DATA_FINE;
 
-- controllare che non sia già stato inserito 
select COD_ENTE, DES_ENTE, DATA_INIZIO, DATA_FINE, INDIRIZZO, CAP, PROVINCIA, COMUNE, TELEFONO, E_MAIL, FAX, TIPOENTE, COD_FISCALE, COD_ENTE_SUP, CODI_TRAM, UTENTE, DATA_AGGIORNAMENTO
from SITIENTE where COD_ENTE in (select distinct cod_ente from CONS_CUAA_ENTE ce where PK_CUAA in (select PK_CUAA from cons_sogg_viw c where CUAA = 'xxx' and sysdate between c.data_inizio_val and c.data_fine_val)
 and sysdate between ce.DATA_INIZIO and ce.DATA_FINE);