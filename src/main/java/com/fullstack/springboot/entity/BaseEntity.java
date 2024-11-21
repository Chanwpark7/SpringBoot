package com.fullstack.springboot.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/*
 * BaseEntity Bean : DB에 C, Insert, Update 시마다 등록시간이나 수정시간등을
 * 쿼리로 직접 작성해야하는 불편함을 해소시키는 Entity.
 * 
 * 이 클래스를 상속받는 모든 Entity 는 자동으로 위의 값을 DB에 등록함. 관리하기 편함.
 * 
 * 즉, 엔티티가 변경되면, 변경된 시간값을 자동으로 저장함. 이 값이 DB에 쓰이게 되는 것.
 * 사용법은 어노테이션으로 설정함.
 */

//BaseEntity로 만글기
@MappedSuperclass
//아래의 리스터를 활성화하기 위해서는 반드시 main 클래스에 @EnableJpaAuditing 을 반드시 선언해야 함.
@EntityListeners(value = {AuditingEntityListener.class})
//여기까지가 BaseEntity로 선언하는 부분.
@Getter

//이 클래스는 모든 하위 Entity 의 Insert 시간과 Update 되는 시간값을 자동으로 관리하는 엔티티로 선언합니다.
//때문에 모든 엔티티가 위 시간값을 자동관리 되려면, 이 클래스를 상속받으면 됨.
//또한 신규/수정 시간은 이 클래스가 관리하기 때문에 필드를 여기서 선언함.
//그리고 getter 를 만들어서 하위에서 필요시에 사용할 수 있도록함.
public abstract class BaseEntity {

	//아래의 객체를 자동 주입하도록 날짜 객체 @ 사용
	@CreatedDate
	@Column(name = "regDate", updatable = false)
	private LocalDateTime regDate; //신규 방명록 글의 등록 시간.
	
	@LastModifiedDate
	@Column(name = "modDate", updatable = true)
	private LocalDateTime modDate; //수정된 시간
}
