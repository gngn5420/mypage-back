package org.kdh.mypage.service;

import org.kdh.mypage.domain.TodoList;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.dto.TodoListDTO;

import java.util.List;

public interface TodoListService {
  TodoListDTO saveTodoList(TodoListDTO todoListDTO);
  void deleteTodoList(Long id);
  TodoListDTO getTodoListById(Long id);
  void updateTodoList(TodoListDTO todoListDTO);
  List<TodoListDTO> findAllTodoLists();

  default TodoList dtoToEntity(TodoListDTO todoListDTO) {
    TodoList todoList = TodoList.builder()
        .todoId(todoListDTO.getTodoId())
        .content(todoListDTO.getContent())
        .regDate(todoListDTO.getRegDate())
        .updateDate(todoListDTO.getUpdateDate())
        .is_complete(todoListDTO.is_complete())

        .build();
    return todoList;
  }
  default TodoListDTO EntityToDTO(TodoList todoList) {
    TodoListDTO todoListDTO = TodoListDTO.builder()
        .todoId()

        .build();
  }
}
