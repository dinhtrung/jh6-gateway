package com.ft.service.mapper;

import org.mapstruct.Mapper;

import com.ft.domain.Authority;
import com.ft.service.dto.AuthorityDTO;

@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority>{

}
