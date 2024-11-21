package com.fullstack.springboot;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import com.fullstack.springboot.dto.GuestBookDTO;
import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResultDTO;
import com.fullstack.springboot.entity.GuestBook;
import com.fullstack.springboot.entity.QGuestBook;
import com.fullstack.springboot.repository.GuestBookRepository;
import com.fullstack.springboot.service.GuestBookService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import jakarta.transaction.Transactional;

@SpringBootTest
class GuestBookApplicationTests {

	@Autowired
	private GuestBookRepository guestBookRepository;
	
	@Autowired
	private GuestBookService guestBookService;
	
	@Test
//	void insertDummies() {
//		IntStream.rangeClosed(1, 300)
//			.forEach(value -> {
//				GuestBook guestBook = GuestBook.builder()
//						.title("title : "+value)
//						.content(value + " content")
//						.writer("writer"+value)
//						.build();
//				System.out.println(guestBookRepository.save(guestBook));
//			});
//	}

//	void updateTest() {
//		//여기에 글번호 300번인 애를 찾아서 제목을 바뀐 제목입니다, 내용을 바뀐 내용입니다 라고 unit test 로 코드 작성
//		Optional<GuestBook> theRow = guestBookRepository.findById(300L);
//		if(theRow.isPresent()) {
//			GuestBook guestBook =  theRow.get();
//			
//			guestBook.changeContent("또 또 바뀐 내용");
//			guestBook.changeTitle("또 또 바뀐 제목");
//			
//			System.out.println(guestBookRepository.save(guestBook));
//		}
//	}
	
	/*
	 * QQuery 사용 방법. 일반적인 엔티티를 바탕으로 바로 검색을 구현할 수 있도록 하는게 주 목적.
	 * 예를 들어, GuestBook 엔티티같은 경우, 각 필드가 있고, 그 필드에는 DB 값이 있음.
	 * 이때 필드를 직접 조회 대상으로 잡아서, 특정 값이 있는지를 메소드로(마치 String 클래스의 메소드 사용처럼) 검색할때 매우 요긴함.
	 * 
	 * 사용 순서 : BooleanBuilder(쉽게 말하면 sql 의 where 절을 담당함) 객체를 생성.
	 * 검색의 대상으로 심은 QEntity 클래스를 얻어냄. QEntity.className 으로 하게 되면 get 가능함.
	 * 다음으론 얻어낸 QEntity 클래스의 조건을 검색할 필드명에, 조건 검색할 메소드를 사용함.
	 * 이때 리턴타입은 Predicate 라고 하는 Q클래스의 내부 객체로 리턴됨.(존재 여부를 메소드로 리턴하도록 설계된 클래스, Java의 Predicate 와 유사)
	 * 이렇게 얻어낸 타입을 BooleanBuilder 의 다른 조건 결합 메소드등으로 조건을 계속 추가할 수 있음.
	 * 
	 * 만약 조건을 추가하지 않을 경우엔 리턴된 빌드를 이용해서 pageable 등을 이용해서 jpa 의 기본 메소드와 결합해서 사용가능.
	 */
	
//	void useQdsl1() {
//		Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
//		
//		BooleanBuilder booleanBuilder = new BooleanBuilder();
//		QGuestBook qGuestBook = QGuestBook.guestBook;
//		String keyword = "6";
//		
//		BooleanExpression expression = qGuestBook.content.contains(keyword);
//		
//		//검색을 추가할때는 expresssion 객체를 추가로 생성해서 받아내고 이를 builder 에 추가하는 방식으로 처리함.
//		//아래는 제목에서 검색
//		BooleanExpression expressionTitle = qGuestBook.title.contains(keyword);
//		
//		//위처럼 검색이 하나 이상인 경우, BooleanExpression 의 메소드를 통해 or/and 등의 조건으로 합침.
//		BooleanExpression allExp = expression.or(expressionTitle);
//		
//		booleanBuilder.and(allExp).and(qGuestBook.gno.lt(100L));
//		
//		//위의 builer 객체를 파라미터로 받는 findAll()(jpaRepository.findAll())을 이용해서 paging처리된 검색 결과 받아내기
//		Page<GuestBook> result = guestBookRepository.findAll(booleanBuilder, pageable);
//		
//		result.stream().forEach(t -> {
//			System.out.println(t.getGno());
//		});
//		System.out.println(result.getTotalPages());
//	}
	
	//서비스 계층의 DTO 변환 테스트
//	void testRegister() {
//		
//		GuestBookDTO dto = GuestBookDTO.builder()
//				.writer("chanw")
//				.title("노쌤")
//				.content("썰푼다")
//				.build();
//		System.out.println(guestBookService.register(dto));
//	}
	
	void testList() {
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
		
		PageResultDTO<GuestBookDTO, GuestBook> pageResultDTO =
				guestBookService.getList(pageRequestDTO);
		
		//페이징 처리 테스트
		System.out.println("이전 : " + pageResultDTO.isPrev());
		System.out.println("다음 : " + pageResultDTO.isNext());
		System.out.println("총 페이지 수 : " + pageResultDTO.getTotalPage());
		System.out.println("----------------------------");
		
		for(GuestBookDTO dto : pageResultDTO.getDtoList()) {
			System.out.println(dto);
		}
		System.out.println("페이징 처리 결과 확인============");
		pageResultDTO.getPageList().forEach(t -> System.out.println(t));
	}
}
