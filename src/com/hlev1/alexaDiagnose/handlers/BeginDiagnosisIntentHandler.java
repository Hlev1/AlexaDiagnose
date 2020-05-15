package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.*;
import com.hlev1.alexaDiagnose.utils.SkillUtils;

import java.util.Optional;
import java.util.ResourceBundle;

public class BeginDiagnosisIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("BeginDiagnosisIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

        String dialogState = intentRequest.getDialogState().getValue().toString();

        if (dialogState.equals("COMPLETED")) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("I have all the information I need.")
                    .build();
        } else {
            Intent currentIntent = intentRequest.getIntent();
                return handlerInput.getResponseBuilder()
                        .addDelegateDirective(currentIntent)
                        .build();
        }

    }

}
