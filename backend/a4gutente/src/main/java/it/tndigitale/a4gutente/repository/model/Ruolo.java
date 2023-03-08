package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

@SqlResultSetMapping(
	    name = "ruoloModelMapping",
	    classes = @ConstructorResult(
	            targetClass = Ruolo.class,
	            columns = {
	                    @ColumnResult(name = "identificativo"),
	                    @ColumnResult(name = "descrizione")
	            }
    )
)
@NamedNativeQuery(name = "findRuoliUtente",
query = "select distinct r.identificativo as identificativo," +
	" r.descrizione as descrizione" +
	" from A4GD_RUOLO r" +
	" join A4GR_PROFILO_RUOLO rp on rp.id_ruolo = r.identificativo" +
	" join A4GR_UTENTE_PROFILO pu on pu.id_profilo = rp.id_profilo" +
	" join A4GT_UTENTE u on u.id = pu.id_utente" +
	" where u.identificativo = :utente",
	resultSetMapping = "ruoloModelMapping"
)
@Entity
@Table(name="A4GD_RUOLO")
public class Ruolo implements Serializable {

	private static final long serialVersionUID = -4449782133335660215L;

	@Id
	private String identificativo;
	private String descrizione;

	public Ruolo(String identificativo, String descrizione) {
		super();
		this.identificativo = identificativo;
		this.descrizione = descrizione;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
