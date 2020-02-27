package es.udc.fi.dc.fd.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import es.udc.fi.dc.fd.dtos.SearchUsersDto;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public Query findByCriteriaQuery(SearchCriteria criteria, Long userId, Boolean count) {

		String queryString = "SELECT p FROM User p ";

		queryString += "JOIN User self ON self.id = :userId ";
		queryString += "WHERE (p.premium = true OR (";
		// LOGICA SOBRE CRITERIA
		queryString += " TIMESTAMPDIFF(year, self.date, curdate()) >= p.criteriaMinAge ";
		queryString += " AND TIMESTAMPDIFF(year, self.date, curdate()) <= p.criteriaMaxAge ";
		queryString += " AND (self.city IN (SELECT aux.cityCriteriaId.city FROM Cities aux WHERE aux.cityCriteriaId.userId = self.id) ";
		queryString += " OR (0 = (SELECT count(aux.cityCriteriaId.city )FROM Cities aux WHERE aux.cityCriteriaId.userId = self.id))) ";
		queryString += " AND "
			+ "((p.criteriaSex = 'ANY') OR "
			+ "(p.criteriaSex = 'OTHER' AND (self.sex <> 'Male') AND (self.sex <> 'Female')) OR "
			+ "(p.criteriaSex = 'MALE' AND self.sex = 'Male') OR "
			+ "(p.criteriaSex = 'FEMALE' AND self.sex = 'Female')) ";
		queryString += " AND p.date <= :maxDate ";
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
		queryString += ")) AND p.id != :userId ";

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

	@Override
	public int findByCriteriaMaxResults(SearchCriteria criteria, Long userId) {

		return findByCriteriaQuery(criteria, userId, true).getResultList().size();


	}

	@SuppressWarnings("unchecked")
	@Override
	public Slice<UserImpl> searchUsersByMetadataAndKeywords(SearchUsersDto params, int page, int size) {
		String queryString = "SELECT u FROM User u WHERE ";

		if (params.getKeywords() != null) {
			final String[] keywords = params.getKeywords().split(" ");
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("( ");
			for (int i = 0; i < keywords.length - 1; i++) {
				stringBuilder.append("u.description LIKE ");
				stringBuilder.append(":keyword" + i);
				stringBuilder.append(" OR ");
			}
			stringBuilder.append("u.description LIKE ");
			stringBuilder.append(":keyword" + (keywords.length - 1));
			stringBuilder.append(" )");
			queryString += stringBuilder.toString();
		}

		if (params.getMetadata() != null && params.getKeywords() != null) {
			queryString += " AND ";
		}

		SexCriteriaEnum sex = null;
		Integer minAge = null;
		Integer maxAge = null;
		List<String> cities = null;
		Double minRate = null;
		if (params.getMetadata() != null) {
			sex = params.getMetadata().getSex();
			minAge = params.getMetadata().getMinAge();
			maxAge = params.getMetadata().getMaxAge();
			cities = params.getMetadata().getCity();
			minRate = (double) params.getMetadata().getMinRate();

			queryString += "( u.sex = :sex";
			queryString += " OR (timestampdiff(year, u.date, curdate()) >= :minAge AND timestampdiff(year, u.date, curdate()) <= :maxAge )";
			queryString += " OR u.city IN (:cities)";
			queryString += " OR u.rating >= :minRate )";
		}

		final Query query = entityManager.createQuery(queryString).setFirstResult(page * size).setMaxResults(size + 1);

		if (params.getKeywords() != null) {
			final String[] keywords = params.getKeywords().split(" ");
			for (int i = 0; i < keywords.length; i++) {
				query.setParameter("keyword" + i, keywords[i]);
			}
		}

		if (params.getMetadata() != null) {
			query.setParameter("sex", sex.toString());
			query.setParameter("minAge", minAge);
			query.setParameter("maxAge", maxAge);
			query.setParameter("cities", cities);
			query.setParameter("minRate", minRate);
		}

		final ArrayList<UserImpl> result = (ArrayList<UserImpl>) query.getResultList();

		final boolean hasNext = result.size() == size + 1;
		if (hasNext) {
			result.remove(result.size() - 1);
		}
		return new SliceImpl<>(result, PageRequest.of(page, size), hasNext);

	}
}
