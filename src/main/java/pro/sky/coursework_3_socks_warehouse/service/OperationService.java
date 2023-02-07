package pro.sky.coursework_3_socks_warehouse.service;

import java.time.LocalDateTime;
import java.util.Map;
import pro.sky.coursework_3_socks_warehouse.model.Operations;
import pro.sky.coursework_3_socks_warehouse.model.Socks;
import pro.sky.coursework_3_socks_warehouse.model.TypeOperation;

public interface OperationService {

  void addOperations(TypeOperation type, LocalDateTime time, Socks socks);

  Map<Long, Operations> getMapOperations();
}
