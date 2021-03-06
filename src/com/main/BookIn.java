package com.main;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Container;
import java.util.*;
import java.sql.*;
import javax.swing.text.JTextComponent;

 //extends JFrame

public class BookIn implements  ActionListener {
	JFrame f3;
	Container cp;
	JPanel jp1,jp2,jp3,jp4,jp,jpanelWest;
	JButton  jbt1,jbt2;//按钮:确定、取消、
	JLabel label;				//标签
	JTextField tf1,tf2,tf3,tf4,tf5,tf6;    //定义文本框
	JLabel label1,label2,label3,label4;
	String sno;
    BookIn()
   { 
	f3=new JFrame();
	cp=f3.getContentPane(); // 初始化面板、按钮、标签、文本框
	jp1=new JPanel();       
	jp2=new JPanel();
	jp3=new JPanel();
	jp4=new JPanel();
	jpanelWest=new JPanel();
	jp=new JPanel();
	//------------------------------------------------
	jbt1=new JButton("确定");   
	jbt2=new JButton("取消");
	
	//------------------------------------------------
	label=new JLabel("<html><font color=#CC00FF size='4'>图书入库</font>",SwingConstants.CENTER);
	label.setForeground(Color.blue);
	
    
	
    
	//------------------------------------------------
	tf1=new JTextField(20);
	tf2=new JTextField(20);
	tf3=new JTextField(20);
	tf4=new JTextField(20);
	tf5=new JTextField(20);
    tf6=new JTextField(20);
	//------------------------------------------------
	//布局,添加控件
	
	jp1.add(jbt1);
	jp1.add(jbt2);
	
	
	sno=tf4.getText();
	
	jp1.add(new JLabel("您好"+sno+"欢迎登陆学生信息系统"));
	
	JPanel jpanel=new JPanel();
	jpanel.add(label);
	
	
	JPanel pp4=new JPanel();
    JPanel jpane4=new JPanel();
	
	cp.add(jpanel,"North");
	JPanel pp2=new JPanel(new GridLayout(6,1));
	JPanel pp3=new JPanel();
	pp4.setLayout(new GridLayout(6,1));
	pp4.add(new JLabel("图书名",SwingConstants.CENTER));
	
	pp2.add(tf1);
	pp4.add(new JLabel("图书号",SwingConstants.CENTER));
	pp2.add(tf2);
	pp4.add(new JLabel("单  价",SwingConstants.CENTER));
	pp2.add(tf3);
	pp4.add(new JLabel("作  者",SwingConstants.CENTER));
	pp2.add(tf4);
	pp4.add(new JLabel("出版社",SwingConstants.CENTER));
	pp2.add(tf5);
	pp4.add(new JLabel("入库时间",SwingConstants.CENTER));
	pp2.add(tf6);
	pp3.add(jbt1);

	pp3.add(jbt2);
  
   
	cp.add(pp4,"West");
	cp.add(pp2,"Center");
    cp.add(pp3,"South");
	 
	cp.add(jpane4,"East");
	
	//------------------------------------------------
     Toolkit kit=Toolkit.getDefaultToolkit();
	Dimension screen=kit.getScreenSize();
	int x=screen.width;					/*取得显示器窗口的宽度*/
	int y=screen.height;					/*取得显示器窗口的高度*/
	f3.setSize(350,330);
	 int xcenter=(x-350)/2;
	 int ycenter=(y-330)/2;
	 f3.setLocation(xcenter,ycenter);/*显示在窗口中央*/
	 f3.setVisible(true);
   //-------------------------------------------------
    jbt1.addActionListener(this);//注册监听器
    jbt2.addActionListener(this);
    
    
    /*f3.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		}
		);*/
	}
	//------------------------------------------------
   public void insertRecord()
	{ 
      if(tf1.getText().equals("")||tf2.getText().equals("")||tf3.getText().equals("")||
		  tf4.getText().equals("")||tf5.getText().equals("")||tf6.getText().equals(""))
		{
		JOptionPane.showMessageDialog(f3,"请填写图书资料");
		return;
		}

        try{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		   }catch(ClassNotFoundException e){System.out.println("加载驱动程序失败!");}
		try{
			String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=Book.mdb";//直接使用当前类目录下的数据库文件
			Connection con=DriverManager.getConnection(url);
			Statement sql;		
			
			String s="insert into book  values('"+tf1.getText()+"','"+tf2.getText()+"','"+
				tf3.getText()+"','"+tf4.getText()+"','"+tf5.getText()+"','"+tf6.getText()+"');";
			
			//查询输入的图书号是否在数据库中存在
            String query="select * from book where 图书号='"+tf2.getText()+"'";
			sql=con.createStatement();      
			ResultSet rs=sql.executeQuery(query);//返回查询结果集
			boolean moreRecords=rs.next();//判断结果集是否有数据
			  if(moreRecords)
			     {
				 JOptionPane.showMessageDialog(f3,"图书号已经被使用，请重新输入");
				 con.close();
				 tf2.setText("");
				 return;
			     }
				
				int insert=sql.executeUpdate(s);
			 if(insert==1)
			    {
				JOptionPane.showMessageDialog(null,"图书信息录入成功！");
				tf1.setText("");
				tf2.setText("");
				tf3.setText("");
				tf4.setText("");
				tf5.setText("");
				tf6.setText("");
			   }
            }catch(SQLException g)
				{
				System.out.println("E Code"+g.getErrorCode());
				System.out.println("E M"+g.getMessage());
			     }
    
	  

	}
	

   public void actionPerformed(ActionEvent e)
	{  
		 String cmd=e.getActionCommand();
		    if(cmd.equals("确定"))
			   {
			      insertRecord();
				
			    } 
					
			 else if(cmd.equals("取消"))
				   f3.hide();
		         
	}
   public static void main(String []arg){
		   
		   BookIn a=new BookIn();
    }

}
