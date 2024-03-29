 SET REFERENTIAL_INTEGRITY FALSE;
 DROP ALL OBJECTS;
 SET REFERENTIAL_INTEGRITY TRUE;
 
 create schema SUAGS04; 
 
   CREATE TABLE AABARAIA_TAB
   (ID_RAIA				NUMBER NOT NULL,
   ANNO_CAMP			NUMBER NOT NULL,
   COD_FISC				VARCHAR2(16 BYTE) NOT NULL,
   DATA_FINE_ASSE		DATE,
   DATA_FINE_POSS_REQU	DATE,
   DATA_INIZ_ASSE		DATE,
   DATA_INIZ_POSS_REQU	DATE,
   DTL__CAPXACTION		VARCHAR2(16 BYTE),
   DTL__CAPXTIMESTAMP	VARCHAR2(16 BYTE),
   FLAG_AGRI_ATTI		NUMBER,
   FLAG_GIOV_AGRI		NUMBER,
   ID_ORPA				NUMBER,
   ID_SOGG				NUMBER);
   
   CREATE TABLE TITOLI_PAC (
	ID_TIT NUMBER NOT NULL,
	CUAA_PROP VARCHAR2(16),
	UTILIZZO CHAR(8),
	CUAA VARCHAR2(16) NOT NULL,
	NUME_TITO VARCHAR2(12) NOT NULL,
	VALO_TITO_2015 NUMBER(8,2),
	VALO_TITO_2016 NUMBER(8,2), 
	VALO_TITO_2017 NUMBER(8,2),
	VALO_TITO_2018 NUMBER(8,2),
	VALO_TITO_2019 NUMBER(8,2),
	VALO_TITO_2020 NUMBER(8,2),
	SUPE_TITO NUMBER(5,2),
	DECO_TIPO VARCHAR2(10),
	DECO_ORIG VARCHAR2(10),
	DECO_MOVI VARCHAR2(10),
	DATA_INIZ CHAR(8),
	DECO_SALA NUMBER,
	DATA_FINE_POSS CHAR(8),
	NUME_CAMP_INIZ NUMBER(4,0) NOT NULL,
	NUME_CAMP_FINE NUMBER(4,0),
	DECO_SALA_VALI NUMBER,
	PRESENZA_PEGNO CHAR(2),
	PRESENZA_VINCOLO CHAR(2), 
	BLOCCO CHAR(2),
	COD_OP VARCHAR2(5) NOT NULL,
	DATA_INS_REC DATE,
	DATA_FINE_REC DATE,
	VALO_TITO_2021 NUMBER(8,2),
	VALO_TITO_2022 NUMBER(8,2));
	
set schema SUAGS04;

	CREATE TABLE AABAGARE_TAB (
	ID_GARE NUMBER NOT NULL,
	TIPO_REGI_GA NUMBER NOT NULL,
	ID_SOGG NUMBER NOT NULL, 
	ID_ORPA NUMBER NOT NULL, 
	ANNO_CAMP NUMBER NOT NULL, 
	FLAG_GIOV_AGRI NUMBER NOT NULL, 
	DATA_INIZ_ASSE DATE NOT NULL, 
	DATA_FINE_ASSE DATE NOT NULL, 
	USER_NAME_INSE VARCHAR2(100 BYTE) NOT NULL, 
	USER_NAME_AGGI VARCHAR2(100 BYTE), 
	DATA_INIZ_POSS_REQU DATE, 
	DATA_FINE_POSS_REQU DATE, 
	DATA_CARI_REGI DATE, 
	COD_FISC VARCHAR2(16 BYTE) ); 	