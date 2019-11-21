package es.udc.fi.dc.fd.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;

public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Slice<FriendChatTitle> getLatestConversations(Long userId, int page, int size) {
		Query q = entityManager.createNativeQuery(
				"select f.friend friendId, f.name friendName, m.messageContent content, (case when m.transmitter = :userId then 0 else 1 end) sentByYou, m.date date from\r\n" + 
				"(select (case when aux.user1 = :userId then aux.user2 else aux.user1 end) friend, tmp.userName name\r\n" + 
				"from MatchTable aux\r\n" + 
				"join UserTable tmp\r\n" + 
				"on tmp.id = (case when aux.user1 = :userId then aux.user2 else user1 end)\r\n" + 
				"where (aux.user1 = :userId) or (aux.user2 = :userId)) f\r\n" + 
				"join MessageTable m\r\n" + 
				"on m.user1 = f.friend or m.user2 = f.friend\r\n" + 
				"where m.date in (select MAX(m1.date)\r\n" + 
				"				from MessageTable m1\r\n" + 
				"				where (((case when m1.transmitter = m1.user1 then m1.user1 else m1.user2 end) = (case when m.transmitter = m.user1 then m.user1 else m.user2 end)) and \r\n" + 
				"				((case when m1.transmitter = m1.user1 then m1.user2 else m1.user1 end) = (case when m.transmitter = m.user1 then m.user2 else m.user1 end))) or\r\n" + 
				"				(((case when m1.transmitter = m1.user1 then m1.user1 else m1.user2 end) = (case when m.transmitter = m.user1 then m.user2 else m.user1 end)) and \r\n" + 
				"				((case when m1.transmitter = m1.user1 then m1.user2 else m1.user1 end) = (case when m.transmitter = m.user1 then m.user1 else m.user2 end))))\r\n" + 
				"order by date desc " +
				"limit :size offset :page" + 
				"");
		
		q.setParameter("userId", userId);
		q.setParameter("size", size);
		q.setParameter("page", page * size);

		
		List<FriendChatTitle> result= ((List<Object[]>)q.getResultList()).stream().map((Object[] e) ->{
			return new FriendChatTitle(
				((BigInteger)e[0]).longValue(),
				(String)e[1],
				(String)e[2], 
				((Integer)e[3]).equals(0) ? false : true,
				((Timestamp)e[4]).toLocalDateTime()
			);
		}).collect(Collectors.toList());
		
		
		boolean hasNext = result.size() == size + 1;
		if (hasNext) {
			result.remove(result.size() - 1);
		}
		return new SliceImpl<FriendChatTitle>(result, PageRequest.of(page, size), hasNext);
	}

}
