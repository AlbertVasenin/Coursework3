package pro.sky.coursework_3_socks_warehouse.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operations {

  private TypeOperation typeOperation;
  private LocalDateTime dateTime;
  private Socks socks;
}
