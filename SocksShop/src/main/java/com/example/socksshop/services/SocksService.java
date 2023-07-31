package com.example.socksshop.services;

import com.example.socksshop.dto.SocksDto;
import com.example.socksshop.models.Color;
import com.example.socksshop.models.Size;
import org.springframework.stereotype.Service;

@Service
public interface SocksService {
    void addNewSocks(SocksDto socksDto);

    int getSocks(Color color, Size size, int cottonMin, int cottonMax);

    boolean removeSocks(SocksDto socksDto);

    SocksDto editSocks(SocksDto socksDto);

    void saveToFile();

    String readFromFile();
}
