package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.persistence.RequestId;
import es.udc.fi.dc.fd.model.persistence.RequestImpl;

public interface RequestRepository extends JpaRepository<RequestImpl, RequestId> {

}
