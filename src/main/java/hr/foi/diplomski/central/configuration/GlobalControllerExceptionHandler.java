package hr.foi.diplomski.central.configuration;

import hr.foi.diplomski.central.exceptions.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = { BadRequestException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Bad request");
        response.setPath(request.getRequestURI());

        if (StringUtils.isNotBlank(ex.getMessage())) {
            response.setMessage(ex.getMessage());
        } else {
            response.setMessage("Zahtjev nije ispravan!");
        }

        return response;
    }

}
