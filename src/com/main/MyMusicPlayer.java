package com.main;
import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.awt.event.*;
import java.net.*;

public class MyMusicPlayer extends Thread implements MouseListener,ItemListener {
	JFrame MainFrame=new JFrame("MyMusicPlayer");      //����������
	JLabel songname=new JLabel();                    //�ñ�ǩ��ʾ״̬
	JButton last=new JButton();
	JButton play=new JButton();                
	JButton next=new JButton();
	JButton loop=new JButton();                       //������һ��,����,��һ��,ѭ���ĸ���ť
	JLabel list=new JLabel("�����б�");
	List songlist=new List();                         //��ʾ�����б�
	AudioClip[] song=new AudioClip[10];                     //����Ƶ�ļ�����������
	AudioClip playsong;                                     //��ǰѡ�����ŵ���Ƶ�ļ�
	String[] name={"��С�� - ˳����Ȼ.wav","����Ѹ-�������.wav","��С�� _û��ô��.wav",
			 "��ޱ-���������.wav","���ɽ� - �ݺݿ�.wav","test.wav"};    
	        //�������ļ���������ַ�������name��
	String playname;                         //��ǰѡ�����ŵ���Ƶ��
	int j=0;                                //��¼��ǰѡ�����ŵ������׸�����Ĭ��Ϊ��һ��
	boolean playbutton=true;                     //��¼����״̬��Ĭ��Ϊ��ͣ����
	boolean loopbutton=false;                     //��¼ѭ��״̬��Ĭ��Ϊû��ѭ��
    Thread thread=new Thread("pl");
    static MyMusicPlayer Yu;
    
    public  MyMusicPlayer(){
    	 MainFrame.setLayout(null);
    	 MainFrame.setBounds(300,50,310,500);              
    	 MainFrame.setVisible(true);
    	 MainFrame.setDefaultCloseOperation(3);
    	 
    	 MainFrame.add(songname);
    	 Font sname=new Font("б��",Font.ITALIC,18);     
    	 songname.setFont(sname);                          //������ʾ״̬������Ϊб��
    	 songname.setText("�ҵ����ֲ�����");               
    	 songname.setBounds(10,10,300,40);
   
    	 last.setBounds(10,70,50,40);
    	 play.setBounds(70,70,50,40);                   //�����ĸ����ܼ�λ�úʹ�С
    	 next.setBounds(130,70,50,40);
    	 loop.setBounds(190,70,50,40);
    	 last.setIcon(new ImageIcon("1.png"));            
    	 play.setIcon(new ImageIcon("2.png"));
    	 next.setIcon(new ImageIcon("3.png"));
    	 loop.setIcon(new ImageIcon("4.png"));            //�����ĸ����ܼ���ͼƬ
    	 last.addMouseListener(this);
    	 play.addMouseListener(this);
    	 next.addMouseListener(this);                 
    	 loop.addMouseListener(this);                     //��Ӱ�����������
    	 
    	 MainFrame.add(last);
    	 MainFrame.add(play);
    	 MainFrame.add(next);
    	 MainFrame.add(loop);                       
    	
      	list.setBounds(10,120,100,30);
      	Font lis=new Font("��",Font.BOLD,15);
      	list.setFont(lis);                         //��ʾ�������б�
      	MainFrame.add(list);
      	
        songlist.setBounds(10,150,250,300);           
        songlist.setBackground(Color.GREEN);           //���ò����б�ı���ɫΪ��ɫ
      	songlist.setVisible(true);
      	songlist.addItemListener((ItemListener) this);          //����б������
      	MainFrame.add(songlist);
      	
      	for(int i=0;i<name.length;i++)	                              
      	{
      		 song[i]=loadSound(name[i]);          //�����ȡ��Ƶ�ļ�
      		 songlist.add(name[i]);                       //����������ӵ������б�	
      	}  
      	playsong=song[0];
      	
     }
    
     public static void main(String[] args){
    	 Yu=new MyMusicPlayer();
      	 Yu.start();
     }
     
	public void mouseClicked(MouseEvent e) {
		JButton btn=(JButton)e.getSource();
		playsong.stop();
		if(btn==play)
		{   if(playbutton==false)
		        playbutton=true;                    
		    else
		        playbutton=false;                  //������play��ı䲥��״̬
		}
		else
			if(btn==last)
			 {   j-=1;                              //������last����һ��ѡ��
			     if(j<0)
				     j=name.length-1;               //��֮ǰѡ��Ϊ��һ�ף����������һ��
				 playbutton=true;
				 loopbutton=false;
			 }
			else
				if(btn==next)
				{   j+=1;                          //������next����һ��ѡ��
				    if(j>=name.length)
				    	j=0;                       //��֮ǰѡ��Ϊ���һ�ף���������һ��
					playbutton=true;
					loopbutton=false;
				}
				else
					if(btn==loop)
					{  if(loopbutton==false) 
					    {  
					        loopbutton=true;
					        playbutton=true;
					    }
					   else
					    {                               //����loop�󣬸ı�ѭ��״̬�Ͳ���״̬
					        loopbutton=false;
					        playbutton=false;
					    }
					}  
		
		if(playbutton==true)
			Yu.run();
		else
		{	                            
		    songname.setText("��ͣ���ţ�"+playname);    //��ͣ���Ÿ���  
		    play.setIcon(new ImageIcon("2.png"));
		}
	}
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void itemStateChanged(ItemEvent arg0) {
		String str=songlist.getSelectedItem();
		playsong.stop();
		for(int i=0;i<name.length;i++)
		   if(str==name[i])
		     { 
		       j=i;
		       break;
		     }
		Yu.run();
	   
	}
	public void run(){
		playsong=song[j];              //����״̬Ϊ����ʱ
	    playsong.play();                //����ѡ������
        playname=name[j];             
        if(loopbutton==true)
        {                               
        	  playsong.loop();            //ѭ������ѡ������
        	  songname.setText("ѭ�����ţ�"+playname);
        }
        else
        	songname.setText("���ڲ��ţ�"+playname);
        
        songlist.select(j);                       //�����б���ѡ�����Ÿ�����Ŀ
        play.setIcon(new ImageIcon("5.png"));	   
		
	}
	
	public AudioClip loadSound(String filename){        //����һ��AudioClip����
	   URL url=null;                                                                   
	   try{
	        url=new URL("file:"+filename);                                
	      }catch(MalformedURLException e){ }
	   return Applet.newAudioClip(url);                           
	}

}
