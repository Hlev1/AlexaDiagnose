package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.*;
import com.hlev1.alexaDiagnose.utils.SkillUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.*;

public class BeginDiagnosisIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("BeginDiagnosisIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        DialogState dialog = intentRequest.getDialogState();

        if (dialog.getValue().toString().equals("COMPLETED")) {

            Map slots = intentRequest.getIntent().getSlots();
            Slot ageSlot = (Slot) slots.get("age");
            Slot genderSlot = (Slot) slots.get("gender");


            String age = ageSlot.getValue();
            String gender = genderSlot.getValue();
            makeRequest(age, gender);
            return handlerInput.getResponseBuilder()
                    .withSpeech("I have all the information I need.")
                    .build();
        } else {
            Intent thisIntent = intentRequest.getIntent();
            return handlerInput.getResponseBuilder()
                    .addDelegateDirective(thisIntent)
                    .build();
        }



    }

    private void makeRequest(String age, String gender) {
        String requestUrl = "https://api.infermedica.com/covid19/diagnosis";
        try {

        } catch (Exception e) {

        }

    }

}
