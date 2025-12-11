package org.kdh.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.dto.TodoDTO;
import org.kdh.mypage.dto.TodoUpdateDTO;
import org.kdh.mypage.repository.TodoRepository;
import org.kdh.mypage.security.UserPrincipal;
import org.kdh.mypage.service.TodoService;
import org.kdh.mypage.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
@Log4j2
public class TodoController {

  private final TodoService todoService;
  private final UserService userService;
  private final TodoRepository todoRepository;

  // todolist 가져오기
  @GetMapping("/list")
  public ResponseEntity<List<TodoDTO>> getTodos(@AuthenticationPrincipal UserPrincipal user) {
    log.info(("Authenticated user: " + user.getUsername()));
    return ResponseEntity.ok(todoService.getTodoList(user.getUsername()));
  }

  @PostMapping("/add")
  public ResponseEntity<TodoDTO> addTodo(@RequestBody TodoDTO dto,
      @AuthenticationPrincipal UserPrincipal user // @AuthenticationPrincipal로 User 정보를 받아옴
      ) {
    log.info("todo"+ dto);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증되지 않은 경우 401 상태 코드 반환
    }

    // username으로 User 객체를 찾기
    User foundUser = userService.findUserByUsername(user.getUsername());

    // dto에 username과 날짜 등 서버에서 처리할 필드들을 설정
    dto.setUsername(foundUser.getUsername());  // 인증된 사용자 정보로 username 설정
    dto.setRegDate(LocalDateTime.now());  // 현재 시간 설정
    dto.setUpdateDate(LocalDateTime.now());  // 현재 시간 설정
    dto.setComplete(false);  // 완료 상태 초기값

    // TodoDTO를 서비스에 넘겨서 저장 처리
    TodoDTO savedTodo = todoService.addTodo(dto);

    // 저장된 TodoDTO 반환
    return ResponseEntity.ok(savedTodo);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<TodoDTO> updateTodo(
      @PathVariable Long id,  // 경로에서 id를 받음
      @RequestBody TodoUpdateDTO updateDTO, // 수정된 내용
      @AuthenticationPrincipal UserPrincipal user) {

    // updateTodoContent에서 TodoDTO를 반환하므로 이를 처리
    TodoDTO updatedTodoDTO = todoService.updateTodoContent(id, updateDTO.getContent(), user.getUsername());

    return ResponseEntity.ok(updatedTodoDTO); // 수정된 TodoDTO를 반환
  }


  @PutMapping("/toggle/{id}")
  public ResponseEntity<TodoDTO> toggleComplete(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable Long id) {

    TodoDTO updatedTodo = todoService.toggleTodoComplete(id, user.getUsername());
    return ResponseEntity.ok(updatedTodo); // 변경된 todo를 반환
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteTodo(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable Long id) {

    todoService.deleteTodo(id, user.getUsername());
    return ResponseEntity.ok().build();
  }
}
