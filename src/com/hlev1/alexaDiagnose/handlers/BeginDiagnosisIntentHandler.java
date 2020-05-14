package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Intent;
import com.hlev1.alexaDiagnose.utils.SkillUtils;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Optional;
import java.util.ResourceBundle;

public class BeginDiagnosisIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("BeginDiagnosisIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Intent currentIntent = intentRequest.getIntent();

        if (intentRequest.getDialogState().equals("COMPLETED")) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("I have all the information I need.")
                    .build();
        } else {
            return handlerInput.getResponseBuilder()
                    .addDelegateDirective(currentIntent)
                    .build();
        }
    }

}
