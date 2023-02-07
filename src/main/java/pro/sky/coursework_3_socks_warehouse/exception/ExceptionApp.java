package pro.sky.coursework_3_socks_warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionApp extends Exception {

  public ExceptionApp(String message) {
    super(message);
  }
}
