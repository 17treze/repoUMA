package it.tndigitale.a4g.proxy.services.protocollo.rest.template;

import java.util.*;

import it.tndigitale.a4g.proxy.dto.protocollo.TipologiaDocumento;
import it.tndigitale.a4g.proxy.services.protocollo.rest.PiTreDocumentsConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.protocollo.client.model.Document;
import it.tndigitale.a4g.protocollo.client.model.Field;
import it.tndigitale.a4g.protocollo.client.model.GetTemplateResponse;
import it.tndigitale.a4g.protocollo.client.model.Template;
import it.tndigitale.a4g.proxy.dto.protocollo.DocumentDto;
import it.tndigitale.a4g.proxy.services.protocollo.client.PitreClient;

@Component
public class CreazioneTemplateComponent {
	private static Logger logger = LoggerFactory.getLogger(CreazioneTemplateComponent.class);
	
	@Autowired
	private PitreClient pitreClient;

	@Autowired
	private PiTreDocumentsConfigurationServiceImpl piTreDocumentsConfigurationService;

	private static EnumMap<TipologiaDocumento, IFieldNameList[]> TEMPLATE_FIELDS =
			new EnumMap<TipologiaDocumento, IFieldNameList[]>(TipologiaDocumento.class);

	static {
		TEMPLATE_FIELDS.put(TipologiaDocumento.MANDATO, MetadatiTemplateMandato.values());
	}

	public void createTemplate(DocumentDto incomingDocument, Document document) {
		String templateName = piTreDocumentsConfigurationService.getTemplateName(incomingDocument.getMainDocumentType());

		if (templateName == null)
			return;

		GetTemplateResponse documentTemplate = pitreClient.getDocumentTemplate(templateName);
		Template template = documentTemplate.getTemplate();

		IFieldNameList[] expectedFieldsList = TEMPLATE_FIELDS.get(incomingDocument.getMainDocumentType());
		List<Field> fields = new ArrayList<>();

		for (Field originalField : template.getFields()) {
			String fieldName = originalField.getName();
			if (fieldName != null) {
				IFieldNameList expectedField =
						Arrays.stream(expectedFieldsList)
								.filter(expected -> expected.getOriginalFieldName().equals(fieldName))
								.findAny()
								.orElseThrow(() -> {
									logger.warn("il campo {} non e' tra quelli attesi");
									return new IllegalStateException(String.format("Unsupported type %s.", fieldName));
								});
				Field field = new Field();
				field.setName(fieldName);
				field.setValue(incomingDocument.getMetadatiTemplate().get(expectedField.name()));
				fields.add(field);
			}
		}

		template.setFields(fields);
		document.setTemplate(template);
	}
}
