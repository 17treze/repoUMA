package it.tndigitale.a4g.framework.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SQLSupportTest {
    @Test
    public void forUpperLikeItReturnStringForUpperLike() {
        String result = SQLSupport.upperLike("sTrAda");

        assertThat(result).isEqualTo("%STRADA%");
    }

    @Test
    public void forAddWhereIfQueryNotContainsWhereThenAdd() {
        String query = "select * from tabella";

        String result = SQLSupport.addWhere(query);

        assertThat(result).contains(" where ");
        assertThat(result).isEqualTo("select * from tabella where ");
    }

    @Test
    public void forAddWhereIfQueryContainsWhereThenAdd() {
        String query = "select * from tabella where ";

        String result = SQLSupport.addWhere(query);

        assertThat(result).contains(" where ");
        assertThat(result).isEqualTo(query);
    }

    @Test
    public void forAddOrIfQueryNotContainsWhereThenNotAdd() {
        String query = " select * from tabella ";

        String result = SQLSupport.addOr(query);

        assertThat(result).doesNotContain(" or ");
        assertThat(result).isEqualTo(query);
    }

    @Test
    public void forAddOrIfQueryContainsWhereAndNotContainsOtherConditionThenNotAdd() {
        String query = " select * from tabella where ";

        String result = SQLSupport.addOr(query);

        assertThat(result).doesNotContain(" or ");
        assertThat(result).isEqualTo(query);
    }

    @Test
    public void forAddOrIfQueryContainsWhereAndContainsOtherConditionThenAdd() {
        String query = " select * from tabella where property<10 ";

        String result = SQLSupport.addOr(query);

        assertThat(result).contains(" or ");
        assertThat(result).isEqualTo(" select * from tabella where property<10  or ");
    }

    @Test
    public void forAddAndIfQueryNotContainsWhereThenNotAdd() {
        String query = " select * from tabella ";

        String result = SQLSupport.addAnd(query);

        assertThat(result).doesNotContain(" and ");
        assertThat(result).isEqualTo(query);
    }

    @Test
    public void forAddAndIfQueryContainsWhereAndNotContainsOtherConditionThenNotAdd() {
        String query = " select * from tabella where ";

        String result = SQLSupport.addAnd(query);

        assertThat(result).doesNotContain(" and ");
        assertThat(result).isEqualTo(query);
    }

    @Test
    public void forAddAndIfQueryContainsWhereAndContainsOtherConditionThenAdd() {
        String query = " select * from tabella where property<10 ";

        String result = SQLSupport.addAnd(query);

        assertThat(result).contains(" and ");
        assertThat(result).isEqualTo(" select * from tabella where property<10  and ");
    }
}
