package es.udc.fi.dc.fd.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;

public interface MatchRepository extends JpaRepository<MatchImpl, MatchId> {

	@Query("SELECT m FROM Match m WHERE m.matchId.user1 = ?1 OR m.matchId.user2 = ?1")
	Slice<MatchImpl> findFriends(Long userId, Pageable pageable);

}
