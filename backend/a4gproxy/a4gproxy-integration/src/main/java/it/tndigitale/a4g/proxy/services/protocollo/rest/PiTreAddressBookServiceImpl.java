package it.tndigitale.a4g.proxy.services.protocollo.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.protocollo.client.model.AddCorrespondentRequest;
import it.tndigitale.a4g.protocollo.client.model.Correspondent;
import it.tndigitale.a4g.protocollo.client.model.Filter;
import it.tndigitale.a4g.protocollo.client.model.Filter.TypeEnum;
import it.tndigitale.a4g.protocollo.client.model.GetCorrespondentResponse;
import it.tndigitale.a4g.protocollo.client.model.SearchCorrespondentsResponse;
import it.tndigitale.a4g.protocollo.client.model.SearchFiltersOnlyRequest;

@Service
public class PiTreAddressBookServiceImpl extends PiTreBaseService {

	private static final Logger log = LoggerFactory.getLogger(PiTreAddressBookServiceImpl.class);

	public SearchCorrespondentsResponse searchCorrespondents(String code, String codeRegistryOrRF) throws Exception {
		log.debug("PiTre SearchCorrespondents");

		SearchFiltersOnlyRequest request = new SearchFiltersOnlyRequest();
		List<Filter> filters = new ArrayList<Filter>();
		createDefaultFilters(filters);

		Filter filter = new Filter();
		filter.setName("CODE");
		filter.setValue(code);
		filter.setType(TypeEnum.NUMBER_0);
		filters.add(filter);
		filter = new Filter();
		filter.setName("REGISTRY_OR_RF");
		filter.setValue(codeRegistryOrRF);
		filter.setType(TypeEnum.NUMBER_0);
		filters.add(filter);

		request.setFilters(filters);

		//		Chiamata Rest
		return restApi4Ptre.postSearchCorrespondents(request);
	}


	public GetCorrespondentResponse addCorrespondent(Correspondent correspondentInfo) throws Exception {
		log.debug("PiTre AddCorrespondent");

		AddCorrespondentRequest request = new AddCorrespondentRequest();
		request.correspondent(correspondentInfo);

		//		Chiamata Rest
		return restApi4Ptre.putAddCorrespondent(request);
	}

	private void createDefaultFilters(List<Filter> filters) {

		Filter filter = new Filter();
		filter.setName("OFFICES");
		filter.setValue(Boolean.TRUE.toString());
		filter.setType(TypeEnum.NUMBER_1);
		filters.add(filter);
		filter = new Filter();
		filter.setName("USERS");
		filter.setValue(Boolean.TRUE.toString());
		filter.setType(TypeEnum.NUMBER_1);
		filters.add(filter);
		filter = new Filter();
		filter.setName("ROLES");
		filter.setValue(Boolean.TRUE.toString());
		filter.setType(TypeEnum.NUMBER_1);
		filters.add(filter);
		filter = new Filter();
		filter.setName("COMMON_ADDRESSBOOK");
		filter.setValue(Boolean.TRUE.toString());
		filter.setType(TypeEnum.NUMBER_1);
		filters.add(filter);
		filter = new Filter();
		filter.setName("TYPE");
		filter.setValue("GLOBAL");
		filter.setType(TypeEnum.NUMBER_0);
		filters.add(filter);
	}
}
