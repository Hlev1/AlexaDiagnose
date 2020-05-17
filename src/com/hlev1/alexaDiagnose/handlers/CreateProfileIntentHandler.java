package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Map;
import java.util.Optional;

/*
    {
      "name": "CreateProfileIntent",
      "slots": [
        {
          "name": "profile_name",
          "type": "AMAZON.GB_FIRST_NAME"
        },
        {
          "name": "age",
          "type": "AMAZON.NUMBER"
        },
        {
          "name": "gender",
          "type": "GENDER"
        }
      ],
      "samples": [
        "I am {age}",
        "I am {gender}"
      ]
    }


    dialog: {
        {
          "name": "CreateProfileIntent",
          "prompts": {},
          "slots": [
            {
              "name": "profile_name",
              "type": "AMAZON.GB_FIRST_NAME",
              "confirmationRequired": false,
              "elicitationRequired": true,
              "prompts": {
                "elicitation": "Elicit.Slot.1323207333291.563366041035"
              }
            },
            {
              "name": "age",
              "type": "AMAZON.NUMBER",
              "confirmationRequired": false,
              "elicitationRequired": true,
              "prompts": {
                "elicitation": "Elicit.Slot.1323207333291.854898796228"
              }
            },
            {
              "name": "gender",
              "type": "GENDER",
              "confirmationRequired": false,
              "elicitationRequired": true,
              "prompts": {
                "elicitation": "Elicit.Slot.1323207333291.563366041005"
              }
            }
          ]
        }
    }
 */

public class CreateProfileIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("CreateProfileIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

        if (intentRequest.getDialogState().getValue().equals("COMPLETED")) {
            Map slotMap = intentRequest.getIntent().getSlots();
            String profileName = ((Slot)slotMap.get("profile_name")).getValue();
            String profileGender = ((Slot)slotMap.get("gender")).getValue();
            String profileAge = ((Slot)slotMap.get("age")).getValue();
            String speechText = String.format("Welcome %s. To begin your diagnosis, please say begin diagnosis", profileName);

            Map session = handlerInput.getAttributesManager().getSessionAttributes();
            session.put("age", profileAge);
            session.put("gender", profileGender);
            handlerInput.getAttributesManager().setSessionAttributes(session);

            return handlerInput.getResponseBuilder()
                    .withSpeech(speechText)
                    .build();
        } else {
            Intent currentIntent = intentRequest.getIntent();
            return handlerInput.getResponseBuilder()
                    .addDelegateDirective(currentIntent)
                    .build();
        }
    }

    private void putAttributes(Intent intent, HandlerInput handlerInput) {
        Map persistent = handlerInput.getAttributesManager().getPersistentAttributes();
        String profileName = intent.getSlots().get("profile_name").getValue();
        String profileAge = intent.getSlots().get("age").getValue();
        String profileGender = intent.getSlots().get("gender").getValue();

        persistent.put("profile_name", profileName);
        persistent.put(String.format("%s_age", profileName), profileAge);
        persistent.put(String.format("%s_gender", profileName), profileGender);

        handlerInput.getAttributesManager().setPersistentAttributes(persistent);
        handlerInput.getAttributesManager().savePersistentAttributes();

    }

}
