package vttp_csf.day39.repositories;

import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp_csf.day39.models.Post;

@Repository
public class PostRepository {
    
    @Autowired
    private MongoTemplate template;

    private static String C_POST = "post";

    // insert post
    public ObjectId insertPost(Post p) {
        Document insertDoc = template.insert(p.toDocument(), C_POST);
        return insertDoc.getObjectId("_id");
    }

    // retrive post
    public Optional<Post> getPost(String postId) {
        Criteria cri = Criteria.where("postId").is(postId);
        Query q = Query.query(cri);
        Document result = template.findOne(q, Document.class, C_POST);
        if (null == result) {
            return Optional.empty();
        }

        return Optional.of(Post.create(result));
    }
}
