package kh.spring.grougle.chat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import kh.spring.grougle.chat.domain.ChatMessage;
import kh.spring.grougle.chat.model.service.ChatServiceImpl;
import kh.spring.grougle.employee.domain.Employee;

@Controller
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	private ChatServiceImpl service;
//	${loginSsInfo.emp_id}
	// 채팅방 목록 조회
	@GetMapping("chatlist")
	@ResponseBody
	public String chatList(
			@RequestParam(value = "rm_id", defaultValue = "0") String rm_id,
			HttpSession ss) {
		System.out.println("사용자 아이디이이"+ss.getAttribute("loginSsInfo"));
		// 세션에서 아이디
		Employee empInfo = (Employee)ss.getAttribute("loginSsInfo");
		String emp_no =  Integer.toString(empInfo.getEmp_no());
		// 채팅방 리스트
		List<Map<String,Object>> chatList = service.selectAllRooms(emp_no);
		
//		// TODO 채팅방 멤버 카운트
//		if(!rm_id.equals("0")) {
//			System.out.println("방번호오~~"+ rm_id);
//			String memberCount = service.memberCount(rm_id);
//			return new Gson().toJson(memberCount);
//		}
		
		return new Gson().toJson(chatList);

	}
	
	// 사원 리스트
	@GetMapping(value="emplist", produces = "application/text; charset=utf8") 
	@ResponseBody
	public String empList(HttpSession ss) {
		// 사원 리스트
		List<Map<String,Object>> empList = service.selectEmployeeList();
		return new Gson().toJson(empList);
	}
		
	// 채팅방 조회
	@GetMapping("room")
	public ModelAndView chat(ModelAndView mv,
			@RequestParam(value = "rm_id", defaultValue = "0") String rm_id,
			RedirectAttributes rttr,
			HttpSession ss) {
		if(ss.getAttribute("loginSsInfo") == null) {
			mv.setViewName("redirect:/login");
			rttr.addFlashAttribute("msg", "로그인 먼저 해주세요");
			return mv;
		}
		List<ChatMessage> chatMessages = service.selectMessage(rm_id);
		//메시지 호출
		mv.addObject("chatMessages", chatMessages);
		
		System.out.println("사용자 아이디이이"+ss.getAttribute("loginSsInfo"));
		Employee empInfo = (Employee)ss.getAttribute("loginSsInfo");
		String emp_id =  empInfo.getEmp_id();
		
		// 채팅방 클릭시
		if(!rm_id.equals("0")) {
			System.out.println("방번호오~~"+ rm_id);
			List<Map<String,Object>> chatMember = service.selectRoomMember(rm_id);
			mv.addObject("chatMember", chatMember);
		}
		mv.addObject("emp_id", emp_id);
		mv.setViewName("chat/room");
		return mv;
	}
	
}