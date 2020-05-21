package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.hlev1.alexaDiagnose.utils.SessionStorage.CONTINUOUS_QUESTION;
import static com.hlev1.alexaDiagnose.utils.SessionStorage.EVIDENCE;

public class MultipleChoiceIntentHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("MultipleChoiceIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Map session = handlerInput.getAttributesManager().getSessionAttributes();
        ArrayList evidence = (ArrayList) session.getOrDefault(EVIDENCE, new ArrayList());

        Slot slot = intentRequest.getIntent().getSlots().get("answer");
        ArrayList question = (ArrayList) session.get(CONTINUOUS_QUESTION);
        char answer = slot.getValue().toLowerCase().charAt(0);
        int i = answer - 'a';

        LinkedHashMap newEvidence = (LinkedHashMap) question.get(i);
        newEvidence.remove("name");
        if (newEvidence.containsKey("explanation")) { newEvidence.remove("name"); }

        evidence.add(newEvidence);
        // if the evidence.size() is greater than 1, that means that there was previous evidence saved.
        // And so we do not need to put it back into the session because it has already been updated.
        if (evidence.size() <= 1) {
            session.put(EVIDENCE, evidence);
        }

        BeginDiagnosisIntentHandler switchIntent = new BeginDiagnosisIntentHandler();
        return switchIntent.handle(handlerInput, intentRequest);
    }
}
