package com.puce.libro.service;

import com.puce.libro.model.Libro;
import com.puce.libro.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> traerTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> traerUno(int id) {
        return libroRepository.findById(id);
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    public Optional<Libro> editar(int id, Libro libroActualizado) {
        return libroRepository.findById(id)
                .map(libroExistente -> {
                    libroExistente.setTitulo(libroActualizado.getTitulo());
                    libroExistente.setAutor(libroActualizado.getAutor());
                    libroExistente.setAnio(libroActualizado.getAnio());
                    libroExistente.setDisponible(libroActualizado.getDisponible());
                    return libroRepository.save(libroExistente);
                });
    }

    public boolean eliminar(int id) {
        if (libroRepository.existsById(id)) {
            libroRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Libro> disponibles() {
        return libroRepository.findByDisponibleTrue();
    }

    public List<Libro> autor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor);
    }

    public List<Libro> antiguos(int anio) {
        return libroRepository.findByAnioLessThan(anio);
    }

    public Optional<Libro> prestar(int id) {
        Optional<Libro> libroOpt = libroRepository.findById(id);

        if (libroOpt.isEmpty()) {
            return Optional.empty();
        }

        Libro libro = libroOpt.get();
        if (Boolean.FALSE.equals(libro.getDisponible())) {
            throw new IllegalStateException("El libro ya se encuentra prestado");
        }

        libro.setDisponible(false);
        return Optional.of(libroRepository.save(libro));
    }

    public Optional<Libro> devolver(int id) {
        Optional<Libro> libroOpt = libroRepository.findById(id);

        if (libroOpt.isEmpty()) {
            return Optional.empty();
        }

        Libro libro = libroOpt.get();
        if (Boolean.TRUE.equals(libro.getDisponible())) {
            throw new IllegalStateException("El libro no estaba prestado");
        }

        libro.setDisponible(true);
        return Optional.of(libroRepository.save(libro));
    }
}