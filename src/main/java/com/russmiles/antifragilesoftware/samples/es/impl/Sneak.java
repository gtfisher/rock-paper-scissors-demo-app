package com.russmiles.antifragilesoftware.samples.es.impl;

/**
 * Created by gtarrant-fisher on 12/05/2016.
 */
// // TODO: 12/05/2016 get rid of this? 
public class Sneak {
    public static RuntimeException sneakyThrow(Throwable t) {
        if (t == null) throw new NullPointerException("t");
        Sneak.<RuntimeException>sneakyThrow0(t);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void sneakyThrow0(Throwable t) throws T {
        throw (T) t;
    }
}
