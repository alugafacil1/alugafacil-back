package br.edu.ufape.alugafacil.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.edu.ufape.alugafacil.dtos.realStateAgency.MemberResponse;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyResponse;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.User;

@Mapper(componentModel = "spring")
public interface RealStateAgencyPropertyMapper {

    @Mapping(target = "agencyId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RealStateAgency toEntity(RealStateAgencyRequest request);

    RealStateAgencyResponse toResponse(RealStateAgency realStateAgency);

    MemberResponse toMemberResponse(User user);

    @Mapping(target = "agencyId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", ignore = true) 
    @Mapping(target = "updatedAt", ignore = true) 
    void updateEntityFromRequest(RealStateAgencyRequest request, @MappingTarget RealStateAgency realStateAgency);
}