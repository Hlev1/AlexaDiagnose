package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.hlev1.alexaDiagnose.utils.SkillUtils;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelpIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("AMAZON.HelpIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final ResourceBundle messages = SkillUtils.getResourceBundle(handlerInput, "Messages");
        final String item = SkillUtils.getResourceBundle(handlerInput, "Recipes").getKeys().nextElement();
        final String speechText = String.format(messages.getString("HELP_MESSAGE"), item);
        final String repromptText = String.format(messages.getString("HELP_REPROMPT"), item);
        final Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
        sessionAttributes.put("speakOutput", speechText);
        sessionAttributes.put("repromptSpeech", repromptText);

        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .build();
    }
}
