package pro.sky.coursework_3_socks_warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.coursework_3_socks_warehouse.model.Operations;
import pro.sky.coursework_3_socks_warehouse.service.OperationService;

@RestController
@RequestMapping("/operation")
@Tag(name = "Операции", description = "GET-запрос операций")
public class OperationController {

  private final OperationService operationService;

  public OperationController(OperationService operationService) {
    this.operationService = operationService;
  }

  @Operation(summary = "Получение списка операций", description = "Входные данные не нужны")
  @GetMapping("/get/operations")
  public ResponseEntity<Map<Long, Operations>> getAllSocks() {
    Map<Long, Operations> operationMap = operationService.getMapOperations();
    if (operationMap != null) {
      return ResponseEntity.ok(operationMap);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
