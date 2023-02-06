package pro.sky.coursework_3_socks_warehouse.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import pro.sky.coursework_3_socks_warehouse.exception.BadRequest;
import pro.sky.coursework_3_socks_warehouse.exception.ProductIsOutOfStock;
import pro.sky.coursework_3_socks_warehouse.model.Color;
import pro.sky.coursework_3_socks_warehouse.model.Size;
import pro.sky.coursework_3_socks_warehouse.model.Socks;
import pro.sky.coursework_3_socks_warehouse.service.FileService;
import pro.sky.coursework_3_socks_warehouse.service.SocksService;

@Service
public class SocksServiceImpl implements SocksService {

  private Map<Long, Socks> mapSocks = new TreeMap<>();
  private static long id = 1;

  public SocksServiceImpl(FileService fileService) {
    this.fileService = fileService;
  }

  private final FileService fileService;

  @Override
  public long addSocks(Socks socks) throws BadRequest {
    validate(socks);
    if (mapSocks.containsValue(socks)) {
      for (Entry<Long, Socks> entry : mapSocks.entrySet()) {
        if (entry.getValue().equals(socks)) {
          long key = entry.getKey();
          int oldQuantity = entry.getValue().getQuantity();
          int newQuantity = oldQuantity + socks.getQuantity();
          Socks socksNew = new Socks(socks.getColor(), socks.getSize(), socks.getCottonPercent(),
              newQuantity);
          mapSocks.put(key, socksNew);
          saveToFile();
          return key;
        }
      }
    } else {
      mapSocks.put(id, socks);
      saveToFile();
    }
    return id++;
  }

  @Override
  public boolean takeSocksFromTheWarehouse(Socks socks) throws ProductIsOutOfStock, BadRequest {
    validate(socks);
    if (isContainsValueMapSocks(socks)) {
      saveToFile();
      return true;
    }
    throw new ProductIsOutOfStock("На складе не хватает пар носков");
  }

  private boolean isContainsValueMapSocks(Socks socks) {
    if (mapSocks.containsValue(socks)) {
      for (Entry<Long, Socks> entry : mapSocks.entrySet()) {
        if (entry.getValue().equals(socks)) {
          long key = entry.getKey();
          int oldQuantity = entry.getValue().getQuantity();
          int newQuantity = socks.getQuantity();
          if (oldQuantity >= newQuantity) {
            int quantity = oldQuantity - newQuantity;
            Socks socksNew = new Socks(socks.getColor(), socks.getSize(), socks.getCottonPercent(),
                quantity);
            mapSocks.put(key, socksNew);
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public int getSocksQuantity(Color color, Size size, Integer minCottonPercent,
      Integer maxCottonPercent) {
    int count = 0;
    for (Entry<Long, Socks> entry : mapSocks.entrySet()) {
      if (color != null && !entry.getValue().getColor().equals(color)) {
        continue;
      }
      if (size != null && !entry.getValue().getSize().equals(size)) {
        continue;
      }
      if (minCottonPercent != null && entry.getValue().getCottonPercent() < minCottonPercent) {
        continue;
      }
      if (maxCottonPercent != null && entry.getValue().getCottonPercent() > maxCottonPercent) {
        continue;
      }
      count += entry.getValue().getQuantity();
    }
    return count;
  }

  @Override
  public boolean deleteSocks(Color color, Size size, int cottonPercent, int quantity)
      throws ProductIsOutOfStock {
    Socks socks = new Socks(color, size, cottonPercent, quantity);
    if (isContainsValueMapSocks(socks)) {
      saveToFile();
      return true;
    }
    throw new ProductIsOutOfStock("Нечего списать");
  }

  private void saveToFile() {
    try {
      String json = new ObjectMapper().writeValueAsString(mapSocks);
      fileService.saveToFile(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Ошибка сохранения файла");
    }
  }

  private void readFromFile() {
    String json = fileService.readeFromFile();
    try {
      mapSocks = new ObjectMapper().readValue(json, new TypeReference<TreeMap<Long, Socks>>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Ошибка чтения файла");
    }
  }

  @PostConstruct
  private void init() {
    readFromFile();
  }

  @Override
  public Map<Long, Socks> getMapSocks() {
    return Map.copyOf(mapSocks);
  }

  private void validate(Socks sock) throws BadRequest {
    if (sock.getColor() == null || sock.getSize() == null) {
      throw new BadRequest("Все поля должны быть заполнены");
    }
    if (sock.getQuantity() <= 0) {
      throw new BadRequest("параметр quantity должен быть больше 0");
    }
    if (sock.getCottonPercent() < 0 || sock.getCottonPercent() > 100) {
      throw new BadRequest("параметр cottonPercent  должен быть между 0 и 100");
    }
  }
}
