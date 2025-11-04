package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

public class ScoreControllerRA {
	
	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;
	
	private Long existingMovieId, nonexistingMovieId;
	private Double scoreValid;
	
	private Map<String, Object> postScoreInstance;
	
	@BeforeEach
	void setU() throws JSONException {
		baseURI = "http://localhost:8080";
		
		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";
		
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "SFC";
		
		existingMovieId = 1L;
		nonexistingMovieId = 300L;
		scoreValid = 4.5;
		
		postScoreInstance = new HashMap<>();
		postScoreInstance.put("movieId", nonexistingMovieId);
		postScoreInstance.put("score", scoreValid);
		
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		given()
			.header("Content-type","application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(postScoreInstance)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		postScoreInstance.put("movieId", "");
		given()
			.header("Content-type","application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(postScoreInstance)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		postScoreInstance.put("score", -2.5);
		postScoreInstance.put("movieId", "");
		given()
			.header("Content-type","application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(postScoreInstance)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(422);
	}
	
}
