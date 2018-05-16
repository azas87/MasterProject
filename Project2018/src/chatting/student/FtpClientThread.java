package chatting.student;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;

import chatting.data.Data;



public class FtpClientThread extends JFrame implements Runnable{
	
	private DataInputStream dis;
	private DataOutputStream dos;
	private FileInputStream fis;
	private FileOutputStream fos;
	private BufferedInputStream bisFile;
	private BufferedOutputStream bosFile;
	private String uufName;
	private String SourceFielPath;
	private int mode;
	private String msg;
	public static boolean isCancel;
	private File file;
	private Data data;
	private String s [];	
	
	public FtpClientThread(DataInputStream dis, DataOutputStream dos, int mode, String SourceFielPath) {
		this.dis = dis;
		this.dos = dos;
		this.mode = mode;
		this.SourceFielPath = SourceFielPath;
	}
	
	public void run()
	{
		if(mode==Data.FILE_DOWN)
		{	
			try 
			{
				dos.writeInt(Data.FILE_DOWN);
				s= SourceFielPath.split("\\|");
				System.out.println(s.length);
				System.out.println(s[0]);
				System.out.println(s[1]);
				//s[0] = ���� ��� s[1] = �����ϰ� ���� ��ġ
				dos.writeUTF(s[0]);
				if(dis.readUTF().equals("Y"))
				{
					//���α׷����� �����
					fileDown();
				}	
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			
		}	
	}
	
	public void fileDown() 
	{
		try 
		{
			bosFile = new BufferedOutputStream(new FileOutputStream(s[1]));
			
			byte[] b = new byte[4096];
			int c = 0;
			while( (c=dis.read(b)) != -1 )
				bosFile.write(b, 0, c);
			
			bosFile.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		
	}
	
	public void fileUp() {
		try {
			bisFile = new BufferedInputStream(new FileInputStream(file));
			
			byte[] b = new byte[4096];
			int c = 0;
			while( (c=bisFile.read(b)) != -1 )
				dos.write(b, 0, c);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		
	}
	
/*	@Override
	public void run() {
		try 
		{
			if(mode == 0)
			{
				dos.writeUTF("STC");
				dos.flush();
				dos.writeUTF(SourceFielPath);
				dos.flush();
				
				msg = dis.readUTF();
				if(msg.equals("YES"))
				{
					Long size = Long.parseLong(dis.readUTF());
					String[] directoryName = SourceFielPath.split("\\\\");
					System.out.println(directoryName[directoryName.length -1]);
					uufName = directoryName[directoryName.length -1];
					
					JFileChooser save = new JFileChooser();
					save.setSelectedFile(new File(uufName));
					
					// ������ selectfile�� null ����������.. ���� ���ϸ� ������ setSelectedFile �ؼ� ������ �� ��
					if(save.showSaveDialog(this) == JFileChooser.APPROVE_OPTION )
					{
						File selectFile = save.getSelectedFile();
						file = new File(selectFile.getAbsolutePath());

						file = save.getSelectedFile();
						
						fos = new FileOutputStream(file);
						System.out.println("Sever to Client. Start");
						dos.writeUTF("START");
						isCancel = false;
						//ChattingMain.progressbar.setValue(0);
						byte[] buf = new byte[4096];
						long total = 0;
						int read = 0, per = 0;
						while((read = dis.read(buf)) != -1)
						{
							total += read;
							per = (int)(total / (size/100L));
							//ChattingMain.progressbar.setValue(per);
							//ChattingMain.lbl_per.setText(per+"%");
							
							fos.write(buf, 0, read);
							fos.flush();
							
							if(isCancel) break;
						}
						
						if(isCancel)
						{
							System.out.println("�ٿ�ε尡 ��� �Ǿ����ϴ�.");
						}
						else
						{
							System.out.println("[System] �ٿ�ε带 �Ϸ��߽��ϴ�.");
						}
					}
					else
					{
						dos.writeUTF("GIVE UP");
						System.out.println("�����θ� �������� �ʰ� ���� ��.");
					}
				}
				else
				{
					System.out.println("������ �ش� ������ ������ �������� ����. Ʈ�� ���� �ʿ�.");
				}
			}
			else
			{
				System.out.println(SourceFielPath);
				String paths[] = SourceFielPath.split("\\|");
					
				file= new File(paths[1]);
				fis = new FileInputStream(file);
				
				dos.writeUTF("CTS");
				
				dos.writeUTF(paths[0]+"\\"+file.getName());
				
				msg = dis.readUTF();
				if(msg.equals("YES"))
				{
					
					byte[] buf = new byte[4096];
					int read = 0;
					long size = file.length();
					BufferedInputStream bis = new BufferedInputStream(fis);
					long total = 0;
					int per = 0;
					isCancel = false;
					//ChattingMain.progressbar.setValue(0);
					while((read = bis.read(buf)) != -1)
					{
						total += read;
						per = (int)(total / (size/100L));
						//ChattingMain.progressbar.setValue(per);
						//ChattingMain.lbl_per.setText(per+"%");
						dos.write(buf, 0, read);
						if(isCancel) break;
					}
					dos.flush();
					System.out.println("���� ���� �Ϸ�");
				}
				else
				{
					//�뷮 �����̳� ���� �̸��̳� ��Ÿ ������ ���� ����
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally 
		{
			
			//ChattingMain.progressbar.setValue(0);
			//ChattingMain.lbl_per.setText(0+"%");
			
			closeAll();
			
			if(isCancel)
			{
				if(file.exists())
				{
					file.delete();
				}
			}
		}
		System.out.println("ftpclient ������ ����");
	}
	*/
	public void closeAll(){
		System.out.println("��� �ڿ� ����");
		try { if(bosFile != null) {bosFile.close(); }} catch (IOException e) {}
		try { if(bisFile != null) {bisFile.close(); }} catch (IOException e) {}
		try { if(dis != null) {dis.close();}} catch (IOException e) {}
		try { if(dos != null) {dos.close(); }} catch (IOException e) {}
	}
}
