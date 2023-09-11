package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_CONF")
public class ConfigurazioneModel  extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 4844786332004495577L;

	@Column(name="CAMPAGNA", nullable = false)
	private Integer campagna;
	
	@Column(name="DATA_LIMITE_PRELIEVI", nullable = false)
	private LocalDateTime dataPrelievi;

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public LocalDateTime getDataPrelievi() {
		return dataPrelievi;
	}

	public void setDataPrelievi(LocalDateTime dataPrelievi) {
		this.dataPrelievi = dataPrelievi;
	}
}
