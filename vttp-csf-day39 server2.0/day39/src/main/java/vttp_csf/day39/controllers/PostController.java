package vttp_csf.day39.controllers;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp_csf.day39.models.Post;
import vttp_csf.day39.services.PostService;

@Controller
@RequestMapping(path="/post")
@CrossOrigin(origins = "*")
public class PostController {

	private Logger logger = Logger.getLogger(PostController.class.getName());

	@Autowired
	private PostService postSvc;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> postPostJson(@RequestPart MultipartFile image,
			@RequestPart String email, @RequestPart String title, @RequestPart String text) {
		
		// create new model object
		Post post = new Post();
		post.setEmail(email);
		post.setTitle(title);
		post.setText(text);

		// create new post
		Optional<String> opt = postSvc.createPost(post, image);
		String postId = opt.get();

		logger.log(Level.INFO, "New postId: %s".formatted(postId));

		// return postId to Angular
		JsonObject response = Json.createObjectBuilder()
			.add("postId", postId)
			.build();

		return ResponseEntity.ok(response.toString());
	}

	// SB testing method
	@PostMapping
	public String postPost(@RequestPart MultipartFile image,
			@RequestPart String email, @RequestPart String title, @RequestPart String text,
			Model model) {

		logger.log(Level.INFO, "File name: %s\n".formatted(image.getOriginalFilename()));

		Post post = new Post();
		post.setEmail(email);
		post.setTitle(title);
		post.setText(text);

		Optional<String> opt = postSvc.createPost(post, image);
		String postId = opt.get();

		logger.log(Level.INFO, "New postId: %s".formatted(postId));

		return "redirect:/post/%s".formatted(postId);
	}

	// thymeleaf
	// @GetMapping(path="{postId}")
	// public String getPost(@PathVariable String postId, Model model) {

	// 	Optional<Post> opt = postSvc.getPost(postId);

	// 	// If we cannot find post, return to index.html
	// 	if (opt.isEmpty())
	// 		return "redirect:/";

	// 	model.addAttribute("post", opt.get());
	// 	return "post";
	// }

	@GetMapping(path="{postId}")
	@ResponseBody
	public ResponseEntity<Post> getPostAngular(@PathVariable String postId) {

		System.out.println(">>>> calling on /post/id\n\n");

		Optional<Post> opt = postSvc.getPost(postId);

		// If we cannot find post, return to index.html
		// if (opt.isEmpty()) {
		// 	JsonObject response = Json.createObjectBuilder()
		// 								.add("error", postId.concat(" not found"))
		// 								.build();
		// 	return ResponseEntity.notFound();
		// }
		
		Post p = opt.get();
		System.out.println(">>>> post ----- " + p);

		return ResponseEntity.ok(p);
	}

	// thymeleaf
	// @PostMapping(path="{postId}/{vote}")
	// public String postLike(@PathVariable String postId, @PathVariable String vote, Model model) {

	// 	if ("like".equals(vote.trim().toLowerCase()))
	// 		postSvc.like(postId);

	// 	else if ("dislike".equals(vote.trim().toLowerCase()))
	// 		postSvc.dislike(postId);

	// 	Optional<Post> opt = postSvc.getPost(postId);

	// 	model.addAttribute("post", opt.get());
	// 	return "post";
	// }

	@PostMapping(path="{postId}/{vote}")
	@ResponseBody
	public ResponseEntity<Post> postLike(@PathVariable String postId, @PathVariable String vote) {

		if ("like".equals(vote.trim().toLowerCase()))
			postSvc.like(postId);

		else if ("dislike".equals(vote.trim().toLowerCase()))
			postSvc.dislike(postId);

		Optional<Post> opt = postSvc.getPost(postId);
		System.out.println(">>>> update likes ----- " + opt.get());

		return ResponseEntity.ok(opt.get());
	}
}