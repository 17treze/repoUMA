package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder;

import org.springframework.data.jpa.domain.Specification;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.DocumentazioneRichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.DocumentazioneRichiestaModificaSuoloModel_;

public class DocumentazioneRichiestaSpecificationBuilder {

    private DocumentazioneRichiestaSpecificationBuilder() {
    }

    public static Specification<DocumentazioneRichiestaModificaSuoloModel> filterByIdRichiestaAndIdDocumento(Long idRichiesta, Long idDocumento) {
        return (root, query, builder) -> Specification
            .where(filterByIdRichiesta(idRichiesta))
            .and(filterByIdDocumento(idDocumento))
            .toPredicate(root, query, builder);
    }
    public static Specification<DocumentazioneRichiestaModificaSuoloModel> filterByIdDichiaratoAndIdDocumento(Long idDichiarato, Long idDocumento) {
        return (root, query, builder) -> Specification
            .where(filterByIdDichiarato(idDichiarato))
            .and(filterByIdDocumento(idDocumento))
            .toPredicate(root, query, builder);
    }
    public static Specification<DocumentazioneRichiestaModificaSuoloModel> filterByIdRichiesta(Long idRichiesta) {
        return (root, query, builder) -> idRichiesta == null
            ? null
            : builder.equal(root.get(DocumentazioneRichiestaModificaSuoloModel_.richiestaModificaSuolo), idRichiesta);
    }
    public static Specification<DocumentazioneRichiestaModificaSuoloModel> filterByIdDichiarato(Long idDichiarato) {
        return (root, query, builder) -> idDichiarato == null
            ? null
            : builder.equal(root.get(DocumentazioneRichiestaModificaSuoloModel_.suoloDichiarato), idDichiarato);
    }
    public static Specification<DocumentazioneRichiestaModificaSuoloModel> filterByIdDocumento(Long idDocumento) {
        return (root, query, builder) -> idDocumento == null
            ? null
            : builder.equal(root.get(DocumentazioneRichiestaModificaSuoloModel_.id), idDocumento);
    }
}