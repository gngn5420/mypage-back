package org.kdh.mypage.service;

import org.kdh.mypage.domain.Todo;
import org.kdh.mypage.dto.TodoListDTO;

import java.util.List;

public interface TodoListService {
  TodoListDTO saveTodoList(TodoListDTO todoListDTO);
  void deleteTodoList(Long id);
  TodoListDTO getTodoListById(Long id);
  void updateTodoList(TodoListDTO todoListDTO);
  List<TodoListDTO> findAllTodoLists();

  default Todo dtoToEntity(TodoListDTO todoListDTO) {
    Todo todo = Todo.builder()
        .todoId(todoListDTO.getTodoId())
        .content(todoListDTO.getContent())
        .regDate(todoListDTO.getRegDate())
        .updateDate(todoListDTO.getUpdateDate())
        .is_complete(todoListDTO.is_complete())

        .build();
    return todo;
  }
  default TodoListDTO EntityToDTO(Todo todo) {
    TodoListDTO todoListDTO = TodoListDTO.builder()
        .todoId()

        .build();
  }
}
