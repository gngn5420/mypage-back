package org.kdh.mypage.service;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.domain.Todo;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.dto.TodoDTO;
import org.kdh.mypage.repository.TodoRepository;
import org.kdh.mypage.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final UserService userService;


   //사용자의 할 일 목록 조회
    @Override
    public List<TodoDTO> getTodoList(String username) {
      User user = userService.findUserByUsername(username);
      return todoRepository.findByUserUsername(username)
          .stream()
          .map(this::entityToDTO)
          .collect(Collectors.toList());
    }

    // todo 추가 하기
  @Override
  public TodoDTO addTodo(TodoDTO dto) {
    User user = userRepository.findByUsername(dto.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Todo todo = Todo.builder()
        .content(dto.getContent())
        .complete(false)
        .user(user)
        .regDate(dto.getRegDate())  // 클라이언트에서 받은 날짜 사용
        .updateDate(dto.getUpdateDate())  // 클라이언트에서 받은 날짜 사용
        .build();

    Todo savedTodo = todoRepository.save(todo);
    return entityToDTO(savedTodo);
  }

  @Override
  public TodoDTO updateTodoContent(Long todoId, String content, String username) {
    // Todo 엔티티를 찾음
    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new RuntimeException("Todo not found"));

    // username을 통해 User 객체를 찾음
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    // 수정된 내용 적용
    todo.setContent(content);
    todo.setUser(user); // User 객체로 할 일 수정

    // DB에 변경된 할 일 저장
    Todo updatedTodo = todoRepository.save(todo);

    // 변경된 Todo를 TodoDTO로 변환하여 반환
    return new TodoDTO(updatedTodo.getTodoId(), updatedTodo.getUser().getUsername(), updatedTodo.getContent(), updatedTodo.getRegDate(), updatedTodo.getUpdateDate(), updatedTodo.isComplete());
  }

    // 토글 체크 반환
    public TodoDTO toggleTodoComplete(Long todoId, String username) {
      Todo todo = todoRepository.findById(todoId)
          .orElseThrow(() -> new RuntimeException("Todo not found"));

      // 사용자 권한 확인: todo의 사용자와 현재 로그인한 사용자의 username 비교
      if (!todo.getUser().getUsername().equals(username)) {
        throw new RuntimeException("권한이 없습니다.");
      }

      // 완료 상태를 반전
      todo.setComplete(!todo.isComplete());

      // 변경된 todo 저장
      Todo updatedTodo = todoRepository.save(todo);

      return new TodoDTO(updatedTodo.getTodoId(), updatedTodo.getUser().getUsername(), updatedTodo.getContent(),
          updatedTodo.getRegDate(), updatedTodo.getUpdateDate(), updatedTodo.isComplete());
    }


    // todolist 삭제하기
    @Override
    public void deleteTodo(Long todoId, String username) {

      Todo todo = todoRepository.findById(todoId)
          .orElseThrow(() -> new RuntimeException("Todo not found"));

      if (!todo.getUser().getUsername().equals(username))
        throw new RuntimeException("권한이 없습니다.");

      todoRepository.delete(todo);
    }

    private TodoDTO entityToDTO(Todo todo) {
      return TodoDTO.builder()
          .todoId(todo.getTodoId())
          .username(todo.getUser().getUsername())
          .content(todo.getContent())
          .regDate(todo.getRegDate())
          .updateDate(todo.getUpdateDate())
          .complete(todo.isComplete())
          .build();
    }
  }
