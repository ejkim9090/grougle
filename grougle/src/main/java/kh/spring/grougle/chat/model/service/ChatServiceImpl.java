package kh.spring.grougle.chat.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.spring.grougle.chat.model.dao.ChatDao;

@Service
public class ChatServiceImpl implements ChatService{
	@Autowired
	private ChatDao dao;
	
	// 채팅방 리스트
//	@Override
//	public List<Map<String, Object>> selectAllRooms(String emp_id) {
//		return dao.selectAllRooms(emp_id);
//	}
	// 채팅방 리스트
	@Override
	public List<Map<String, Object>> selectAllRooms(String emp_no) {
		return dao.selectAllRooms(emp_no);
	}
	// 채팅방 멤버
	public List<Map<String, Object>> selectRoomMember(String rm_id) {
		return dao.selectRoomMember(rm_id);
	}
	
	
	
	
}
