package it.tndigitale.a4g.ags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Splitter;

import it.tndigitale.a4g.ags.utils.ExpandManager;

public class RestInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (request.getMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
			String expandParams = request.getParameter("expand");
			List<String> expandList = new ArrayList<String>();
			if (expandParams != null) {
				expandList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(expandParams);
			}/* else {
				expandList.add("flat");
			}*/
			ExpandManager.setExpands(expandList);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		ExpandManager.clean();
	}
}
