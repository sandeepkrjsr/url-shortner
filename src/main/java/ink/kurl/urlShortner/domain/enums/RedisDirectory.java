package ink.kurl.urlShortner.domain.enums;

public enum RedisDirectory {
	
	URL_SHORT_CODE("url:shortcode:"),
	URL_ORIGINAL("url:originalurl:");
	
	private final String directory;

	RedisDirectory(String directory) {
		this.directory = directory;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public String buildKey(String key) {
		return this.directory + "'" + key + "'";
	}

}
