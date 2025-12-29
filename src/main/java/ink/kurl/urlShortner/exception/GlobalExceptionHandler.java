package ink.kurl.urlShortner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RequestNotPermitted.class)
	public ResponseEntity<String> handleRateLimit(RequestNotPermitted ex) {
	    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
	            .body("Too many requests - please try again later.");
	}

}
