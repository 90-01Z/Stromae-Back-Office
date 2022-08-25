package fr.insee.queen.api.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.insee.queen.api.domain.Campaign;
import fr.insee.queen.api.domain.Comment;
import fr.insee.queen.api.domain.Data;
import fr.insee.queen.api.domain.Metadata;
import fr.insee.queen.api.domain.Nomenclature;
import fr.insee.queen.api.domain.ParadataEvent;
import fr.insee.queen.api.domain.Personalization;
import fr.insee.queen.api.domain.QuestionnaireModel;
import fr.insee.queen.api.domain.StateData;
import fr.insee.queen.api.domain.StateDataType;
import fr.insee.queen.api.domain.SurveyUnit;
import fr.insee.queen.api.service.CampaignService;
import fr.insee.queen.api.service.CommentService;
import fr.insee.queen.api.service.DataService;
import fr.insee.queen.api.service.DataSetInjectorService;
import fr.insee.queen.api.service.MetadataService;
import fr.insee.queen.api.service.NomenclatureService;
import fr.insee.queen.api.service.ParadataEventService;
import fr.insee.queen.api.service.PersonalizationService;
import fr.insee.queen.api.service.QuestionnaireModelService;
import fr.insee.queen.api.service.StateDataService;
import fr.insee.queen.api.service.SurveyUnitService;

