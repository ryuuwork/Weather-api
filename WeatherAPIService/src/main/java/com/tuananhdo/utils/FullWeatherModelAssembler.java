package com.tuananhdo.utils;

import com.tuananhdo.controller.FullWeatherAPIController;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import payload.FullWeatherDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class FullWeatherModelAssembler implements RepresentationModelAssembler<FullWeatherDTO, EntityModel<FullWeatherDTO>> {
    @Override
    @NonNull
    public EntityModel<FullWeatherDTO> toModel(@NonNull FullWeatherDTO dto) {
        EntityModel<FullWeatherDTO> entityModel = EntityModel.of(dto);
        entityModel.add(linkTo(FullWeatherAPIController.class).withSelfRel());
        return entityModel;
    }
}
