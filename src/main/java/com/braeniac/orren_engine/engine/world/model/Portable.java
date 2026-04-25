package com.braeniac.orren_engine.engine.world.model;

//marker capability for objects the player can take
//a player should not be able to pick up every world object physically.

//EXAMPLE
//"lantern" -> portable
//"key" -> portable
//"iron door" -> NOT portable

//TakeHandler can do a portable check just in case a player attempts to pick up something unportable

public interface Portable {
}
