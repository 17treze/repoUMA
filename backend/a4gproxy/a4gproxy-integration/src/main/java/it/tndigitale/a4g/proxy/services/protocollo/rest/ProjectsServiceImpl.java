package it.tndigitale.a4g.proxy.services.protocollo.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.protocollo.client.model.Filter;
import it.tndigitale.a4g.protocollo.client.model.Filter.TypeEnum;
import it.tndigitale.a4g.protocollo.client.model.Project;
import it.tndigitale.a4g.protocollo.client.model.SearchProjectsResponse;
import it.tndigitale.a4g.proxy.dto.ProjectDto;

@Service
public class ProjectsServiceImpl {

	@Autowired
	private PiTreProjectsServiceImpl piTreProjectsService;

	public ProjectDto getProjectByCode(String code) throws Exception {

		List<Filter> filters = new ArrayList<Filter>();
		Filter codeFilter = new Filter();
		codeFilter.setName("PROJECT_CODE");
		codeFilter.setType(TypeEnum.NUMBER_0);
		codeFilter.setValue(code);
		filters.add(codeFilter);

		SearchProjectsResponse response = piTreProjectsService.searchProjects(filters);

		List<Project> projects = response.getProjects();

		if (projects.stream().count() > 1)
			throw new Exception("More than one project found for this code.");

		ProjectDto returnPrj = new ProjectDto();
		Project foundProject = projects.stream().findFirst().orElse(null);
		BeanUtils.copyProperties(foundProject, returnPrj);
		return returnPrj;
	}
}
