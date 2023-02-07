package pro.sky.coursework_3_socks_warehouse.service.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Service;
import pro.sky.coursework_3_socks_warehouse.model.Operations;
import pro.sky.coursework_3_socks_warehouse.model.Socks;
import pro.sky.coursework_3_socks_warehouse.model.TypeOperation;
import pro.sky.coursework_3_socks_warehouse.service.FileService;
import pro.sky.coursework_3_socks_warehouse.service.OperationService;

@Service
public class OperationServiceImp implements OperationService {

  private final Map<Long, Operations> mapOperations = new TreeMap<>();
  private static long id = 1;

  private final FileService fileService;

  public OperationServiceImp(FileService fileService) {
    this.fileService = fileService;
  }

  @Override
  public void addOperations(TypeOperation type, LocalDateTime time, Socks socks) {
    Socks newSock = new Socks(socks.getColor(), socks.getSize(), socks.getCottonPercent(),
        socks.getQuantity());
    Operations operations = new Operations(type, time, newSock);
    mapOperations.put(id, operations);
    id++;
  }

  @Override
  public Map<Long, Operations> getMapOperations() {
    return Map.copyOf(mapOperations);
  }
}
