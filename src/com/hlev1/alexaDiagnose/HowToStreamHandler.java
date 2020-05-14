package com.hlev1.alexaDiagnose;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.hlev1.alexaDiagnose.handlers.ErrorHandler;
import com.hlev1.alexaDiagnose.handlers.ExitIntentHandler;
import com.hlev1.alexaDiagnose.handlers.HelpIntentHandler;
import com.hlev1.alexaDiagnose.handlers.LaunchHandler;
import com.hlev1.alexaDiagnose.handlers.RecipeIntentHandler;
import com.hlev1.alexaDiagnose.handlers.RepeatIntentHandler;
import com.hlev1.alexaDiagnose.handlers.SessionEndedHandler;

public class HowToStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new LaunchHandler(),
                        new HelpIntentHandler(),
                        new RecipeIntentHandler(),
                        new RepeatIntentHandler(),
                        new ExitIntentHandler(),
                        new ErrorHandler(),
                        new SessionEndedHandler()
                )
                // Add your skill id below
                // .withSkillId("")
                .build();
    }

    public HowToStreamHandler() {
        super(getSkill());
    }
}
