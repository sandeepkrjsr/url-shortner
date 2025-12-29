package ink.kurl.urlShortner.service;

public interface UrlShortenerService {

	public String shortenUrl(String originalUrl);
	
	public void saveUrl(String shortCode, String originalUrl);

	public String getOriginalUrl(String shortCode);

	public String getShortCode(String originalUrl);

}
