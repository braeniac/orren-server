package com.braeniac.orren_engine.engine.resolver;

import com.braeniac.orren_engine.engine.model.CommandModifier;
import com.braeniac.orren_engine.engine.model.CommandTarget;
import com.braeniac.orren_engine.engine.model.UserCommand;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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

      //"it" references the last object user interacted with.
      if (directTarget.isPronoun()) {
        return resolvePronoun(directTarget.getPronoun(), turnContext);
      }

      //core resolver.
      return findBestMatch(directTarget, turnContext.getAccessibleObjects());

    }


    /**
     * This is the core of the resolver
     * gien a text target from the player, pick the best matching real object from a list of candidates
     *
     * EXAMPLE
     *
     * "take bronze key"
     *
     * room/inventory:
     *  - small bronze key
     *  - iron door
     *  - old chest
     *
     *  best batches with "small bronze key"
     *
     */

    private WorldObject findBestMatch(CommandTarget directTarget, List<WorldObject> accessibleObjects) {

        // there is nothing to resolve.
        // if the player target has no main NOUN (pronoun only or malformed object)
        //          OR
        //  there are no OBJECTS to check (no visible objects in world/in inventory)
        if (directTarget.getHead() == null || accessibleObjects.isEmpty()) {
            return null;
        }

        return accessibleObjects.stream()
                //for each accessible object calculate score the represents
                //"how much this object looks like what the player asked for"
                .map(obj -> new ScoredObject(obj, score(directTarget, obj)))
                //removing accessible objects that scored 0 or below.
                .filter(scored -> scored.score > 0)
                //from what's remaining pick the one with the highest score.
                .max(Comparator.comparingInt(ScoredObject::score))
                //we just want the winning object
                //discard the score
                .map(ScoredObject::object)
                .orElse(null);
    }

    //simple wrapper that pairs the WorldObject with its score (a way of expressing confidence).
    private record ScoredObject(WorldObject object, int score) { }

    //computes how well a given WorldObject matches the players commandTarget.
    private int score(CommandTarget directTarget, WorldObject object) {

        int score = 0;

        //normalize for consistent comparison

        String head = normalize(directTarget.getHead());
        String name = normalize(object.getName());

        List<String> aliases = object.getAliases()
                .stream()
                .map(this::normalize)
                .toList();

        //matching the noun
        //if the user types "key"
        //what we're asking is "does the object seem to be a key?"

        //the perfect match
        if (name.equals(head)) {
            //name = "key"
            //head = "key"
            score += 10;
        } else if (name.contains(head)) {
            //object word contains the head word
            //name="key"
            //head="small bronze key"

            //it's strong but not exactly it.
            score += 6;
        }

        //matching the aliases
        for (String alias : aliases) {
            if (alias.equals(head)) {
                //exact alias match
                //slightly weaker than a perfect match.
                score += 8;
            } else if (alias.contains(head)) {
                //partial alias match
                //"head = "key"
                //"alias" = "bronze key"
                score += 4;
            }
        }

        //there might be multiple keys
        //bronze key and iron key
        //which key is the user referring to?
        //a descriptor like "bronze" could help narrow down the exact object.
        for (String descriptor : directTarget.getDescriptors()) {
            String normalizedDescriptor = normalize(descriptor);

            //exactly matches a descriptor (stronger)
            if (name.equals(normalizedDescriptor)) {
                score += 3;
            } else if (name.contains(normalizedDescriptor)) {
                //partial match of descriptor (weakest)
                score += 2;
            }
        }

        return score;
    }

    //return the last item user interacted with when using the pronoun "it"
    //EXAMPLE: pick up key, open the door with it. -- open the door with "KEY = IT"
    private WorldObject resolvePronoun(String pronoun, TurnContext turnContext) {
        if (pronoun == null) {
            return null;
        }

        return switch (pronoun.toLowerCase(Locale.ROOT)) {
            case "it" -> turnContext.getLastReferencedObject();
            default -> null;
        };
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.toLowerCase(java.util.Locale.ROOT).trim();
    }

}
