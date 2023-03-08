--delete from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8672161;
--delete from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8672162;
--delete from A4GT_ESITO_CALCOLO_CAPO where id_allevam_du = 8672163;
--delete from A4GT_TRANSIZIONE_ISTRUTTORIA where id = 12020314;
delete from A4GT_ALLEVAMENTO_IMPEGNATO where id = 8672161;
delete from A4GT_ALLEVAMENTO_IMPEGNATO where id = 8672162;
delete from A4GT_ALLEVAMENTO_IMPEGNATO where id = 8672163;
delete from A4GT_ISTRUTTORIA where id = 8672251;
delete FROM A4GT_DOMANDA WHERE ID = 8661346;

COMMIT;