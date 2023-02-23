package vttp_csf.day39.services;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import vttp_csf.day39.models.Post;
import vttp_csf.day39.models.User;
import vttp_csf.day39.repositories.ImageRepository;
import vttp_csf.day39.repositories.PostRepository;
import vttp_csf.day39.repositories.UserRepository;
import vttp_csf.day39.repositories.VoteRepository;

@Service
public class PostService {

	private Logger logger = Logger.getLogger(PostService.class.getName());

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ImageRepository imageRepo;

	@Autowired
	private PostRepository postRepo;

	@Autowired
	private VoteRepository voteRepo;

	public void like(String postId) {
		voteRepo.like(postId);
	}
	public void dislike(String postId) {
		voteRepo.dislike(postId);
	}

	// retrive post by postId
	public Optional<Post> getPost(String postId) {

		// Find the post
		Optional<Post> opt = postRepo.getPost(postId);
		if (opt.isEmpty())
			return Optional.empty();

		// Get the # likes and dislikes
		Map<String, Integer> votes = voteRepo.getVotes(postId);

		Post post = opt.get();					// create model object
		post.setLike(votes.get("like"));		// populate # of likes
		post.setDislike(votes.get("dislike"));	// populate # of dislikes

		return Optional.of(post);
	}

	// new post
	public Optional<String> createPost(Post post, MultipartFile file) {

		// Check if the user is valid
		Optional<User> opt = userRepo.findUserByEmail(post.getEmail());
		if (opt.isEmpty())
			return Optional.empty();

		// Fill the post with pertinent user details
		User user = opt.get();
		post.setName(user.getName());
		post.setUserId(user.getUserId());

		// Set the post date
		post.now();

		// Set a unique post id
		String postId = UUID.randomUUID().toString().substring(0, 8);
		post.setPostId(postId);

		// Upload the image to Spaces
		imageRepo.upload(post, file);

		// Create post in Mongo
		ObjectId objectId = postRepo.insertPost(post);

		logger.log(Level.INFO
				, "postId: %s -> _id: %s".formatted(postId, objectId.toString()));

		voteRepo.initialize(postId);		// initialise 0 likes, 0 dislikes

		// Setup likes and dislike
		return Optional.of(postId);
	}
}