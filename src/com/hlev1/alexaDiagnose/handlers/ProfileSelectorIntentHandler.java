package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProfileSelectorIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("ProfileSelectorIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Slot slot = intentRequest.getIntent().getSlots().get("profile_name");
        String name = slot.getValue();
        //Map attr = handlerInput.getAttributesManager().getPersistentAttributes();

        //if (attr.containsKey(name)){
        //    return handlerInput.getResponseBuilder()
        //            .withSpeech(String.format("Welcome %s.", slot.toString()))
        //            .build();
        //} else { // chain to create a new profile with the given name
        Intent createProfile = Intent.builder().withName("CreateProfileIntent")
                .putSlotsItem("profile_name", slot)
                .build();

        return handlerInput.getResponseBuilder()
                .withSpeech(String.format("It looks like I dont have you in my records %s. Lets get you set up.", name))
                .addDelegateDirective(createProfile)
                .build();
        //}
    }

}
