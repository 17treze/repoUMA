package it.tndigitale.a4g.framework.support;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.tndigitale.a4g.framework.support.ListSupport.emptyIfNull;
import static it.tndigitale.a4g.framework.support.ListSupport.intersect;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

public class ListSupportTest {

    private static final Set<String> NOT_EMPTY_SET = new HashSet<String>() {
				private static final long serialVersionUID = 2965120126566769140L;

				{
						add("xxxxxx");
				}
		};
		
    private static final List<String> NOT_EMPTY_LIST = new ArrayList<String>() {
				private static final long serialVersionUID = 5410628916363407191L;

				{
    				add("xxxxxx");
    		}
    };

    @Test
    public void forConvertToListIfSetNullThenReturnEmptyList() {
        List<?> result =  ListSupport.convert(null);

        assertThat(result).isEmpty();
    }

    @Test
    public void forConvertToListIfSetEmptyThenReturnEmptyList() {
        List<?> result =  ListSupport.convert(emptySet());

        assertThat(result).isEmpty();
    }

    @Test
    public void forConvertToListIfSetNotEmptyThenReturnNotEmptyList() {
        List<?> result =  ListSupport.convert(NOT_EMPTY_SET);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    public void forConvertToSetIfListNullThenReturnEmptySet() {
        Set<?> result = ListSupport.convertToSet(null);

        assertThat(result).isEmpty();
    }

    @Test
    public void forConvertToSetIfListEmptyThenReturnEmptySet() {
        Set<?> result = ListSupport.convertToSet(emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    public void forConvertToSetIfListNotEmptyThenReturnNotEmptySet() {
        Set<?> result =  ListSupport.convertToSet(NOT_EMPTY_LIST);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    public void forGetFirstElementIfListNullThenThrowing() {
        String element = ListSupport.getFirstElementOf(null);

        assertThat(element).isNull();
    }

    @Test
    public void forGetFirstElementIfListEmptyThenThrowing() {
        String element = ListSupport.getFirstElementOf(emptyList());

        assertThat(element).isNull();
    }

    @Test
    public void forGetFirstElementIfListContainsOneElementThenReturnElement() {
        String element = ListSupport.getFirstElementOf(asList("elemento"));

        assertThat(element).isEqualTo("elemento");
    }

    @Test
    public void forGetFirstElementIfListContainsMoreElementThenReturnFirstElement() {
        String element = ListSupport.getFirstElementOf(asList("elemento1", "elemento2"));

        assertThat(element).isEqualTo("elemento1");
    }


    @Test
    public void forEmptyIfNull__IfNullListThenReturnEmptyList() {
        List<?> list = emptyIfNull(null);

        assertThat(list).isEmpty();
    }

    @Test
    public void forEmptyIfNull__IfEmptyListThenReturnEmptyList() {
        List<?> list = emptyIfNull(emptyList());

        assertThat(list).isEmpty();
    }

    @Test
    public void forEmptyIfNull__IfNotEmptyListThenReturnNotEmptyList() {
        List<?> list = emptyIfNull(asList(new Object()));

        assertThat(list).isNotEmpty();
    }

    @Test
    public void forIntersectIfFirstListIsNullThenReturnEmptyList() {
        List<Long> intersectList = intersect(null, asList(1L,2L));

        assertThat(intersectList).isEmpty();
    }

    @Test
    public void forIntersectIfFirstListIsEmptyThenReturnEmptyList() {
        List<Long> intersectList = intersect(emptyList(), asList(1L,2L));

        assertThat(intersectList).isEmpty();
    }

    @Test
    public void forIntersectIfSecondListIsNullThenReturnEmptyList() {
        List<Long> intersectList = intersect(asList(1L,2L), null);

        assertThat(intersectList).isEmpty();
    }

    @Test
    public void forIntersectIfSecondListIsEmptyThenReturnEmptyList() {
        List<Long> intersectList = intersect(asList(1L,2L), emptyList());

        assertThat(intersectList).isEmpty();
    }

    @Test
    public void forIntersectIfEitherNotEmptyBatNotMatchThenEmptyList() {
        List<Long> intersectList = intersect(asList(1L,2L), asList(3L,4L));

        assertThat(intersectList).isEmpty();
    }

    @Test
    public void forIntersectIfEitherNotEmptyAndMatchThenInterseptList() {
        List<Long> intersectList = intersect(asList(3L,2L), asList(3L,4L));

        assertThat(intersectList).isNotEmpty();
        assertThat(intersectList).contains(3L);
    }
}
