package com.football.Table_Football_Scorekeeper_API.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)  // needed to create an annotation. Where is it used? On fields!
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

    String columnName() default "";
    boolean isPrimaryKey() default false;
}
