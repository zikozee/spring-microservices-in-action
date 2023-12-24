package com.optimagrowth.organization.events.model;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 19 Dec, 2023
 */

public record OrganizationChangeModel(String type, String action, String organizationId, String correlationId) {
}
