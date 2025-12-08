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

  @Override
  public List<TodoDTO> getTodoList(String username) {
    return todoRepository.findByUserUsername(username)
        .stream()
        .map(this::entityToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public TodoDTO addTodo(String username, String content) {

//    User user = userRepository.findByUsername(username);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Todo todo = Todo.builder()
        .content(content)
        .complete(false)
        .user(user)
        .build();

    return entityToDTO(todoRepository.save(todo));
  }

  @Override
  public TodoDTO updateTodoContent(Long todoId, String content, String username) {

    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new RuntimeException("Todo not found"));

    if (!todo.getUser().getUsername().equals(username))
      throw new RuntimeException("권한이 없습니다.");

    todo.setContent(content);

    return entityToDTO(todoRepository.save(todo));
  }

  @Override
  public void toggleTodoComplete(Long todoId, String username) {

    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new RuntimeException("Todo not found"));

    if (!todo.getUser().getUsername().equals(username))
      throw new RuntimeException("권한이 없습니다.");

    todo.setComplete(!todo.isComplete());
    todoRepository.save(todo);
  }

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
