package ink.kurl.urlShortner.service.impl;

import static ink.kurl.urlShortner.util.Constants.CODE_LENGTH;
import static ink.kurl.urlShortner.util.Constants.UPPERCASE;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import ink.kurl.urlShortner.domain.enums.RedisDirectory;
import ink.kurl.urlShortner.service.UrlShortenerService;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
	
	private final StringRedisTemplate redisTemplate;
	
	UrlShortenerServiceImpl(StringRedisTemplate redisTemplate){
		this.redisTemplate = redisTemplate;
	}
	
	@Override
	public String shortenUrl(String originalUrl) {
		StringBuilder shortCode = new StringBuilder();
		for (int i = 0; i < CODE_LENGTH; i++) {
			int index = (int) (Math.random() * UPPERCASE.length());
			shortCode.append(UPPERCASE.charAt(index));
		}
		return shortCode.toString();
	}
	
	@Override
	public void saveUrl(String shortCode, String originalUrl) {
		redisTemplate.opsForValue().set(RedisDirectory.URL_SHORT_CODE.buildKey(shortCode), originalUrl);
		redisTemplate.opsForValue().set(RedisDirectory.URL_ORIGINAL.buildKey(originalUrl), shortCode);
	}

	@Override
	public String getOriginalUrl(String shortCode) {
		return redisTemplate.opsForValue().get(RedisDirectory.URL_SHORT_CODE.buildKey(shortCode));
	}
	
	@Override
	public String getShortCode(String originalUrl) {
		return redisTemplate.opsForValue().get(RedisDirectory.URL_ORIGINAL.buildKey(originalUrl));
	}

}
