package pro.sky.coursework_3_socks_warehouse.service;

import java.io.File;
import java.nio.file.Path;

public interface FileService {

  boolean saveToFile(String json);

  String readeFromFile();

  boolean cleanFile();

  File getDataFile();

  Path createTempFile(String suffix);
}
