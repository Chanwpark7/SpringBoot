package com.fullstack.springboot.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;

//리스트에서 요청되는 페이지의 정보를 담고 있는 DTO
//이 DTO는 요청페이지에 따라서 page 번호를 요청할 수 있도록 정의함.
@Builder
@AllArgsConstructor
public class PageRequestDTO {

	private int page;
	private int size;//글 목록 수
	
	public PageRequestDTO() {
		//기본값 초기화
		this.page = 1;
		this.size = 10;
	}
	
	public Pageable getPageable(Sort sort) {
		return PageRequest.of(page-1, size, sort);
	}
}
