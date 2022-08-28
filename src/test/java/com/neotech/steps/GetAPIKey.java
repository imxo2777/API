package com.neotech.steps;

import java.net.http.HttpResponse.BodyHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;


import com.neotech.utils.APIConstants;
import com.neotech.utils.APIGlobalVariables;

import groovyjarjarantlr4.v4.parse.ANTLRParser.throwsSpec_return;
import io.restassured.RestAssured;
import io.restassured.internal.http.HttpResponseException;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class GetAPIKey {

	
	
	@Test
	public void getApiKey() throws Exception  {
		
		RestAssured.baseURI = APIConstants.BASE_URI;
		
		Response response = RestAssured.given()
		.when()
		.get(APIConstants.GetApiKeyEndPoint)
		.prettyPeek();
		
		String api_key = response.body().jsonPath().getString("api_key");
		System.out.println(api_key);
		//Assert get api_key status code
		int expectedStatusCode = response.getStatusCode();
		int actualStatusCode = 200;
		
		Assert.assertEquals(expectedStatusCode, actualStatusCode);
        System.out.println("Status code of get api_key request is -> " + response.getStatusCode());
		
		
		RestAssured.baseURI = APIConstants.BASE_URI;
		
		Response patient = RestAssured.given()
		.when()
		.get(APIConstants.GetPatientEndPoint)
		.prettyPeek();
		
		String patientBody = patient.getBody().asString();
        System.out.println("Patientbody ====> " + patientBody);
		
		//Assert getpatient status code
		int expectedPatientStatusCode = 200;
		int actualPatientStatusCode = patient.getStatusCode();
		Assert.assertEquals(expectedPatientStatusCode, actualPatientStatusCode);
		
		//int size = response.body().jsonPath().getInt("responseBody.size()");
		//or
		int size = patient.body().jsonPath().getList("").size();
		System.out.println(size);
	    
		int JuneApp = 0;
		//Confirming that there are 1 or 2 June appointments 
		for(int i = 0; i < size; i++) {
			if(patient.body().jsonPath().getString("["+i+"].appointment_date").contains("2022-07")) {
			System.out.println(patient.body().jsonPath().getString("["+i+"]"));
			System.out.println(i);
			JuneApp++;
			}
	
		}
          		System.out.println("There are " + JuneApp + " people with June appointments");
          		
		System.out.println("==============Fail if Steve Roger is not there=====================");
		//Fail if patient Steve Rogers does not exist in the response body
		for(int i = 0; i < size; i++) {
			if(patient.body().jsonPath().getString("["+i+"].id").contains("SR19760827202206208364")) {
				System.out.println(patient.body().jsonPath().getString("["+i+"]"));
				System.out.println(i);
				Assert.assertEquals(patient.body().jsonPath().getString("["+i+"]") , "[id:SR19760827202206208364, birthdate:1976-08-27, phone:347-555-9876, appointment_date:2022-06-20, name:[lastName:Rogers, firstName:Steve], address:[street:45 W 45th St, city:New York, state:NY, zip:10036]]");
			}

		}
        

		//Create a payload
		
		String payload = " {\r\n"
				+ "        \"id\": \"SR19760827202206208364\",\r\n"
				+ "        \"name\": {\r\n"
				+ "            \"lastName\": \"Tester\",\r\n"
				+ "            \"firstName\": \"Awesome\"\r\n"
				+ "        },\r\n"
				+ "        \"address\": {\r\n"
				+ "            \"street\": \"123 Sesame St\",\r\n"
				+ "            \"city\": \"Hawaii\",\r\n"
				+ "            \"state\": \"HW\",\r\n"
				+ "            \"zip\": \"20001\"\r\n"
				+ "        }\r\n"
				+ " }";
		
		/*
		 * In a test, verify the “id” for all of the patients returned follows the format below:
[patient first initial] + [patient last initial] + [patient 4
digit birth year] + [patient 2 digit birth month] + [patient 2
digit birth day] + [patient 4 digit appointment year] +
[patient 2 digit appointment month] + [patient 2 digit
appointment day] + [4 random digits]
Example ID: AB19800121202206019999
Example “exploded” ID: A B 1980 01 21 2022 06 01 9999
		 */
		
		// SR19760827202206208364
		
		String id = "{\r\n"
				+ "      \"id\": \"SR19760827202206208364\",\r\n"
				+ "      \"birthdate\": \"1976-08-27\",\r\n"
				+ "      \"phone\": \"347-555-9876\",\r\n"
				+ "      \"appointment_date\": \"2022-06-20\",\r\n"
				+ "      \"name\": {\r\n"
				+ "         \"lastName\": \"Rogers\",\r\n"
				+ "         \"firstName\": \"Steve\"\r\n"
				+ "      },\r\n"
				+ "      \"address\": {\r\n"
				+ "         \"street\": \"45 W 45th St\",\r\n"
				+ "         \"city\": \"New York\",\r\n"
				+ "         \"state\": \"NY\",\r\n"
				+ "         \"zip\": \"10036\"\r\n"
				+ "      }\r\n"
				+ "   }";
//		char firstInitial = patient.body().jsonPath().getString("name.firstName").charAt(0);
//		char lastInitial = patient.body().jsonPath().getString("name.lastName").charAt(0);
//		String birthdate = patient.body().jsonPath().getString("birthdate").replaceAll("[^0-9]", "");
//		String appointYear = patient.body().jsonPath().getString("appointment_date").replaceAll("[^0-9]", "");
		
		for(int i = 0; i < size; i++) {
			
			char firstInitial = patient.body().jsonPath().getString("["+i+"].name.firstName").charAt(0);
		//	System.out.println(firstInitial);
			char lastInitial = patient.body().jsonPath().getString("["+i+"].name.lastName").charAt(0);
			//System.out.println(lastInitial);
			String birthdate = patient.body().jsonPath().getString("["+i+"].birthdate").replaceAll("[^0-9]", "");
			//System.out.println(birthdate);
			String appointYear = patient.body().jsonPath().getString("["+i+"].appointment_date").replaceAll("[^0-9]", "");
			//System.out.println(appointYear);
			
			String idPart = ""+firstInitial+"" + ""+lastInitial+"" + ""+birthdate+"" + ""+appointYear+"";
			System.out.println(idPart);
			
			boolean IDformat = patient.body().jsonPath().getString("["+i+"].id").contains(idPart);
			
			System.out.println("Id format is correct? " + IDformat);
		}
		
		
		
	//patch request
		RestAssured.baseURI = APIConstants.BASE_URI;
		
		Response patch = RestAssured.given()
				         .queryParam("api_key", APIGlobalVariables.api_key)
				         .body(payload)
				         .when()
				         .patch(APIConstants.UpdatePatientEndPoint)
				         .prettyPeek();
		
		//Assert status code
		int expectedPatchStatusCode = 200;
		int actualPatchStatusCode = patch.statusCode();
		Assert.assertEquals(expectedPatchStatusCode, actualPatchStatusCode);
		
		String patchResponseBody = patch.getBody().asString();
		String expectedPatchBody = "{\"id\": \"SR19760827202206208364\", \"birthdate\": \"1976-08-27\", \"phone\": \"347-555-9876\", \"appointment_date\": \"2022-06-20\", \"name\": {\"lastName\": \"Tester\", \"firstName\": \"Awesome\"}, \"address\": {\"street\": \"123 Sesame St\", \"city\": \"Hawaii\", \"state\": \"HW\", \"zip\": \"20001\"}}";
		System.out.println("patch request body ===> " + patchResponseBody);
		
		System.out.println("expected body ===> " + expectedPatchBody);
		Assert.assertEquals(patchResponseBody, expectedPatchBody);
		
		//Assert either this way
		Assert.assertTrue(patchResponseBody.contains("\"id\": \"SR19760827202206208364\""));
		//or
		//Assert id, birthdate, phone, appointment_date
		String expectedID = "SR19760827202206208364";
		String actualID = patch.body().jsonPath().getString("id");
		Assert.assertEquals(expectedID, actualID);
		
		//Assert birthdate is unchanged
		String expectedBirthdate = "1976-08-27";
		String actualBirthdate = patch.body().jsonPath().getString("birthdate");
		Assert.assertEquals(expectedBirthdate, actualBirthdate);
		
		//Assert phone is unchanged
		String expectedPhone = "347-555-9876";
		String actualPhone = patch.body().jsonPath().getString("phone");
		Assert.assertEquals(expectedPhone, actualPhone);
		
		//Assert appointment date is unchanged
		String expectedAppointmentDate = "2022-06-20";
		String actualAppointmentDate = patch.body().jsonPath().getString("appointment_date");
		Assert.assertEquals(expectedAppointmentDate, actualAppointmentDate);
		
		//Assert lastName
		String expectedLastName = "Tester";
		String actualLastName = patch.body().jsonPath().getString("name.lastName");
		Assert.assertEquals(expectedLastName, actualLastName);
		
		//Assert firstName
		String expectedFirstName = "Awesome";
		String actualFirstName = patch.body().jsonPath().getString("name.firstName");
		Assert.assertEquals(expectedFirstName, actualFirstName);
		
		//Assert street
		String expectedStreet = "123 Sesame St";
		String actualStreet = patch.body().jsonPath().getString("address.street");
		Assert.assertEquals(expectedStreet, actualStreet);
		
		//Assert city 
		String expectedCity = "Hawaii";
		String actualCity = patch.body().jsonPath().getString("address.city");
		Assert.assertEquals(expectedCity, actualCity);
		
	    //Assert state
		String expectedState = "HW";
		String actualState = patch.body().jsonPath().getString("address.state");
		Assert.assertEquals(expectedState, actualState);
		
		//Assert zip
		String expectedZip = "20001";
		String actualZip = patch.body().jsonPath().getString("address.zip");
		Assert.assertEquals(expectedZip, actualZip);
		
		
		//Post request
		String postPayload = "{\r\n"
				+ "\"firstName\": \"Awesome\",\r\n"
				+ "\"lastName\": \"Tester\",\r\n"
				+ "\"url\": \"www.tester.com\"\r\n"
				+ "}";
		
		RestAssured.baseURI = APIConstants.BASE_URI;
		
		Response post = RestAssured.given()
				.queryParam("api_key", api_key)
				.body(postPayload)
				.when()
				.post(APIConstants.Post)
				.prettyPeek();
		
		//Assert status code 201 created
		int expectedPostStatusCode = 201;
		int actualPostStatusCode = post.statusCode();
		Assert.assertEquals(expectedPostStatusCode, actualPostStatusCode);
		System.out.println(actualPostStatusCode);
		
		
	}
	
	
}
