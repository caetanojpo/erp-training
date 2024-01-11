package br.com.erptraining.controller.advice;

import br.com.erptraining.exception.EntityNotFoundException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handle404Error() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorsData>> handle400Error(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors();
        List<ValidationErrorsData> validationErrorsDataList = errors.stream().map(ValidationErrorsData::new).toList();
        return ResponseEntity.badRequest().body(validationErrorsDataList);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();

        if (rootCause instanceof PSQLException psqlException && (isUniqueConstraintViolation(psqlException))) {
            String constraintName = extractConstraintName(psqlException);

            return new ResponseEntity<>("Violation of unique constraint: " + constraintName, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isUniqueConstraintViolation(PSQLException ex) {
        return "23505".equals(ex.getSQLState()); // SQLState 23505 represents a unique constraint violation
    }

    private String extractConstraintName(PSQLException ex) {
        return Objects.requireNonNull(ex.getServerErrorMessage()).getConstraint();
    }

    private record ValidationErrorsData(String field, String message) {
        public ValidationErrorsData(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
