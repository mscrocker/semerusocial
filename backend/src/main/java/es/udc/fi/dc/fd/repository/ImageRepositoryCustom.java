package es.udc.fi.dc.fd.repository;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import org.springframework.data.domain.Slice;

public interface ImageRepositoryCustom {

  Slice<ImageImpl> findAnonymousCarrusel(String city, int page, int size);

}
