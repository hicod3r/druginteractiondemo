package com.google.ce.demos.DrugInteraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DrugConsole {

	public static void main(String[] args) throws Exception {
		
		
		String s = "RECORD #155092 121547079 | CMH | 18332143 | | 268595 | 2/17/1995 12:00:00 AM | Discharge Summary | Signed | DIS | Admission Date: 1/1/1995 Report Status: Signed Discharge Date: 11/29/1995 PRINCIPAL DIAGNOSIS: POST MYOCARDIAL INFARCTION ANGINA SIGNIFICANT PROBLEMS: 1. Coronary artery disease 2. Hypertension 3. Non-insulin dependent diabetes mellitus HISTORY OF PRESENT ILLNESS: Patient is a 72 year old white male Status post recent myocardial infarction who was admitted for recurrent chest discomfort. On the 1 of July , the patient noticed a \\\\\\\"uncomfortable chest sensation\\\\\\\" as he was driving. He experienced diaphoresis at that time , but no shortness of breath and no nausea or vomiting. He was seen at an outside hospital and noted to have 2-3 mm ST elevations in leads 2 , 3 , FV5 and V6 ( Humpde Hospital ). The patient was given TPA , Heparin intravenous and aspirin. His peak CPK was noted to be 5 , 742 with an MB fraction of 199. The patient's hospital stay was complicated by frequent PVCs on cardiac monitor , for which he was temporarily treated with Lidocaine. Immediately after the TPA infusion , the patient noted that his chest discomfort which was located primarily on the left side , across the midline , to the right. An echocardiogram that was performed , showed an ejection fraction of 60% with posterolateral dyssynergia. On the 16 of September of July , the patient was discharged from Cocku Health after a submax ETT. He reached a heart rate of 95 and blood pressure 168/90. On the 25 of June , the patient noted that he had a recurrence of this vague chest discomfort as he was sitting and talking to friends. He took a sublingual Nitroglycerin without relief. This chest discomfort episode resolved spontaneously after approximately ten minutes. On the 8 of September , the patient was at a party and , again , developed a similar anxious sensation which was similar to the feeling that he experienced prior to his myocardial infarction. He took two sublingual Nitroglycerin again , without relief , and his discomfort resolved after two hours. Patient denies PND , orthopnea , or edema. The patient was admitted on the 8 of September for cardiac catheterization to evaluate post myocardial infarction angina. ALLERGIES: Include Penicillin and Toprol which leads to an itch. PAST MEDICAL HISTORY: Significant for hypertension for five to six years and diet controlled diabetes mellitus times four years. ADMISSION MEDICINES: Ecotrin , Vasotec 20 mg po qd. Atenolol 50 mg po qd. Nitropatch which was to be continued the day of admission. SOCIAL HISTORY: The patient has never smoked and he denies alcohol use. FAMILY HISTORY: His father died at the age of 48 with the history of coronary artery disease. The mother died at the age of 80 secondary to congestive heart failure. His brother died of a myocardial infarction in his 60s. ADMITTING PHYSICAL EXAM: BP 120/80. Heart rate 54. Temperature 97.2 , with 95% room air saturation. Patient is a well-developed elderly male , lying in bed in no acute distress. HEENT exam reveals pupils are equal , round , reactive to light and accommodation , oropharynx is within normal limits. Neck exam is supple with jugular venous pressure 4 cm and carotids without bruits. Heart exam shows S1 and S2 with a positive S4 , 1/6 systolic murmur heard at the left lower sternal border. Chest is clear to auscultation bilaterally. Abdominal exam is benign with positive bowel sounds , soft and non-tender and is non-distended. Extremities exam shows no clubbing , cyanosis , or edema , with +2 pulses bilaterally at the femoral deep tendon , and posterior tibial sites. Rectal exam is guaiac negative. Neurological exam is non-focal , with down-going toes bilaterally , and 2+ reflexes bilaterally. ADMISSION LAB: Sodium 139 , potassium 4.9 , chloride 190 , bicarbonate 26 , BUN 23 , creatinine 1.4 , glucose 183 , white count 9.3 , hematocrit 36 with platelets of 274 , CK 219 , LDH 407. Urinalysis negative. Electrocardiogram showed sinus rhythm 55 , axis 20 , normal intervals , biphasic V4 through V6 and 1 mm ST elevation in V5 and V6. chest x-ray was within normal limits. HOSPITAL COURSE: The physical therapy was started on Heparin immediately after admission for presumed post myocardial infarction angina. He underwent cardiac catheterization the morning following admission. This demonstrated a proximal 100% occluded left circ lesion which was easily angio-positive to 20% residual. This was complicated by a small dissection. An LV gram demonstrated inferior hypokinesis. The patient tolerated the procedure well without significant decrease in hematocrit or increase in creatinine. He was heparinized for a full forty-eight hours post cardiac catheterization procedure. At the time of discharge , he was ambulating. At no time during this admission did he develop chest discomfort. The patient was discharged to home in good condition and will be followed by his cardiologist in one week post discharge. DISCHARGE MEDICATIONS: Aspirin 325 mg po qd. Atenolol 50 mg po qd. Vasotec 20 mg po qd. Sublingual Nitroglycerin prn. Dictated By: MARC T. JAJI , M.D. HK19 Attending: DAMIAN GLOMB , M.D. UX16 LC444/5227 Batch: 3436 Index No. T0DUWD0PBV D: 8/2/95 T: 8/2/95 [report_end]";
		System.out.println(s.getBytes().length);
		
		byte[] bytes = Compression.compress(s);
		
		String compressedString = new String(bytes,StandardCharsets.UTF_8);
		
		System.out.println(compressedString);
		
		System.out.println(bytes.length);
		
		String d = Compression.decompress(compressedString.getBytes(StandardCharsets.UTF_8));
		
		System.out.println(d);
		
		
		if(true)
			return;

		GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

		credentials.refresh();

		String accessToken = credentials.getAccessToken().getTokenValue();

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("documentContent",
				"RECORD #155092 121547079 | CMH | 18332143 | | 268595 | 2/17/1995 12:00:00 AM | Discharge Summary | Signed | DIS | Admission Date: 1/1/1995 Report Status: Signed Discharge Date: 11/29/1995 PRINCIPAL DIAGNOSIS: POST MYOCARDIAL INFARCTION ANGINA SIGNIFICANT PROBLEMS: 1. Coronary artery disease 2. Hypertension 3. Non-insulin dependent diabetes mellitus HISTORY OF PRESENT ILLNESS: Patient is a 72 year old white male Status post recent myocardial infarction who was admitted for recurrent chest discomfort. On the 1 of July , the patient noticed a \\\"uncomfortable chest sensation\\\" as he was driving. He experienced diaphoresis at that time , but no shortness of breath and no nausea or vomiting. He was seen at an outside hospital and noted to have 2-3 mm ST elevations in leads 2 , 3 , FV5 and V6 ( Humpde Hospital ). The patient was given TPA , Heparin intravenous and aspirin. His peak CPK was noted to be 5 , 742 with an MB fraction of 199. The patient's hospital stay was complicated by frequent PVCs on cardiac monitor , for which he was temporarily treated with Lidocaine. Immediately after the TPA infusion , the patient noted that his chest discomfort which was located primarily on the left side , across the midline , to the right. An echocardiogram that was performed , showed an ejection fraction of 60% with posterolateral dyssynergia. On the 16 of September of July , the patient was discharged from Cocku Health after a submax ETT. He reached a heart rate of 95 and blood pressure 168/90. On the 25 of June , the patient noted that he had a recurrence of this vague chest discomfort as he was sitting and talking to friends. He took a sublingual Nitroglycerin without relief. This chest discomfort episode resolved spontaneously after approximately ten minutes. On the 8 of September , the patient was at a party and , again , developed a similar anxious sensation which was similar to the feeling that he experienced prior to his myocardial infarction. He took two sublingual Nitroglycerin again , without relief , and his discomfort resolved after two hours. Patient denies PND , orthopnea , or edema. The patient was admitted on the 8 of September for cardiac catheterization to evaluate post myocardial infarction angina. ALLERGIES: Include Penicillin and Toprol which leads to an itch. PAST MEDICAL HISTORY: Significant for hypertension for five to six years and diet controlled diabetes mellitus times four years. ADMISSION MEDICINES: Ecotrin , Vasotec 20 mg po qd. Atenolol 50 mg po qd. Nitropatch which was to be continued the day of admission. SOCIAL HISTORY: The patient has never smoked and he denies alcohol use. FAMILY HISTORY: His father died at the age of 48 with the history of coronary artery disease. The mother died at the age of 80 secondary to congestive heart failure. His brother died of a myocardial infarction in his 60s. ADMITTING PHYSICAL EXAM: BP 120/80. Heart rate 54. Temperature 97.2 , with 95% room air saturation. Patient is a well-developed elderly male , lying in bed in no acute distress. HEENT exam reveals pupils are equal , round , reactive to light and accommodation , oropharynx is within normal limits. Neck exam is supple with jugular venous pressure 4 cm and carotids without bruits. Heart exam shows S1 and S2 with a positive S4 , 1/6 systolic murmur heard at the left lower sternal border. Chest is clear to auscultation bilaterally. Abdominal exam is benign with positive bowel sounds , soft and non-tender and is non-distended. Extremities exam shows no clubbing , cyanosis , or edema , with +2 pulses bilaterally at the femoral deep tendon , and posterior tibial sites. Rectal exam is guaiac negative. Neurological exam is non-focal , with down-going toes bilaterally , and 2+ reflexes bilaterally. ADMISSION LAB: Sodium 139 , potassium 4.9 , chloride 190 , bicarbonate 26 , BUN 23 , creatinine 1.4 , glucose 183 , white count 9.3 , hematocrit 36 with platelets of 274 , CK 219 , LDH 407. Urinalysis negative. Electrocardiogram showed sinus rhythm 55 , axis 20 , normal intervals , biphasic V4 through V6 and 1 mm ST elevation in V5 and V6. chest x-ray was within normal limits. HOSPITAL COURSE: The physical therapy was started on Heparin immediately after admission for presumed post myocardial infarction angina. He underwent cardiac catheterization the morning following admission. This demonstrated a proximal 100% occluded left circ lesion which was easily angio-positive to 20% residual. This was complicated by a small dissection. An LV gram demonstrated inferior hypokinesis. The patient tolerated the procedure well without significant decrease in hematocrit or increase in creatinine. He was heparinized for a full forty-eight hours post cardiac catheterization procedure. At the time of discharge , he was ambulating. At no time during this admission did he develop chest discomfort. The patient was discharged to home in good condition and will be followed by his cardiologist in one week post discharge. DISCHARGE MEDICATIONS: Aspirin 325 mg po qd. Atenolol 50 mg po qd. Vasotec 20 mg po qd. Sublingual Nitroglycerin prn. Dictated By: MARC T. JAJI , M.D. HK19 Attending: DAMIAN GLOMB , M.D. UX16 LC444/5227 Batch: 3436 Index No. T0DUWD0PBV D: 8/2/95 T: 8/2/95 [report_end] ");

		jsonObject.add("licensedVocabularies", new JsonArray());

		try {

			JsonObject element = (JsonObject) sendRequest(accessToken, jsonObject);

			JsonArray entityMentions = (JsonArray) element.get("entityMentions");
			JsonArray entities = (JsonArray) element.get("entities");

			HashSet<String> entityIds = new HashSet<String>();

			HashSet<String> rxNorm = new HashSet<String>();
			
			HashSet<String> drugInteraction = new HashSet<String>();

			for (JsonElement eachEntity : entityMentions) {

				String type = eachEntity.getAsJsonObject().get("type").getAsString();

				if (type.equals("MEDICINE") == false)
					continue;

				System.out.println(eachEntity.toString());

				String entityId = eachEntity.getAsJsonObject().get("linkedEntities").getAsJsonArray().get(0)
						.getAsJsonObject().get("entityId").getAsString();

				entityIds.add(entityId);

			}

			for (JsonElement je : entities) {

				JsonObject jo = (JsonObject) je;

				String entityId = jo.get("entityId").getAsString();

				if (!entityIds.contains(entityId))
					continue;

				JsonArray vocabCodes = jo.get("vocabularyCodes").getAsJsonArray();

				for (JsonElement eachVocabCode : vocabCodes) {

					String eachVocab = eachVocabCode.getAsString();

					if (!eachVocab.contains("RXNORM"))
						continue;

					String RxNormId = (eachVocab.split("/"))[1];

					rxNorm.add(RxNormId);

//					System.out.println(eachVocab);

				}
			}

			for (String rxNormId : rxNorm) {

				HashMap<String, String> parameterMap = new HashMap<String, String>();

				parameterMap.put("rxcui", rxNormId);

				JsonObject rxInteraction = sendRequest("https://rxnav.nlm.nih.gov/REST/interaction/interaction.json", 
					parameterMap).getAsJsonObject();
				
				
				JsonArray interactionPair = 
						rxInteraction.get("interactionTypeGroup").getAsJsonArray().get(0).getAsJsonObject().
							get("interactionType").getAsJsonArray().get(0).getAsJsonObject().
								get("interactionPair").getAsJsonArray();
				
				
				for(JsonElement eachInteractionPair : interactionPair) {
					
					JsonObject eachPairObject = eachInteractionPair.getAsJsonObject();
					
					String eachDestination = eachPairObject.get("interactionConcept").getAsJsonArray().
						get(1).getAsJsonObject().get("minConceptItem").getAsJsonObject().get("rxcui").getAsString();
					
					if(rxNorm.contains(eachDestination)) {
						
						String description = eachPairObject.get("description").getAsString();
						
						drugInteraction.add(description);
					}
				}
				
				
				
			}
			
			System.out.println(drugInteraction);

		} catch (AuthenticationException | IOException e) {
			e.printStackTrace();
		}

	}

	public static JsonElement sendRequest(String accessToken, JsonObject body)
			throws ClientProtocolException, IOException, AuthenticationException {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(
				"https://healthcare.googleapis.com/v1beta1/projects/google.com:hicoder-playground/locations/us-central1/services/nlp:analyzeEntities");

		httpPost.addHeader("Authorization", "Bearer " + accessToken);

		StringEntity entity = new StringEntity(body.toString());
		httpPost.setEntity(entity);

		CloseableHttpResponse response = client.execute(httpPost);

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		String jsonResponse = reader.lines().collect(Collectors.joining());

		System.out.println(jsonResponse);

		client.close();

		return JsonParser.parseString(jsonResponse);

	}

	public static JsonElement sendRequest(String url, HashMap<String, String> parameters)
			throws URISyntaxException, UnsupportedOperationException, IOException {

		URIBuilder builder = new URIBuilder(url);

		for (String eachKey : parameters.keySet()) {

			builder.setParameter(eachKey, parameters.get(eachKey));

		}

		HttpGet get = new HttpGet(builder.build());

		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(get);

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		String jsonResponse = reader.lines().collect(Collectors.joining());

		System.out.println(jsonResponse);

		client.close();

		return JsonParser.parseString(jsonResponse);

	}

}
