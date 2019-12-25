package com.food.chain.server.fcserver.repository;


import com.food.chain.server.fcserver.domain.Action;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActionRepository extends CrudRepository<Action, Long> {
    Optional<Action> findActionByActionName(String actionName);

    Optional<Action> findActionByActionNameAndActionIdNot(String actionName, Long actionId);
}
