package com.perfulandia.mic_reportes_fase2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perfulandia.mic_reportes_fase2.model.Reportes;

/*Esta interfaz define un repositorio Spring Data JPA para la entidad `Reportes`,
 heredando métodos CRUD básicos como `findAll()`, `findById()` y `existsById()`. */
 
public interface ReportesRepository extends JpaRepository<Reportes, Long>{
    List<Reportes> findAll(); 
    Optional<Reportes> findById(Long id);
    boolean existsById(Long id);
}


