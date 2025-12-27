package ink.kurl.urlShortner.service.impl;

import static ink.kurl.urlShortner.util.Constants.CODE_LENGTH;
import static ink.kurl.urlShortner.util.Constants.UPPERCASE;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import ink.kurl.urlShortner.service.UrlShortenerService;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
	
	private final RedisTemplate<String, String> redisTemplate;
	
	UrlShortenerServiceImpl(RedisTemplate<String, String>redisTemplate){
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
		redisTemplate.opsForValue().set(shortCode, originalUrl);
	}

	@Override
	public String getOriginalUrl(String shortCode) {
		return redisTemplate.opsForValue().get(shortCode);
	}

}
