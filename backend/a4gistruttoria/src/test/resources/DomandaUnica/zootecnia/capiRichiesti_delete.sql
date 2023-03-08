delete from A4GT_IMPORTO_UNITARIO where id_intervento = 43;
delete from A4GT_IMPORTO_UNITARIO where id_intervento = 44;
delete from A4GT_IMPORTO_UNITARIO where id_intervento = 46;
delete from A4GT_CAPO_TRACKING where ID_RICH_ALLEVAM_ESITO in (select id from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8675566);
delete from A4GT_CAPO_TRACKING where ID_RICH_ALLEVAM_ESITO in (select id from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8675567);
delete from A4GT_CAPO_TRACKING where ID_RICH_ALLEVAM_ESITO in (select id from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8675568);
delete from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8675566;
delete from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8675567;
delete from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8675568;
delete from A4GT_ALLEVAMENTO_IMPEGNATO where id_domanda = 8664187;
delete from A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria = 8675888;
delete from A4GT_ISTRUTTORIA where id = 8675888;
delete from A4GD_STATO_LAV_SOSTEGNO where id = 27673;
delete FROM A4GT_DOMANDA WHERE ID = 8664187;

