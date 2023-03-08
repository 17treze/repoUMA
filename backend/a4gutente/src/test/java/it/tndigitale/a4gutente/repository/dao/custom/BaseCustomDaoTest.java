package it.tndigitale.a4gutente.repository.dao.custom;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BaseCustomDaoTest {

    private BaseCustomDao baseCustomDao;
    private EntityManager entityManager;

    private static final String QUERY_STR = "";

    public BaseCustomDaoTest() {
        entityManager = mock(EntityManager.class);

        baseCustomDao = new BaseCustomDao().setEntityManager(entityManager);
    }

    @Test
    public void forExecuteJpaQuery() {
        TypedQuery queryObj = mock(TypedQuery.class);
        when(entityManager.createQuery(QUERY_STR, Object.class)).thenReturn(queryObj);
        when(queryObj.getResultList()).thenReturn(Arrays.asList(new Object()));

        List<Object> results = baseCustomDao.executeJpaQuery(QUERY_STR, Object.class);

        assertThat(results).hasSize(1);
        verify(entityManager).createQuery(QUERY_STR, Object.class);
    }

}
