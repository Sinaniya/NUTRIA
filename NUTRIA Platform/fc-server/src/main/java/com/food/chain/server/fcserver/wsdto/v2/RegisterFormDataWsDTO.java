package com.food.chain.server.fcserver.wsdto.v2;

import com.food.chain.server.fcserver.domain.Action;
import com.food.chain.server.fcserver.domain.Certificate;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFormDataWsDTO {
    List<Action> actions = new ArrayList<>();
    List<Certificate> certificates = new ArrayList<>();
}
