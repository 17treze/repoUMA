delete from A4GT_FASCICOLO where cuaa='DPDNDR77B03L378X';
delete from A4GT_PERSONA_FISICA where id = (select ID from A4GT_PERSONA where CODICE_FISCALE='DPDNDR77B03L378X');
delete from A4GT_PERSONA where CODICE_FISCALE='DPDNDR77B03L378X';
COMMIT;
