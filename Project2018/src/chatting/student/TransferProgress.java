package chatting.student;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import chatting.server.LoginServerUI;
import javafx.scene.control.ProgressBar;

public class TransferProgress extends JFrame implements Runnable, ActionListener
{
	private long fileSize;
	private int per;
	private JLabel lbl_per;
	private JProgressBar jp;
	private JButton jb;
	private long totalSize;
	public TransferProgress(long fileSize)
	{
		this.fileSize = fileSize;
		setPreferredSize(new Dimension(300, 65));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("파일 전송");
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
		
		
		jp = new JProgressBar();
		getContentPane().add(jp);
		
		lbl_per = new JLabel("\uD3EC\uD2B8 \uBC88\uD638");
		getContentPane().add(lbl_per);
		
		jb = new JButton("취소");
		jb.addActionListener(this);
		getContentPane().add(jb);
		
		
		
		pack();
		setVisible(true);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long p = (fileSize/100L);
		int cnt = 0;
		while( true)
		{
			//per = (int)(FtpClientThread.totalSize / p);
			totalSize+=4096;
			per = (int)(totalSize / p);
			
			lbl_per.setText("  "+per+"%   ");
			jp.setValue(per);
			
			if( (totalSize >= fileSize) )
			{
				per = 100;
				lbl_per.setText(per+"%");
				jp.setValue(per);
				jb.setText("확인");
				break;
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		TransferProgress tp = new TransferProgress(1000000L);
		Thread t = new Thread(tp);
		t.start();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jb)
		{
			if(jb.getText().equals("취소"))
			{
				
			}
			else
			{
				
			}
			dispose();
		}
		
	}
}
