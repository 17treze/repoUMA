DELETE FROM A4GT_MANDATO;

DELETE FROM A4GT_DETENZIONE WHERE ID_FASCICOLO = (SELECT ID FROM A4GT_FASCICOLO WHERE CUAA = 'BLDGDN61M17L378K');

DELETE FROM A4GT_FASCICOLO WHERE ID_PERSONA = (SELECT ID FROM A4GT_PERSONA WHERE CODICE_FISCALE = 'BLDGDN61M17L378K');

DELETE A4GT_PERSONA_FISICA WHERE ID = (SELECT ID FROM A4GT_PERSONA WHERE CODICE_FISCALE = 'BLDGDN61M17L378K');

DELETE A4GT_PERSONA WHERE CODICE_FISCALE = 'BLDGDN61M17L378K';

DELETE FROM A4GT_SPORTELLO;

DELETE FROM A4GT_CAA;

