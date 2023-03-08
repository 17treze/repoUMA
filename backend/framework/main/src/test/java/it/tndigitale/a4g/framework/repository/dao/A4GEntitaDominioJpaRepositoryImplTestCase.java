package it.tndigitale.a4g.framework.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@SpringBootTest
@RunWith(SpringRunner.class)
public class A4GEntitaDominioJpaRepositoryImplTestCase {
	
	@Autowired
	private MyTestEntityRepository repo;
	
	@MockBean
	UtenteComponent utComp;
	
	@Test
	@Transactional
	public void testInsertCustom() {
		mockUser();
		final String TEST1 = "Prova 1";
		final String TEST2 = "Prova 2";
		MyTestEntity e = new MyTestEntity();
		e.test = TEST1;
		e = repo.saveAndFlush(e);
		assertNotNull(e);
		assertNotNull(e.getId());
		assertNotNull(e.test);
		assertThat(e.test).isEqualTo(TEST1);
		e.test = TEST2;
		e = repo.saveAndFlush(e);
		assertNotNull(e);
		assertNotNull(e.getId());
		assertNotNull(e.test);
		assertThat(e.test).isEqualTo(TEST2);
		// test errore ottimistico
		e.test = TEST1;
		e.setVersion(0);
		try {
			e = repo.cleanSave(e);
			assertNotNull(null);
		} catch (ObjectOptimisticLockingFailureException e1) {
			// Mi aspetto errore ottimistico
		}
	}
	
	private void mockUser() {
		when(utComp.utenza()).thenReturn("betty");
	}

}
