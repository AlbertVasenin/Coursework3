package pro.sky.coursework_3_socks_warehouse.service;

import java.io.File;

public interface FileService {

  boolean saveToFile(String json);

  String readeFromFile();

  boolean cleanFile();

  File getDataFile();
}
