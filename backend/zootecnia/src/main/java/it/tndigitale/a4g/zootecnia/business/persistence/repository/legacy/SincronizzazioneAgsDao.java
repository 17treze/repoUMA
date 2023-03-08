package it.tndigitale.a4g.zootecnia.business.persistence.repository.legacy;

public interface SincronizzazioneAgsDao {

    public void sincronizzaFascicoloAgs(String cuaa, Integer idValidazione) throws SincronizzazioneAgsException;
}