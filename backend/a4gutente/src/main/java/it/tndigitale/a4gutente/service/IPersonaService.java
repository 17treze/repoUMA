package it.tndigitale.a4gutente.service;

import java.util.List;

import it.tndigitale.a4gutente.dto.Persona;

public interface IPersonaService {

	Persona caricaPersona(Long id) throws Exception;

	List<Persona> ricercaPersone(String codiceFiscale) throws Exception;

	// Long inserisciPersona(Persona persona) throws Exception;

	Persona aggiornaPersona(Persona persona) throws Exception;
	
	Long inserisciAggiornaPersona(Persona persona) throws Exception;
}