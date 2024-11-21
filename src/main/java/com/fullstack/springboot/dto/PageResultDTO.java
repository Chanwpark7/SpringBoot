package com.fullstack.springboot.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

/*
 * 요청된 페이지의 결과를 되돌려주는 DTO.
 * 이 클래스에서 할 일은 Page<Entity> 객체들을 DTO 로 변환해서 Collection 타입으로 리턴해주고
 * 화면 출력에 필요한 페이징 처리된 페이지 index값도 되돌려주도록함.
 * 
 * 이 클래스에서는 Paging 처리도 같이 해서 보내야 함. 1,2,3,4,5 [다음] / [이전] 11,12,13 [다음]
 * 
 * 당연히 페이징 처리 연산을 해야 함.
 * 
 * 다음은 연산 처리시 기준이 되는 내용.
 * 
 * 시작 페이지 번호, 끝 페이지 번호(이건 실제 갯수와 페이지를 연산한 페이지 여야 함. 이전/다음, 현재 페이지)
 * 
 * 연산법
 * 1. 가장 마지막 페이지부터 연산 ex)tempEnd = (int)(Math.ceil(페이지 번호 / 10.0)) * 10;
 * 	예를 들어, 1페이지의 경우엔 10이 나오고 10 페이지인 경우에도 10, 11 페이지인 경우 20이 리턴됨.
 * 	이렇게 하면 요청된 페이지에 따른 그룹의 마지막 페이지 번호가 산출됨.
 * 
 * 이때 주의해야 할 건 마지막 페이지가 항상 글의 목록수와 비례해서 떨어지지 않음.
 * ex) 마지막 페이지가 33인 경우 33/10.0 * 10 --> 40 즉 마지막 페이지가 40으로 보여짐.
 * 이런 경우 33 페이지 이후부터는 공갈 페이지가 됨.
 * 
 * Page 클래스에 보면 getTotalPages()가 있음.
 * 이 메소드를 이용해서 위 연산한 결과와 비교함.
 * 
 * totalPage = pageRes.getTotalPages();
 * end = totalPage> 계산한 결과의 마지막 페이지 값 ? 계산한 결과의 마지막을 주고 : 아니면 totalPage 값을 매핑.
 * 
 * Start Page 검증식. 마짐가 페이지에서 -9 를 해주면 됨.
 */
@Data
public class PageResultDTO<DTO, En> {

	//리턴될 항목을 담는 List 선언.
	private List<DTO> dtoList;
	
	//총 페이지 번호.
	private int totalPage;
	
	//현재 페이지 번호
	private int page;
	
	//목록의 사이즈
	private int size;
	
	//시작페이지, 끝 페이지 번호
	private int start, end;
	
	//page 객체를 통해서 이전/이후가 있는지 알 수 있음. isFirst, isLast, hasNext,hasPrev
	private boolean prev, next;
	
	//페이지 번호 목록을 담은 List
	private List<Integer> pageList;
	
	public PageResultDTO(Page<En> result, Function<En, DTO> fn){
		
		//dto 리스트를 파람으로 받은 result 에서 스트림을 통해 생성
		//page 객체에는 내부에 entity의 묶음이 있음. 따라서 이걸 추출하려면 루프나 스트림을 통해 얻어야 함.
		dtoList = result.stream().map(fn).collect(Collectors.toList());
		
		//totalPage를 얻어냄 result(페이지 객체)를 통해 얻어낼 수 있음.
		totalPage = result.getTotalPages();
		
		//페이징 처리수행하는 메소드 호출해서 결과값 get
		makePageList(result.getPageable());
	}
	
	private void makePageList(Pageable pageable) {
		this.page = pageable.getPageNumber()+1;
		this.size = pageable.getPageSize();
		
		//가상의 마지막 페이지 값 산출 10의 배수. 글 갯수와 상관없이 10 단위로 get
		int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;
		start = tempEnd-9;
		
		prev = start > 1;
		
		//실제 마지막 페이지 번호 get
		end = totalPage > tempEnd ? tempEnd : totalPage;
		
		//다음은 페이지 블락단위로 결정. 10 페이지씩 이동
		next = totalPage > tempEnd;
		
		pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
	}
}
