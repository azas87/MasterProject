package chatting.student;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.StringTokenizer;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import chatting.data.Data;
import jdk.nashorn.internal.scripts.JO;

public class StudentChattingMain extends JFrame implements ActionListener, Runnable, MouseListener {
	// 찬주 테스트
	private JPanel contentPane;
	private JScrollPane sp_chatOutput;
	private JTextArea ta_chatOutput;
	private JPanel p_south;
	private JTextField tf_chatInput;
	private JButton btn_send;
	private JButton btn_filelist;
	private JPanel p_east;
	private JLabel lbl_filelist;
	private JScrollPane sp_userList;
	private JList li_fileList;
	private JList li_userList;
	private String id;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Data data;
	private JPanel panel;
	private JButton b_download;
	private DefaultListModel content = new DefaultListModel();
	private ArrayList<String> as = new ArrayList<String>();
	private int count;
	private JButton b_upload;
	private String str = "D:\\IT_MASTER";
	private JTree tree;
	private JButton btn_create_folder;
	private JButton btn_delete_folder;
	private JButton btn_download;
	DefaultMutableTreeNode node;
	public static JProgressBar progressbar;
	public static JLabel lbl_per;
	// private String SEVER_IP = "127.0.0.1";
	// private String SEVER_IP = "203.233.196.50";
	// private String SEVER_IP = "203.233.196.48";
	private String SEVER_IP = null; //"203.233.196.40";

	private FtpClientThread cst;
	private JButton btn_cancel;
	private boolean exit;
	private JButton btn_logs;
	private DataInputStream dis;
	private DataOutputStream dos;
	private FileInputStream fis;
	private FileOutputStream fos;
	private String file_access;
	private String file_str;
	private JButton b_filelist;
	private int fileServer_port;
	private HashMap<String, Boolean> HashMapFileList;
	private long parentFile_amount;
	private long File_amount;
	private String sendFile_path;
	private String sendFile_amount;
	private int serverPort;
	private JScrollPane sp_personlist;
	private JPanel panel_1;
	private JLabel lbl_count;
	
	/**
	 * private FtpClientThread cst; Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public StudentChattingMain(String id, String ipAddress, String portNum) {

		this.id = id;
		SEVER_IP = ipAddress;
		serverPort = Integer.parseInt(portNum);

		setTitle("SCIT(\uD559\uC0DD)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		sp_chatOutput = new JScrollPane();
		sp_chatOutput.setPreferredSize(new Dimension(220, 2));
		sp_chatOutput.setSize(new Dimension(130, 0));
		sp_chatOutput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		contentPane.add(sp_chatOutput, BorderLayout.CENTER);

		ta_chatOutput = new JTextArea();
		ta_chatOutput.setEditable(false);
		ta_chatOutput.setRows(10);
		ta_chatOutput.setColumns(40);
		sp_chatOutput.setViewportView(ta_chatOutput);

		p_south = new JPanel();
		contentPane.add(p_south, BorderLayout.SOUTH);
		p_south.setLayout(new BorderLayout(0, 0));

		tf_chatInput = new JTextField();
		tf_chatInput.addActionListener(this);
		p_south.add(tf_chatInput, BorderLayout.CENTER);
		tf_chatInput.setColumns(25);

		btn_send = new JButton("\uADD3\uB9D0");
		btn_send.addActionListener(this);
		p_south.add(btn_send, BorderLayout.EAST);

		p_east = new JPanel();
		p_east.setPreferredSize(new Dimension(220, 10));
		contentPane.add(p_east, BorderLayout.EAST);
		p_east.setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		p_east.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		lbl_filelist = new JLabel("\uD30C\uC77C\uBAA9\uB85D");
		lbl_filelist.setPreferredSize(new Dimension(110, 25));
		panel.add(lbl_filelist, BorderLayout.NORTH);
		lbl_filelist.setHorizontalAlignment(SwingConstants.CENTER);

		b_upload = new JButton("\uC5C5\uB85C\uB4DC");
		b_upload.setActionCommand("");
		b_upload.setPreferredSize(new Dimension(81, 23));
		b_upload.addActionListener(this);
		panel.add(b_upload, BorderLayout.CENTER);

		/*
		 * DefaultMutableTreeNode root = new DefaultMutableTreeNode("파일 목록"); tree = new
		 * JTree(root); tree.disable(); tree.addMouseListener(this); JScrollPane jsp =
		 * new JScrollPane(tree); p_east_left.add(jsp, BorderLayout.CENTER); b_download
		 * = new JButton("\uB2E4\uC6B4\uB85C\uB4DC");
		 * b_download.addActionListener(this); panel.add(b_download, BorderLayout.EAST);
		 */
		b_download = new JButton("\uB2E4\uC6B4\uB85C\uB4DC");
		b_download.addActionListener(this);
		panel.add(b_download, BorderLayout.EAST);

