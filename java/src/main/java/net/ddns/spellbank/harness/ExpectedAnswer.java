package net.ddns.spellbank.harness;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** A verified answer for a part using a resource in the day's input directory. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExpectedAnswer.Cases.class)
public @interface ExpectedAnswer {
    String value();

    String input() default "input1";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Cases {
        ExpectedAnswer[] value();
    }
}
