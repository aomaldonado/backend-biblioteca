package com.puce.libro.repository;

import com.puce.libro.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Integer> {

    List<Libro> findByDisponibleTrue();

    List<Libro> findByAutorContainingIgnoreCase(String autor);

    List<Libro> findByAnioLessThan(int anio);
}