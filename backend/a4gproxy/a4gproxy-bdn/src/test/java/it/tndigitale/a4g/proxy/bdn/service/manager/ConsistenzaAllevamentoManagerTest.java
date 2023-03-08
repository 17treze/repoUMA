package it.tndigitale.a4g.proxy.bdn.service.manager;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAPASCOLO2015.CONSISTENZAPASCOLO2015;
import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAlPascoloPAC2015DO;
import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAlPascoloPAC2015DOBuilder;
import it.tndigitale.a4g.proxy.bdn.dto.FasciaEtaConsistenzaPascolo;
import it.tndigitale.a4g.proxy.bdn.repository.dao.ConsistenzaAllevamentoDAOImpl;
import it.tndigitale.a4g.proxy.ws.bdn.dsregistripascolig.DsREGISTRIPASCOLIG;
import it.tndigitale.a4g.proxy.ws.bdn.dsregistripascolig.DsREGISTRIPASCOLIG.REGISTRIPASCOLI;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ConsistenzaAllevamentoManagerTest {
	@Autowired
	private ConsistenzaAllevamentoDAOImpl azienda;

	@Test
	@Transactional
	public void aggiungoLocazioneNuovaAzienda() throws Exception {

		azienda.insertAziendaNuova("000XX000", "240");

		boolean esiste = azienda.esisteAziendaInCache("000XX000");
		assertTrue(esiste);
	}
}
