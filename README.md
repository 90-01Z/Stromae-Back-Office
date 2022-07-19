![Build](https://github.com/90-01Z/Stromae-Back-Office/actions/workflows/docker.yml/badge.svg)

# Stromae-Back-Office
Back-office services for Stromae  
REST API for communication between Stromae DB and Stromae UI.

Fork from : https://github.com/InseeFr/Queen-Back-Office


## Demo version

https://stromae-api-90-01Z.dev.insee.io/swagger-ui.html

## End-Points
- Campaign
	- `GET /campaigns` : get the campaign list
	- `POST /campaigns` : post a new campaign
	- `POST /campaign/context` : integrates the context of a campaign
	- `DELETE /campaign/{id}` : delete a campaign
	

- Questionnaire
	- `GET /questionnaire/{id}` : get the questionnaire by his id
	- `GET /campaign/{id}/questionnaires` : get the list of model json 
	- `GET /campaign/{idCampaign}/questionnaire-id` : id of the questionnaire associated to the campaign
	- `POST /questionnaire-models` : post a new questionnaire

- SurveyUnit
	- `GET /survey-unit/{id}` : get survey-unit by id
	- `GET /survey-unit/{id}/deposit-prof` : get the deposit-proof of survey-unit by id
	- `GET /campaign/{id}/survey-units` : get the list of survey-unit of campaign id
	- `PUT /survey-unit/{id}` : update a survey-unit
	- `POST /campaign/{id}/survey-unit` : post a survey-unit for a campaign
	- `DELETE /survey-unit/{id}` : delete a survey-unit by id

- Data
	- `GET /survey-unit/{id}/data` : get the data of reporting unit id
	- `PUT /survey-unit/{id}/data` : update the data of reporting unit id

- Comment
	- `GET /survey-unit/{id}/comment` : get the comment of reporting unit id 
	- `PUT /survey-unit/{id}/comment` : update the comment of reporting unit id

- Nomenclatures
	- `GET /campaign/{id}/required-nomenclatures` : get the nomenclature of a campaign
	- `GET /questionnaire/{id}/required-nomenclatures` : get the nomenclature of a questionnaire
	- `GET /nomenclature/{id}` : get the nomenclature json
	- `POST /nomenclature` : post a new nomenclature

- Personalization
	- `GET /survey-unit/{id}/personalization` : get the personalization of a survey-unit
	- `PUT /survey-unit/{id}/personalization` : put the personalization for a survey-unit
	
- Metadata
	- `GET /campaign/{id}/metadata` : get the metadata by campaign Id
	- `GET /questionnaire/{id}/metadata` : get the metadata by questionnaire id

- Paradata
	- `POST /paradata` : post the metadata of a campaign
	
- StateData
	- `GET /survey-unit/{id}/state-data` : get the state-data of a survey-unit
	- `PUT /survey-unit/{id}/state-data` : put the state-data for a survey-unit
	- `POST /survey-units/state-data` : Get state-data for all survey-units defined in request body
	
- DataSet
	- `POST /create-dataset` : Create dataset

## License
Please check [LICENSE](https://github.com/InseeFr/Queen-Back-Office/blob/master/LICENSE) file
