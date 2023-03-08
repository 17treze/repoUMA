package it.tndigitale.a4gistruttoria;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import it.tndigitale.a4gistruttoria.dto.CustomThreadLocal;
import it.tndigitale.a4gistruttoria.dto.Pagination;
import it.tndigitale.a4gistruttoria.dto.Sort;

public class RestInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (request.getMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
			String expandParams = request.getParameter("expand");
			if (expandParams != null) {
				List<String> expandList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(expandParams);
				CustomThreadLocal.addVariables(ImmutableMap.of("expand", expandList));
			}			
			String paginationSizeParam = request.getParameter("pagSize");
			String paginationStartParam = request.getParameter("pagStart");
			if (paginationSizeParam != null && paginationStartParam != null) {
				CustomThreadLocal.addVariable("pagination", new Pagination(paginationSizeParam,paginationStartParam));
			}
			String sortParam = request.getParameter("sortBy");
			if (!StringUtils.isEmpty(sortParam)) {
				CustomThreadLocal.addVariable("sort", new Sort(sortParam.split(",")));
			}
			
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		CustomThreadLocal.clean();
	}
}
