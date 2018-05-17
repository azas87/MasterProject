package chatting.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
	private String clientIp;
	
	public ChattingServerThread(ObjectInputStream ois, ObjectOutputStream oos, int port, String ClientIp) {
		this.ois = ois;
		this.oos = oos;
		this.port = port;
		this.clientIp = clientIp;
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
						
						System.out.println();
//						System.out.println("fefefe");
						//dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', "접속", date.format(new Date()), time.format(new Date())));
//						System.out.println("grgrgrg");
						insertLog("접속 "+ clientIp);
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
//						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', "해제", date.format(new Date()), time.format(new Date())));
						broadCasting();
						break;
					
					case Data.Log_ALL:
						data.setLog(dao.listLogs());
						ArrayList<Log> l = dao.listLogs();
						for( Log d : l ) {
							System.out.println(d);
						}
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
//						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', fileNameSplit(), date.format(new Date()), time.format(new Date())));
						userList.get(targetId).writeObject(data);
						break;
						
					case Data.FILE_ACCESS:
						path = new File(base_Path);
						System.out.println(base_Path);
						data.setFileList(makeFileList(base_Path));
						data.setMessage(base_Path);
						broadCasting();
						break;
						
					case Data.FILE_REQ:
						data.setStatus(Data.FILE_ACCEPT);
						data.setFileList(makeFileList(data.getMessage()));
						File test[] = path.listFiles();
						for(int i = 0; i < test.length; i++ )
						{
							System.out.println(test[i].getName()+"    "+test[i].isDirectory());
						}
						broadCasting();
						//targetId = data.getId();
						//userList.get(targetId).writeObject(data);
						break;
						
					case Data.FILE_CREATE:
						path = new File(data.getMessage());
						path.mkdir();
						getParent();
//						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', fileNameSplit(), date.format(new Date()), time.format(new Date())));
						broadCasting();
						break;
						
					case Data.FILE_DELETE:
						path = new File(data.getMessage());
						if( path.isDirectory() ) {
							deleteDir(path);
						}
						path.delete();
						getParent();
//						dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', fileNameSplit(), date.format(new Date()), time.format(new Date())));
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
	public HashMap<String, Boolean> makeFileList(String path)
	{
		HashMap<String, Boolean> list = new HashMap<String, Boolean >();
		System.out.println(path);
		
		File files = new File(path);
		for(File file : files.listFiles())
		{
			list.put(file.getName(), file.isDirectory());
		}		
		return list;
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
		
		data.setFileList(makeFileList(parent.getAbsolutePath()));
		data.setStatus(Data.FILE_ACCEPT);
	}
	
	public String fileNameSplit() {
		String[] str = data.getMessage().split("IT_Master");
		
		return str[1];
	}
	
	public void insertLog(String result) {
		dao.insertLog(new Log(data.getStatus(), dao.getStdNo(data.getId()), dao.logCount()+1, 'o', result, "18/05/17", "18/05/17"));
	}
}
