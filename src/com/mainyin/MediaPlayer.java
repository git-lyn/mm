package com.mainyin;
import javax.media.ControllerEvent; 
import javax.media.ControllerListener; 
import javax.media.EndOfMediaEvent; 
import javax.media.PrefetchCompleteEvent; 
import javax.media.RealizeCompleteEvent; 
import javax.media.*; 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
public class MediaPlayer extends JFrame implements ActionListener, 
ItemListener, ControllerListener  {
	String title; 

	Player player; 
	boolean first = true, loop = false; 
	Component vc, cc; 
	String currentDirectory=null; 
	// ���캯�������а�����������Ӧ�����¼��ļ������� 
	MediaPlayer(String title) { 
	super(title); 
	/* �رհ�ť��ʵ�֡��� */ 
	addWindowListener(new WindowAdapter() { 
	public void windowClosing(WindowEvent e) { 
	dispose(); 
	} 

	public void windowClosed(WindowEvent e) { 
	if (player != null) 
	player.close(); 
	System.exit(0); 
	} 

	}); 
	// ���ó���˵����ķ�����Ա��ɲ˵��Ĳ��� 
	setupMenu(); 
	setSize(400, 400); 
	setVisible(true); 
	} 

	// �������������ó���˵��� 
	public void setupMenu() { 
	// ����һ���˵� 
	Menu f = new Menu("�ļ�"); 
	// �����õĲ˵����Ӳ˵��� 
	MenuItem mi = new MenuItem("��"); 
	f.add(mi); 
	mi.addActionListener(this); 
	f.addSeparator(); 
	CheckboxMenuItem cbmi = new CheckboxMenuItem("ѭ��", false); 
	cbmi.addActionListener(this); 
	f.add(cbmi); 
	f.addSeparator(); 
	MenuItem ee = new MenuItem("�˳�"); 
	ee.addActionListener(this); 
	f.add(ee); 
	f.addSeparator(); 

	Menu l = new Menu("�����б�"); 
	Menu c = new Menu("���ſ���"); 
	MenuItem move = new MenuItem("����"); 
	move.addActionListener(this); 
	c.add(move); 
	c.addSeparator(); 
	MenuItem pause = new MenuItem("��ͣ"); 
	pause.addActionListener(this); 
	c.add(pause); 
	c.addSeparator(); 
	MenuItem stop = new MenuItem("ֹͣ"); 
	stop.addActionListener(this); 
	c.add(stop); 
	c.addSeparator(); 
	// ����һ���˵��� 
	MenuBar mb = new MenuBar(); 
	mb.add(f); 
	mb.add(c); 
	mb.add(l); 
	// ��������ɵĲ˵���������ǰ����Ĵ���; 
	setMenuBar(mb); 
	} 

	// ����ʱ����Ӧ��Ա����׽���͵�������ĸ����¼�; 
	public void actionPerformed(ActionEvent e) { 
	// TODO Auto-generated method stub 
	String cufile, selectfile, currentDirectory; 
	if (e.getActionCommand().equals("�˳�")) { 
	// ����dispose�Ա�ִ��windowClosed 
	dispose(); 
	return; 
	} 
	// ���±���ӵ��ѡ���ˡ����š�����; 
	// �����ǰ��һ���ļ����Բ�����ִ�в�������; 
	if (e.getActionCommand().equals("����")) { 
	if (player != null) { 
	player.start(); 
	} 
	return; 
	} 
	// �����ǰ���ڲ���ĳһ�ļ�����ִ����ͣ; 
	if (e.getActionCommand().equals("��ͣ")) { 
	if (player != null) { 
	player.stop(); 
	} 
	return; 
	} 
	// ֹͣ�������Ӧ; 
	if (e.getActionCommand().equals("ֹͣ")) { 
	if (player != null) { 
	player.stop(); 
	player.setMediaTime(new Time(0)); 
	} 
	return; 
	} 
	// �û�ѡ��Ҫ���ŵ�ý���ļ� 
	if (e.getActionCommand().equals("��")) { 
	FileDialog fd = new FileDialog(this, "��ý���ļ�", FileDialog.LOAD); 
	// fd.setDirectory(currentDirectory); 
	fd.setVisible(true); 
	// ����û�����ѡ���ļ����򷵻� 
	if (fd.getFile() == null) { 
	return; 
	} 
	// ��������ѡ�ļ������Ƽ���·�������ѱ��Ժ�ʹ�� 
	// ͬʱ���õ�ǰ�ļ���·�� 
	selectfile = fd.getFile(); 
	currentDirectory = fd.getDirectory(); 
	cufile = currentDirectory + selectfile; 
	// ���û�ѡ����ļ���Ϊһ���˵�����벥���б�,�ò˵�����Ϊ���ļ���; 
	// ����������������Ǹ��ļ���ȫ·���� 
	MenuItem mi = new MenuItem(selectfile); 
	mi.setActionCommand(cufile); 
	MenuBar mb = getMenuBar(); 
	Menu m = mb.getMenu(2); 
	mi.addActionListener(this); 
	m.add(mi); 
	} else { 
	// �����߼����е��α�ʾ�û�ѡ����һ���������б����е�ý���ļ� 
	// ��ʱ����ͨ�����¶�����ø��ļ���ȫ·���� 
	cufile = e.getActionCommand(); 
	selectfile = cufile; 
	} 
	// �������һ�������������Ƚ���رգ��Ժ������´��� 
	// ����������ʱ��Ҫ��׽һЩ�쳣 
	if (player != null) { 
	player.close(); 
	} 
	try { 
	player = Manager.createPlayer(new MediaLocator("file:" + cufile)); 
	} catch (Exception e2) { 
	System.out.println(e2); 
	return; 
	}/* 
	 * catch(NoPlayerException e2){ System.out.println("�����ҵ�������"); 
	 * return ; } 
	 */ 
	if (player == null) { 
	System.out.println("�޷�����������"); 
	return; 
	} 
	first = false; 
	setTitle(selectfile); 
	// ���ô������ſ�����ʵ�ʵĶ��� 
	/**/ 
	player.addControllerListener(this); 
	player.prefetch(); 
	} 

	// �˵�״̬�ı��¼�����Ӧ������ 
	public void itemStateChanged(ItemEvent arg0) { 
	// TODO Auto-generated method stub 

	} 
	public static void main(String[] args) { 
	// TODO Auto-generated method stub 
	new MediaPlayer("������"); 
	} 

	// ���û�ͼ�������н���Ļ��� // public void update() { 
	// } 
	// ��ͼ������Ա //public void paint(Graphics g) { 
	// } 
	public void controllerUpdate(ControllerEvent e) { 
	// TODO Auto-generated method stub 
	Container tainer = getContentPane(); 
	// ����player.close()ʱControllerClosedEvent�¼����� 
	// ��������Ӿ���������ò���Ӧ�ò����Ϊ��һ����������ǶԿ�����沿��Ҳִ��ͬ���Ĳ�������һ����Ҫʱ�ٹ���) 
	if (e instanceof ControllerClosedEvent) { 
	if (vc != null) { 
	remove(vc); 
	vc = null; 
	} 
	if (cc != null) { 
	remove(cc); 
	cc = null; 
	} 
	} 

	// ���Ž���ʱ��������ָ�������ļ�֮�ף�����趨��ѭ�����ţ����ٴ������������� 
	if (e instanceof EndOfMediaEvent) { 
	player.setMediaTime(new Time(0)); 
	if (loop) { 
	player.start(); 
	} 
	return; 
	} 

	// PrefetchCompletEvent�¼����������start,��ʽ�������� 
	if (e instanceof PrefetchCompleteEvent) { 
	player.start(); 
	return; 
	} 

	// ���¼���ʾ���ڲ��ŵ���Դ�Ѿ�ȷ������ʱҪ��ý���ͼ��conmopnent 
	// �������ʾ������ͬʱ��������player�Ŀ�����ʾ������� 
	if (e instanceof RealizeCompleteEvent) { 
	// ���ý������ͼ�񣬽���Ӧͼ��component���봰�壻 
	vc = player.getVisualComponent(); 
	if (vc != null) 
	tainer.add(vc, BorderLayout.CENTER); 
	// ����Ӧ������component���봰��; 
	cc = player.getControlPanelComponent(); 
	cc.setBackground(Color.blue); 
	if (cc != null) 
	tainer.add(cc, BorderLayout.SOUTH); 
	// ��һЩ����ý���ڲ���ʱ�ṩ����Ŀ����ֶΣ���������һ�����봰�ڣ� 
	/* 
	 * gc=player.getGainControl(); gcc=gc.getControlComponent(); 
	 * if(gcc!=null) tainer.add(gcc,BorderLayout.NORTH); 
	 */ 
	// ����ý���ļ����Ƿ���ͼ���趨��Ӧ�Ĵ��ڴ�С 
	if (vc != null) { 
	pack(); 
	return; 
	} else { 
	setSize(300, 75); 
	setVisible(true); 
	return; 
	} 
	} 

	}

}