package com.optimagrowth.license.resilience;

import com.optimagrowth.license.model.License;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author: Ezekiel Eromosei
 * @created: 29 June 2023
 */

public class ConditionalRetryResultPredicate implements Predicate<List<License>> {
    @Override
    public boolean test(List<License> licenses) {
        System.out.println("currently in Result predicate");
        return licenses.isEmpty() ||
                licenses.stream()
                        .anyMatch(license -> license.getOrganizationId().equalsIgnoreCase("BOOMSHAKASHAKA"));
    }
}
