package org.grupo1.markapbe.service;

import org.grupo1.markapbe.controller.dto.FormularioDTO;
import org.grupo1.markapbe.persistence.entity.FormularioEntity;
import org.grupo1.markapbe.persistence.repository.FormularioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FormularioService {

    @Autowired
    private FormularioRepository formularioRepository;

    public FormularioDTO registrarFormulario(FormularioDTO formularioDTO){

        FormularioEntity formulario = new FormularioEntity();
        formulario.setNombreCompleto(formularioDTO.nombreCompleto());
        formulario.setProblematica(formularioDTO.problematica());
        formulario.setFotoBase64(formularioDTO.fotoBase64());
        formulario.setDescripcion(formularioDTO.descripcion());

        formulario = formularioRepository.save(formulario);

        return new FormularioDTO(
                formulario.getId(),
                formulario.getNombreCompleto(),
                formulario.getProblematica(),
                formulario.getFotoBase64(),
                formulario.getDescripcion()
        );

    }

    public Page<FormularioDTO> obtenerTodosLosFormularios(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return formularioRepository.findAll(pageable).map(formulario -> new FormularioDTO(
                formulario.getId(),
                formulario.getNombreCompleto(),
                formulario.getProblematica(),
                formulario.getFotoBase64(),
                formulario.getDescripcion()
        ));

    }

}
