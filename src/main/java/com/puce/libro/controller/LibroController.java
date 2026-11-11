package com.puce.libro.controller;

import com.puce.libro.model.Libro;
import com.puce.libro.service.LibroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/libro")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<Libro>> traerTodos() {
        return ResponseEntity.ok(libroService.traerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> traerUno(@PathVariable int id) {
        return libroService.traerUno(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Libro> guardar(@RequestBody Libro libro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.guardar(libro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> editar(@PathVariable int id, @RequestBody Libro libro) {
        return libroService.editar(id, libro)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (libroService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Libro>> disponibles() {
        return ResponseEntity.ok(libroService.disponibles());
    }

    @GetMapping("/autor/{texto}")
    public ResponseEntity<List<Libro>> buscarPorAutor(@PathVariable String texto) {
        return ResponseEntity.ok(libroService.autor(texto));
    }

    @GetMapping("/antiguos")
    public ResponseEntity<List<Libro>> antiguos(@RequestParam(defaultValue = "1950") int antes) {
        return ResponseEntity.ok(libroService.antiguos(antes));
    }

    @PutMapping("/{id}/prestar")
    public ResponseEntity<?> prestar(@PathVariable int id) {
        try {
            Optional<Libro> libroOptional = libroService.prestar(id);
            if (libroOptional.isPresent()) {
                return ResponseEntity.ok(libroOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable int id) {
        try {
            Optional<Libro> libroOptional = libroService.devolver(id);
            if (libroOptional.isPresent()) {
                return ResponseEntity.ok(libroOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}