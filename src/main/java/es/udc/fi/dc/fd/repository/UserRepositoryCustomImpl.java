package es.udc.fi.dc.fd.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public Query findByCriteriaQuery(SearchCriteria criteria, Long userId, Boolean count) {

		String queryString = "SELECT p FROM User p ";
		if (count) {
			queryString = "SELECT count(id) FROM User p ";
		}

		queryString += "WHERE p.premium = true OR (p.date <= :maxDate ";
		queryString += "AND p.date >= :minDate ";
		if (criteria.getSex() == SexCriteriaEnum.OTHER) {
			queryString += "AND LOWER(p.sex) NOT LIKE 'female' ";
			queryString += "AND LOWER(p.sex) NOT LIKE 'male' ";
		} else if (criteria.getSex() != SexCriteriaEnum.ANY) {
			queryString += "AND LOWER(p.sex) LIKE LOWER(:sex) ";
		}

		queryString += "AND (p.ratingVotes = 0 OR p.rating >= :minRate) ";

		if (criteria.getCity() != null && !criteria.getCity().isEmpty()) {
			queryString += "AND LOWER(p.city) in (:cities) ";
		}

		// Que no te sugiera a ti mismo
		queryString += ") AND p.id != :userId ";

		//		if (!criteria.getCity().isEmpty()) {
		//			queryString += "AND p.city IN :cities ";
		//		}

		// Si esta bloqueado que no lo sugiera
		queryString += "AND p.id NOT IN (SELECT b.blockedId.object FROM Blocked b WHERE b.blockedId.subject=:userId) ";

		// Si ya son amigos que no lo sugiera
		queryString += "AND p.id NOT IN (SELECT m1.matchId.user1 FROM Match m1 WHERE m1.matchId.user2=:userId) ";
		queryString += "AND p.id NOT IN (SELECT m2.matchId.user2 FROM Match m2 WHERE m2.matchId.user1=:userId) ";

		// Si ya lo rechazó que no lo sugiera
		queryString += "AND p.id NOT IN (SELECT r.rejectedId.object FROM Rejected r WHERE r.rejectedId.subject=:userId) ";

		// Si ya lo aceptaste que no lo sugiera
		queryString += "AND p.id NOT IN (SELECT req.requestId.object FROM Request req WHERE req.requestId.subject= :userId) ";

		queryString += "ORDER BY p.id";

		Query query = entityManager.createQuery(queryString).setMaxResults(1);

		if (count) {
			query = entityManager.createQuery(queryString);
		}
		final LocalDateTime dateMax = LocalDateTime.now().minus(criteria.getMinAge(), ChronoUnit.YEARS);
		final LocalDateTime dateMin = LocalDateTime.now().minus(criteria.getMaxAge(), ChronoUnit.YEARS);
		final Double minRate = Double.valueOf(criteria.getMinRate());

		query.setParameter("maxDate", dateMax);
		query.setParameter("minDate", dateMin);
		query.setParameter("userId", userId);
		query.setParameter("minRate", minRate);
		if (criteria.getSex() != SexCriteriaEnum.ANY && criteria.getSex() != SexCriteriaEnum.OTHER) {
			query.setParameter("sex", criteria.getSex().toString());
		}
		if (criteria.getCity() != null && !criteria.getCity().isEmpty()) {
			query.setParameter("cities",
					criteria.getCity().stream().map(city -> city.toLowerCase()).collect(Collectors.toList()));
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<UserImpl> findByCriteria(SearchCriteria criteria, Long userId) {

		final List<UserImpl> users = findByCriteriaQuery(criteria, userId, false).getResultList();

		Optional<UserImpl> user = Optional.empty();
		if (!users.isEmpty()) {
			user = Optional.of(users.get(0));
		}

		return user;

	}

	@SuppressWarnings("unchecked")
	@Override
	public int findByCriteriaMaxResults(SearchCriteria criteria, Long userId) {

		final List<Long> numberUsers = findByCriteriaQuery(criteria, userId, true).getResultList();

		return numberUsers.get(0).intValue();

	}
}
