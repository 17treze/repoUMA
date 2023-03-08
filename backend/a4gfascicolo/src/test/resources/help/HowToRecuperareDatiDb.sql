-- ENTI: recuperare dati da ags 

select 'nxtnbr.nextval' as ID, 0 as VERSIONE, cod_ente as IDENTIFICATIVO, DES_ENTE as DESCRIZIONE
from sitiente s
where s.cod_ente in (4,5,7,12,18,19, 20)
and s.tipoente <> 'SOGGETTO'
and sysdate between data_inizio and data_fine;

-- esporta risultati
-- sostituisci sitente a4gt_ente
-- sostituisci 'xx' con id fisso o nxtnbr.nextval

