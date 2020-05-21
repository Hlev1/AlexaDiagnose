package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import static com.hlev1.alexaDiagnose.utils.SessionStorage.*;

import java.util.Map;
import java.util.Optional;

public class GroupMultipleQIntentHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("GroupMultipleQIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Map session = handlerInput.getAttributesManager().getSessionAttributes();

        // Check if we have just received a response for a previous question
        Map slots = intentRequest.getIntent().getSlots();
        if (slots.containsKey("answer")) {
            String answer = ((Slot) slots.get("answer")).getValue();
            String question = (String) session.get(JUST_ASKED_ID);
            session.remove(JUST_ASKED_ID);

            JSONArray evidence = (JSONArray) session.getOrDefault(EVIDENCE, new JSONArray());

            JSONObject newEvidence = new JSONObject();
            newEvidence.put("id", question);
            newEvidence.put("choice_id", answer);

            evidence.add(newEvidence);
        }

        if (session.containsKey(CONTINUOUS_QUESTION)) {

            JSONObject questionObj = (JSONObject) session.get(CONTINUOUS_QUESTION);
            JSONArray questionList = (JSONArray) questionObj.get("items");

            JSONObject question = (JSONObject) questionList.get(0);
            questionList.remove(0);

            if (questionList.isEmpty()) {
                session.remove(CONTINUOUS_QUESTION); // There are no more questions left to ask.
            }

            String questionText = question.get("name").toString();
            String questionId = question.get("id").toString();

            session.put(JUST_ASKED_ID, questionId);

            return handlerInput.getResponseBuilder()
                    .withSpeech(questionText)
                    .build();
        }

        Intent beginIntent = Intent.builder().withName("BeginDiagnosisIntent").build();
        return handlerInput.getResponseBuilder()
                .addDelegateDirective(beginIntent)
                .build();
        // Search for a question to ask
        // If there is a question, ask the question. Remove the question from local storage.
        // If there is no question, move back to beginDiagnosisIntentHandler
    }
}
