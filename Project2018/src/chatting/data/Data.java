package chatting.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JTree;

public class Data implements Serializable 
{
	private String id; 		 //����� �α��� 
	private String message;  //���� �޼���(��ȭ����, �α���/�α׾ƿ� �޼��� ��)
	private String targetId; //�Ӹ� ������ ID
	private int status; 	 //Data ��ü ó�� ����� ������ ��
	private Vector<String> userList; //������ ������ �ִ� ����� ID���
	private JTree jtree;
	private ArrayList<Log> log;
	private HashMap<String, Boolean> fileList;
	
	public static final int CHAT_LOGIN = 1;   //�α���
	public static final int CHAT_MESSAGE = 2; //�Ϲ� ��ȭ
	public static final int CHAT_WHISPER = 3; //�Ӹ� ��ȭ
	public static final int CHAT_LOGOUT = 4;  //�α׾ƿ�
//	public static final int CHAT_TREE = 5;		// �α׾ƿ�
	public static final int FILE_DELETE = 5;		// �α׾ƿ�
	public static final int FILE_CREATE = 6;		// �α׾ƿ�
	public static final int Log_ALL = 7;
	public static final int FILE_REQ = 8; //���Ͽ�û
	public static final int FILE_ACCEPT = 9; //���Ϲޱ�
	public static final int FILE_ACCESS = 10; //���Ͻ���-ó�����ϰ�θ� �ޱ�����
	public static final int FILE_DOWN = 11;
	public static final int FILE_UP=12;
	public static final int Log_Search = 13;
	
	public Data(String id, String message, String targetId, int status) 
	{
		super();
		this.id = id;
		this.message = message;
		this.targetId = targetId;
		this.status = status;
	}
	
	public JTree getJtree() {
		return jtree;
	}

	public void setJtree(JTree jtree) {
		this.jtree = jtree;
	}

	public ArrayList<Log> getLog() {
		return log;
	}

	public void setLog(ArrayList<Log> log) {
		this.log = log;
	}

	public Data(String id, String message, int status) 
	{
		super();
		this.id = id;
		this.message = message;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Vector<String> getUserList() {
		return userList;
	}

	public void setUserList(Vector<String> userList) {
		this.userList = userList;
	}

	public HashMap<String, Boolean> getFileList() {
		return fileList;
	}

	public void setFileList(HashMap<String, Boolean> fileList) {
		this.fileList = fileList;
	}
}	
