package es.udc.fi.dc.fd.repository;

import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<MatchImpl, MatchId> {

  @Query("SELECT m FROM Match m WHERE m.matchId.user1 = ?1 OR m.matchId.user2 = ?1")
  Slice<MatchImpl> findFriends(Long userId, Pageable pageable);

  @Query("SELECT m FROM Match m WHERE m.matchId.user1 = ?1 AND m.matchId.user2 = ?2")
  Optional<MatchImpl> findMatch(Long userId1, Long userId2);

  @Query("SELECT m FROM Match m WHERE (m.matchId.user1 = ?1 AND m.matchId.user2 = ?2) OR "
      + "(m.matchId.user2 = ?1 AND m.matchId.user1 = ?2) ")
  Optional<MatchImpl> weAreFriends(Long subject, Long object);

}
