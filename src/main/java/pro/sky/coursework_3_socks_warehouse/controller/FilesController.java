package pro.sky.coursework_3_socks_warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.coursework_3_socks_warehouse.service.FileService;

@Tag(name = "Файловый контроллер")
@RestController
@RequestMapping("/files")
public class FilesController {

  private final FileService fileService;

  public FilesController(FileService fileService) {
    this.fileService = fileService;
  }

  @Operation(summary = "Скачать файлы", description = "Входные данные не нужны")
  @GetMapping(value = "/export")
  public ResponseEntity<InputStreamResource> downLoadFiles()
      throws FileNotFoundException {
    File file = fileService.getDataFile();
    if (file.exists()) {
      InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
          .contentLength(file.length())
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "socks.json")
          .body(resource);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @Operation(summary = "Загрузить файлы", description = "Входные данные не нужны")
  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> upLoadFiles(
      @RequestParam MultipartFile file) {
    fileService.cleanFile();
    File dataFile = fileService.getDataFile();
    try (FileOutputStream fos = new FileOutputStream(dataFile)) {
      IOUtils.copy(file.getInputStream(), fos);
      return ResponseEntity.ok().build();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
