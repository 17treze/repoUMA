DELETE A4GT_PASSO_TRANSIZIONE
WHERE id_transiz_sostegno IN (SELECT ID FROM A4GT_TRANSIZIONE_ISTRUTTORIA WHERE ID_ISTRUTTORIA IN (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA = (SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300000)))
AND codice_passo = 'DISCIPLINA_FINANZIARIA';

DELETE A4GT_PASSO_TRANSIZIONE
WHERE id_transiz_sostegno IN (SELECT ID FROM A4GT_TRANSIZIONE_ISTRUTTORIA WHERE ID_ISTRUTTORIA IN (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA = (SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300002)))
AND codice_passo = 'DISCIPLINA_FINANZIARIA';

DELETE A4GT_TRANSIZIONE_ISTRUTTORIA
WHERE id_istruttoria IN (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA IN (SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300000))
AND id_stato_iniziale = (SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'LIQUIDABILE')
AND id_stato_finale = (SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'CONTROLLI_INTERSOSTEGNO_OK');

DELETE A4GT_TRANSIZIONE_ISTRUTTORIA
WHERE id_istruttoria IN (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA IN (SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300002))
AND id_stato_iniziale = (SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'LIQUIDABILE')
AND id_stato_finale = (SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'CONTROLLI_INTERSOSTEGNO_OK');

DELETE A4GT_ISTRUTTORIA
WHERE id_domanda = (SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300000)
AND SOSTEGNO = 'ZOOTECNIA'
AND id_stato_lavorazione = (SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO');

DELETE A4GT_ISTRUTTORIA
WHERE id_domanda = (SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300002)
AND SOSTEGNO = 'ZOOTECNIA'
AND id_stato_lavorazione = (SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO');

-- DELETE a4gr_domanda_elenco
-- WHERE id_domanda = (SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300000)
-- AND id_elenco = 9999999;

-- DELETE a4gr_domanda_elenco
-- WHERE id_domanda = (SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300002)
-- AND id_elenco = 9999999;

DELETE a4gt_elenco_liquidazione
WHERE ID = 9999999
AND cod_elenco = '009-801-20190513-9999999';

COMMIT;