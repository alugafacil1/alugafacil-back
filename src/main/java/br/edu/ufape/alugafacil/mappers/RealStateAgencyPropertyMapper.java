package br.edu.ufape.alugafacil.mappers;

import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyResponse;
import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RealStateAgencyPropertyMapper {

    @Mapping(target = "agencyId", ignore = true)
    RealStateAgency toEntity(RealStateAgencyRequest request);

    RealStateAgencyResponse toResponse(RealStateAgency realStateAgency);

    @Mapping(target = "agencyId", ignore = true)
    void updateEntityFromRequest(RealStateAgencyRequest request, @MappingTarget RealStateAgency realStateAgency);
}
