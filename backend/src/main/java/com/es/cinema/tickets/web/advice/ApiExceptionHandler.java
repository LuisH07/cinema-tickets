package com.es.cinema.tickets.web.advice;

import com.es.cinema.tickets.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Value("${app.errors.include-stacktrace:false}")
    private boolean includeStacktrace;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBeanValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = baseProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Campos inválidos na requisição.",
                "VALIDATION_ERROR",
                request
        );

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        problemDetail.setProperty("errors", fieldErrors);
        attachDebugIfEnabled(problemDetail, exception);

        logger.warn("Validation error: path={}, fields={}", request.getRequestURI(), fieldErrors.keySet());
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintValidation(ConstraintViolationException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = baseProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Parâmetros inválidos na requisição.",
                "VALIDATION_ERROR",
                request
        );

        problemDetail.setProperty("errors", exception.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList());

        attachDebugIfEnabled(problemDetail, exception);

        logger.warn("Constraint validation error: path={}", request.getRequestURI());
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableJson(HttpMessageNotReadableException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = baseProblemDetail(
                HttpStatus.BAD_REQUEST,
                "JSON inválido",
                "O corpo da requisição está malformado ou inválido.",
                "INVALID_JSON",
                request
        );

        attachDebugIfEnabled(problemDetail, exception);
        logger.warn("Invalid JSON: path={}", request.getRequestURI());
        return problemDetail;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ProblemDetail handleBadRequestParams(Exception exception, HttpServletRequest request) {
        ProblemDetail problemDetail = baseProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                "Parâmetros obrigatórios ausentes ou inválidos.",
                "BAD_REQUEST",
                request
        );

        attachDebugIfEnabled(problemDetail, exception);
        logger.warn("Bad request params: path={}, type={}", request.getRequestURI(), exception.getClass().getSimpleName());
        return problemDetail;
    }

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException exception, HttpServletRequest request) {
        HttpStatus httpStatus = exception.getHttpStatus();

        ProblemDetail problemDetail = baseProblemDetail(
                httpStatus,
                exception.getTitle(),
                exception.getMessage(),
                exception.getErrorCode(),
                request
        );

        attachDebugIfEnabled(problemDetail, exception);

        if (httpStatus.is5xxServerError()) {
            logger.error("ApiException: code={}, path={}", exception.getErrorCode(), request.getRequestURI(), exception);
        } else {
            logger.warn("ApiException: code={}, path={}", exception.getErrorCode(), request.getRequestURI());
        }

        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = baseProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Não autenticado",
                "Faça login para acessar este recurso.",
                "UNAUTHENTICATED",
                request
        );

        attachDebugIfEnabled(problemDetail, exception);
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = baseProblemDetail(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                "Você não tem permissão para acessar este recurso.",
                "FORBIDDEN",
                request
        );

        attachDebugIfEnabled(problemDetail, exception);
        return problemDetail;
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ProblemDetail handleSpringErrorResponse(ErrorResponseException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = exception.getBody();

        problemDetail.setStatus(exception.getStatusCode().value());

        if (problemDetail.getTitle() == null) {
            problemDetail.setTitle("Falha na requisição");
        }
        if (problemDetail.getDetail() == null) {
            problemDetail.setDetail("A requisição falhou.");
        }

        problemDetail.setProperty("code", "SPRING_ERROR");
        problemDetail.setProperty("path", request.getRequestURI());
        problemDetail.setProperty("timestamp", Instant.now().toString());

        attachDebugIfEnabled(problemDetail, exception);
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception exception, HttpServletRequest request) {
        ProblemDetail problemDetail = baseProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro inesperado",
                "Ocorreu um erro inesperado.",
                "UNEXPECTED_ERROR",
                request
        );

        attachDebugIfEnabled(problemDetail, exception);

        logger.error("Unhandled exception: path={}", request.getRequestURI(), exception);
        return problemDetail;
    }

    private ProblemDetail baseProblemDetail(
            HttpStatus httpStatus,
            String title,
            String detail,
            String errorCode,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setProperty("code", errorCode);
        problemDetail.setProperty("path", request.getRequestURI());
        problemDetail.setProperty("timestamp", Instant.now().toString());
        return problemDetail;
    }

    private void attachDebugIfEnabled(ProblemDetail problemDetail, Exception exception) {
        if (!includeStacktrace) return;

        problemDetail.setProperty("debugException", exception.getClass().getName());
        problemDetail.setProperty("debugMessage", exception.getMessage());
        problemDetail.setProperty("debugStacktrace", exception.getStackTrace());
    }
}
