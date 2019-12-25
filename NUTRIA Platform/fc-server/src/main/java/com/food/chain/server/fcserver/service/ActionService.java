package com.food.chain.server.fcserver.service;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.domain.Action;
import com.food.chain.server.fcserver.exception.EmptyFieldExceptin;
import com.food.chain.server.fcserver.exception.ResourceAlreadyExistsException;
import com.food.chain.server.fcserver.exception.ResourceNotFoundException;
import com.food.chain.server.fcserver.repository.ActionRepository;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActionService {

    private final ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<Action> getAllActions() {
        List<Action> actions = new ArrayList<>();
        actionRepository.findAll().forEach(actions::add);
        return actions;
    }

    public Action getAction(Long id) {
        return actionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(FCConst.ACTION, FCConst.ACTION_ID, id));
    }

    public Action createAction(Action action) {
        if (Strings.isNullOrEmpty(action.getActionName()))
            throw new EmptyFieldExceptin(FCConst.ACTION, FCConst.ACTION_NAME);
        else if (actionRepository.findActionByActionName(action.getActionName()).isPresent())
            throw new ResourceAlreadyExistsException(FCConst.ACTION, FCConst.ACTION_NAME, action.getActionName());
        else
            return actionRepository.save(action);
    }

    public Action updateAction(Action action) {
        if (Strings.isNullOrEmpty(action.getActionName()))
            throw new EmptyFieldExceptin(FCConst.ACTION, FCConst.ACTION_NAME);
        if (!actionRepository.findById(action.getActionId()).isPresent())
            throw new ResourceNotFoundException(FCConst.ACTION, FCConst.ACTION_ID, action.getActionId());
        if (actionRepository.findActionByActionNameAndActionIdNot(action.getActionName(), action.getActionId()).isPresent())
            throw new ResourceAlreadyExistsException(FCConst.ACTION, FCConst.ACTION_NAME, action.getActionName());
        else
            return actionRepository.save(action);
    }

    public void deleteAction(Long id) {
        if (!actionRepository.findById(id).isPresent())
            throw new ResourceNotFoundException(FCConst.ACTION, FCConst.ACTION_ID, id);
        else
            actionRepository.deleteById(id);
    }

    public void saveProducersActions(Set<Action> producerActions, Map<String, String> errors) {
        producerActions.forEach(action -> {
            if (action.getActionId() != null) {
                if (!actionRepository.findById(action.getActionId()).isPresent()) {
                    errors.put(FCConst.ACTION, String.format("doesn't exist with id: %s", action.getActionId()));
                }
            } else {
                if (Strings.isNullOrEmpty(action.getActionName())) { // this will probably newer happen but just in case
                    errors.put(FCConst.ACTION, String.format("please provide '%s' or '%s'", FCConst.ACTION_ID, FCConst.ACTION_NAME));
                } else {
                    Optional<Action> optionalAction = actionRepository.findActionByActionName(action.getActionName());
                    if (optionalAction.isPresent()) {
                        action.setActionId(optionalAction.get().getActionId());
                    } else {
                        action.setActionId(actionRepository.save(action).getActionId());
                    }
                }
            }
        });
    }
}
