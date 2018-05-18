package chatting.student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;

import chatting.data.Data;
import chatting.data.Log;

import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.GridLayout;

public class TeacherChattingMain extends JFrame implements ActionListener, Runnable, MouseListener {

	private JPanel contentPane;
	private JScrollPane sp_chatOutput;
	private JTextArea ta_chatOutput;
	private JPanel p_south;
	private JTextField tf_chatInput;
	private JButton btn_send;
	private JPanel p_east;
	private JLabel lbl_count;
	private JScrollPane scrollPane_1;
	private JList li_userList;
	private String id;
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Data data;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JList li_fileList;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_9;
	private JButton b_upload;
	private JButton b_download;
	private DefaultListModel content = new DefaultListModel();
	private ArrayList<String> as = new ArrayList<String>();
	private int count;
	private JPanel panel_3;
	private JPanel panel_5;
	private JLabel lblNewLabel;
	private JButton b_log;
	private JButton b_serch;
	private JTextField log_serchbox;
	private DefaultListModel logContent = new DefaultListModel();
	
	private String str = "D:\\IT_MASTER";
	private JTree tree;
	private JButton btn_create_folder;
	private JButton btn_delete_folder;
	private JButton btn_download;
	DefaultMutableTreeNode node;
	public static JProgressBar progressbar;
	public static JLabel lbl_per;
//	private String SEVER_IP = "127.0.0.1";
//	private String SEVER_IP = "203.233.196.50";
//	private String SEVER_IP = "203.233.196.48";
	private String SEVER_IP = null; //"203.233.196.40";
	private int serverPort;
	
	private FtpClientThread cst;
	private JButton btn_cancel;
	private boolean exit;
	private JButton btn_logs;
	private DataInputStream dis;
	private DataOutputStream dos;
	private FileInputStream fis;
	private FileOutputStream fos;
	
	private JScrollPane sp_userList;
	
	private JButton b_filelist;
	private String file_access;
	private String file_str;
	private String contents [][];
	private String header[];
	private DefaultTableModel model;
	private JScrollPane scrollPane_3;
	private JTable table;
	private HashMap<String, Boolean> HashMapFileList;
	private long File_amount;
	private int fileServer_port;
	private String sendFile_path;
	private String sendFile_amount;
	private String logSearchContent;
	private JButton b_filecreate;
	private JButton b_filedelete;
	private JTabbedPane main;
	private JButton btn_NewFolder;
	private JTextField tf_folderName;
	private JFrame jp_create_folder;
	
	
	
