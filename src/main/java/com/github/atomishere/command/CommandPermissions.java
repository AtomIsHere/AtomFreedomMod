package com.github.atomishere.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.github.atomishere.rank.Rank;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermissions
{

    Rank level();

    SourceType source();

    boolean blockHostConsole() default false;
}
