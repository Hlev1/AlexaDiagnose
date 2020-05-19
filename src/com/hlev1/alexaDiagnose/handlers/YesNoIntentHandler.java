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
        if (session.containsKey(JUST_ASKED)) {
            String qId = (String) session.remove(JUST_ASKED);
            String answer = intentRequest.getIntent().getName().equals("AMAZON.YesIntent") ? "present" : "absent";

            ArrayList evidence = (ArrayList) session.getOrDefault(EVIDENCE, new ArrayList());
            JSONObject newEvidence = new JSONObject();
            newEvidence.put("id", qId);
            newEvidence.put("choice_id", answer);

            evidence.add(newEvidence);
            // if the evidence.size() is greater than 1, that means that there was previous evidence saved.
            // And so we do not need to put it back into the session because it has already been updated.
            if (evidence.size() <= 1) {
                session.put(EVIDENCE, evidence);
            }
        }

        Object obj = null;
        try {

            // Ask another question
            obj = session.get(CONTINUOUS_QUESTION);
            if (obj.getClass() == String.class) {
                if (obj.equals(QUESTION_ANSWERED)) {
                    BeginDiagnosisIntentHandler switchIntent = new BeginDiagnosisIntentHandler();
                    return switchIntent.handle(handlerInput, intentRequest);
                }
            }

        } catch (Exception e) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("Error")
                    .withReprompt("Error")
                    .build();
        }

        ArrayList listOfQuestions = (ArrayList) obj;
        LinkedHashMap nextQuestion = (LinkedHashMap) listOfQuestions.remove(0);




        if (listOfQuestions.size() == 0) {
            session.put(CONTINUOUS_QUESTION, QUESTION_ANSWERED);
        }

        String nextQuestionText = (String) nextQuestion.get("name");
        String nextQuestionId = (String) nextQuestion.get("id");

        session.put(JUST_ASKED, nextQuestionId);


        return handlerInput.getResponseBuilder()
                .withSpeech(nextQuestionText)
                .withReprompt("Well? yes or no.")
                .build();

    }
}
