package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtCapoTracking;

@Repository
public interface CapoTrackingDao extends JpaRepository<A4gtCapoTracking, Long> {
}
