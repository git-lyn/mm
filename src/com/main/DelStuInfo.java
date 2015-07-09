package com.main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.bean.Student;
import com.dao.StudentDao;
import com.db.DB;
import com.main.AddStudent.BackAction;

//ɾ��ѧ����Ϣ

public class DelStuInfo extends JFrame{
	StudentDao studao = new StudentDao();
	DB db = new DB();
	Connection conn = db.getConnection();
	JComboBox snocom = null;
	public void init() {
		// �������
		this.setLayout(null);
		this.setTitle("ѧ����Ϣ����ϵͳ");
		this.setSize(500, 400);
		JLabel title = new JLabel("ѧ����Ϣά��ģ��---ɾ������");
		title.setBounds(180, 20, 200, 30);
		this.add(title);
		// ѡ��ѧ��
		JLabel snoLabel = new JLabel("��ѡ��Ҫɾ��ѧ����Ϣ��ѧ��");
		snoLabel.setBounds(20, 60, 200,25);
		this.add(snoLabel);
		try {
			Vector vec = studao.getSno();
			snocom = new JComboBox(vec); // �����ݿ����ѧ����Ϣװ����������
			snocom.setBounds(30, 90, 150, 25);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.add(snocom);
		// ɾ������
		JButton delbtn = new JButton("ɾ��");
		delbtn.setBounds(100, 180, 60, 30);
		delbtn.addActionListener(new delAction());
		this.add(delbtn);
		// ����
		JButton backbtn = new JButton("����");
		backbtn.setBounds(180, 180, 60, 30);
		backbtn.addActionListener(new BackAction ());
		this.add(backbtn);
				this.setLocationRelativeTo(null); // ʹ�������
		this.setResizable(false);// �̶������С
		this.setVisible(true); // ���ô���ɼ�
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ����ر�ʱ���������
	}
	public static void main(String[] args) {
		DelteStudent delstu = new DelteStudent();
		delstu.init();

	}
	// ɾ���ļ�����
	class delAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String delsno = (String) snocom.getSelectedItem();
			boolean b = studao.delete(delsno, conn);
			if (b) {
				JOptionPane.showMessageDialog(null, "ɾ��¼��ɹ���");
			} else {
				JOptionPane.showMessageDialog(null, "ɾ��¼��ʧ�ܣ�");
			}
		}
	}
	class BackAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			 MainFrame  m=new  MainFrame ();
			 m.init();
			
		}
		
	}

}
