package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.hlev1.alexaDiagnose.utils.SkillUtils;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class AgeIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("AgeIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        String age = "";
        final ResourceBundle messages = SkillUtils.getResourceBundle(handlerInput, "Messages");
        final Slot ageSlot = intentRequest.getIntent().getSlots().get("age");
        String repromptSpeech = messages.getString("GENDER_REPROMPT");
        String speakOutput = "";

        if (ageSlot != null) {
            age = ageSlot.getValue().toLowerCase();
            speakOutput += String.format(messages.getString("AGE_CONFIRMATION"), age);

            return handlerInput.getResponseBuilder()
                    .withSpeech(speakOutput)
                    .withReprompt(repromptSpeech)
                    .build();
        } else {

            speakOutput = messages.getString("AGE_NOT_FOUND");
            repromptSpeech = messages.getString("AGE_REPROMPT");
            return handlerInput.getResponseBuilder()
                    .withSpeech(speakOutput)
                    .withReprompt(repromptSpeech)
                    .build();
        }

    }

}
