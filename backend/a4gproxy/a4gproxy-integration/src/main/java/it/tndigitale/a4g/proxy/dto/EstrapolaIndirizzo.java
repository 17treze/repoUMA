package it.tndigitale.a4g.proxy.dto;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EstrapolaIndirizzo {

	public static Indirizzo estrapolaIndirizzo(String indirizzoCompleto) {
		Indirizzo indirizzo =new Indirizzo();
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(indirizzoCompleto);
		int count=0;
		//contenggio numeri presenti nella stringa indirizzo
		while (m.find()) {
			count++;
		}
		m.reset();
		//se count = 0 significa che non sono stati trovati numeri nell'indirizzo
		if (count == 0) {
			indirizzo.setIndirizzo(indirizzoCompleto);
			indirizzo.setNumeroCivico("ND");
			return indirizzo;
		} 
		//logica utilizzata per spostare il cursore sul numero giusto in base alle logiche di business
		if (count==1) {
			//se count = 1 significa che è stato trovato solo un numero e che sicuramente rappresenta il numero civico
			m.find();
		} else {
			//se count > 1 il numero civico inizia dopo la fine del primo numero
			//esempio: VIA TAMANINI 41 ED 1 INT 3 --> indirizzo=VIA TAMANINI / n. civico=41 ED 1 INT 3
			m.find();
			//lista usata per gestire i seguenti casi
			//via 10 settembre 15
			//CORSO 3 NOVEMBRE N 72
			List<String> toponimi=Arrays.asList("CORSO","VIA","PIAZZA");
			if (toponimi.contains(indirizzoCompleto.substring(0, m.start()).trim().toUpperCase())) {
				m.find();//proseguo sul numero successivo perchè l'attuale rappresenta una informazione dell'indirizzo
			}
		}
		String numeroCivico = EstrapolaIndirizzo.estrapolaNumeroCivico(indirizzoCompleto, m.start(), m.end());
		indirizzo.setIndirizzo(indirizzoCompleto.replace(numeroCivico, ""));
		indirizzo.setNumeroCivico(numeroCivico);
		//NUME_CIVI_RECA VARCHAR2(10) -> lunghezza massima è 10 caratteri
		int maxLength = (indirizzo.getNumeroCivico().length() < 10)?indirizzo.getNumeroCivico().length():10;
		indirizzo.setNumeroCivico(indirizzo.getNumeroCivico().substring(0, maxLength));
		return indirizzo;
	}	

	//usato per gestire questo caso: FRAZIONE LANZA 25/B
	//VIA SPIAZZE 35/5
	private static String estrapolaNumeroCivico(String indirizzoCompleto,int start, int end) {
		return EstrapolaIndirizzo.checkSlash(indirizzoCompleto,end)? 
				indirizzoCompleto.substring(start, indirizzoCompleto.length()) : indirizzoCompleto.substring(start, end);
	}
	
	private static boolean checkSlash(String indirizzoCompleto, int end) {
		if (end+1>indirizzoCompleto.length()) return false;//check out of index
		String stringToCheck=indirizzoCompleto.substring(end, end+1);
		return "/".equals(stringToCheck) || "\\".equals(stringToCheck);
	}

	public static void main(String[] args) {
		System.out.println("####");		
		List<String> indirizzi= Arrays.asList("FRAZIONE LANZA 25/B","FRAZIONE LANZA 25\\B","VIA DOLOMITI 37 INT 6","LOC MAS 3 RONCHI","VIA SPIAZZE 35/5","VIA TOVEL 65 - FR TUENNO","VIA DI ZELL 97 ABIT",
				"FRZ CALTRON 5/E","VIA TAMANINI 41 ED 1 INT 3","VIA AL DOS DEL PIN 29 ABIT INT 2","LOCALITA PASSO CEREDA 53 C","VIA DEL DOS 25 FRAZIONE VERLA",
				"FRAZIONE VASIO19","VIA DELLA PIEVE 6 INT DX","VIA 18 SETTEMBRE 55 A","CORSO 3 NOVEMBRE N 72","VIA I SILVESTRI 11","LOC MATTON","ASSENTE"
				,"CORSO 3 NOVEMBRE N 72");
		for (String ind : indirizzi) {
			System.out.println(ind+ " ; "+EstrapolaIndirizzo.estrapolaIndirizzo(ind));
		}

	}
}
