package it.tndigitale.a4gutente.dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import it.tndigitale.a4gutente.codici.CodResponsabilita;
import lombok.Data;

@Data
public class ResponsabilitaRichieste implements Serializable {

    private static final long serialVersionUID = 6204613961021118223L;

    private List<TitolareImpresa> responsabilitaTitolare;
    private List<TitolareImpresa> responsabilitaLegaleRappresentante;
    private List<RuoloCAA> responsabilitaCaa;
    private List<RuoloPAT> responsabilitaPat;
    private List<CollaboratoreAltriEnti> responsabilitaAltriEnti;
    private List<Consulente> responsabilitaConsulente;
    private List<RuoloDistributore> responsabilitaDistributore;

    @Data
    public static class TitolareImpresa implements Serializable {

        private static final long serialVersionUID = -4644903587000651738L;

        private String cuaa;
        private String denominazione;
    }
    @Data
    public static class RuoloCAA implements Serializable {

        private static final long serialVersionUID = -4644903587000651738L;

        private Long idResponsabilita;
        private String responsabile;
        @Enumerated(EnumType.STRING)
        private CodResponsabilita codResponsabilita;
        private String elencoCaa;
        private List<EnteSede> sedi;
        private byte[] allegato;
    }
    @Data
    public static class RuoloPAT implements Serializable {

        private static final long serialVersionUID = -4644903587000651738L;

        private Long idResponsabilita;
        @Enumerated(EnumType.STRING)
        private CodResponsabilita codResponsabilita;
        private String matricola;
        private String dirigente;
        private String dipartimento;
        private byte[] allegato;
        private String note;
    }
    @Data
    public static class CollaboratoreAltriEnti implements Serializable {

        private static final long serialVersionUID = -4644903587000651738L;

        private Long idResponsabilita;
        @Enumerated(EnumType.STRING)
        private CodResponsabilita codResponsabilita;
        private String denominazione;
        private String piva;
        private String note;
        private String dirigente;
        private byte[] allegato;
    }
    @Data
    public static class Consulente implements Serializable {

        private static final long serialVersionUID = -4644903587000651738L;

        private Long idResponsabilita;
        @Enumerated(EnumType.STRING)
        private CodResponsabilita codResponsabilita;
        private String ordine;
        private String iscrizione;
        private String cuaa;
        private String denominazione;
        private String rappresentante;
        private byte[] allegato;
    }
    @Data
    public static class RuoloDistributore implements Serializable {

        private static final long serialVersionUID = -4644903587000651738L;

        private Long idResponsabilita;
        private String responsabile;
        @Enumerated(EnumType.STRING)
        private CodResponsabilita codResponsabilita;
        private List<Distributore> distributori;
        private byte[] allegato;
    }
}