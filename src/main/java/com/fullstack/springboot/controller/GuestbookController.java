package com.fullstack.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fullstack.springboot.dto.GuestBookDTO;
import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.service.GuestBookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

	private final GuestBookService guestBookService;//서비스 객체 주입
	@GetMapping({"/",""})
	public String getMethodTest() {
		return "redirect:/guestbook/list";
	}
	
	@GetMapping("/list")
	public String list(PageRequestDTO requestDTO, Model model) {
		
		log.warn("list 페이지 요청됨 요청정보 ===>" + requestDTO.toString());
		model.addAttribute("result", guestBookService.getList(requestDTO));
		
		return "/guestbook/list";
	}
	
	@GetMapping("/register")
	public void register() {
		log.warn("등록 요청됨.");
	}
	
	@PostMapping("/registerPost")
	public String registerPost(GuestBookDTO guestBookDTO, RedirectAttributes redirectAttributes) {
		//service 를 이용해서 등록 작업
		System.out.println("요청 값--->"+guestBookDTO);
		
		//Service 객체를 통해 insert 작업 하면 새 Entity 발생. 따라서 글 번호 생성. 이 글번호를 get
		Long gno = guestBookService.register(guestBookDTO);
		
		redirectAttributes.addFlashAttribute("msg",gno);
		
		return "redirect:/guestbook/list";
	}
	
	@GetMapping({"/read","/modify"})
	//파라미터가 2개임. 글번호, 페이지번호
	public void read(@RequestParam("gno") Long gno, @ModelAttribute(value = "pageRequestDTO") PageRequestDTO pageRequestDTO, Model model) {
		
		GuestBookDTO guestBookDTO = guestBookService.read(gno);
		
		model.addAttribute("dto", guestBookDTO);
	}
	
	@PostMapping("/modify")
	public String getMethodName(GuestBookDTO guestBookDTO, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
			RedirectAttributes redirectAttributes) {
		
		guestBookService.modify(guestBookDTO);
		
		redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
		redirectAttributes.addFlashAttribute("msg", guestBookDTO.getGno()+"번 글이 수정됨");
		
		return "redirect:/guestbook/list";
	}
	
	@GetMapping("/delete")
	public String getMethodName(GuestBookDTO guestBookDTO, RedirectAttributes redirectAttributes) {
		
		guestBookService.delete(guestBookDTO);
		redirectAttributes.addFlashAttribute("msg", guestBookDTO.getGno()+"번 글이 삭제됨");
		
		return "redirect:/guestbook/list";
	}
	
}
