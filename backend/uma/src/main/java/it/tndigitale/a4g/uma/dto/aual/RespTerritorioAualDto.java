package it.tndigitale.a4g.uma.dto.aual;

import java.util.List;

public class RespTerritorioAualDto {

	private String code;
	private String text;
	private List<TerritorioAualDto> data;
      
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<TerritorioAualDto> getData() {
		return data;
	}
	public void setData(List<TerritorioAualDto> data) {
		this.data = data;
	}
}

