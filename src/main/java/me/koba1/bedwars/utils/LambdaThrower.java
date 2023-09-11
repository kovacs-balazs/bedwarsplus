package me.koba1.bedwars.utils;

import me.koba1.bedwars.Main;

import java.util.function.Supplier;

public class LambdaThrower {

    //Bárki kérdezi, én írtam eskü

    @FunctionalInterface
    public interface SupplierWithExceptions<T, E extends Exception> {
        T get() throws E;
    }

    public static <T, E extends Exception> Supplier<T> rethrowSupplier(SupplierWithExceptions<T, E> function) {
        return () -> {
            try {
                return function.get();
            } catch (Exception exception) {
                Main.sendError(exception.toString() + "IDK MIEZ");
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
        throw (E) exception;
    }
}
