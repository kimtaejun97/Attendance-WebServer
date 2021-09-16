package com.attendance.modules.main;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String handleIllegalArgumentException(IllegalArgumentException exception, @CurrentUser Account account, HttpServletRequest request){
        String username = getUsername(account);
        log.error("[{} Requested {}] But, throw IllegalArgumentException {}",username, request.getRequestURI(),exception.getMessage());

        return "/error/4xx";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String handleIllegalAccessException(IllegalAccessException exception, @CurrentUser Account account, HttpServletRequest request){
        String username = getUsername(account);
        log.error("[{} Requested {}] But, throw IllegalAccessException {}",username, request.getRequestURI(),exception.getMessage());

        return "/error/4xx";
    }

    private String getUsername(@CurrentUser Account account) {
        String username = "";
        if (account != null) {
            username = account.getUsername();
        }
        return username;
    }
}
