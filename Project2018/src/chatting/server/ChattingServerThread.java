package chatting.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import chatting.dao.ChattingDAO;
import chatting.data.Data;
import chatting.data.Log;

public class ChattingServerThread implements Runnable {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean exit;
	private Data data;
	private int port;
	private static HashMap<String, ObjectOutputStream> userList = new HashMap<>();
	private File path;
	private String base_Path = "D:\\IT_Master";
	private ChattingDAO dao = new ChattingDAO();
	private SimpleDateFormat date = new SimpleDateFormat("yyyy/mm/dd");
	private SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	
	public ChattingServerThread(ObjectInputStream ois, ObjectOutputStream oos, int port) {
		this.ois = ois;
		this.oos = oos;
		this.port = port;
	}

	@Override
	public void run() {
		while( !exit )
		{
			try {
				data = (Data) ois.readObject();
				int status = data.getStatus();
				String targetId;
				
				switch( status )
				{
					case Data.CHAT_LOGIN:
						userList.put(data.getId(), oos);
						Set<String> idList = userList.keySet();
						Vector<String> list = new Vector<>();
						for( String id : idList ) {
							list.add(id);
						}
						data.setUserList(list);
						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', "접속", date.format(new Date()), time.format(new Date())));
						broadCasting();
						break;
						
					case Data.CHAT_MESSAGE:
						broadCasting();
						break;
						
					case Data.CHAT_WHISPER:
						targetId = data.getTargetId();
						userList.get(targetId).writeObject(data);
						break;
						
					case Data.CHAT_LOGOUT:
						userList.remove(data.getId());
						Set<String> idList2 = userList.keySet();
						Vector<String> list2 = new Vector<>();
						for( String id : idList2 ) {
							list2.add(id);
						}
						data.setUserList(list2);
						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', "해제", date.format(new Date()), time.format(new Date())));
						broadCasting();
						break;
					
					case Data.Log_ALL:
						data.setLog(dao.listLogs());
						broadCasting();
						
					case Data.FILE_UP:
						data.setTargetId(port+"");
						targetId = data.getId();
//						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', "", date.format(new Date()), time.format(new Date())));
						// 업로드 파일 경로
						userList.get(targetId).writeObject(data);
						break;
						
					case Data.FILE_DOWN:
						path = new File(data.getMessage());
						if( path.exists() )
						{
							data.setMessage("Y" + "|" + path.length());
							data.setTargetId(port+"");
						}
						targetId = data.getId();
						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', fileNameSplit(), date.format(new Date()), time.format(new Date())));
						userList.get(targetId).writeObject(data);
						break;
						
					case Data.FILE_ACCESS:
						path = new File(base_Path);
						data.setFile(path.listFiles());
						data.setMessage(base_Path);
						broadCasting();
						break;
						
					case Data.FILE_REQ:
						path = new File(data.getMessage());
						data.setStatus(Data.FILE_ACCEPT);
						data.setFile(path.listFiles());
						broadCasting();
						//targetId = data.getId();
						//userList.get(targetId).writeObject(data);
						break;
						
					case Data.FILE_CREATE:
						path = new File(data.getMessage());
						path.mkdir();
						getParent();
						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', fileNameSplit(), date.format(new Date()), time.format(new Date())));
						broadCasting();
						break;
						
					case Data.FILE_DELETE:
						path = new File(data.getMessage());
						if( path.isDirectory() ) {
							deleteDir(path);
						}
						path.delete();
						getParent();
						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', fileNameSplit(), date.format(new Date()), time.format(new Date())));
						broadCasting();
						break;
						
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				exit = true;
			}
		} // while
		
	} // run()
	
	// 서버에 전달된 Data 객체를 접속한 모든 사용자에게 전파한다.
	public void broadCasting() {
		Collection<ObjectOutputStream> oosList = userList.values();
		
		for( ObjectOutputStream oos : oosList )
		{
			try {
				oos.writeObject(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteDir(File dir) {
		File[] rmFiles = dir.listFiles();
		
		for(int i=0; i<rmFiles.length; i++)
		{
			if( rmFiles[i].isDirectory() ) {
				deleteDir(dir);
				rmFiles[i].delete();
			}
			else
				rmFiles[i].delete();
		}
	}
	
	public void getParent() {
		File parent = path.getParentFile();
		
		data.setFile(parent.listFiles());
		data.setStatus(Data.FILE_ACCEPT);
	}
	
	public String fileNameSplit() {
		String[] str = data.getMessage().split("IT_Master");
		
		return str[1];
	}
}
