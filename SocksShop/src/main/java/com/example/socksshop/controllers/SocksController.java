package com.example.socksshop.controllers;

import com.example.socksshop.dto.SocksDto;
import com.example.socksshop.models.Color;
import com.example.socksshop.models.Size;
import com.example.socksshop.models.Socks;
import com.example.socksshop.services.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
@Tag(name = "Socks", description = "CRUD")
public class SocksController {
    private final SocksService socksService;

    @PostMapping
    @Operation(summary = "Добавление товара на склад")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Товар добавлен на склад.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Socks.class))
                    }
            ),
            @ApiResponse(responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."),
            @ApiResponse(responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны.")
    })
    public ResponseEntity<Socks> addNewSocks(@Valid @RequestBody SocksDto socksDto) {
        socksService.addNewSocks(socksDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Общее количество товаров на складе.",
            description = "Возвращение общего количества носков на складе, количество носков остается неизменным.")
    public int getSocks(@RequestParam(name = "color") Color color,
                        @RequestParam(name = "size") Size size,
                        @RequestParam(name = "minCotton") int cottonMin,
                        @RequestParam(name = "maxCotton") int cottonMax) {
        int socksCount = socksService.getSocks(color, size, cottonMin, cottonMax);
        if (socksCount == 0) {
            ResponseEntity.notFound().build();
        }
        return socksCount;
    }

    @PutMapping
    @Operation(summary = "Отпуск товара со склада.",
            description = "Получение носков со склада по параметрам, на складе количество уменьшается.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Товар получен со склада.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Socks.class))
                    }
            ),
            @ApiResponse(responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."),
            @ApiResponse(responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны.")
    })
    public void editSocks(@RequestBody SocksDto socksDto) {
        SocksDto socksDto1 = socksService.editSocks(socksDto);
        if (socksDto1 == null) {
            ResponseEntity.notFound().build();
        } else {
            ResponseEntity.ok().build();
        }
    }

    @DeleteMapping
    @Operation(summary = "Списание испорченных (бракованных) носков.")
    @ApiResponse(responseCode = "200", description = "Списание испорченных (бракованных) носков.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Socks.class))})
    public ResponseEntity<Void> removeSocks(@RequestBody SocksDto socksDto) {
        if (socksService.removeSocks(socksDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
