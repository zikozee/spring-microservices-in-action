package com.optimagrowth.license.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

/**
 * @author: Ezekiel Eromosei
 * @created: 19 June 2023
 */

@Data
public class Organization extends RepresentationModel<Organization> {

    String id;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;

}
