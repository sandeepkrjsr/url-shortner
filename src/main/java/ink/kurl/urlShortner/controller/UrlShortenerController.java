package ink.kurl.urlShortner.controller;

import static ink.kurl.urlShortner.util.Constants.REDIRECT_URL;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ink.kurl.urlShortner.service.UrlShortenerService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UrlShortenerController {
	
	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);
	
	private final UrlShortenerService service;
	
	UrlShortenerController(UrlShortenerService service) {
		this.service = service;
	}
	
	@RateLimiter(name = "apiLimiter")
	@PostMapping("/shorten")
	public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
		logger.debug("Inside UrlShortenerController::shortenUrl");
		originalUrl = originalUrl.replace("\"", "");
		String shortCode = service.getShortCode(originalUrl);
		if (shortCode == null) {
			shortCode = service.shortenUrl(originalUrl);
			service.saveUrl(shortCode, originalUrl);
		}
		return ResponseEntity.ok(REDIRECT_URL + shortCode);
	}
	
	@RateLimiter(name = "apiLimiter")
	@GetMapping("/u/{shortCode}")
	public void redirectToOriginalUrl(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
		logger.debug("Inside UrlShortenerController::redirectToOriginalUrl");
		String originalUrl = service.getOriginalUrl(shortCode);
		if (originalUrl == null) {
			// URL not found → 404
			response.sendError(HttpStatus.NOT_FOUND.value(), "URL not found");
		}
		originalUrl = originalUrl.replace("\"", "");
		// Redirect to original URL → 302
		response.sendRedirect(originalUrl);
	}

}
