package edu.kh.fin.common.schedule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.fin.board.model.service.BoardService;

@Component // bean 등록
public class ImageDeleteScheduler {

	// DB에 있는 Attachment 테이블의 데이터와
	// 서버에 저장된 file의 정보가 불일치하는 파일을 삭제
	
	// ex) 서버  :   A  B  C  D
	//     DB    :   A  B
	//     --> 서버에 C,D 삭제
	
	@Autowired
	private BoardService service;
	
	@Autowired
	private ServletContext servletContext; // 서버 경로를 얻어오기 위한 객체
	
	@Scheduled(cron = "0 0 0 * * *") // 매일 0시 마다 실행
	//@Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
	public void deleteImage() {
		
		// 자유게시판 이미지가 저장된 폴더 경로 얻어오기
		String savePath = servletContext.getRealPath("/resources/images/freeboard");
		
		// savePath 경로에 있는 파일 리스트 얻어오기
		File[] serverFileList = new File(savePath).listFiles();
		
		
		// * 현재 시간으로 부터 72시간 보다 전에 생성된 파일 목록만 추려내기
		Date threeDaysAgo = new Date( System.currentTimeMillis() - ( 3 * 24 * 60 * 60 * 1000 )  );
		// java.util.Date
		
		// 20210630151349_96049
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		String standard = sdf.format(threeDaysAgo); // 72시간 전 시간이 yyyyMMddHH 형식의 문자열로 저장됨
		
		// 72시간보다 더 이전에 생성된 파일을 저장할 List 생성
		List<File> list = new ArrayList<File>();
		
		
		System.out.println(standard);
		
		for(File f : serverFileList) {
					// 폴더에 저장된 모든 이미지 파일 목록
			
			// 경로를 제거해서 파일 이름만 저장하기
			// String fileName = f.toString().substring( f.toString().lastIndexOf("\\") + 1  );
			// String.substring(index) : 문자열을 index부터 끝 까지 부분만을 잘라내어 반환
			// String.lastIndexOf(str) : 문자열을 뒤에서 부터 검색하여 str가 일치하는 부분의 index 반환
			
			String fileName = f.getName();
			
			//System.out.println(fileName);
			
			//  파일 : 2021073116              
			//  기준 : 2021080110
			
			// A.compareTo(B) 
			// B가 더 큰 경우 == -1 반환
			// A와 B가 같은 경우 == 0 반환
			// A가 더 큰 경우 == 1 반환
			if(fileName.substring(0, 10).compareTo(standard) < 0 ) {
				// 파일명과 기준을 비교했을 때
				// 파일명에 작성된 시간이 72시간 보다 더 과거일 때
				
				list.add(f);
			}
		}
		
		// for문 종료 후 list에는 72시간 보다 과거에 생성된 파일만 담겨있음.
		
		
		// DB에서 72시간 보다 과거에 추가된 파일명을 조회
		List<String> dbList = service.selectDBList(standard);
		
		System.out.println("-----------------------------------------");
		/*for(String str : dbList){
			System.out.println(str);
		}*/
		
		// list : 72시간 보다 과거에 추가된 서버 파일 목록
		// dbList : 72시간 보다 과거에 추가된 DB 파일명 목록
		
		if( !list.isEmpty() && !dbList.isEmpty() ) { // 두 list에 데이터가 있을 경우
			
			for(File f : list) {
				
				if( dbList.indexOf( f.getName() ) < 0  ) {
					// DB에는 없는데 Server에는 파일이 존재하는 경우
					
					// List.indexOf("객체") : List 내부에 "객체"와 일치하는 객체가 존재하면
					//                        해당 인덱스를 반환, 없으면 -1 반환
					
					System.out.println(f.getName() + " 삭제");
					f.delete(); // 실제 파일 삭제
				}
			}
		}
		
	}
	
	
	
	
	
	
	
}

