package it.tndigitale.a4g.proxy.services.protocollo.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.protocollo.client.model.Filter;
import it.tndigitale.a4g.protocollo.client.model.Filter.TypeEnum;
import it.tndigitale.a4g.protocollo.client.model.SearchProjectsResponse;
import it.tndigitale.a4g.protocollo.client.model.SearchRequest;

@Service
public class PiTreProjectsServiceImpl extends PiTreBaseService {

	private static final Logger log = LoggerFactory.getLogger(PiTreProjectsServiceImpl.class);

	public SearchProjectsResponse searchProjects(List<Filter> additionalFilters) {
		log.debug("PiTre searchProjects");
		List<Filter> filters = new ArrayList<>();
		createDefaultFilters(filters);
		filters.addAll(additionalFilters);
		SearchRequest request = new SearchRequest();
		request.setFilters(filters);
		request.setPageNumber(1);
		request.setElementsInPage(10);
		return restApi4Ptre.postSearchProjects(request);
	}

	private void createDefaultFilters(List<Filter> filters) {
		Filter filter = new Filter();
		filter.setName("STATE");
		filter.setValue("O");
		filter.setType(TypeEnum.NUMBER_0);
		filters.add(filter);
	}
}
