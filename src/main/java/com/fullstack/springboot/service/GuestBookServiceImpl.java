package com.fullstack.springboot.service;

import java.util.Optional;
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
import com.fullstack.springboot.entity.QGuestBook;
import com.fullstack.springboot.repository.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import jakarta.transaction.Transactional;
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
		
		//검색 추가해서 page 가져오기
		BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);
		
		Page<GuestBook> page = guestBookRepository.findAll(booleanBuilder, pageable);
		
		//Function 객체를 생성해서 Entity --> DTO 변환 목록을 get. 뷰로 이어짐
		Function<GuestBook, GuestBookDTO> fn = entity -> entityToDTO(entity);
		
		return new PageResultDTO<GuestBookDTO, GuestBook>(page, fn);
	}
	
	@Override
	public GuestBookDTO read(Long gno) {
		
		Optional<GuestBook> optional = guestBookRepository.findById(gno);
		GuestBookDTO dto = null;
		if(optional.isPresent()) {
			dto = entityToDTO(optional.get());
		}
		
		log.warn("specific dto!!!!! = "+dto);
		
		return dto;
	}
	
	@Override
	@Transactional
	public void modify(GuestBookDTO guestBookDTO) {
		
		Optional<GuestBook> optional = guestBookRepository.findById(guestBookDTO.getGno());

		if(optional.isPresent()) {
			GuestBookDTO guestBookDto = entityToDTO(optional.get());
			guestBookDto.setContent(guestBookDTO.getContent());
			guestBookDto.setTitle(guestBookDTO.getTitle());
			 
			guestBookRepository.save(dtoToEntity(guestBookDto));
		}
	}
	
	@Override
	public void delete(GuestBookDTO guestBookDTO) {
		
		Optional<GuestBook> optional = guestBookRepository.findById(guestBookDTO.getGno());
		
		if(optional.isPresent()) {
			guestBookRepository.delete(optional.get());
		}
	}

	private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {
		String type = pageRequestDTO.getType();
		
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		
		QGuestBook qGuestBook = QGuestBook.guestBook;
		
		String keyword = pageRequestDTO.getKeyword();
		
		BooleanExpression expression = qGuestBook.gno.gt(0L);//검색시 무조건 1번 글부터 처리하도록 설정
		
		booleanBuilder.and(expression);
		
		//keyword 값이 null 이거나 "" 이라면 검색 없음. 따라서 생성한 booleanbuilder 만 리턴.
		if(keyword=="") {
			return booleanBuilder;
		}
		
		//검색 조건 처리
		BooleanBuilder searchBooleanBuilder = new BooleanBuilder();
		
		if(type.equalsIgnoreCase("t")) {//title 기준 검색
			searchBooleanBuilder.or(qGuestBook.title.contains(keyword));
		}
		if(type.equalsIgnoreCase("c")) {//content 기준 검색
			searchBooleanBuilder.or(qGuestBook.content.contains(keyword));
		}
		if(type.equalsIgnoreCase("w")) {//writer 기준 검색
			searchBooleanBuilder.or(qGuestBook.writer.contains(keyword));
		}
		if(type.equalsIgnoreCase("tc")) {//writer 기준 검색
			searchBooleanBuilder.or(qGuestBook.title.contains(keyword)).or(qGuestBook.content.contains(keyword));
		}
		if(type.equalsIgnoreCase("tw")) {//writer 기준 검색
			searchBooleanBuilder.or(qGuestBook.title.contains(keyword)).or(qGuestBook.writer.contains(keyword));
		}
		if(type.equalsIgnoreCase("cw")) {//writer 기준 검색
			searchBooleanBuilder.or(qGuestBook.content.contains(keyword)).or(qGuestBook.writer.contains(keyword));
		}
		if(type.equalsIgnoreCase("tcw")) {//writer 기준 검색
			searchBooleanBuilder.or(qGuestBook.title.contains(keyword)).or(qGuestBook.content.contains(keyword)).or(qGuestBook.writer.contains(keyword));
		}
		//조건 통합
		
		booleanBuilder.and(searchBooleanBuilder);
		
		return booleanBuilder;
	}
}
