package it.tndigitale.a4g.proxy.dto.builder;

import org.springframework.beans.BeanUtils;

import it.tndigitale.a4g.protocollo.client.model.MainDocument;
import it.tndigitale.a4g.proxy.dto.protocollo.FileDocumentDto;

public class MainDocumentBuilder {
	
	public static MainDocument  from(FileDocumentDto fileDocumentDto) {
		MainDocument mainDocument=new MainDocument();
		BeanUtils.copyProperties(fileDocumentDto, mainDocument);
		mainDocument.setContent(fileDocumentDto.getContent());
		mainDocument.setDescription(fileDocumentDto.getDescription());
		return mainDocument;
	}

}
