DELETE FROM A4GT_PASSO_TRANSIZIONE WHERE ID IN (6668347, 6681131, 6681129, 6681138, 6681137, 5366430, 5366431, 5366436, 5366439, 5366443, 5366447, 5366444);
DELETE FROM A4GT_TRANSIZIONE_ISTRUTTORIA WHERE ID IN (6668345, 6681127, 5366425);
DELETE FROM A4GT_ISTRUTTORIA WHERE ID = 1018333;
DELETE FROM A4GT_ELENCO_LIQUIDAZIONE WHERE ID = 6683375;
DELETE FROM A4GT_STATISTICA_DU WHERE ID_DOMANDA = 186823;
DELETE FROM A4GT_DOMANDA WHERE ID = 841504;

COMMIT;