package com.midas2018mobile5.serverapp.error;

import com.midas2018mobile5.serverapp.error.exception.cafe.CafeMenuDuplicationException;
import com.midas2018mobile5.serverapp.error.exception.cafe.CafeMenuNotFoundException;
import com.midas2018mobile5.serverapp.error.exception.order.OrderInvalidProcessException;
import com.midas2018mobile5.serverapp.error.exception.order.OrderNotFoundException;
import com.midas2018mobile5.serverapp.error.exception.user.UserDuplicationException;
import com.midas2018mobile5.serverapp.error.exception.user.UserNotFoundException;
import com.midas2018mobile5.serverapp.error.exception.user.UserPasswordInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class ErrorExceptionController {

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        final ErrorCode userNotFound = ErrorCode.USER_NOT_FOUND;
        log.error(userNotFound.getMessage(), ex.getId());
        return buildError(userNotFound);
    }

    @ExceptionHandler(value = UserPasswordInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleUserPasswordInvalidException(UserPasswordInvalidException ex) {
        final ErrorCode passwordInvalid = ErrorCode.LOGIN_INPUT_INVALID;
        log.error(passwordInvalid.getMessage() + ": {}", ex.getPassword());
        return buildError(passwordInvalid);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        final ErrorCode accessdenied = ErrorCode.HANDLE_ACCESS_DENIED;
        log.error(accessdenied.getMessage() + ": {}", ex.getLocalizedMessage());
        return buildError(accessdenied);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleBadCredentialsException(BadCredentialsException ex) {
        final ErrorCode unauthorized = ErrorCode.UNAUTHORIZED;
        log.error(unauthorized.getMessage() + ": {}", ex.getMessage());
        return buildError(unauthorized);
    }

    @ExceptionHandler(value = CafeMenuNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleCafeMenuNotFoundException(CafeMenuNotFoundException ex) {
        final ErrorCode menuNotFound = ErrorCode.MENU_NOT_FOUND;
        log.error(menuNotFound.getMessage(), ex.getMenuName());
        return buildError(menuNotFound);
    }

    @ExceptionHandler(value = OrderInvalidProcessException.class)
    protected ErrorResponse handleOrderInvalidProcessException(OrderInvalidProcessException ex) {
        final ErrorCode invalidOrderChange = ErrorCode.BAD_ORDER_CHANGE;
        log.error(invalidOrderChange.getMessage() + ": {}", ex.getId());
        return buildError(invalidOrderChange);
    }

    @ExceptionHandler(value = OrderNotFoundException.class)
    protected ErrorResponse handlerOrderNotFoundException(OrderNotFoundException ex) {
        final ErrorCode orderNotFound = ErrorCode.ORDER_NOT_FOUND;
        log.error(orderNotFound.getMessage() + ": {}", ex.getId());
        return buildError(orderNotFound);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidExcepteion(MethodArgumentNotValidException ex) {
        final List<ErrorResponse.CustomFieldError> fieldErrors = getFieldErrors(ex.getBindingResult());
        return buildCustomFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
    }

    @ExceptionHandler(UserDuplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleUserDuplicationException(UserDuplicationException ex) {
        final ErrorCode code = ErrorCode.USER_DUPLICATION;
        log.error(ex.getMessage(), ex.getUserid() + ex.getField());
        return buildError(code);
    }

    @ExceptionHandler(CafeMenuDuplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleCafeMenuDuplicationException(CafeMenuDuplicationException ex) {
        final ErrorCode code = ErrorCode.MENU_DUPLICATION;
        log.error(ex.getMessage(), ex.getName());
        return buildError(code);
    }

    private List<ErrorResponse.CustomFieldError> getFieldErrors(BindingResult bindingResult) {
        final List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.parallelStream().map(error -> ErrorResponse.CustomFieldError.builder()
                .reason(error.getDefaultMessage())
                .field(error.getField()).value((String) error.getRejectedValue())
                .build())
                .collect(Collectors.toList());
    }

    private ErrorResponse buildError(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
    }

    private ErrorResponse buildCustomFieldErrors(ErrorCode errorCode, List<ErrorResponse.CustomFieldError> errors) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
    }
}