	public TeacherChattingMain( String id, String ipAddress, String portNum) {

		this.id = id;
		this.SEVER_IP = ipAddress;
		this.serverPort = Integer.parseInt(portNum);
		setTitle("SCIT\uCC44\uD305(\uAD00\uB9AC\uC790)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 475);
		main = new JTabbedPane();
		main.setToolTipText("main");
		contentPane = new JPanel();
		main.add(contentPane);
		main.setTitleAt(0, "Main");
		main.addMouseListener(this);
		contentPane.setPreferredSize(new Dimension(20, 20));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(main);
		
		sp_chatOutput = new JScrollPane();
		sp_chatOutput.setEnabled(false);
		contentPane.add(sp_chatOutput, BorderLayout.CENTER);
		
		ta_chatOutput = new JTextArea();
		ta_chatOutput.setPreferredSize(new Dimension(24, 24));
		ta_chatOutput.setRows(10);
		ta_chatOutput.setColumns(20);
		sp_chatOutput.setViewportView(ta_chatOutput);
		
		p_south = new JPanel();
		contentPane.add(p_south, BorderLayout.SOUTH);
		p_south.setLayout(new BorderLayout(0, 0));
		
		btn_send = new JButton("\uADD3\uB9D0");
		btn_send.addActionListener(this);
		p_south.add(btn_send, BorderLayout.EAST);
		
		tf_chatInput = new JTextField();
		p_south.add(tf_chatInput, BorderLayout.CENTER);
		tf_chatInput.addActionListener(this);
		tf_chatInput.setColumns(25);
		
		p_east = new JPanel();
		contentPane.add(p_east, BorderLayout.WEST);
		p_east.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		p_east.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		lbl_count = new JLabel("\uC811\uC18D \uC778\uC6D0 : 00\uBA85");
		panel_1.add(lbl_count, BorderLayout.CENTER);
		lbl_count.setSize(new Dimension(50, 15));
		lbl_count.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl_count.setHorizontalAlignment(SwingConstants.CENTER);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setMaximumSize(new Dimension(50, 50));
		p_east.add(scrollPane_1, BorderLayout.CENTER);
		
		li_userList = new JList<String>();
		
		li_userList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		li_userList.setPreferredSize(new Dimension(100, 100));
		scrollPane_1.setViewportView(li_userList);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		li_fileList = new JList<String>();
		li_fileList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		li_fileList.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		//li_fileList.setForeground(Color.LIGHT_GRAY);
		li_fileList.addMouseListener(this);
		scrollPane.setViewportView(li_fileList);
		
		panel_9 = new JPanel();
		panel.add(panel_9, BorderLayout.NORTH);
		panel_9.setLayout(new GridLayout(2, 2, 1, 0));
		
		b_filecreate = new JButton("\uC0C8 \uD3F4\uB354");
		b_filecreate.addActionListener(this);
		
		b_upload = new JButton("\uC5C5\uB85C\uB4DC");
		b_upload.setActionCommand("\uD30C\uC77C\uC5C5\uB85C\uB4DC");
		b_upload.addActionListener(this);
		panel_9.add(b_upload);
		panel_9.add(b_filecreate);
		
		b_filelist = new JButton("\uBAA9\uB85D \uAC31\uC2E0");
		b_filelist.addActionListener(this);
		panel_9.add(b_filelist);
		
		b_download = new JButton("\uB2E4\uC6B4\uB85C\uB4DC");
		panel_9.add(b_download);
		b_download.addActionListener(this);
		
		b_filedelete = new JButton("\uC0AD\uC81C");
		panel_9.add(b_filedelete);
		b_filedelete.addActionListener(this);
		
		panel_2 = new JPanel();
		panel_2.addMouseListener(this);
		panel_2.setPreferredSize(new Dimension(100, 170));
		main.addTab("Management", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.NORTH);
		
		lblNewLabel = new JLabel("Memory");
		panel_3.add(lblNewLabel);
		
		panel_5 = new JPanel();
		panel_5.setOpaque(false);
		panel_5.setRequestFocusEnabled(false);
		
		panel_2.add(panel_5, BorderLayout.SOUTH);
		panel_5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		log_serchbox = new JTextField();
		panel_5.add(log_serchbox);
		log_serchbox.setColumns(10);
		
		b_log = new JButton("\uC804\uCCB4 \uB85C\uADF8");
		b_log.addActionListener(this);
		
		b_serch = new JButton("\uAC80\uC0C9");
		b_serch.addActionListener(this);
		panel_5.add(b_serch);
		panel_5.add(b_log);
		table = new JTable();
		table.setAutoCreateRowSorter(true);
		TableRowSorter tablesorter = new TableRowSorter(table.getModel());
		table.setRowSorter(tablesorter);
		scrollPane_3 = new JScrollPane(table);
		scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_2.add(scrollPane_3, BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
		
		setVisible(true);
		connectServer();
	
		addWindowListener(new WindowAdapter() 
		{
			public void windowOpened(WindowEvent e) 
			{
				tf_chatInput.requestFocus();
			}
			
		});
			
	}
	
	private void connectServer() {
		Socket client = null;
		try {
			client = new Socket(SEVER_IP, serverPort);
			oos = new ObjectOutputStream(client.getOutputStream());
			ois = new ObjectInputStream(client.getInputStream());
			data = new Data(id, "님이 접속했습니다.", Data.CHAT_LOGIN);
			Thread t = new Thread(this);
			t.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("접속 실패1");
		} catch (IOException e) {
			e.printStackTrace();
			// 잘못 된 주소 넣으면 하~~~~~안참 있다가 여기 수행함. UnknownHostException는 언제 타나?
			// 서버가 동작중이지 않는 경우.
			System.out.println("접속 실패2");
		}

	}
	
	private void sendData(Data data)
	{
		try 
		{
			oos.writeObject(data);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if(source == tf_chatInput)
		{	
			String message1 = tf_chatInput.getText();
			/*
			if(li_userList.getSelectedValue() != null)
			{
				data = new Data(id, message1, (String)li_userList.getSelectedValue(), Data.CHAT_WHISPER); 
			}
			else
			{	
				data = new Data(id, message1, Data.CHAT_MESSAGE);
			}*/
			data = new Data(id, message1, Data.CHAT_MESSAGE);
			sendData(data);
			tf_chatInput.setText("");
		}
		else if(source == b_filelist)
		{
			data = new Data(id, file_str, null, Data.FILE_REQ);
			sendData(data);
		}
		else if(source == b_upload)
		{
			data = new Data(id, null, null, Data.FILE_UP);
			sendData(data);
		}
		else if(source == b_download)
		{
			String s = file_str + "\\" + li_fileList.getSelectedValue();
			System.out.println("b_download : " + s);
			data = new Data(id, s, null, Data.FILE_DOWN);
			sendData(data);
		}
		else if(source == b_filedelete)
		{
			String deleteStr = file_str + "\\" + li_fileList.getSelectedValue();
			data = new Data(id, deleteStr, null, Data.FILE_DELETE);
			sendData(data);
		}	
		else if(source == b_filecreate)
		{
			createFolder();
		}
		else if( e.getSource() == btn_NewFolder )
		{
			String createStr = file_str + "\\" + tf_folderName.getText();
			System.out.println("btn_NewFolder : " + createStr);
			data = new Data(id, createStr, null, Data.FILE_CREATE);
			sendData(data);
			jp_create_folder.dispose();
		}
		/*else if(source == mi_exit)
		{
			int choice = JOptionPane.showConfirmDialog(this, "정말로 종료 하겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION)
				
			data = new Data(id, "님이 종료했습니다.", Data.CHAT_LOGOUT);
			sendData(data);
		
			{
				System.exit(0);
			}
		}	*/
		else if(source == btn_send)
		{
			System.out.println("btn_send");
			String targetId = (String)li_userList.getSelectedValue();
			if(targetId == null)
			{
				JOptionPane.showConfirmDialog(this, "귓말을 보낼 대상을 선택하시오","ERROR", JOptionPane.WARNING_MESSAGE);
				
			}	
			else
			{
				String message1 = tf_chatInput.getText();
				data = new Data(id, message1, targetId, Data.CHAT_WHISPER); /****************/
				sendData(data);
				ta_chatOutput.append("[" + data.getId() + "] (귓말) " + data.getMessage() + "\n");
			}	
			
			tf_chatInput.requestFocus();
		}
		else if(source == b_log)
		{
			data = new Data(id, null, null, Data.Log_ALL); 
			System.out.println(data.getId());
			sendData(data);
		}
		else if(source == b_serch)
		{
			logSearchContent = log_serchbox.getText();
			System.out.println("b_serch " + logSearchContent);
			data = new Data(id, logSearchContent, null, Data.Log_Search);
			sendData(data);
		}
		else if( source == btn_cancel)
		{
			FtpClientThread.isCancel = true;
		}
	}

	
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getSource() == main)
		{
			System.out.println("tab motion");
			data = new Data(id, null, null, Data.Log_ALL); 
			System.out.println(data.getId());
			sendData(data);
		}
		else
		{
			if (e.getClickCount() == 2) 
			{
				System.out.println("------------mouseClicked-----------");
				if (li_fileList.getSelectedValue().equals("..")) 
				{
					String parent = "";
					String[] path = file_str.split("\\\\"); // ***파일에서 \\는 찾을 때 \를 기호로 인식하므로 \"처럼 \\\\써야함
					/*
					 * for(int i = 0 ; i < path.length-1 ; i++) { parent += path[i] + "\\"; }
					 * file_str = parent;
					 */
					parent = path[0];
					for (int i = 1; i < path.length - 1; i++) {
						parent += ("\\" + path[i]);
					}
					file_str = parent;
	
					data = new Data(id, file_str, null, Data.FILE_REQ);
					System.out.println("click .. " + file_str);
					sendData(data);
				} 
				else 
				{
					// System.out.println(file_str+"\\"+li_fileList.getSelectedValue());
					System.out.println(HashMapFileList.size());
					for (Map.Entry<String, Boolean> f : HashMapFileList.entrySet()) 
					{
						if (f.getKey().equals(li_fileList.getSelectedValue())) 
						{
							if (f.getValue()) 
							{
								System.out.println("11111111");
								data = new Data(id, file_str + "\\" + li_fileList.getSelectedValue(), null, Data.FILE_REQ);
								System.out.println("click : " + data.getMessage());
								sendData(data);
							} 
							else 
							{
								System.out.println("2222222222");
								data = new Data(id, file_str + "\\" + li_fileList.getSelectedValue(), null, Data.FILE_DOWN);
								sendData(data);
							}
						}
					
					}
				}
					
			}
			else
			{
				System.out.println(e.getClickCount());
			}
		}
	}
/*
	
	public void fileName(File f, int count)
	{
		
		File [] ff = f.listFiles();
		
		
		for(File file : ff)
		{
			if(file.isDirectory())
			{
				for(int i = 0 ; i < count ; i++)
				{
					as.add("\t");
				}	
				as.add("+"+file.getName());
				as.add("\n");
				fileName(file,count+1);
				
			}
			else
			{
				for(int i = 0 ; i < count ; i++)
				{
					as.add("\t");
				}	
				as.add("-"+file.getName());
				as.add("\n");
			}
			
		}
		count--;
		
	}*/
	
	
	public void getList(HashMap<String, Boolean> maplist, String str_file)
	{
	
		//File [] f = ff;
		System.out.println("getList " + str_file);
		ArrayList<String> af = new ArrayList<String>();
		ArrayList<String> ad = new ArrayList<String>();
		
		for(Map.Entry<String, Boolean> f : maplist.entrySet())
		{
			if(f.getValue())
			{
				//System.out.println("폴더");
				ad.add(f.getKey());
			}
			else
			{
				//System.out.println("기타");
				af.add(f.getKey());
			}	
		}
		
		/*for(int i = 0 ; i < f.length ; i++)
		{
			if(f[i].isFile())
			{
				af.add(f[i].getName());
			}
			else
			{
				ad.add(f[i].getName());
			}	
		}*/
		Collections.sort(af);
		Collections.sort(ad);
		
		content.clear();
		
		if(!str_file.equals(file_access))
		{
			String back = "..";
			content.addElement(back);
		}
		
		for(String ss : ad)
		{
			content.addElement(ss);
		}
		
		for(String stt : af)
		{
			content.addElement(stt);
		}	
		li_fileList.setModel(content);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
	
		
	}
	
	public void ftpconnect(String path, int mode, long amount)
	{
		Socket ftpclient;
		
		try 
		{
			ftpclient = new Socket(SEVER_IP, fileServer_port);
			dos = new DataOutputStream(ftpclient.getOutputStream());
			dis = new DataInputStream(ftpclient.getInputStream());
			FtpClientThread cst = new FtpClientThread(dis, dos, mode, path, amount);
			Thread t1 = new Thread(cst);
			t1.start();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("IOException 2222");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("서버가 켜지 있지 않을 때 나왔음. IOException 1111");
		} 
	}	
	
	public void closeAll()
	{
		System.out.println("모든 자원 종료");
		try { if(ois != null) {oos.close(); }} catch (IOException e) {}
		try { if(oos != null) {ois.close();}} catch (IOException e) {}
	}

	

	@Override
	public void run() 
	{
		try {
			oos.writeObject(data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(!exit)
		{
			try 
			{
				data = (Data)ois.readObject();
				switch(data.getStatus())
				{
					case Data.CHAT_LOGIN :
						if(data.getTargetId()!=null)
						{
							System.out.println("id가 올바르지 않음");
							JOptionPane.showMessageDialog(null, "권한이 없는 사용자 입니다");
							System.exit(0);
						}
						else
						{
							
							ta_chatOutput.append("[" + data.getId() + "] " + data.getMessage() + "\n");
							li_userList.setListData(data.getUserList());
							lbl_count.setText("전체인원  " + data.getUserList().size() + "명" );
							
							data = new Data(id, null, null, Data.FILE_ACCESS);
							sendData(data);
						}
						break;
					case Data.CHAT_LOGOUT :
						ta_chatOutput.append("[" + data.getId() + "] " + data.getMessage() + "\n");
						li_userList.setListData(data.getUserList());
						lbl_count.setText("전체인원 " + data.getUserList().size() + "명");
						break;
											
				   case Data.CHAT_MESSAGE : 
					   System.out.println("Data.CHAT_MESSAGE : " + data.getId() + " " + data.getMessage());
					    ta_chatOutput.append("["+data.getId()+"] "+data.getMessage()+"\n");
					    tf_chatInput.setText("");
						break;
					case Data.CHAT_WHISPER : 
						ta_chatOutput.append("["+data.getId()+"](귓말) "+data.getMessage()+"\n");
						break;
					case Data.Log_ALL : 
						String hh [] = {"반", "이름", "관리자", "권한", "작업", "결과", "로그", "IP", "날짜", "시간"};
						header = hh;
						model = new DefaultTableModel(contents, header);
						
						ArrayList<Log> l = data.getLog();
						contents = new String[l.size()][10];
						
						for(int i = 0 ; i < l.size() ; i++)
						{
							String [] s = l.get(i).toString().split(",");
							contents[i] = s;
						}	

						model = new DefaultTableModel(contents, header);
						
						table.setModel(model);
						
						DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
						cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
						TableColumnModel columnModel = table.getColumnModel();
						for(int i = 0; i < columnModel.getColumnCount(); i++)
						{
							columnModel.getColumn(i).setCellRenderer(cellRenderer);
						}
						
						// 크기 조절.
						
						table.getColumn("반").setPreferredWidth(10);
						table.getColumn("이름").setPreferredWidth(20);
						table.getColumn("관리자").setPreferredWidth(20);
						table.getColumn("권한").setPreferredWidth(140);
						table.getColumn("작업").setPreferredWidth(40);
						table.getColumn("결과").setPreferredWidth(5);
						table.getColumn("로그").setPreferredWidth(250);
						table.getColumn("IP").setPreferredWidth(80);
						table.getColumn("날짜").setPreferredWidth(20);
						table.getColumn("시간").setPreferredWidth(20);
						
						break;
					case Data.FILE_ACCEPT : 
						file_str = data.getMessage();
						HashMapFileList = data.getFileList();
						getList(HashMapFileList, file_str);
						System.out.println("file_accept " + data.getMessage());
						break;
					case Data.FILE_ACCESS : 
						file_access = data.getMessage();
						System.out.println("file_acccess " + data.getMessage());
						data = new Data(id, file_access, null, Data.FILE_REQ);
						sendData(data);
						break;
					case Data.FILE_DOWN :
						System.out.println("FILE_DOWN : " + data.getMessage());
						String s[] = data.getMessage().split("\\|");
						// s[0] : y/n검사 s[1] : 용량
						System.out.println(file_str + "\\" + li_fileList.getSelectedValue());
						if (s[0].equals("Y")) {
							System.out.println(s[0]);
							System.out.println(s[1]);
							fileServer_port = Integer.parseInt(data.getTargetId());
							File_amount = Long.parseLong(s[1]);
							JFileChooser save = new JFileChooser();
							save.setSelectedFile(new File((String) li_fileList.getSelectedValue()));

							if (save.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) 
							{
								// System.out.println(save.getSelectedFile().toString());
								// System.out.println(parentFile_amount);
								// parentFile_amount = save.getSelectedFile().getFreeSpace();
								//if(File_amount <= parentFile_amount)
								{
									ftpconnect(file_str + "\\" + li_fileList.getSelectedValue() + "|"
											+ save.getSelectedFile().getAbsolutePath(), Data.FILE_DOWN, File_amount);
									System.out.println("FILE_DOWN : " + file_str + "\\" + li_fileList.getSelectedValue());
									System.out.println("FILE_DOWN : " + save.getSelectedFile().getAbsolutePath());
								}
							} 
							else 
							{
								
							}
						} else {
							System.out.println("파일이 없습니다.");
						}
						break;
					case Data.FILE_UP : 
						fileServer_port = Integer.parseInt(data.getTargetId());
						JFileChooser send = new JFileChooser();
						if (send.showOpenDialog(this) == send.APPROVE_OPTION) {

							sendFile_path = file_str + "\\" + send.getSelectedFile().getName();
							File file = new File(send.getSelectedFile().getPath());
							System.out.println("내파일" + send.getSelectedFile().getPath());
							sendFile_amount = String.valueOf(file.length());
							System.out.println("File_up : " + sendFile_path);
							ftpconnect(sendFile_path + "|" + sendFile_amount + "|" 
									+ send.getSelectedFile().getPath(), Data.FILE_UP, file.length());
						} else {
							System.out.println("파일 업로드 취소");
						}
						break;			
										
				}	
			} 
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				exit = true;
			}
			
		}
		closeAll();
	}
	
	public void createFolder()
	{
		jp_create_folder = new JFrame("폴더 생성");
		jp_create_folder.setPreferredSize(new Dimension(340, 65));
		jp_create_folder.setDefaultCloseOperation(EXIT_ON_CLOSE);
		jp_create_folder.setLocationRelativeTo(null);
		jp_create_folder.setResizable(false);
		jp_create_folder.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
		JLabel lbl_port = new JLabel("폴더 명");
		jp_create_folder.getContentPane().add(lbl_port);
			
		tf_folderName = new JTextField();
		jp_create_folder.getContentPane().add(tf_folderName);
		tf_folderName.setColumns(10);
			
		btn_NewFolder = new JButton("생성");
		btn_NewFolder.addActionListener(this);
		jp_create_folder.getContentPane().add(btn_NewFolder);
			
		jp_create_folder.pack();
		jp_create_folder.setVisible(true);
	}
}

