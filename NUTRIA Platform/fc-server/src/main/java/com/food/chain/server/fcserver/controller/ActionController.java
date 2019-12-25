package com.food.chain.server.fcserver.controller;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.constant.OperationType;
import com.food.chain.server.fcserver.domain.Action;
import com.food.chain.server.fcserver.service.ActionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.food.chain.server.fcserver.response.ResponseBodyGenerator.generateSuccessResponceBody;

@RestController
@RequestMapping(value = "/api/v1/actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping
    public ResponseEntity<List<Action>> getAllActions() {
        return ResponseEntity.ok().body(actionService.getAllActions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Action> getAction(@PathVariable String id) {
        return ResponseEntity.ok().body(actionService.getAction(Long.valueOf(id)));
    }

    @PostMapping
    public ResponseEntity<?> addAction(@RequestBody Action action) {
        actionService.createAction(action);
        return generateSuccessResponceBody(FCConst.ACTION, action.getActionId(), OperationType.CREATE, HttpStatus.OK, "");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAction(@PathVariable String id, @RequestBody Action action) {
        action.setActionId(Long.valueOf(id));
        actionService.updateAction(action);
        return generateSuccessResponceBody(FCConst.ACTION, action.getActionId(), OperationType.UPDATE, HttpStatus.OK, "");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAction(@PathVariable String id) {
        actionService.deleteAction(Long.valueOf(id));
        return generateSuccessResponceBody(FCConst.ACTION, Long.valueOf(id), OperationType.DELETE, HttpStatus.OK, "");
    }
}