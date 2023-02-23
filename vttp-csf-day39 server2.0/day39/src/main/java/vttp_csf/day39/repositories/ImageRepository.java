package vttp_csf.day39.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import vttp_csf.day39.models.Post;

@Repository
public class ImageRepository {
    
    private Logger logger = Logger.getLogger(ImageRepository.class.getName());

    @Value("${space.bucket}")
    private String bucket;

    @Value("${space.endpoint.url}")
    private String endpointUrl;

    @Autowired
    private AmazonS3 s3;


    public boolean upload(Post p, MultipartFile file) {

        Map<String, String> userData = new HashMap<>();
        userData.put("name", p.getName());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // Calculate MD5
		/*
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(file.getBytes());
			byte[] md5hash = md5.digest();
			String hash = HexFormat.of().formatHex(md5hash);
			System.out.printf(">>>> md5 hash: %s\n", hash);
			metadata.setContentMD5(hash);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Calculate MD5", ex);
		}
		*/

        try {
            PutObjectRequest putReq = new PutObjectRequest(
                                    bucket, p.getPostId(),
                                    file.getInputStream(), metadata);
            putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putReq);
            
        } catch (Exception e) {
            // TODO: handle exception
            logger.log(Level.WARNING, "Put S3", e);
            return false;

        }

        String imgUrl = "https://%s.%s/%s".formatted(bucket, endpointUrl, p.getPostId());
        p.setImageUrl(imgUrl);
        return true;
    }
}
