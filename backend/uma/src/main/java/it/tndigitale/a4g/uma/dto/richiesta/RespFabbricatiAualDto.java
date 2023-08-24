package it.tndigitale.a4g.uma.dto.richiesta;

import java.util.List;

public class RespFabbricatiAualDto {

	private String text;
	private List<FabbricatoAualDto> data;
      
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<FabbricatoAualDto> getData() {
		return data;
	}
	public void setData(List<FabbricatoAualDto> data) {
		this.data = data;
	}
}

