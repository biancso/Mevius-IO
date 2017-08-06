package biancso.mevius.handler.ramidzkh;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks the method of being able to listen for packets
 * 
 * @author ramidzkh
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface PacketListener {

}
