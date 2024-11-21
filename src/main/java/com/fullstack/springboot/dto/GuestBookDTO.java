package com.fullstack.springboot.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestBookDTO {

	private Long gno;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime regdate;
	private LocalDateTime moddate;
}
