package pro.sky.coursework_3_socks_warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Статус", description = "GET запросы, показывающие статус веб-приложения и инфо о разработчике")
public class FirstController {

  @Operation(summary = "Статус приложения", description = "Получение информации об успешном запуске приложения")
  @GetMapping
  public String startApp() {
    return "<center>Веб-приложение успешно запущено</center>";
  }

  @Operation(summary = "Информация", description = "Получение информации о разработчике")
  @GetMapping("/info")
  public String infoAboutApp() {
    return "<center>Разработчик: Альберт Васенин,</center><br>"
        + "<center>Приложение \"Socks Warehouse\",</center><br>"
        + "<center>Дата создания: 02.02.2023 год,</center><br>"
        + "<center>Приложение, помогает интернет-магазину, вести учет носков на складе</center>";
  }
}