		b_filelist = new JButton("\uBAA9\uB85D");
		b_filelist.addActionListener(this);
		panel.add(b_filelist, BorderLayout.WEST);

		sp_userList = new JScrollPane();
		sp_userList.setAutoscrolls(true);
		sp_userList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp_userList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		p_east.add(sp_userList, BorderLayout.CENTER);

		li_fileList = new JList<String>();
		li_fileList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		li_fileList.addMouseListener(this);
		sp_userList.setViewportView(li_fileList);
		
		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		lbl_count = new JLabel("\uC804\uCCB4\uC778\uC6D0");
		lbl_count.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_count.setPreferredSize(new Dimension(57, 25));
		panel_1.add(lbl_count, BorderLayout.NORTH);
		
		sp_personlist = new JScrollPane();
		panel_1.add(sp_personlist);
		sp_personlist.setPreferredSize(new Dimension(220, 2));
		sp_personlist.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		li_userList = new JList();
		sp_personlist.add(li_userList);
		sp_personlist.setViewportView(li_userList);
		
		setLocationRelativeTo(null);
		
		setVisible(true);
		connectServer();		
		
		// li_userList.setListData(new String [] {id});

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				tf_chatInput.requestFocus();
			}

		});

	}

	private void connectServer() {
		Socket client = null;
		try {
			System.out.println(SEVER_IP);
			System.out.println(serverPort);
			client = new Socket(SEVER_IP, serverPort);
			oos = new ObjectOutputStream(client.getOutputStream());
			ois = new ObjectInputStream(client.getInputStream());
			data = new Data(id, "접속 확인 ", Data.CHAT_LOGIN);
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

	private void sendData(Data data) {
		try {
			oos.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		BufferedWriter out = null;
		BufferedReader in = null;

		String message = ta_chatOutput.getText();
		// data = new Data(id, message, Data.CHAT_MESSAGE);
		// sendData(data);

		StringTokenizer st = new StringTokenizer(message, "\n");

		if (source == tf_chatInput) {
			String message1 = tf_chatInput.getText();
			// ta_chatOutput.append("["+id+"]" + message1 +"\n");
			data = new Data(id, message1, Data.CHAT_MESSAGE);
			sendData(data);
			tf_chatInput.setText("");
		} else if (source == btn_send) {
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
		} else if (source == b_filelist) {
			System.out.println("btn_filelist");
			data = new Data(id, null, Data.FILE_ACCESS);
			sendData(data);
		} else if (source == b_upload) {
			data = new Data(id, null, null, Data.FILE_UP);
			sendData(data);
		} else if (source == b_download) {
			String s = file_str + "\\" + li_fileList.getSelectedValue();
			System.out.println("b_download : " + s);
			data = new Data(id, s, null, Data.FILE_DOWN);
			sendData(data);
		}

		/*
		 * else if( source == mi_exit ) { int choice =
		 * JOptionPane.showConfirmDialog(this, "정말로 종료하시겠습니까?", "종료",
		 * JOptionPane.YES_NO_OPTION);
		 * 
		 * if( choice == JOptionPane.YES_OPTION ) { data = new Data(name, "님이 종료 했습니다.",
		 * Data.CHAT_LOGOUT); sendData(data); System.exit(0); } }
		 */
		else if (source == btn_create_folder) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (node != null) {
				TreeNode[] tn = node.getPath();

				int index = tn.length;
				if (node.getChildCount() == 0) {
					index -= 2;
				} else {
					index--;
				}

				int choice = JOptionPane.showConfirmDialog(this, tn[index] + "폴더에 새로운 폴더를 생성하시겠습니까?", "종료",
						JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					String path = "";
					for (int i = 1; i <= index; i++) {
						path += tn[i] + "\\";
					}

					System.out.println(path);

					data = new Data(id, path + "test", Data.FILE_CREATE);
					sendData(data);

//					data = new Data(id, null, Data.CHAT_TREE);
//					sendData(data);
				}
			} else {
				System.out.println("폴더를 생성할 경로를 선택하세요.");
			}
		} else if (source == btn_delete_folder) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (node != null) {
				TreeNode[] tn = node.getPath();

				int index = tn.length;
				if (node.getChildCount() == 0) {
					index -= 2;
				} else {
					index--;
				}

				int choice = JOptionPane.showConfirmDialog(this, tn[index] + "폴더를 삭제 하시겠습니까?", "종료",
						JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					String path = "";
					// 여긴 마지막 노드까지 나와야 해서 길이만큼 뽑어야 함.
					for (int i = 1; i < tn.length; i++) {
						path += tn[i] + "\\";
					}

					System.out.println(path);

					data = new Data(id, path, Data.FILE_DELETE);
					sendData(data);

//					data = new Data(id, null, Data.CHAT_TREE);
//					sendData(data);
				}
			} else {
				System.out.println("폴더를 생성할 경로를 선택하세요.");
			}

		}
		/*
		 * // 트리용 else if( source == btn_download) { node = (DefaultMutableTreeNode)
		 * tree.getLastSelectedPathComponent(); if (node != null &&
		 * (node.getChildCount() == 0) ) { String path=""; TreeNode[] tn =
		 * node.getPath(); for(int i = 1; i < tn.length; i++) { path += tn[i]+"\\"; }
		 * 
		 * System.out.println(path.substring(0, path.length()-1));
		 * ftpconnect(path.substring(0, path.length()-1),0); } }
		 */
		else if (source == btn_cancel) {
			FtpClientThread.isCancel = true;
		} else if (source == btn_logs) {
			System.out.println("1111");
			data = new Data(id, null, Data.Log_ALL);
			sendData(data);
		}

	}

	public void fileName(File f, int count) {

		File[] ff = f.listFiles();

		for (File file : ff) {
			if (file.isDirectory()) {
				for (int i = 0; i < count; i++) {
					// as.add("\t");
				}
				as.add(file.getName());
				fileName(file, count + 1);

			} else {
				for (int i = 0; i < count; i++) {
					// as.add("\t");
				}
				as.add(file.getName());
			}

		}

	}

	/*
	 * else if(source == mi_exit) { int choice = JOptionPane.showConfirmDialog(this,
	 * "정말로 종료 하겠습니까?", "종료", JOptionPane.YES_NO_OPTION); if(choice ==
	 * JOptionPane.YES_OPTION)
	 * 
	 * data = new Data(id, "님이 종료했습니다.", Data.CHAT_LOGOUT); sendData(data);
	 * 
	 * { System.exit(0); } } else if(source==mi_save) { JFileChooser save = new
	 * JFileChooser(); save.showSaveDialog(this); File file =
	 * save.getSelectedFile();
	 * 
	 * try { out = new BufferedWriter(new FileWriter(file));
	 * while(st.hasMoreTokens()) { String s = st.nextToken(); out.write(s);
	 * out.newLine(); } } catch (IOException e1) { e1.printStackTrace(); } finally {
	 * try { if(out!=null) { out.close(); } } catch (IOException e1) {
	 * e1.printStackTrace(); }
	 * 
	 * }
	 * 
	 * }
	 */
	/*
	 * else if(source==mi_text) { JFileChooser save = new JFileChooser();
	 * save.showOpenDialog(this); File file = save.getSelectedFile(); try { in = new
	 * BufferedReader(new FileReader(file)); } catch (FileNotFoundException e1) {
	 * e1.printStackTrace(); } }
	 * 
	 * else if(source == btn_send) {
	 * 
	 * String targetId = (String)li_fileList.getSelectedValue(); if(targetId ==
	 * null) { JOptionPane.showConfirmDialog(this, "귓말을 보낼 대상을 선택하시오","ERROR",
	 * JOptionPane.WARNING_MESSAGE);
	 * 
	 * } else { String message1 = tf_chatInput.getText(); data = new Data(name,
	 * message1, targetId, Data.CHAT_WHISPER); sendData(data); }
	 * tf_chatInput.requestFocus(); }
	 * 
	 * 
	 * }
	 */

	@Override
	public void run() 
	{
		try {
			oos.writeObject(data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (!exit) {
			try 
			{
				data = (Data) ois.readObject();
				switch (data.getStatus()) {
				case Data.CHAT_LOGIN:
					
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
				case Data.CHAT_LOGOUT:
					ta_chatOutput.append("[" + data.getId() + "]" + data.getMessage() + "\n");
					li_userList.setListData(data.getUserList());
					lbl_count.setText("전체인원  " + data.getUserList().size() + "명" );
					break;

				case Data.CHAT_MESSAGE:
					System.out.println("Data.CHAT_MESSAGE : " + data.getId() + " " + data.getMessage());
					ta_chatOutput.append("[" + data.getId() + "]" + data.getMessage() + "\n");
					tf_chatInput.setText("");

					break;
				case Data.CHAT_WHISPER:
					ta_chatOutput.append("[" + data.getId() + "](귓말)" + data.getMessage() + "\n");
					break;
				case Data.FILE_ACCEPT:
					file_str = data.getMessage();
					HashMapFileList = data.getFileList();
					System.out.println("----------Data.FILE_ACCEPT-------------");
					/*
					 * for(int i=0; i < FileList.length; i++) {
					 * System.out.println(FileList[i].getAbsolutePath()+"    "+
					 * FileList[i].isDirectory() +"    " + FileList[i].exists()); }
					 */
					System.out.println("file_accept " + data.getMessage());
					getList(HashMapFileList, file_str);

					break;
				case Data.FILE_ACCESS:
					file_access = data.getMessage();
					System.out.println("file_acccess " + data.getMessage());
					data = new Data(id, file_access, null, Data.FILE_REQ);
					sendData(data);
					break;
				case Data.FILE_DOWN:
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
				case Data.FILE_UP:
					fileServer_port = Integer.parseInt(data.getTargetId());
					JFileChooser send = new JFileChooser();
					if (send.showOpenDialog(this) == send.APPROVE_OPTION) 
					{

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
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				exit = true;
				
			} 
		}
		closeAll();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
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
				System.out.println(file_str);
				data = new Data(id, file_str, null, Data.FILE_REQ);
				sendData(data);
			} 
			else 
			{
				// System.out.println(file_str+"\\"+li_fileList.getSelectedValue());
				System.out.println(HashMapFileList.size());
				for (Map.Entry<String, Boolean> f : HashMapFileList.entrySet()) 
				{
					System.out.println("경로경로" + file_str);
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
				/*
				 * for(int i = 0 ; i < f.length ; i++) { System.out.println("f[i].getName() : "
				 * + f[i].getAbsolutePath());
				 * System.out.println("li_fileList.getSelectedValue() : " +
				 * li_fileList.getSelectedValue());
				 * if(f[i].getName().equals(li_fileList.getSelectedValue())) {
				 * if(f[i].isDirectory()) { System.out.println("11111111"); data = new Data(id,
				 * file_str+"\\"+li_fileList.getSelectedValue(), null, Data.FILE_REQ);
				 * System.out.println("click : " + data.getMessage()); } else {
				 * System.out.println("2222222222");
				 * System.out.println(f[i].getAbsolutePath()+"    " + f[i].isDirectory() +
				 * "      " + f[i].isFile()); System.out.println("333333333"); data = new
				 * Data(id, file_str+"\\"+li_fileList.getSelectedValue(), null, Data.FILE_DOWN);
				 * } }
				 * 
				 * }
				 */
				/*
				 * if(file.isDirectory()) { file_access += "\\"+li_fileList.getSelectedValue();
				 * }
				 */

			}
			// getList();
		}

	}

	public void getList(HashMap<String, Boolean> maplist, String str_file) {

		// String [] str = file.list();
		// File [] f = file.listFiles();
		System.out.println("---------getList------------");

		/*
		 * for(int i=0; i < f.length; i++) {
		 * System.out.println(f[i].getAbsolutePath()+"    "+
		 * f[i].isDirectory()+"    ----"); }
		 */

		ArrayList<String> af = new ArrayList<String>();
		ArrayList<String> ad = new ArrayList<String>();

		for (Map.Entry<String, Boolean> f : maplist.entrySet()) {
			if (f.getValue()) {
				System.out.println("폴더");
				ad.add(f.getKey());
			} else {
				System.out.println("기타");
				af.add(f.getKey());
			}
		}
		/*
		 * for(int i = 0 ; i < f.length ; i++) {
		 * 
		 * 
		 * if(f[i].isDirectory()) { System.out.println("폴더"); ad.add(f[i].getName()); }
		 * else { System.out.println("기타"); af.add(f[i].getName()); } }
		 */
		Collections.sort(af);
		Collections.sort(ad);

		content.clear();
		if (!str_file.equals(file_access)) {
			String back = "..";
			content.addElement(back);
		}

		for (String ss : ad) {
			content.addElement(ss);
		}

		for (String stt : af) {
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
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public void ftpconnect(String path, int mode, long amount) {
		Socket ftpclient;

		try {
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

	public void closeAll() {
		System.out.println("모든 자원 종료");
		try {
			if (ois != null) {
				oos.close();
			}
		} catch (IOException e) {
		}
		try {
			if (oos != null) {
				ois.close();
			}
		} catch (IOException e) {
		}

	}

	/*
	 * private DefaultMutableTreeNode getNode(File file) { DefaultMutableTreeNode
	 * sub_root = new DefaultMutableTreeNode(file.getName());
	 * 
	 * File list[] = file.listFiles(); DefaultMutableTreeNode temp = null; //
	 * programfiles//windowsapps 같은 애들이 null 나옴. // 관리자 권한이 없어서 못 보는 폴더네... if(list
	 * == null || list.length == 0) { temp = new DefaultMutableTreeNode("");
	 * sub_root.add(temp); } else { ArrayList<File> folderList = new
	 * ArrayList<File>(); ArrayList<File> fileList = new ArrayList<File>();
	 * 
	 * for(File f : list) { if(f.isDirectory()) { folderList.add(f); } else
	 * if(f.isFile()) { fileList.add(f); } }
	 * 
	 * Collections.sort(folderList); Collections.sort(fileList);
	 * 
	 * for (int i = 0; i < folderList.size(); ++i) { temp =
	 * getNode(folderList.get(i)); sub_root.add(temp); }
	 * 
	 * File tempFile; for (int i = 0; i < fileList.size(); ++i) { tempFile =
	 * fileList.get(i) ; temp = new DefaultMutableTreeNode(tempFile.getName());
	 * sub_root.add(temp); } }
	 * 
	 * return sub_root; }
	 */

}