package com.braeniac.orren_engine.engine.resolver;

import com.braeniac.orren_engine.engine.model.CommandModifier;
import com.braeniac.orren_engine.engine.model.CommandTarget;
import com.braeniac.orren_engine.engine.model.UserCommand;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolver is used to convert UserCommand (text based level) into a ResolvedCommand (world level)
 *
 * Example:
 * "Unlock the iron door with it"
 *
 * Before (UserCommand):
 *      verb = "unlock"
 *      directTarget = "iron door"
 *      modifier = ["with", "it"]
 *
 * after (ResolveCommand)
 *      verb = "unlock"
 *      directObject = WorldObject("iron door")
 *      modifierTarget = WorldObject("bronze key")
 *
 */

public class Resolver implements ReferencedResolver {

    @Override
    public ResolvedCommand resolve(UserCommand command, TurnContext turnContext) {

        WorldObject directObject = resolveTarget(command.getDirectTarget(), turnContext);

        //modifier : "with it" , "bronze key", "from chest".
        List<ResolvedModifier> resolvedModifier = new ArrayList<>();

        for (CommandModifier modifier : command.getModifierList()) {
            WorldObject modifierTarget = resolveTarget(modifier.getTarget(), turnContext);

            resolvedModifier.add(
                    new ResolvedModifier(
                            modifier.getPreposition(),
                            modifierTarget
                    )
            );
        }

        // Final object that the engine can execute against
        return new ResolvedCommand(
                command.getDomain(),
                command.getVerb(),
                directObject,
                command.getQuotedText(),
                resolvedModifier,
                command.getAdverb()
        );
    }

    private WorldObject resolveTarget(CommandTarget directTarget, TurnContext turnContext) {

      if (directTarget == null) {
        return null; 
      }
      
      if (directTarget.isPronoun()) {

      }

      return null;
    }

}
