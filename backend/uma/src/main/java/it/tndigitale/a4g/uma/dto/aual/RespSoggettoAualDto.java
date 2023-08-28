package it.tndigitale.a4g.uma.dto.aual;

import java.util.List;

public class RespSoggettoAualDto {

	private String text;
	private SoggettoAualDto data;
      
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public SoggettoAualDto getData() {
		return data;
	}
	public void setData(SoggettoAualDto data) {
		this.data = data;
	}
}

