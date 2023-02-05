package pro.sky.coursework_3_socks_warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.coursework_3_socks_warehouse.exception.BadRequest;
import pro.sky.coursework_3_socks_warehouse.exception.ProductIsOutOfStock;
import pro.sky.coursework_3_socks_warehouse.model.Color;
import pro.sky.coursework_3_socks_warehouse.model.Size;
import pro.sky.coursework_3_socks_warehouse.model.Socks;
import pro.sky.coursework_3_socks_warehouse.service.SocksService;

@RestController
@RequestMapping("/socks")
@Tag(name = "Носки", description = "CRUD - операции с носками")
public class SocksController {

  private final SocksService socksService;

  public SocksController(SocksService socksService) {
    this.socksService = socksService;
  }

  @Operation(summary = "Создание пары носков", description =
      "Выберите подходящий цвет:RED, BLUE, GREEN, BLACK, WHITE и выберите размер от R35 "
          + "(соответствующего 35 размеру) или R35_5 (соответсвующего размеру 35.5) до R43 (43й размер)")
  @PostMapping()
  public ResponseEntity<Long> addSocks(@Valid @RequestBody Socks socks) throws BadRequest {
    long id = socksService.addSocks(socks);
    return ResponseEntity.ok(id);
  }

  @Operation(summary = "Забрать носки со склада", description =
      "Выберите подходящий цвет:RED, BLUE, GREEN, BLACK, WHITE и выберите размер от R35 "
          + "(соответствующего 35 размеру) или R35_5 (соответсвующего размеру 35.5) до R43 (43й размер)")
  @PutMapping()
  public ResponseEntity<Boolean> takeSocks(@Valid @RequestBody Socks socks)
      throws ProductIsOutOfStock, BadRequest {
    if (socksService.takeSocksFromTheWarehouse(socks)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Узнать количество необходимых носков на складе", description =
      "Выберите подходящий цвет:RED, BLUE, GREEN, BLACK, WHITE, выберите размер от R35 "
          + "(соответствующего 35 размеру) или R35_5 (соответсвующего размеру 35.5) до R43 (43й размер),"
          + " введите min и max процент содержания хлопка (от 0 до 100)")
  @GetMapping()
  public ResponseEntity<Integer> knowHowManySocks(@RequestParam(name = "color") Color color,
      @RequestParam(name = "size") Size size,
      @RequestParam(name = "minCottonPercent") Integer minCottonPercent,
      @RequestParam(name = "maxCottonPercent") Integer maxCottonPercent) {
    Integer quantity = socksService.getSocksQuantity(color, size, minCottonPercent,
        maxCottonPercent);
    return ResponseEntity.ok().body(quantity);
  }

  @Operation(summary = "Списать n-ое количество бракованных носков на складе", description =
      "Выберите подходящий цвет:RED, BLUE, GREEN, BLACK, WHITE, выберите размер от R35, "
          + "(соответствующего 35 размеру) или R35_5 (соответсвующего размеру 35.5) до R43 (43й размер),"
          + " введите процент содержания хлопка (от 0 до 100)")
  @DeleteMapping
  public ResponseEntity<Boolean> deletedDefectiveSocks(@RequestParam(name = "color") Color color,
      @RequestParam(name = "size") Size size,
      @RequestParam(name = "cottonPercent") Integer cottonPercent,
      @RequestParam(name = "quantity") Integer quantity) throws ProductIsOutOfStock {
    if (socksService.deleteSocks(color, size, cottonPercent, quantity)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Получение всего списка носков", description = "Входные данные не нужны")
  @GetMapping("/get/all")
  public ResponseEntity<Map<Long, Socks>> getAllSocks() {
    Map<Long, Socks> socksMap = socksService.getMapSocks();
    if (socksMap != null) {
      return ResponseEntity.ok(socksMap);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @ExceptionHandler(BadRequest.class)
  public ResponseEntity<String> handleValidationExceptions(BadRequest ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(ProductIsOutOfStock.class)
  public ResponseEntity<String> handleValidationExceptions(ProductIsOutOfStock ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
