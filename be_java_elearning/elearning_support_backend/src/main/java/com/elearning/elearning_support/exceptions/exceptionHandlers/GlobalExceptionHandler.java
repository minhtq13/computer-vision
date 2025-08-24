package com.elearning.elearning_support.exceptions.exceptionHandlers;


import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.exceptions.BadRequestException;
import com.elearning.elearning_support.exceptions.CustomBadCredentialsException;
import com.elearning.elearning_support.exceptions.ExceptionResponse;
import com.elearning.elearning_support.exceptions.FileUploadException;
import com.elearning.elearning_support.exceptions.PermissionDeniedException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Listen and handle BadRequestException
     */
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse handlerBadRequestException(BadRequestException exception) {
        log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(),exception);
        return new ExceptionResponse(exception.getCode(), HttpStatus.BAD_REQUEST.value(), exception.getMessage(), exception.getFieldError(),
            exception.getValues());
    }

    /**
     * Listen and handle PermissionDeniedException
     */
    @ExceptionHandler({PermissionDeniedException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ExceptionResponse handlerPermissionDeniedException(PermissionDeniedException exception) {
        log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        return new ExceptionResponse(exception.getCode(), HttpStatus.FORBIDDEN.value(), exception.getMessage(), exception.getValues());
    }

    /**
     * Handler file upload exception
     */
    @ExceptionHandler({FileUploadException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse handlerFileUploadException(FileUploadException exception) {
        log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        return new ExceptionResponse(exception.getCode(), HttpStatus.BAD_REQUEST.value(), exception.getMessage(), exception.getValues());
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handlerBadCredentialsException(BadCredentialsException exception) {
        log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        return new ExceptionResponse(MessageConst.User.USER_WRONG_USERNAME_OR_PASSWORD_ERROR_CODE, HttpStatus.UNAUTHORIZED.value(),
            exception.getMessage(), String.format("%s/%s", ErrorKey.User.USERNAME, ErrorKey.User.PASSWORD));
    }

    @ExceptionHandler({CustomBadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handlerBadCredentialsException(CustomBadCredentialsException exception) {
        log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        return new ExceptionResponse(exception.getCode(), HttpStatus.UNAUTHORIZED.value(),
            exception.getMessage(), exception.getFieldError());
    }

}
