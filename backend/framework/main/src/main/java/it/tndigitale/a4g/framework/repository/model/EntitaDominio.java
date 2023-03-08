package it.tndigitale.a4g.framework.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import it.tndigitale.a4g.framework.repository.handler.PersistenzaEntitaHandler;

@MappedSuperclass
@EntityListeners(PersistenzaEntitaHandler.class)
public abstract class EntitaDominio implements Serializable {
		private static final long serialVersionUID = -2741312835656701915L;

		@Id
		@SequenceGenerator(name = "NXTNBR_GENERATOR", sequenceName = "NXTNBR", allocationSize = 1, initialValue = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NXTNBR_GENERATOR")
		@Column(name = "ID")
		protected Long id;

		@Version
		@Column(name = "VERSIONE")
		protected Integer version;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

}
