package pro.sky.coursework_3_socks_warehouse.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.springframework.stereotype.Service;
import pro.sky.coursework_3_socks_warehouse.exception.BadRequest;
import pro.sky.coursework_3_socks_warehouse.exception.ProductIsOutOfStock;
import pro.sky.coursework_3_socks_warehouse.model.Color;
import pro.sky.coursework_3_socks_warehouse.model.Size;
import pro.sky.coursework_3_socks_warehouse.model.Socks;
import pro.sky.coursework_3_socks_warehouse.service.SocksService;

@Service
public class SocksServiceImpl implements SocksService {

  private Map<Long, Socks> mapSocks = new TreeMap<>();
  private static long id = 1;

  @Override
  public long addSocks(Socks socks) throws BadRequest {
    validate(socks);
    if (mapSocks.containsValue(socks)) {
      for (Entry<Long, Socks> entry : mapSocks.entrySet()) {
        if (entry.getValue().equals(socks)) {
          long getId = entry.getKey();
          int getOldQuantity = entry.getValue().getQuantity();
          int getNewQuantity = getOldQuantity + socks.getQuantity();
          Socks socksNew = new Socks(socks.getColor(), socks.getSize(), socks.getCottonPercent(),
              getNewQuantity);
          mapSocks.put(getId, socksNew);
          return getId;
        }
      }
    } else {
      mapSocks.put(id, socks);
    }
    return id++;
  }

  @Override
  public boolean takeSocksFromTheWarehouse(Socks socks) throws ProductIsOutOfStock, BadRequest {
    validate(socks);
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
          } else {
            throw new ProductIsOutOfStock(
                "На складе не хватает пар носков, в запросе больше на: " + (Math.abs(
                    oldQuantity - newQuantity)));
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
    if (mapSocks.containsValue(socks)) {
      for (Entry<Long, Socks> entry : mapSocks.entrySet()) {
        if (entry.getValue().equals(socks)) {
          long key = entry.getKey();
          int oldQuantity = entry.getValue().getQuantity();
          int defectiveQuantitySocks = socks.getQuantity();
          if (oldQuantity >= defectiveQuantitySocks) {
            int quantityNew = oldQuantity - defectiveQuantitySocks;
            Socks socksNew = new Socks(socks.getColor(), socks.getSize(), socks.getCottonPercent(),
                quantityNew);
            mapSocks.put(key, socksNew);
            return true;
          } else {
            throw new ProductIsOutOfStock("Нечего списать");
          }
        }
      }
    }
    return false;
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
