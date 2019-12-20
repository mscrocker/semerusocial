package es.udc.fi.dc.fd.repository;

import org.springframework.data.domain.Slice;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public interface ImageRepositoryCustom {

	Slice<ImageImpl> findAnonymousCarrusel(String city, int page, int size);

}
