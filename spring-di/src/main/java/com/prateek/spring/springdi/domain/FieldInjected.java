package com.prateek.spring.springdi.domain;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * - Component is a spring annotation type
 * - Classes annotated with @Component are considered as candidates for
 * auto-detection when using annotation-based configuration and classpath
 * scanning.
 * - A component may optionally specify a logical component name via the value
 * attribute of this annotation.
 * - Naming the bean fieldInjectedComponent so it can be used as Qualifier
 * while autowiring
 */
@Profile({ "test", "qa" })
@Component("fieldInjectedComponent")
public class FieldInjected implements MyComponentInterface {

    @Override
    public void whatAmI() {
        System.out.println("I am a Field Injected Component");
    }

}
