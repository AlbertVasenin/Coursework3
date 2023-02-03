package pro.sky.coursework_3_socks_warehouse.model;

import java.util.Objects;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Socks {

  @NotBlank
  @NotEmpty
  private Color color;
  private Size size;
  @Positive
  @Max(value = 100)
  @Min(0)
  private int cottonPercent;
  @Positive
  @Min(1)
  private int quantity;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Socks socks = (Socks) o;
    return cottonPercent == socks.cottonPercent && color == socks.color && size == socks.size;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, size, cottonPercent);
  }
}
