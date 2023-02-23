package vttp_csf.day39.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VoteRepository {

	@Autowired @Qualifier("post")
	private RedisTemplate<String, String> template;

	// create and initialise maps for each new post
	public void initialize(String postId) {
		HashOperations<String, String, String> voteMap = template.opsForHash();
		voteMap.put(postId, "like", "0");
		voteMap.put(postId, "dislike", "0");
	}

	public void like(String postId) {
		addVote(postId, "like");
	}
	public void dislike(String postId) {
		addVote(postId, "dislike");
	}

	public Map<String, Integer> getVotes(String postId) {
		HashOperations<String, String, String> voteMap = template.opsForHash();
		Map<String, Integer> votes = new HashMap<>();
		votes.put("like", Integer.parseInt(voteMap.get(postId, "like")));
		votes.put("dislike", Integer.parseInt(voteMap.get(postId, "dislike")));
		return votes;
	}

	public void addVote(String postId, String key) {
		HashOperations<String, String, String> voteMap = template.opsForHash();
		voteMap.increment(postId, key, 1);
	}
}