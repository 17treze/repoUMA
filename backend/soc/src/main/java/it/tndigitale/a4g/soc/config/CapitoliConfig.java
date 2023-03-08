package it.tndigitale.a4g.soc.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.soc.business.dto.CapitoloDisciplina;


@Component
@ConfigurationProperties(prefix="soc")
public class CapitoliConfig {
	
	private List<CapitoloDisciplina> capitoli;
	
	public List<CapitoloDisciplina> getCapitoli() {
		return capitoli;
	}
	public void setCapitoli(List<CapitoloDisciplina> capitoli) {
		this.capitoli = capitoli;
	}
}
