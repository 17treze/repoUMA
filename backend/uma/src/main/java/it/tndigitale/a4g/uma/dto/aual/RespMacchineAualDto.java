package it.tndigitale.a4g.uma.dto.aual;

import java.util.List;

public class RespMacchineAualDto {

	private String text;
	private List<MacchinaAualDto> data;
      
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<MacchinaAualDto> getData() {
		return data;
	}
	public void setData(List<MacchinaAualDto> data) {
		this.data = data;
	}
}

