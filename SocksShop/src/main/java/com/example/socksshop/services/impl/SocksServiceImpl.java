package com.example.socksshop.services.impl;

import com.example.socksshop.dto.SocksDto;
import com.example.socksshop.dto.SocksMapper;
import com.example.socksshop.exceptions.ProductNotFoundException;
import com.example.socksshop.models.Color;
import com.example.socksshop.models.Size;
import com.example.socksshop.models.Socks;
import com.example.socksshop.services.FileService;
import com.example.socksshop.services.SocksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {
    HashMap<Socks, Integer> productHashMap = new HashMap<>();
    private final FileService fileService;
    private final SocksMapper socksMapper;

    @Value("${name.of.socks.file}")
    private String productFileName;

    @Value("${path.to.date.file}")
    private String dataFilePath;

    @PostConstruct
    private void init() {
        readFromFile();
    }

    @Override
    public void addNewSocks(SocksDto socksDto) {
        Socks socks1 = socksMapper.toSocks(socksDto);
        if (productHashMap.containsKey(socks1)) {
            productHashMap.put(socks1, productHashMap.get(socks1) + socksDto.getQuantity());
            saveToFile();
        } else {
            productHashMap.put(socks1, socksDto.getQuantity());
        }
    }

    @Override
    public int getSocks(Color color, Size size, int cottonMin, int cottonMax) {
        Integer count = 0;
        for (Map.Entry<Socks, Integer> entry : productHashMap.entrySet()) {
            if (entry.getKey().getColor().equals(color)
                    && entry.getKey().getSize().equals(size)
                    && cottonMin <= entry.getKey().getCottonPart()
                    && cottonMax >= entry.getKey().getCottonPart()) {
                count += entry.getValue();
            } else if (!productHashMap.entrySet().iterator().hasNext()) {
                throw new ProductNotFoundException("Товар с данными параметрами не найден");
            }
        }
        return count;
    }

    @Override
    public boolean removeSocks(SocksDto socksDto) {
        editSocks(socksDto);
        return productHashMap.entrySet().iterator().hasNext();
    }

    @Override
    public SocksDto editSocks(SocksDto socksDto) {
        Socks socks1 = socksMapper.toSocks(socksDto);
        int quantity = productHashMap.getOrDefault(socks1, 0);
        if (quantity >= socksDto.getQuantity()) {
            productHashMap.put(socks1, quantity - socksDto.getQuantity());
            saveToFile();
        } else {
            throw new ProductNotFoundException("На складе нет носков");
        }
        return socksDto;
    }


    public void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(productHashMap);
            fileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readFromFile() {
        Path path = Path.of(dataFilePath, productFileName);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
