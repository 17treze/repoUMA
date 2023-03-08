package it.tndigitale.a4gutente.api;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gutente.dto.CsvFile;
import it.tndigitale.a4gutente.service.IUtenteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtenteA4GControllerTest {
	
	@Autowired
	private IUtenteService utenteService;
	
	@Test
	@Transactional
	@WithMockUser(username="PIPPO")
	//@Sql("/customsql/ricercaUtenti.sql")
	public void domandeIstruttoria() {
		
		try {
			CsvFile csvFile = utenteService.getUtentiCsv();
			OutputStream  os = new FileOutputStream("D:\\prova1.csv");//csvFile.getCsvFileName());
            os.write(csvFile.getCsvByteArray());
            os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return entity;
		
	}

}
