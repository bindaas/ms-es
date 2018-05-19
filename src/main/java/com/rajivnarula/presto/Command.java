package com.rajivnarula.presto;

import java.util.UUID;

/**
 * All Commands will implement this interface.
 * In future- this can have more common functionality
 * 
 * */

public interface Command {
    UUID aggregateId();
}