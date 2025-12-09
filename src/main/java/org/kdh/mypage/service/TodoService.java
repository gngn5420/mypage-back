package org.kdh.mypage.service;

import org.kdh.mypage.dto.TodoDTO;

import java.util.List;

public interface TodoService {
  // 할 일 목록 리스트
  List<TodoDTO> getTodoList(String username);

  // 할 일 목록 더하기
  TodoDTO addTodo(TodoDTO dto);

  // 할 일 목록 수정
  TodoDTO updateTodoContent(Long todoId, String content, String username);

  // 할 일 목록 완료 버튼
  TodoDTO toggleTodoComplete(Long todoId, String username);

  // 할 일 목록 지우기
  void deleteTodo(Long todoId, String username);
}
