package ar.com.challenge.heroes.ex;

import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
 
@ControllerAdvice
public class GlobalExceptionHandler {
     
    private static final Logger LOGGER = LoggerFactory.getLogger(
                        GlobalExceptionHandler.class);
     
    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex) {
         
        LOGGER.error(ex.getMessage(), ex);
         
        return "general_error";
    }
}