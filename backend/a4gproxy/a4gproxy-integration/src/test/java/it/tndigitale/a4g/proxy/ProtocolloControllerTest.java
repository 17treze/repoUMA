package it.tndigitale.a4g.proxy;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.protocollo.client.model.ClassificationScheme;
import it.tndigitale.a4g.protocollo.client.model.Correspondent;
import it.tndigitale.a4g.protocollo.client.model.CreateDocumentResponse;
import it.tndigitale.a4g.protocollo.client.model.Document;
import it.tndigitale.a4g.protocollo.client.model.Field;
import it.tndigitale.a4g.protocollo.client.model.GetCorrespondentResponse;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentResponse;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentResponse.CodeEnum;
import it.tndigitale.a4g.protocollo.client.model.GetRoleResponse;
import it.tndigitale.a4g.protocollo.client.model.GetTemplateResponse;
import it.tndigitale.a4g.protocollo.client.model.MainDocument;
import it.tndigitale.a4g.protocollo.client.model.MessageResponse;
import it.tndigitale.a4g.protocollo.client.model.Project;
import it.tndigitale.a4g.protocollo.client.model.Register;
import it.tndigitale.a4g.protocollo.client.model.Role;
import it.tndigitale.a4g.protocollo.client.model.SearchCorrespondentsResponse;
import it.tndigitale.a4g.protocollo.client.model.SearchProjectsResponse;
import it.tndigitale.a4g.protocollo.client.model.Template;
import it.tndigitale.a4g.protocollo.client.model.TransmissionResponse;
import it.tndigitale.a4g.proxy.dto.protocollo.DocumentRegistrationInfoDto;
import it.tndigitale.a4g.proxy.services.protocollo.client.PitreClient;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // (addFilters = false)
@WithMockUser
public class ProtocolloControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PitreClient pitreClient;

	@Test
	public void ShouldCreateAndTransmitANewDocumentWithOneAttachmentAndExistingCorrespondent() throws Exception {
		SearchCorrespondentsResponse searchCorrResp = new SearchCorrespondentsResponse();
		CreateDocumentResponse createRes = new CreateDocumentResponse();
		GetDocumentResponse protoPresRes = new GetDocumentResponse();
		SearchProjectsResponse searchProjResp = new SearchProjectsResponse();
		GetRoleResponse roleRes = new GetRoleResponse();
		MessageResponse msgRes = new MessageResponse();
		TransmissionResponse trsmRes = new TransmissionResponse();
		GetTemplateResponse gtRes = new GetTemplateResponse();

		setExistingCorrespondentResponses(searchCorrResp, createRes, protoPresRes, msgRes, trsmRes, gtRes);
		setCommonResponse(searchProjResp, roleRes);
		setExistingCorrespondentWSMock(searchCorrResp, searchProjResp, createRes, roleRes, protoPresRes, msgRes, trsmRes, gtRes);

		DocumentRegistrationInfoDto inputData = objectMapper
				.readValue(new File("src/test/resources/pitre/ShouldCreateAndTransmitANewDocumentWithOneAttachmentAndExistingCorrespondent_PostRequest.json"), DocumentRegistrationInfoDto.class);

		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(inputData).getBytes());

		String fileName = "MODULI_AMF.pdf.p7m";
		Path filePdfPath = Paths.get("src/test/resources/pitre/" + fileName);
		MockMultipartFile documento = new MockMultipartFile("documento", fileName, "", Files.readAllBytes(filePdfPath));

		String allegatoFileName = "dichiarazioneFamigliariConviventi.pdf";
		Path allegatoFilePdfPath = Paths.get("src/test/resources/pitre/" + allegatoFileName);
		MockMultipartFile allegati = new MockMultipartFile("allegati", "Allegato_1.pdf", "application/pdf", Files.readAllBytes(allegatoFilePdfPath));

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/protocollo/documenti");

		builder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(documento).file(allegati)).andExpect(status().isOk()).andExpect(content().string("PAT_TEST/RFS151-23/07/2020-0001296"));

		verify(pitreClient).postSearchCorrespondents(Mockito.any());
		verify(pitreClient, times(2)).postSearchProjects(Mockito.any());
		verify(pitreClient).postAddInProject(Mockito.any());
		verify(pitreClient, times(2)).putUploadFileToDocument(Mockito.any());
		verify(pitreClient).getRole(Mockito.any(), Mockito.any());
		verify(pitreClient).postExecuteTransmissionDocument(Mockito.any());
		verify(pitreClient).postProtocolPredisposed(Mockito.any());
		verify(pitreClient).getDocumentTemplate(Mockito.anyString());
	}

	@Test
	public void ShouldCreateAndTransmitANewDocumentWithOneAttachmentAndNewCorrespondent() throws Exception {
		SearchCorrespondentsResponse searchCorrResp = new SearchCorrespondentsResponse();
		GetCorrespondentResponse addCorrRes = new GetCorrespondentResponse();
		CreateDocumentResponse createDocRes = new CreateDocumentResponse();
		GetDocumentResponse protoPresRes = new GetDocumentResponse();
		SearchProjectsResponse searchProjResp = new SearchProjectsResponse();
		GetRoleResponse roleRes = new GetRoleResponse();
		MessageResponse msgRes = new MessageResponse();
		TransmissionResponse trsmRes = new TransmissionResponse();

		setNewCorrespondentResponses(searchCorrResp, addCorrRes, createDocRes, protoPresRes, msgRes, trsmRes);
		setCommonResponse(searchProjResp, roleRes);
		setNewCorrespondentWSMock(searchCorrResp, addCorrRes, searchProjResp, createDocRes, roleRes, protoPresRes, msgRes, trsmRes);

		DocumentRegistrationInfoDto inputData = objectMapper.readValue(new File("src/test/resources/pitre/ShouldCreateAndTransmitANewDocumentWithOneAttachmentAndNewCorrespondent_PostRequest.json"),
				DocumentRegistrationInfoDto.class);

		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(inputData).getBytes());

		String fileName = "MODULI_AMF.pdf.p7m";
		Path filePdfPath = Paths.get("src/test/resources/pitre/" + fileName);
		MockMultipartFile documento = new MockMultipartFile("documento", fileName, "", Files.readAllBytes(filePdfPath));

		String allegatoFileName = "dichiarazioneFamigliariConviventi.pdf";
		Path allegatoFilePdfPath = Paths.get("src/test/resources/pitre/" + allegatoFileName);
		MockMultipartFile allegati = new MockMultipartFile("allegati", "Allegato_1.pdf", "application/pdf", Files.readAllBytes(allegatoFilePdfPath));

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/protocollo/documenti");

		builder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(documento).file(allegati)).andExpect(status().isOk()).andExpect(content().string("PAT_TEST/RFS151-06/11/2018-0000641"));

		verify(pitreClient).postSearchCorrespondents(Mockito.any());
		verify(pitreClient).putAddCorrespondent(Mockito.any());
		verify(pitreClient, times(2)).postSearchProjects(Mockito.any());
		verify(pitreClient).putCreateDocument(Mockito.any());
		verify(pitreClient, times(2)).putUploadFileToDocument(Mockito.any());
		verify(pitreClient).getRole(Mockito.any(), Mockito.any());
		verify(pitreClient).postExecuteTransmissionDocument(Mockito.any());
		verify(pitreClient).postProtocolPredisposed(Mockito.any());
	}

	private void setExistingCorrespondentWSMock(SearchCorrespondentsResponse searchCorrResp, SearchProjectsResponse searchProjResp, CreateDocumentResponse createRes, GetRoleResponse roleRes,
			GetDocumentResponse protoPresRes, MessageResponse msgRes, TransmissionResponse trsmRes, GetTemplateResponse gtRes) {
		Mockito.when(pitreClient.postSearchCorrespondents(Mockito.any())).thenReturn(searchCorrResp);
		Mockito.when(pitreClient.postSearchProjects(Mockito.any())).thenReturn(searchProjResp);
		Mockito.when(pitreClient.putCreateDocument(Mockito.any())).thenReturn(createRes);
		Mockito.when(pitreClient.putUploadFileToDocument(Mockito.any())).thenReturn(null);
		Mockito.when(pitreClient.getRole(Mockito.any(), Mockito.any())).thenReturn(roleRes);
		Mockito.when(pitreClient.postExecuteTransmissionDocument(Mockito.any())).thenReturn(trsmRes);
		Mockito.when(pitreClient.postProtocolPredisposed(Mockito.any())).thenReturn(protoPresRes);
		Mockito.when(pitreClient.postAddInProject(Mockito.any())).thenReturn(msgRes);
		Mockito.when(pitreClient.getDocumentTemplate(Mockito.any())).thenReturn(gtRes);

	}

	private void setNewCorrespondentWSMock(SearchCorrespondentsResponse searchCorrResp, GetCorrespondentResponse addCorrRes, SearchProjectsResponse searchProjResp, CreateDocumentResponse createDocRes,
			GetRoleResponse roleRes, GetDocumentResponse protoPresRes, MessageResponse msgRes, TransmissionResponse trsmRes) {
		Mockito.when(pitreClient.postSearchCorrespondents(Mockito.any())).thenReturn(searchCorrResp);
		Mockito.when(pitreClient.putAddCorrespondent(Mockito.any())).thenReturn(addCorrRes);
		Mockito.when(pitreClient.postSearchProjects(Mockito.any())).thenReturn(searchProjResp);
		Mockito.when(pitreClient.putCreateDocument(Mockito.any())).thenReturn(createDocRes);
		Mockito.when(pitreClient.putUploadFileToDocument(Mockito.any())).thenReturn(null);
		Mockito.when(pitreClient.getRole(Mockito.any(), Mockito.any())).thenReturn(roleRes);
		Mockito.when(pitreClient.postExecuteTransmissionDocument(Mockito.any())).thenReturn(trsmRes);
		Mockito.when(pitreClient.postProtocolPredisposed(Mockito.any())).thenReturn(protoPresRes);
		Mockito.when(pitreClient.postAddInProject(Mockito.any())).thenReturn(msgRes);
	}

	private void setNewCorrespondentResponses(SearchCorrespondentsResponse searchCorrResp, GetCorrespondentResponse addCorrRes, CreateDocumentResponse createDocRes, GetDocumentResponse protoPresRes,
			MessageResponse msgRes, TransmissionResponse trsmRes) {
		// searchCorrespondent
		List<Correspondent> correspList = new ArrayList<Correspondent>();
		searchCorrResp.setCorrespondents(correspList);

		// addCorrespondent
		Correspondent corr = new Correspondent();
		corr.setCode("A4GRFS15111111111111");
		corr.setCodeRegisterOrRF("RFS151");
		corr.setCorrespondentType("E");
		corr.setDescription("Nuovo mittente inesistente");
		corr.setId("130183198");
		corr.setIsCommonAddress(false);
		corr.setPreferredChannel("LETTERA");
		corr.setName("Nuovo");
		corr.setSurname("Mittente");
		corr.setVatNumber("11111111111");
		addCorrRes.setCorrespondent(corr);

		// createDocumentAndAddInProject
		Document docRes = new Document();
		docRes.setAnnulled(false);
		docRes.setCreationDate("06/11/2018 12:24:55");
		docRes.setDocNumber("130183201");
		docRes.setDocumentType("A");
		docRes.setInBasket(false);
		docRes.setIsAttachments(false);
		MainDocument mainDocRes = new MainDocument();
		mainDocRes.setId("130183201");
		mainDocRes.setMimeType("application/pdf");
		mainDocRes.setName("FilePrincipale.pdf");
		mainDocRes.setVersionId("1");
		docRes.setMainDocument(mainDocRes);
		docRes.setObject("Domanda n. X per la certificazione antimafia");
		docRes.setPersonalDocument(false);
		docRes.setPredisposed(false);
		docRes.setPrivateDocument(false);
		docRes.setProtocolDate("06/11/2018");
		Register regRes = new Register();
		regRes.setCode("PAT");
		regRes.setDescription("Provincia Autonoma di Trento");
		regRes.setId("86107");
		regRes.setIsRF(false);
		regRes.setState("Open");
		docRes.setRegister(regRes);
		docRes.setSender(corr);
		docRes.setSignature("PAT_TEST/RFS151-06/11/2018-0000641");
		createDocRes.setDocument(docRes);

		// protocolPredisposed
		Document protoPresDocRes = new Document();
		protoPresDocRes.setSignature("PAT_TEST/RFS151-06/11/2018-0000641");
		protoPresRes.setDocument(protoPresDocRes);
		protoPresRes.setCode(CodeEnum.NUMBER_0);
		protoPresRes.setErrorMessage("");

		msgRes.setCode(it.tndigitale.a4g.protocollo.client.model.MessageResponse.CodeEnum.NUMBER_1);
		msgRes.setErrorMessage("");

		trsmRes.setErrorMessage("");
		trsmRes.setCode(it.tndigitale.a4g.protocollo.client.model.TransmissionResponse.CodeEnum.NUMBER_0);
	}

	private void setExistingCorrespondentResponses(SearchCorrespondentsResponse searchCorrResp, CreateDocumentResponse createRes, GetDocumentResponse protoPresRes, MessageResponse msgRes,
			TransmissionResponse trsmRes, GetTemplateResponse gtRes) {
		// searchCorrespondent
		List<Correspondent> correspList = new ArrayList<Correspondent>();
		Correspondent corresp = new Correspondent();
		corresp.setCode("A4GRFS15101234567891");
		corresp.setCorrespondentType("P");
		corresp.setDescription("Bianchi Roberto");
		corresp.setId("130183118");
		corresp.setIsCommonAddress(false);
		corresp.setLocation("true");
		corresp.setName("Roberto");
		corresp.setSurname("Bianchi");
		corresp.setType("E");
		correspList.add(corresp);
		searchCorrResp.setCorrespondents(correspList);

		// createDocumentAndAddInProject
		Document resDoc = new Document();
		resDoc.setAnnulled(false);
		resDoc.creationDate("06/11/2018 11:49:55");
		resDoc.setDocNumber("130183181");
		resDoc.setDocumentType("A");
		resDoc.setId("130183181");
		resDoc.inBasket(false);
		resDoc.isAttachments(false);
		resDoc.setObject("Domanda n. X per la certificazione antimafia");
		resDoc.personalDocument(false);
		resDoc.setPersonalDocument(false);
		resDoc.setPredisposed(false);
		resDoc.setProtocolDate("06/11/2018");
		Register resReg = new Register();
		resReg.setCode("PAT");
		resReg.setDescription("Provincia Autonoma di Trento");
		resReg.setId("86107");
		resReg.setIsRF(false);
		resReg.setState("Open");
		Correspondent resSend = new Correspondent();
		resSend.setCode("A4GRFS15101234567891");
		resSend.setCodeRegisterOrRF("RFS151");
		resSend.setCorrespondentType("P");
		resSend.setDescription("Bianchi Roberto");
		resSend.setId("130183118");
		resSend.setIsCommonAddress(false);
		resSend.setName("Roberto");
		resSend.setSurname("Bianchi");
		resSend.setPreferredChannel("LETTERA");
		resSend.setType("E");
		resSend.setVatNumber("01234567891");
		resDoc.setRegister(resReg);
		resDoc.setSender(resSend);
		resDoc.setSignature("PAT_TEST/RFS151-23/07/2020-0001296");
		createRes.setDocument(resDoc);

		// protocolPredisposed
		Document protoPresDocRes = new Document();
		protoPresDocRes.setSignature("PAT_TEST/RFS151-23/07/2020-0001296");
		protoPresRes.setDocument(protoPresDocRes);
		protoPresRes.setCode(CodeEnum.NUMBER_0);
		protoPresRes.setErrorMessage("");

		msgRes.setCode(it.tndigitale.a4g.protocollo.client.model.MessageResponse.CodeEnum.NUMBER_1);
		msgRes.setErrorMessage("");

		trsmRes.setErrorMessage("");
		trsmRes.setCode(it.tndigitale.a4g.protocollo.client.model.TransmissionResponse.CodeEnum.NUMBER_0);

		// Get Template
		gtRes.setCode(GetTemplateResponse.CodeEnum.NUMBER_0);
		gtRes.setErrorMessage("");
		Template t = new Template();
		t.setName("Mandato unico ed esclusivo di assistenza");
		t.setId("2325");
		t.setStateDiagram(null);
		t.setType(null);
		List<Field> fields = buildFieldsForMandatoTemplate();
		t.setFields(fields);
		gtRes.setTemplate(t);

	}
	
	private List<Field> buildFieldsForMandatoTemplate() {
		List<Field> fields = new ArrayList<Field>();
		Field f1 = new Field();
		f1.setId("3151");
		f1.setName("CAA di riferimento");
		f1.setRequired(true);
		f1.setMultipleChoice(null);
		f1.setType("TextField");
		f1.setCounterToTrigger(false);
		f1.setCodeRegisterOrRF(null);
		f1.setRights("INSERT_AND_MODIFY");
		
		Field f2 = new Field();
		f2.setId("3152");
		f2.setName("CUAA");
		f2.setRequired(true);
		f2.setMultipleChoice(null);
		f2.setType("TextField");
		f2.setCounterToTrigger(false);
		f2.setCodeRegisterOrRF(null);
		f2.setRights("INSERT_AND_MODIFY");
		
		Field f3 = new Field();
		f3.setId("3230");
		f3.setName("Tipo documento");
		f3.setRequired(true);
		f3.setMultipleChoice(null);
		f3.setType("ExclusiveSelection");
		f3.setCounterToTrigger(false);
		f3.setCodeRegisterOrRF(null);
		f3.setRights("INSERT_AND_MODIFY");
		
		fields.add(f1);
		fields.add(f2);
		fields.add(f3);
		return fields;
	}

	private void setCommonResponse(SearchProjectsResponse searchProjResp, GetRoleResponse roleRes) {
		// searchProject
		List<Project> projList = new ArrayList<Project>();
		Project proj = new Project();
		ClassificationScheme classScheme = new ClassificationScheme();
		classScheme.active(false);
		classScheme.setDescription("Titolario Attivo");
		proj.setClassificationScheme(classScheme);
		proj.setCode("3.5-2018-864");
		proj.setControlled(false);
		proj.setCreationDate("8/10/2018");
		proj.setDescription("Domanda di certificazione antimafia");
		proj.setId("130182128");
		proj.setNumber("864");
		proj.setOpen(false);
		proj.setPaper(false);
		proj.setPrivate(false);
		proj.setType("P");
		projList.add(proj);
		searchProjResp.setProjects(projList);

		// getRole
		Role role = new Role();
		role.setCode("S151COMA4G");
		role.setDescription("Componente A4G Agenzia provinciale per i pagamenti - APPAG");
		role.setId("130182100");
		List<Register> roleRegs = new ArrayList<Register>();
		Register roleReg = new Register();
		roleReg.setCode("PAT");
		roleReg.setDescription("Provincia Autonoma di Trento");
		roleReg.setId("86107");
		roleReg.setIsRF(false);
		roleReg.setState("Open");
		roleRegs.add(roleReg);
		roleReg = new Register();
		roleReg.setCode("RFS151");
		roleReg.setDescription("Agenzia Provinciale per i Pagamenti");
		roleReg.setId("16307061");
		roleReg.setIsRF(true);
		roleRegs.add(roleReg);
		role.setRegisters(roleRegs);
		roleRes.setRole(role);
	}
}
