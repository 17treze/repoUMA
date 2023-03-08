delete from A4GT_DOMANDA_INTEGRATIVA where identificativo = 'DI_205_DUF40';
delete from A4GT_ESITO_CALCOLO_CAPO where ID_ALLEVAM_DU in (select id from A4GT_ALLEVAMENTO_IMPEGNATO where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794U'));
delete from A4GT_ALLEVAMENTO_IMPEGNATO where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794U');
delete from A4GT_ERRORE_RICEVIBILITA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794U');
delete from A4GT_ANOM_DOMANDA_SOSTEGNO where ID_PASSO_LAVORAZIONE in (
   select id FROM A4GT_PASSO_TRANSIZIONE where id_transiz_sostegno = (SELECT ID FROM A4GT_TRANSIZIONE_ISTRUTTORIA WHERE ID_ISTRUTTORIA = 4449871234)
);
DELETE FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO = (select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where ID_ISTRUTTORIA = 4449871234);
delete FROM A4GT_TRANSIZIONE_ISTRUTTORIA where ID_ISTRUTTORIA = 4449871234;
delete FROM A4GT_ISTRUTTORIA where ID = 4449871234;
delete FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794U';

COMMIT;
