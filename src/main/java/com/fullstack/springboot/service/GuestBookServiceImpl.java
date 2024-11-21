package com.fullstack.springboot.service;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.GuestBookDTO;
import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResultDTO;
import com.fullstack.springboot.entity.GuestBook;
import com.fullstack.springboot.repository.GuestBookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor//final or null 인 애들에게 바로 주입가능하도록 설계된 어노테이션
public class GuestBookServiceImpl implements GuestBookService {

	private final GuestBookRepository guestBookRepository;
	//여기서는 변환된 Entity를 repository 에서 insert 를 해주도록 함.
	
	
	@Override
	public Long register(GuestBookDTO bookDTO) {
		
		log.warn("DTO");
		log.warn(bookDTO);
		
		GuestBook entity = dtoToEntity(bookDTO);
		
		log.warn("entity = "+entity);
		
		guestBookRepository.save(entity);
		
		return entity.getGno();
	}

	//페이지에 따른 글 항목 리턴받는 메소드 정의
	@Override
	public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO pageRequestDTO) {

		Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());
		
		Page<GuestBook> page = guestBookRepository.findAll(pageable);
		
		//Function 객체를 생성해서 Entity --> DTO 변환 목록을 get. 뷰로 이어짐
		Function<GuestBook, GuestBookDTO> fn = entity -> entityToDTO(entity);
		
		return new PageResultDTO<GuestBookDTO, GuestBook>(page, fn);
	}
}
