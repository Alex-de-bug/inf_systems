package com.alwx.backend.exceptions;
import java.util.Date;
import lombok.Data;



/**
 * Класс для шаблонных ошибок-ответов клиенту
 */
@Data
public class AppError {
    private int status;
    private String message;
    private Date timestamp;

    public AppError(int status, String message){
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}