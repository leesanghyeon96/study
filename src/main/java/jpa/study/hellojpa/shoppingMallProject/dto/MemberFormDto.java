package jpa.study.hellojpa.shoppingMallProject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberFormDto {

    private String name;
    private String email;
    private String password;
    private String address;

}
