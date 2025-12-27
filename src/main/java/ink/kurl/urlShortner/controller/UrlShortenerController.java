package ink.kurl.urlShortner.controller;

import static ink.kurl.urlShortner.util.Constants.BASE_URL;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ink.kurl.urlShortner.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UrlShortenerController {
	
	private final UrlShortenerService service;
	
	UrlShortenerController(UrlShortenerService service) {
		this.service = service;
	}
	
	@PostMapping("/shorten")
	public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
		String shortCode = service.shortenUrl(originalUrl);
		service.saveUrl(shortCode, originalUrl);
		return ResponseEntity.ok(BASE_URL + shortCode);
	}
	
	@GetMapping("/u/{code}")
	public void redirectToOriginalUrl(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
		String originalUrl = service.getOriginalUrl(shortCode);
		if (originalUrl == null) {
			// URL not found → 404
			response.sendError(HttpStatus.NOT_FOUND.value(), "URL not found");
		}
		// Redirect to original URL → 302
		response.sendRedirect(originalUrl);
	}

}
