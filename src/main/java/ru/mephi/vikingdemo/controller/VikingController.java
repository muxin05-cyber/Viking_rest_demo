package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.SpecificVikingService;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private VikingListener vikingListener;
    private final SpecificVikingService specificVikingService;

    public VikingController(VikingService vikingService, VikingListener vikingListener, SpecificVikingService specificVikingService) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
        this.specificVikingService = specificVikingService;
    }
    
    @GetMapping
    @Operation(summary = "Получить список созданных викингов", 
            operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", 
            operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }
    
    @PostMapping("/post")
    @Operation(summary = "Создать викинга со случайными параметрами", 
            operationId = "post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно создан")
    })
    public void addViking(){
        System.out.println("POST api/vikings/post called");
        vikingListener.testAdd();
    }

    @PostMapping("/by-axes")
    @Operation(summary = "Получить викингов с 1 или 2 топорами",
            operationId = "getByAxes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинги успешно получены")
    })
    public int quantityVikingWithOneOrTwoAxes() {
        return specificVikingService.countVikingsByAxes();
    }


    @PostMapping("/by-beard-and-hair")
    @Operation(summary = "Посчитать викингов с определённой бородой и цветом волос",
            operationId = "countByBeardAndHair")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Количество викингов успешно получено")
    })
    public int countVikingWithCertainBeardAndHair(
            @RequestParam String beard,
            @RequestParam String hair) {
        return specificVikingService.countVikingsByBeardAndHair(beard, hair);
    }

    @PostMapping("/by-age")
    @Operation(summary = "Посчитать викингов в определённом диапазоне",
            operationId = "countByAge")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Количество викингов успешно получено")
    })
    public int countVikingByAge(
            @RequestParam int start,
            @RequestParam int end,
            @RequestParam boolean inSpanOrNo) {

        if (inSpanOrNo){

            return specificVikingService.countVikingsByAgeInSpan(start, end);
        }else{
            return specificVikingService.countVikingsByAgeWithoutSpan(start, end);
        }

    }


    @PostMapping("/generate")
    @Operation(summary = "Массовая генерация случайных викингов",
            operationId = "generateVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинги успешно созданы")
    })
    public List<Viking> generateVikings(@RequestParam(defaultValue = "5") int count) {
        System.out.println("POST /api/vikings/generate called with count=" + count);
        return vikingService.createRandomVikings(count);
    }



}
