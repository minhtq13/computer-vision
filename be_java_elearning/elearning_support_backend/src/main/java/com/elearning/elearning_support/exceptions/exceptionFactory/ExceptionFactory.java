package com.elearning.elearning_support.exceptions.exceptionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.exceptions.BadRequestException;
import com.elearning.elearning_support.exceptions.PermissionDeniedException;
import com.elearning.elearning_support.exceptions.ResourceExistedException;
import com.elearning.elearning_support.utils.object.ObjectUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExceptionFactory {

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String code, String... values) {
        try {
            return messageSource.getMessage(code, values, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException ex) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ex.getMessage(), ex.getCause(), ex);
            return null;
        }
    }

    public BadRequestException resourceNotFoundException(String code, String messageConst, String resource, String fieldError, String... values)
        throws BadRequestException {
        return new BadRequestException(code, resource + " " + messageConst, fieldError, values);
    }

    public PermissionDeniedException permissionDeniedException(String code, String resource, String message) throws PermissionDeniedException {
        return new PermissionDeniedException(code, resource, message);
    }

    public BadRequestException resourceExistedException(String code, String resource, String messageConst, String errorField, String... values)
        throws ResourceExistedException {
        return new BadRequestException(code, resource + " " + messageConst, errorField, values);
    }

    public BadRequestException fileUploadException(String code, String resource, String message) throws BadRequestException {
        return new BadRequestException(code, resource, message);
    }

    public BadRequestException badRequestException(String code, String resource, String messageConst, String errorField, String... values)
        throws BadRequestException {
        return new BadRequestException(code, ObjectUtils.getOrDefault(getMessage(code, values), resource + " " + messageConst), errorField,
            values);
    }
}