@Service
public class DataSetInjectorServiceImpl implements DataSetInjectorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSetInjectorServiceImpl.class);

	@Autowired
	private CampaignService campaignservice;
	@Autowired
	private SurveyUnitService surveyUnitService;
	@Autowired
	private DataService dataService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ParadataEventService paradataEventService;
	@Autowired
	private MetadataService metadataService;
	@Autowired
	private PersonalizationService personalizationService;
	@Autowired
	private StateDataService stateDataService;
	@Autowired
	private QuestionnaireModelService questionnaireModelService;
	@Autowired
	private NomenclatureService nomenclatureService;

	private static final String CURRENT_PAGE = "2.3#5";

	private ObjectMapper objectMapper = new ObjectMapper();
	private Nomenclature n;
	private Nomenclature n2;


	public void createDataSet() {
		LOGGER.info("Dataset creation start");
		createTestSuggesterDataSet();
		LOGGER.info("Dataset creation end");	
	}

	public void createTestSuggesterDataSet() {
		LOGGER.info("Dataset Queen Test suggester creation start");
		
		JsonNode jsonArrayQuestionnaireModelStromae9001Z = objectMapper.createObjectNode();
		JsonNode jsonArrayMetadata = objectMapper.createObjectNode();


		try {
			jsonArrayQuestionnaireModelStromae9001Z = objectMapper.readTree(new File(getClass().getClassLoader().getResource("db//dataset//9001z//questionnaire.json").getFile()));
			jsonArrayMetadata = objectMapper.readTree(new File(getClass().getClassLoader().getResource("db//dataset//9001z//metadata//metadata.json").getFile()));

	 } catch (Exception e) {
		 e.printStackTrace();
	 }

	LOGGER.info("Dataset 9001Z : creation Nomenclature");

	ArrayList<Nomenclature> listNomenclature = createTestSuggesterNomenclature();

	LOGGER.info("Dataset 9001Z : creation Campaign Stromae");

	injectCampaign("90-01Z", "Test suggester 9001Z", jsonArrayQuestionnaireModelStromae9001Z,listNomenclature, jsonArrayMetadata);


	}

	private void injectCampaign(String id, String label,JsonNode jsonQm, ArrayList<Nomenclature> listNomenclature,JsonNode jsonMetadata) {
		Pair<Campaign,QuestionnaireModel> campQm = createCampaign(id,label, jsonQm,jsonMetadata, listNomenclature);
		Campaign camp= campQm.getFirst();
		QuestionnaireModel qm = campQm.getSecond();
		LOGGER.info("Dataset : creation SurveyUnit");
	
		initSurveyUnit(String.format("%s_01", id), camp, qm);
		initSurveyUnit(String.format("%s_02", id), camp, qm);
		initSurveyUnit(String.format("%s_03", id), camp, qm);

	}

	private ArrayList<Nomenclature> createTestSuggesterNomenclature() {
		JsonNode jsonArrayNomenclatureProfFem = objectMapper.createObjectNode();
		JsonNode jsonArrayNomenclatureProfMasc = objectMapper.createObjectNode();

		try {
			jsonArrayNomenclatureProfFem = objectMapper.readTree(new File(getClass().getClassLoader().getResource("db//dataset//9001z//nomenclature//PROF2022F.json").getFile()));
			jsonArrayNomenclatureProfMasc = objectMapper.readTree(new File(getClass().getClassLoader().getResource("db//dataset//9001z//nomenclature//PROF2022H.json").getFile()));
			
	 } catch (Exception e) {
		 e.printStackTrace();
	 }

		Nomenclature nomenclatureeProfFem = new Nomenclature("PROF2022F","Profession fem",jsonArrayNomenclatureProfFem);
		Nomenclature nomenclatureProfMasc = new Nomenclature("PROF2022H","Profession masc",jsonArrayNomenclatureProfMasc);

		ArrayList<Nomenclature> listNomenclature = new ArrayList<Nomenclature>(Arrays.asList(nomenclatureeProfFem,nomenclatureProfMasc));
		createNomenclatures(listNomenclature);
		return listNomenclature;
	}

	private Pair<Campaign,QuestionnaireModel> createCampaign(String id, String label,JsonNode jsonQm,JsonNode jsonMetadata, ArrayList<Nomenclature> listNomenclature) {
		LOGGER.info(String.format("Create Campaing %s",id));
		Campaign camp = new Campaign(id,label,null); 
		QuestionnaireModel qm = new QuestionnaireModel(id,label,jsonQm,new HashSet<>(listNomenclature),camp);
		if(!campaignservice.findById(camp.getId()).isPresent()) {
			camp.setQuestionnaireModels(new HashSet<>(List.of(qm)));
			campaignservice.save(camp);
			if (jsonMetadata != null) {
				Metadata metadata = new Metadata(UUID.randomUUID(),jsonMetadata,camp);
				metadataService.save(metadata);
			}
			if(!questionnaireModelService.findById(qm.getId()).isPresent()) {
				questionnaireModelService.save(qm);
			}
		}
		return Pair.of(camp,qm);
	}

	private void createNomenclatures(ArrayList<Nomenclature> listNomenclature) {
		LOGGER.info("Creation Nomenclatures");
		listNomenclature.stream().forEach((nomenclature) -> {
			if(!nomenclatureService.findById(nomenclature.getId()).isPresent()) {
				nomenclatureService.save(nomenclature);
			}
		}
		);
	}

	private void initSurveyUnit(String id, Campaign campaign, QuestionnaireModel questionnaireModel) {
		if(!surveyUnitService.findById(id).isPresent()) {
			LOGGER.info("initSurveyUnit -> SU Do not present, we create it");
			SurveyUnit su = new SurveyUnit(id,campaign,questionnaireModel,null,null,null,null);
			surveyUnitService.save(su); // That save SU in DB which is necessary to add data, comment etc...
			Data data = new Data(UUID.randomUUID(),objectMapper.createObjectNode(),su);
			dataService.save(data);
			su.setData(data);

			Comment comment = new Comment(UUID.randomUUID(),objectMapper.createObjectNode(),su);
			commentService.save(comment);
			su.setComment(comment);

			Personalization personalization = new Personalization(UUID.randomUUID(),objectMapper.createObjectNode(),su);
			personalizationService.save(personalization);
			su.setPersonalization(personalization);

			StateData stateData = new StateData(UUID.randomUUID(),StateDataType.INIT,900000000L,"1",su);
			stateDataService.save(stateData);
			
			surveyUnitService.save(su);
			LOGGER.info("End of SU Creation");
		}

	}

}
