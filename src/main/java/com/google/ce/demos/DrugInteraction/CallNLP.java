package com.google.ce.demos.DrugInteraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet(name = "CallNLP", urlPatterns = { "/callNLP" })
public class CallNLP extends HttpServlet {

	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			
			response.getWriter().print("Get not supported");
			
		} catch (UnsupportedOperationException | IOException e) {
			
			e.printStackTrace();
		}
	
	}
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			
			String text = request.getParameter("text");
			
			response.getWriter().print(process(text));
			
		} catch (UnsupportedOperationException | IOException | URISyntaxException e) {
			
			e.printStackTrace();
		}
	
	}
	

	public JsonObject process(String documentCont) throws IOException, UnsupportedOperationException, URISyntaxException {

		GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

		credentials.refresh();

		String accessToken = credentials.getAccessToken().getTokenValue();

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("documentContent",
				documentCont);

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

				if(eachEntity.getAsJsonObject().get("linkedEntities") == null)
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
				}
			}

			for (String rxNormId : rxNorm) {

				JsonElement je = getFromDataStore(rxNormId);
				
				JsonObject rxInteraction =null;
				
				if(je != null)
					rxInteraction = je.getAsJsonObject();
				
				
				if(rxInteraction == null) {
				
					HashMap<String, String> parameterMap = new HashMap<String, String>();
	
					parameterMap.put("rxcui", rxNormId);
	
					rxInteraction = sendRequest("https://rxnav.nlm.nih.gov/REST/interaction/interaction.json", 
						parameterMap).getAsJsonObject();
					
					setToDataStore(rxNormId, rxInteraction);
				}
				
				System.out.println(rxInteraction.toString());
				
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
			
			JsonArray interactions = (new Gson()).toJsonTree(drugInteraction).getAsJsonArray();
			JsonElement text = (new Gson()).toJsonTree(documentCont);
			
			element.add("interaction", interactions);
			element.add("text", text);
			
			return element;

		} catch (AuthenticationException | IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public JsonElement sendRequest(String accessToken, JsonObject body)
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

		client.close();

		return JsonParser.parseString(jsonResponse);

	}

	public JsonElement sendRequest(String url, HashMap<String, String> parameters)
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

		client.close();

		return JsonParser.parseString(jsonResponse);

	}
	
	public JsonElement getFromDataStore(String key) {
		
		Key dataKey = KeyFactory.createKey("interaction", key);
		
		Entity e;
		
		try {
			e = datastore.get(dataKey);
			
			Object o =   e.getProperty("blob");
			
			if(o instanceof Blob) {
				
				Blob b = (Blob) o;
			
				String value = Compression.decompress(b.getBytes());
				
				return JsonParser.parseString(value);
			}
			
			Text t = (Text) o;
			
			return JsonParser.parseString(t.getValue());
			
			
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			return null;
		}
	}  
	
	
	
	public void setToDataStore(String key, JsonElement je) throws IOException {
		
		Key dataKey = KeyFactory.createKey("interaction", key);
		
		Entity e = new Entity(dataKey);
		
		Blob b = new Blob(Compression.compress(je.toString()));
		
		e.setProperty("blob", b);
		
		datastore.put(e);
	}

}