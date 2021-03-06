package com.example.curso.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.curso.entity.Profesor;
import com.example.curso.mapper.Mapper;
import com.example.curso.model.MProfesor;
import com.example.curso.service.IProfesorService;

@RestController
@RequestMapping("/api")
public class ProfesorRestController {
	
	@Autowired
	private IProfesorService profesorService;
	
	@GetMapping("/professors")
	@ResponseStatus(HttpStatus.OK)
	public List<Profesor> getProfesores(){
		return profesorService.findAll();
	}
	
	// Busca a través del mail
	@PostMapping("/find_professor")
	public ResponseEntity<?> findProfesor(@RequestBody Profesor profesor){
		
		Profesor profesorDb = profesorService.findProfesor(profesor);
		
		if (profesorDb != null) {
			return new ResponseEntity<>(profesorDb, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/sign_up")
	public ResponseEntity<Void> addProfesor(@RequestBody Profesor profesor){
		
		if (profesorService.findProfesor(profesor) == null) {
			profesorService.save(profesor);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping("login")
	public ResponseEntity<?> loginProfesor(@RequestBody Profesor profesor){
		
		Profesor profesorDb = null;
		profesorDb = profesorService.checkProfesorLogin(profesorDb);
		
		if (profesorDb != null) {
			List<Profesor> profesores = new ArrayList<>();
			profesores.add(profesorDb);
			
			List<MProfesor> mProfesores = new ArrayList<>();
			mProfesores = Mapper.convertirLista(profesores);
			
			return new ResponseEntity<>(mProfesores, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateProfessor(@PathVariable (value = "id")Long id, @RequestBody Profesor profesor){
		
		Profesor profesorDb = null;
		profesorDb = profesorService.findById(id);
		
		if (profesorDb != null) {
			profesorDb.setEmail(profesor.getEmail());
			profesorDb.setNombre(profesor.getNombre());
			
			profesorService.updateProfesor(profesorDb);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/update_sql")
	public ResponseEntity<?> findProfessor(@RequestBody Profesor profesor){
		Profesor profesorDb = null;
		profesorDb = profesorService.findProfesor(profesorDb);
		
		if (profesorDb != null) {
			profesorDb.setEmail(profesor.getEmail());
			profesorDb.setNombre(profesor.getNombre());
			
			profesorService.updateProfesor(profesorDb);
			
			return new ResponseEntity<>(profesorDb, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<Void> deleteProfesor(@PathVariable (value = "id")Long id){
		
		Profesor profesorDb = null;
		profesorDb = profesorService.findById(id);
		
		if (profesorDb != null) {
			profesorService.deleteProfesor(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<Void> deleteAllProfesor(){
		
		profesorService.deleteAllProfesor();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
