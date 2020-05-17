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

/*
    {
      "name": "ProfileSelectorIntent",
      "slots": [
        {
          "name": "profile_name",
          "type": "AMAZON.GB_FIRST_NAME"
        }
      ],
      "samples": [
        "{profile_name}",
        "This is {profile_name}",
        "I am {profile_name}",
        "My name is {profile_name}",
        "You are talking to {profile_name}",
        "That would be {profile_name}"
      ]
    }
 */
public class ProfileSelectorIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("ProfileSelectorIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Slot slot = intentRequest.getIntent().getSlots().get("profile_name");
        String name = slot.getValue();
        /*
        Map persistent = handlerInput.getAttributesManager().getPersistentAttributes();

        if (persistent.containsKey(name)){

            transferFromPersistent(intentRequest.getIntent(), handlerInput, name);
            return handlerInput.getResponseBuilder()
                    .withSpeech(String.format("Welcome %s. To begin your diagnosis, please say begin diagnosis", name))
                    .build();

        } else { // chain to create a new profile with the given name
        }
         */

        Intent createProfile = Intent.builder().withName("CreateProfileIntent")
            .putSlotsItem("profile_name", slot)
            .build();

        return handlerInput.getResponseBuilder()
                .withSpeech(String.format("It looks like I dont have you in my records %s. Lets get you set up.", name))
                .addDelegateDirective(createProfile)
                .build();
    }

    private void transferFromPersistent(Intent intent, HandlerInput handlerInput, String name) {
        Map persistent = handlerInput.getAttributesManager().getPersistentAttributes();
        Map session = handlerInput.getAttributesManager().getSessionAttributes();

        String profileAge = persistent.get(String.format("%s_age", name)).toString();
        String profileGender = persistent.get(String.format("%s_gender", name)).toString();

        session.put("profile_name", name);
        session.put(String.format("%s_age", name), profileAge);
        session.put("gender", profileGender);

        handlerInput.getAttributesManager().setSessionAttributes(session);
    }

}
