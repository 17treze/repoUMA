DELETE FROM A4GT_PASSO_TRANSIZIONE WHERE ID IN (7822915, 7822916, 7822936, 7822931, 7822921, 7822933, 7822930, 7822926, 8551045);
DELETE FROM A4GT_TRANSIZIONE_ISTRUTTORIA WHERE ID IN (7822910, 8551043);
DELETE FROM A4GT_ISTRUTTORIA WHERE ID = 1406056;
DELETE FROM A4GT_STATISTICA_DU WHERE ID_DOMANDA = 182206;
DELETE FROM A4GT_DOMANDA WHERE ID = 831735;
COMMIT;