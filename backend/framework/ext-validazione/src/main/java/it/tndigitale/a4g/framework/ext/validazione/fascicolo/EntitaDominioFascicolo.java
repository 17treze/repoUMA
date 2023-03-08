package it.tndigitale.a4g.framework.ext.validazione.fascicolo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import it.tndigitale.a4g.framework.ext.validazione.repository.handler.PersistenzaEntitaFascicoloHandler;

@MappedSuperclass
@IdClass(EntitaDominioFascicoloId.class)
@EntityListeners(PersistenzaEntitaFascicoloHandler.class)
public abstract class EntitaDominioFascicolo implements Serializable {
		private static final long serialVersionUID = -1777880168600588706L;
		
		@Transient
		protected boolean ignoreValidazioneCheck = false;
		
		@Id
		@GenericGenerator(
				name = "F_GENERATOR",
				strategy = "it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloIdGenerator",
			    parameters = {
			            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "NXTNBR"),
			            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
			            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
			    })
		@GeneratedValue(generator = "F_GENERATOR")
		protected Long id;
		
		@Id
		@GenericGenerator(
				name = "FV_GENERATOR",
				strategy = "it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloIdValidazioneGenerator")
		@GeneratedValue(generator = "FV_GENERATOR")
		@Column(name = "ID_VALIDAZIONE")
		protected Integer idValidazione;

		@Version
		@Column(name = "VERSIONE")
		protected Integer version;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getIdValidazione() {
			return idValidazione;
		}

		public void setIdValidazione(Integer idValidazione) {
			this.idValidazione = idValidazione;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}
		
		public void ignoreValidazioneCheck() {
			this.ignoreValidazioneCheck = true;
		}

		@PreUpdate
		@PreRemove
		public void preventUpdateFascicoloValidato() throws UpdateFascicoloValidatoException {
		    if (!ignoreValidazioneCheck && idValidazione > 0) {
		    	throw new UpdateFascicoloValidatoException();
		    }
		}
}
