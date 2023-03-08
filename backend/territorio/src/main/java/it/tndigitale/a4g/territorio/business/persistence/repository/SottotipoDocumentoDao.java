package it.tndigitale.a4g.territorio.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.territorio.business.persistence.entity.SottotipoDocumentoModel;

@Repository
public interface SottotipoDocumentoDao extends JpaRepository <SottotipoDocumentoModel, Long> {
	
}
