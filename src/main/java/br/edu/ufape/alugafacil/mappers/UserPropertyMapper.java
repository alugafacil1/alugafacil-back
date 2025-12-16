package br.edu.ufape.alugafacil.mappers;

import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserPropertyMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "agency.user", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "agency", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget User user);
}
