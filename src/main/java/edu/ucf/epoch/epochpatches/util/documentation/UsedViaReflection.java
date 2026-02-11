package edu.ucf.epoch.epochpatches.util.documentation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Self-explanatory. Used to suppress the "unused" warning on methods invoked via alternate means, like reflection or bytecode manipulation.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface UsedViaReflection {
}
