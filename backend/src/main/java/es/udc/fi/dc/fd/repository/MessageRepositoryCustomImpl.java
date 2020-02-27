package es.udc.fi.dc.fd.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import es.udc.fi.dc.fd.model.FriendChatTitle;

public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Slice<FriendChatTitle> getLatestConversations(Long userId, int page, int size) {
		//@formatter:off

		final Query q = entityManager.createNativeQuery(
			" Select main.friendId, u.userName,main.content,main.sentByYou,main.date\r\n" +
				"from \r\n" +
				" (\r\n" +
				" (\r\n" +
				"  SELECT \r\n" +
				"    CASE\r\n" +
				"      WHEN conversation.u2 = :userId THEN conversation.u1\r\n" +
				"      ELSE conversation.u2\r\n" +
				"    END friendId, \r\n" +
				"    t.messagecontent content, \r\n" +
				"    ( case when (transmitter = :userId) then 'true' else 'false' end) sentByYou, \r\n" +
				"    conversation.hora date\r\n" +
				"  FROM (\r\n" +
				"    SELECT \r\n" +
				"      user1 u1, \r\n" +
				"      user2 u2, \r\n" +
				"      max(date) hora\r\n" +
				"    FROM MessageTable m\r\n" +
				"    WHERE (\r\n" +
				"      user1 = :userId\r\n" +
				"      OR user2 = :userId\r\n" +
				"    )\r\n" +
				"    GROUP BY \r\n" +
				"      user1, \r\n" +
				"      user2\r\n" +
				"    \r\n" +
				"  ) conversation\r\n" +
				"    JOIN MessageTable t\r\n" +
				"      ON (\r\n" +
				"        t.date = conversation.hora\r\n" +
				"        AND user1 = conversation.u1\r\n" +
				"        AND user2 = conversation.u2\r\n" +
				"      )	\r\n" +
				")\r\n" +
				"UNION ALL (\r\n" +
				"  SELECT \r\n" +
				"    CASE\r\n" +
				"      WHEN user1 = :userId THEN user2\r\n" +
				"      ELSE user1\r\n" +
				"    END friendId, \r\n" +
				"    NULL, \r\n" +
				"    FALSE, \r\n" +
				"    NULL\r\n" +
				"  FROM MatchTable m\r\n" +
				"  WHERE (\r\n" +
				"    (\r\n" +
				"      m.user1 = :userId\r\n" +
				"      AND m.user2 NOT IN ((\r\n" +
				"        SELECT CASE\r\n" +
				"          WHEN user1 = :userId THEN user2\r\n" +
				"          ELSE user1\r\n" +
				"        END\r\n" +
				"        FROM MessageTable\r\n" +
				"        WHERE (\r\n" +
				"          user1 = :userId\r\n" +
				"          OR user2 = :userId\r\n" +
				"        )\r\n" +
				"        GROUP BY \r\n" +
				"          user1, \r\n" +
				"          user2\r\n" +
				"        \r\n" +
				"      ))\r\n" +
				"    )\r\n" +
				"    OR (\r\n" +
				"      m.user1 NOT IN (\r\n" +
				"        SELECT CASE\r\n" +
				"          WHEN user1 = :userId THEN user2\r\n" +
				"          ELSE user1\r\n" +
				"        END\r\n" +
				"        FROM MessageTable\r\n" +
				"        WHERE (\r\n" +
				"          user1 = :userId\r\n" +
				"          OR user2 = :userId\r\n" +
				"        )\r\n" +
				"        GROUP BY \r\n" +
				"          user1, \r\n" +
				"          user2\r\n" +
				"        \r\n" +
				"      )\r\n" +
				"      AND m.user2 = :userId\r\n" +
				"    )\r\n" +
				"  )\r\n" +
				")\r\n" +
				") main\r\n" +
				"join UserTable u \r\n" +
				"on u.id = main.friendId\r\n" +
				"ORDER BY date DESC\r\n" +
				"LIMIT :size\r\n" +
				"OFFSET :page");
		//@formatter:on

		q.setParameter("userId", userId);
		q.setParameter("size", size + 1);
		q.setParameter("page", page * size);

		final List<FriendChatTitle> result = ((List<Object[]>) q.getResultList()).stream().map((Object[] e) -> {
			return new FriendChatTitle(((BigInteger) e[0]).longValue(), (String) e[1],
				e[2] == null ? "" : (String) e[2], e[3].equals("true"),
				e[4] == null ? null : ((Timestamp) e[4]).toLocalDateTime());
		}).collect(Collectors.toList());

		final boolean hasNext = result.size() == size + 1;
		if (hasNext) {
			result.remove(result.size() - 1);
		}
		return new SliceImpl<>(result, PageRequest.of(page, size), hasNext);
	}

}
