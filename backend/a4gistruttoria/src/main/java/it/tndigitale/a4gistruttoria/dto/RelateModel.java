package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

public class RelateModel implements Comparable {

    public RelateModel(Sostegno support, TipoIstruttoria payment, Long idIstruttoria) {
        this.support = support;
        this.payment = payment;
        this.idIstruttoria = idIstruttoria;
    }

    public RelateModel() {
    }

    private Sostegno support;
    private TipoIstruttoria payment;
    private Long idIstruttoria;

    public Sostegno getSupport() {
        return support;
    }

    public void setSupport(Sostegno support) {
        this.support = support;
    }

    public TipoIstruttoria getPayment() {
        return payment;
    }

    public void setPayment(TipoIstruttoria payment) {
        this.payment = payment;
    }

    public Long getIdIstruttoria() {
        return idIstruttoria;
    }

    public void setIdIstruttoria(Long idIstruttoria) {
        this.idIstruttoria = idIstruttoria;
    }

    @Override
    public int compareTo(Object o) {
        RelateModel model = (RelateModel) o;
        if (model.getSupport().equals(this.getSupport())) {
            return this.getPayment().compareTo(model.getPayment());
        } else {
            return model.getSupport().name().compareTo(this.getSupport().name());
        }
    }
}
