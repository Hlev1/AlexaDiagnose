package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.Intent;
import com.hlev1.alexaDiagnose.utils.SkillUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

import static com.hlev1.alexaDiagnose.utils.SessionStorage.*;

public class BeginDiagnosisIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("BeginDiagnosisIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        DialogState dialog = intentRequest.getDialogState();

        if (!dialog.getValue().toString().equals("COMPLETED")) {

            Intent thisIntent = intentRequest.getIntent();
            return handlerInput.getResponseBuilder()
                    .addDelegateDirective(thisIntent)
                    .build();
        }

        String questionType = "";
        JSONObject questionObj = null;

        Map slots = intentRequest.getIntent().getSlots();
        String age = ((Slot) slots.get("age")).getValue();
        String gender = ((Slot) slots.get("gender")).getValue();

        try {
            JSONObject apiResponse = makePOST("https://api.infermedica.com/covid19/diagnosis",
                                                Integer.parseInt(age), gender);
            Boolean shouldStop = (Boolean) apiResponse.get("should_stop");

            if (shouldStop) {
                return handlerInput.getResponseBuilder()
                        .withSpeech("Stopping diagnosis, implementation waiting")
                        .build();
            }

            questionObj = (JSONObject) apiResponse.get("question");
            questionType = (String) questionObj.get("type");

            Map session = handlerInput.getAttributesManager().getSessionAttributes();
            session.put(CONTINUOUS_QUESTION, questionObj);
            handlerInput.getAttributesManager().setSessionAttributes(session);
        } catch (Exception e) {
            // ERROR HANDLING
            questionType = "";
            Boolean shouldStop = false;
        }


        switch (questionType) {
            case "single":
                break;
            case "group_single":
                break;
            case "group_multiple":
                GroupMultipleQIntentHandler handler = new GroupMultipleQIntentHandler();
                return handler.handle(handlerInput, intentRequest);

            default:
                break;
        }

        Intent thisIntent = intentRequest.getIntent();
        return handlerInput.getResponseBuilder()
                .addDelegateDirective(thisIntent)
                .build();
    }

    public JSONObject makePOST(int age, String gender) throws UnirestException, ParseException {
        String json = String.format("{\n    \"sex\": \"%s\",\n    \"age\": %d,\n    \"evidence\": []\n}", gender, age);

        HttpResponse<String> response = Unirest.post("https://api.infermedica.com/covid19/diagnosis")
                .header("Content-Type", "application/json")
                .header("App-Id", "bd3cbf8c")
                .header("App-Key", "48a6449979b3156bda8756b746860788")
                .header("User-Agent", "PostmanRuntime/7.19.0")
                .header("Accept", "*/*")
                .header("Cache-Control", "no-cache")
                .header("Postman-Token", "c8ad7e10-ccf0-4bfc-86ab-cf6c15db2bef,67e475e7-0092-481a-8915-95a40dfe03b2")
                .header("Host", "api.infermedica.com")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Connection", "keep-alive")
                .header("cache-control", "no-cache")
                .body(json)
                .asString();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(response.getBody());

        return obj;
    }

}
