package org.grupo1.markapbe.service;

import org.grupo1.markapbe.controller.dto.FormularioDTO;
import org.grupo1.markapbe.persistence.entity.FormularioEntity;
import org.grupo1.markapbe.persistence.repository.FormularioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
