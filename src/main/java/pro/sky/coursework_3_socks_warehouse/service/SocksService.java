package pro.sky.coursework_3_socks_warehouse.service;

import java.util.Map;
import pro.sky.coursework_3_socks_warehouse.exception.BadRequest;
import pro.sky.coursework_3_socks_warehouse.exception.ProductIsOutOfStock;
import pro.sky.coursework_3_socks_warehouse.model.Color;
import pro.sky.coursework_3_socks_warehouse.model.Size;
import pro.sky.coursework_3_socks_warehouse.model.Socks;

public interface SocksService {

  long addSocks(Socks socks) throws BadRequest;

  boolean takeSocksFromTheWarehouse(Socks socks) throws ProductIsOutOfStock, BadRequest;

  boolean deleteSocks(Color color, Size size, int cottonPercent, int quantity)
      throws ProductIsOutOfStock;

  int getSocksQuantity(Color color, Size size, Integer minCottonPercent,
      Integer maxCottonPercent);

  Map<Long, Socks> getMapSocks();
}
