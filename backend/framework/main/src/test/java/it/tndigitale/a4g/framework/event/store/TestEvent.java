package it.tndigitale.a4g.framework.event.store;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import it.tndigitale.a4g.framework.event.Event;

@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
class TestEvent implements Event {
    Long id;

    public Long getId() {
        return id;
    }

    @Override
    public Integer getNumberOfRetry() {
        return 0;
    }

    public TestEvent setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEvent testEvent = (TestEvent) o;
        return Objects.equals(id, testEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Event setNumberOfRetry(Integer numberOfRetry) {
		// TODO Auto-generated method stub
		return null;
	}
}
