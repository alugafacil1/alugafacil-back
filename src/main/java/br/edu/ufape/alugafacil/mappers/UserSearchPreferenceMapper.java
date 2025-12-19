package br.edu.ufape.alugafacil.mappers;


import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceRequest;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceResponse;
import br.edu.ufape.alugafacil.models.UserSearchPreference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserSearchPreferenceMapper {

    @Mapping(target = "preferenceId", ignore = true)
    @Mapping(target = "searchCenter", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserSearchPreference toEntity(UserPreferenceRequest request);

    UserPreferenceResponse toResponse(UserSearchPreference preference);

    @Mapping(target = "preferenceId", ignore = true)
    @Mapping(target = "searchCenter", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UserPreferenceRequest request, @MappingTarget UserSearchPreference preference);
}
