package de.awr;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class EasierExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Dies ist ein Exception Hook von Spring, diese Methode wird aufgerufen, kurz
	 * bevor Spring ein HTTP400 mit einer Fehlermeldung an den Client senden will.
	 * 
	 * Da spring allerdings die genaue Begründung, also welches Problem genau
	 * aufgetreten ist, dem user verschweigt, wollen wir hier das nachholen und die
	 * Fehlerinformation mit in den body des http400 packen.
	 * 
	 * Wenn es sich Beispielsweise um ein validationproblem eines Feldes handelt, so
	 * wird die Klasse, Objekt, Feldname zurückgeliefert, als auch eine
	 * Beschreibung, warum genau das Feld invalide ist.
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		return handleExceptionInternal(ex, new MethodArgumentError(ex), headers, HttpStatus.BAD_REQUEST, request);
	}
	
	/**
	 * Ein Objekt dieser Klasse wird in ein <b>JSON</b> umgewandelt und per HTTP im body versendet.<br />
	 * Der body enthält die genaue Fehlerbeschreibung: z.B.: welche Felder genau invalide sind.
	 */
	class MethodArgumentError {

		@SuppressWarnings("unused")
		private HttpStatus status = HttpStatus.BAD_REQUEST;
		@SuppressWarnings("unused")
		private String message;
		@SuppressWarnings("unused")
		private List<ObjectError> errors;

		MethodArgumentError(MethodArgumentNotValidException exception) {
			message = exception.getMessage();
			errors = exception.getBindingResult().getAllErrors();
		}
	}

}