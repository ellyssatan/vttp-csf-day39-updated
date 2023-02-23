package vttp_csf.day39;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {
    
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.database}")
    private int redisDb;
    @Value("${spring.redis.username}")
    private String redisUsername;
    @Value("${REDISPASSWORD}")
    private String redispw;

    @Value("${spring.mongo.url}")
    private String mongoUrl;

    @Value("${space.secret.key}")
    private String secretKey;
    @Value("${space.access.key}")
    private String accessKey;

    @Value("${space.endpoint.url}")
    private String spacesEndpointUrl;
    @Value("${space.endpoint.region}")
    private String spacesRegion;

    @Bean("post")
    public RedisTemplate<String, String> createRedisTemplate() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        
        config.setDatabase(redisDb);
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setUsername(redisUsername);
        config.setPassword(redispw);

        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFac);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public MongoTemplate createMongoTemplate() {
        MongoClient client = MongoClients.create(mongoUrl);
        return new MongoTemplate(client, "articles");
    }

    @Bean
    public AmazonS3 createS3Client() {
        BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
        EndpointConfiguration epConfig = new EndpointConfiguration(spacesEndpointUrl, spacesRegion);

        return AmazonS3ClientBuilder.standard()
                                .withEndpointConfiguration(epConfig)
                                .withCredentials(new AWSStaticCredentialsProvider(cred))
                                .build();
    }
}
