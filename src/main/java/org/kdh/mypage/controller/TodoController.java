package org.kdh.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.dto.TodoDTO;
import org.kdh.mypage.security.UserPrincipal;
import org.kdh.mypage.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

  private final TodoService todoService;

  @GetMapping("/list")
  public ResponseEntity<List<TodoDTO>> getTodos(@AuthenticationPrincipal UserPrincipal user) {
    return ResponseEntity.ok(todoService.getTodoList(user.getUsername()));
  }

  @PostMapping("/add")
  public ResponseEntity<TodoDTO> addTodo(
      @AuthenticationPrincipal UserPrincipal user,
      @RequestBody TodoDTO dto) {

    return ResponseEntity.ok(
        todoService.addTodo(user.getUsername(), dto.getContent())
    );
  }

  @PutMapping("/{id}")
  public ResponseEntity<TodoDTO> updateTodo(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable Long id,
      @RequestBody TodoDTO dto) {

    return ResponseEntity.ok(
        todoService.updateTodoContent(id, dto.getContent(), user.getUsername())
    );
  }

  @PutMapping("/toggle/{id}")
  public ResponseEntity<Void> toggleComplete(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable Long id) {

    todoService.toggleTodoComplete(id, user.getUsername());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTodo(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable Long id) {

    todoService.deleteTodo(id, user.getUsername());
    return ResponseEntity.ok().build();
  }
}
