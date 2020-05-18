package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.hlev1.alexaDiagnose.utils.SessionStorage.*;

public class YesNoIntentHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("AMAZON.YesIntent") ||
                intentRequest.getIntent().getName().equals("AMAZON.NoIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Map session = handlerInput.getAttributesManager().getSessionAttributes();

        // If the session storage contains a just asked question id. This intent handler has
        // been caught because the user has replied to a question. We must record the answer
        // to the question and check to see if we need to ask another question.

        // Ask another question
        ArrayList listOfQuestions = (ArrayList) session.get(CONTINUOUS_QUESTION);

        if (listOfQuestions.size() == 0) {
            session.remove(CONTINUOUS_QUESTION);
            BeginDiagnosisIntentHandler switchIntent = new BeginDiagnosisIntentHandler();
            return switchIntent.handle(handlerInput, intentRequest);
        }

        LinkedHashMap nextQuestion = (LinkedHashMap) listOfQuestions.remove(0);

        String nextQuestionText = (String) nextQuestion.get("name");
        String nextQuestionId = (String) nextQuestion.get("id");
        session.put(JUST_ASKED, nextQuestionId);

        return handlerInput.getResponseBuilder()
                .withSpeech(nextQuestionText)
                .build();

    }
}
