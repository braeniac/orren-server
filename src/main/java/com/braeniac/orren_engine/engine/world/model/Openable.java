package com.braeniac.orren_engine.engine.world.model;

//capability for objects that can be opened or closed.

//OpenHandler should not assume every object can be opened.

// instead of checking for object if name contains "door" or "chest"
// we can check for behaviour: is the object openable??

public interface Openable {

    boolean isOpen();
    void open();
    void close();

}
