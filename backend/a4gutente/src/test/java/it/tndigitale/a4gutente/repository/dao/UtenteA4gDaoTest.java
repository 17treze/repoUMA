package it.tndigitale.a4gutente.repository.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gutente.dto.csv.UtenteA4gCsv;
import it.tndigitale.a4gutente.dto.csv.UtenteA4gDTO;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UtenteA4gDaoTest {
	
	@Autowired
	private IUtenteCompletoDao utenteCompletoDao;
	
	@Test
	@Transactional
	@WithMockUser(username="PIPPO")
	@Sql("/customsql/ricercaUtenti.sql")
	public void domandeIstruttoria() {
		
		List<UtenteA4gDTO> utentiA4gList = utenteCompletoDao.findUtenteA4g();
		
		List<UtenteA4gCsv> utentiA4gCsv = UtenteA4gCsv.fromDto(utentiA4gList);
		
		utentiA4gCsv = utentiA4gCsv;
		
		
	}

}
