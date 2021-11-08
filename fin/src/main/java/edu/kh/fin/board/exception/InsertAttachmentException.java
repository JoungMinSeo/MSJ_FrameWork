package edu.kh.fin.board.exception;

// 사용자 정의 예외 : 자바에는 기본적으로 많은 예외 상황에 대한 Exception 클래스가 존재하지만
//					  구현되는 기능에 따라 없는 Exception 클래스 있음.
//					  이럴 때 사용자가 직접 만들 수 있는 Exception 클래스다.
public class InsertAttachmentException extends RuntimeException {
	// RuntimeException 상속
	// -> 해당 클래스는 반드시 예외처리를 하지 않아도 되는
	// UnChecked Exception의 최상위 부모
	
	public InsertAttachmentException() {
		super("파일 정보를 삽입하는 과정에서 문제 발생");
	}
	
}
