package com.example.socksshop.dto;

import com.example.socksshop.models.Socks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SocksMapper {
    Socks toSocks(SocksDto socksDto);

    @Mapping(target = "quantity", ignore = true)
    SocksDto toSocksDto(Socks socks);
}
