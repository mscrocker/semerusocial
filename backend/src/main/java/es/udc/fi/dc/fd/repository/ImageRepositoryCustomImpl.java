package es.udc.fi.dc.fd.repository;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public class ImageRepositoryCustomImpl implements ImageRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Slice<ImageImpl> findAnonymousCarrusel(String city, int page, int size) {
		final String queryString = "SELECT u.id FROM User u WHERE u.city = :ciudad ORDER BY u.rating DESC";

		final Query query = entityManager.createQuery(queryString).setMaxResults(10);

		query.setParameter("ciudad", city);

		final ArrayList<Long> ids = (ArrayList<Long>) query.getResultList();
		if (ids.size() == 0) {
			return new SliceImpl<>(new ArrayList<ImageImpl>(), PageRequest.of(page, size), false);
		}

		String queryString2 = "SELECT i FROM Image i WHERE i.user.id=" + ids.get(0);
		for (int i = 1; i < ids.size(); i++) {
			queryString2 += " OR i.user.id=" + ids.get(i);
		}

		final Query query2 = entityManager.createQuery(queryString2).setFirstResult(page * size)
				.setMaxResults(size + 1);

		final ArrayList<ImageImpl> result = (ArrayList<ImageImpl>) query2.getResultList();


		final boolean hasNext = result.size() == size + 1;
		if (hasNext) {
			result.remove(result.size() - 1);
		}
		return new SliceImpl<>(result, PageRequest.of(page, size), hasNext);


	}
}
