package it.tndigitale.a4g.framework.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyTestEntityRepository extends JpaRepository<MyTestEntity, Long>, A4GEntitaDominioJPARepository<MyTestEntity> {

}
