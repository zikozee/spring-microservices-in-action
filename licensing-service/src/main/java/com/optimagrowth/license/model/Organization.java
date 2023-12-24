package com.optimagrowth.license.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

/**
 * @author: Ezekiel Eromosei
 * @created: 19 June 2023
 */

@Getter @Setter @ToString
public class Organization extends RepresentationModel<Organization> implements Serializable {

    String id;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;

}
