package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "A4GT_CONF_ISTRUTTORIA")
public class ConfigurazioneIstruttoriaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

    private static final long serialVersionUID = 7594333952575839442L;

    @Column(name = "ANNO_CAMPAGNA", nullable = false)
    private Integer campagna;

    @Column(name = "DT_SCADENZA_DOMANDE_INIZIALI")
    private LocalDate dtScadenzaDomandeIniziali;

    @Column(name = "PERC_PAGAMENTO")
    private Double percentualePagamento;

    @Column(name = "PERC_DISCIPLINA_FINANZIARIA")
    private Double percentualeDisciplinaFinanziaria;
    
    @Column(name = "DT_SCADENZA_DOMANDE_MODIFICA")
    private LocalDate dtScadenzaDomandeModifica;

    public Integer getCampagna() {
        return campagna;
    }

    public ConfigurazioneIstruttoriaModel setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }

    public LocalDate getDtScadenzaDomandeIniziali() {
        return dtScadenzaDomandeIniziali;
    }

    public ConfigurazioneIstruttoriaModel setDtScadenzaDomandeIniziali(LocalDate dtScadenzaDomandeIniziali) {
        this.dtScadenzaDomandeIniziali = dtScadenzaDomandeIniziali;
        return this;
    }

    public Double getPercentualePagamento() {
        return percentualePagamento;
    }

    public ConfigurazioneIstruttoriaModel setPercentualePagamento(Double percentualePagamento) {
        this.percentualePagamento = percentualePagamento;
        return this;
    }

    public Double getPercentualeDisciplinaFinanziaria() {
        return percentualeDisciplinaFinanziaria;
    }

    public ConfigurazioneIstruttoriaModel setPercentualeDisciplinaFinanziaria(Double percentualeDisciplinaFinanziaria) {
        this.percentualeDisciplinaFinanziaria = percentualeDisciplinaFinanziaria;
        return this;
    }
    
    public LocalDate getDtScadenzaDomandeModifica() {
        return dtScadenzaDomandeModifica;
    }

    public ConfigurazioneIstruttoriaModel setDtScadenzaDomandeModifica(LocalDate dtScadenzaDomandeModifica) {
        this.dtScadenzaDomandeModifica = dtScadenzaDomandeModifica;
        return this;
    }
}
