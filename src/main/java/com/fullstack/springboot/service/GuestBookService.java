package com.fullstack.springboot.service;

import com.fullstack.springboot.dto.GuestBookDTO;
import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResultDTO;
import com.fullstack.springboot.entity.GuestBook;

/*
 * 얘는 컨트롤러에서 오는 요청에 대한 수행(Service) 를 진행하도록 목적에 맞는 명세를 선언함.
 * 이렇게 선언하고, 이를 수행하는(Business Logic) 객체는 얘를 상속한 클래스가 정의하도록 함.
 * 이렇게 하는경우, 이 명세의 인스턴스를 주입하게 되면, 실제 이를 상속한 클래스 객체가 대입 되도록 약한 커플링으로
 * 설계를 하는 것. 즉 지금 진행하는 스프링 부트에서는 DAO 가 따로 없기 때문에
 * DB에 CRUD 를 실제 처리하는 Repository 에게 위임하고 어떤 CRUD 를 수행할지는 서비스 층에서 결정하도록 하는 것.
 */


public interface GuestBookService {

	//글쓰기 요청이 오면 처리하는 명세 선언.
	public Long register(GuestBookDTO bookDTO);
	
	//
	public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO pageRequestDTO);
	
	//default 메소드로 매퍼 정의. 아래는 dto --> entity
	default GuestBook dtoToEntity(GuestBookDTO dto) {
		GuestBook entity = GuestBook.builder()
				.gno(dto.getGno())
				.title(dto.getTitle())
				.content(dto.getContent())
				.writer(dto.getWriter())
				.build();
		return entity;
	}
	
	//아래는 entity --> dto
	default GuestBookDTO entityToDTO(GuestBook guestBook) {
		GuestBookDTO dto = GuestBookDTO.builder()
				.gno(guestBook.getGno())
				.title(guestBook.getTitle())
				.content(guestBook.getContent())
				.writer(guestBook.getWriter())
				.regdate(guestBook.getRegDate())
				.moddate(guestBook.getModDate())
				.build();
		return dto;
	}
}