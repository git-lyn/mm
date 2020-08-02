package yinyue;
import java.util.*;
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.Time;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MusicPlayer implements ActionListener, Serializable,ControllerListener{
	private static final long serialVersionUID = 1L;
	private JFrame frame = null;
	private JPanel controlPanel = null;
	private JButton btnPlay = null;
	private JButton btnPre = null;
	private JButton btnNext = null;
	private JScrollPane listPane = null;
	private JList list = null;
	private DefaultListModel listModel = null;
	private JMenuBar menubar = null;
	private JMenu menuFile = null, menuAbout = null, menuMode = null;
	private JMenuItem itemOpen, itemOpens, itemExit, itemAbout;
	private JRadioButtonMenuItem itemSingle, itemSequence ,itemRandom;
	private ListItem currentItem = null;
	private static Player player = null;
	private boolean isPause = false;
	private int mode;
	private int currentIndex;
	private ImageIcon iconPlay = new ImageIcon("d:\\1.jpg");
	private ImageIcon iconPre = new ImageIcon("d:\\3.jpg");
	private ImageIcon iconNext = new ImageIcon("d:\\2.jpg");
	private ImageIcon iconPause = new ImageIcon("d:\\4.jpg");
	public static void main(String[] args)
	{
		new MusicPlayer();
	}
	public MusicPlayer()
	{
		init();
	}
	
	public void init()
	{
		frame = new JFrame();
		frame.setTitle("���ֲ�����");
		frame.setSize(400, 300);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menubar = new JMenuBar();
		menuFile = new JMenu("�ļ�");
		menuAbout = new JMenu("����");
		menuMode = new JMenu("����ģʽ");
		itemOpen = new JMenuItem("����ļ�");
		itemOpens = new JMenuItem("����ļ���");
		itemExit = new JMenuItem("�˳�");
		itemAbout = new JMenuItem("����");
		itemOpen.addActionListener(this);
		itemOpens.addActionListener(this);
		itemExit.addActionListener(this);
		itemAbout.addActionListener(this);
		itemSequence = new JRadioButtonMenuItem("˳�򲥷�");
		itemSequence.setSelected(true);
		itemSingle = new JRadioButtonMenuItem("����ѭ��");
		itemSequence.addActionListener(this);
		itemRandom = new JRadioButtonMenuItem("�������");
		itemRandom.addActionListener(this);
		itemSingle.addActionListener(this);
		ButtonGroup bg = new ButtonGroup();
		bg.add(itemRandom);
		bg.add(itemSequence);
		bg.add(itemSingle);
		menuFile.add(itemOpen);
		menuFile.add(itemOpens);
		menuFile.add(itemExit);
		menuAbout.add(itemAbout);
		menuMode.add(itemSequence);
		menuMode.add(itemSingle);
		menuMode.add(itemRandom);
		menubar.add(menuFile);
		menubar.add(menuAbout);
		menubar.add(menuMode);
		frame.setJMenuBar(menubar);
		frame.setLayout(new BorderLayout());
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		btnPlay = new JButton(iconPlay);
		btnPre = new JButton(iconPre);
		btnNext = new JButton(iconNext);
		btnPlay.addActionListener(this);
		btnPre.addActionListener(this);
		btnNext.addActionListener(this);
		controlPanel.add(btnPre);
		controlPanel.add(btnPlay);
		controlPanel.add(btnNext);
		listPane = new JScrollPane();
		listModel = load();
		list = new JList(listModel);
		if (list.getSelectedIndex() == -1 && listModel.size() > 0)
		{
			currentItem = (ListItem) listModel.get(0);
			list.setSelectedIndex(0);
			currentIndex=0;
		}
		listPane.getViewport().add(list);
		list.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					if(player!=null)
					{
						player.close();
						btnPlay.setIcon(iconPlay);
					}
					currentIndex = list.locationToIndex(e.getPoint());
					currentItem = (ListItem) listModel.get(currentIndex);
					list.setSelectedIndex(currentIndex);
					play();
				}
			}
		});
		frame.setLayout(new BorderLayout());
		frame.add(controlPanel, BorderLayout.NORTH);
		frame.add(listPane, BorderLayout.CENTER);
		frame.setVisible(true);
	};
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == itemOpen)
		{// add files
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("�����ļ�", "mp3", "wav");
			jfc.setFileFilter(filter);
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setMultiSelectionEnabled(true);
			if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
			{
				File[] files = jfc.getSelectedFiles();
				for (File f : files)
				{
					ListItem item = new ListItem(f.getName(), f.getAbsolutePath());
					listModel.addElement(item);
				}
			}
		} 
		else if (e.getSource() == itemOpens)
		{// add files in a directory
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
			{
				File directory = jfc.getSelectedFile();
				File[] files = directory.listFiles(new java.io.FileFilter()
				{
					public boolean accept(File f)
					{
						if (f.getName().toLowerCase().endsWith(".mp3")|| f.getName().toLowerCase().endsWith(".wav"))
							return true;
						return false;
					}
				});
				for (File file : files)
				{
					ListItem item = new ListItem(file.getName(), file.getAbsolutePath());
					listModel.addElement(item);
				}
				save(listModel);
			}
		} 
		else if (e.getSource() == itemExit)
		{
			System.exit(0);
		} 
		else if (e.getSource() == itemAbout)
		{
			JOptionPane.showMessageDialog(frame, "����:½��");
		} 
		else if (e.getSource() == btnPlay)
		{
			// play or pause
			play();
		} 
		else if (e.getSource() == btnPre)
		{
			pre();
		} 
		else if (e.getSource() == btnNext)
		{// next music
			next();
		} 
		else if (e.getSource() == itemSequence)
		{
			mode = 0;
		} 
		else if (e.getSource() == itemSingle)
		{
			mode = 1;
		}
		else if (e.getSource() == itemRandom)
		{
			mode = 2;
		}
	}
// play control

/**

����* ����

����*/
	public void play()
	{
		if (btnPlay.getIcon() == iconPlay)
		{
			if (isPause)
			{
				player.start();
				System.out.println("��ͣ����");
				isPause = false;
			} 
			else
			{
				try
				{
					player = Manager.createPlayer(new MediaLocator("file:"+ currentItem.getPath()));
					player.addControllerListener(this); // ��ȡý������
					player.prefetch();
				} 
				catch (NoPlayerException e1)
				{
					e1.printStackTrace();
				} 
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			btnPlay.setIcon(iconPause);
		} 
		else
		{
			player.stop();
			isPause = true;
			System.out.println("��ͣ");
			btnPlay.setIcon(iconPlay);
		}
	}
	public void next()
	{
		if (currentIndex == listModel.getSize() - 1)
		{
			currentIndex = 0;
		} 
		else
		{
			currentIndex++;
		}
		currentItem = (ListItem) listModel.get(currentIndex);
		list.setSelectedIndex(currentIndex);
		Point p = list.indexToLocation(currentIndex);
		JScrollBar jScrollBar = listPane.getVerticalScrollBar();// ��ô�ֱת����
		jScrollBar.setValue(p.y);// ���ô�ֱת����λ��
		btnPlay.setIcon(iconPlay);
		if (player != null)
			player.close();
		isPause = false;
		play();
	}
	public void rand()
	{
		list.setSelectedIndex((int)(Math.random()%(listModel.getSize()-1)));
		Point p=list.indexToLocation((int)(Math.random()%(listModel.getSize()-1)));
		JScrollBar jScrollBar = listPane.getVerticalScrollBar();
		jScrollBar.setValue(p.y);
		btnPlay.setIcon(iconPlay);
		if (player != null)
			player.close();
		isPause = false;
		play();
	}
	public void pre()
	{
		if (currentIndex == 0)
		{
			currentIndex = listModel.getSize() - 1;
		} 
		else
		{
			currentIndex--;
		}
		currentItem = (ListItem) listModel.get(currentIndex);
		list.setSelectedIndex(currentIndex);
		Point p = list.indexToLocation(currentIndex);
		JScrollBar jScrollBar = listPane.getVerticalScrollBar();// ��ô�ֱת����
		jScrollBar.setValue(p.y);// ���ô�ֱת����λ��
		btnPlay.setIcon(iconPlay);
		if (player != null)
		{
			player.close();
		}
		isPause = false;
		play();
	}
	// end play control
	public DefaultListModel load()
	{
		File file = new File("list.lst");
		DefaultListModel dlm = new DefaultListModel();
		if (file.exists())
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(file));
				Integer size = (Integer) ois.readObject();
				if (size != 0)
				{
					for (int i = 0; i < size; i++)
					{
						ListItem item = (ListItem) ois.readObject();
						dlm.addElement(item);
					}
				}
				ois.close();
				return dlm;
			} 
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return dlm;
	}
	public void save(DefaultListModel dlm)
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("list.lst")));
			Integer size = dlm.getSize();
			oos.writeObject(size);
			for (int i = 0; i < size; i++)
			{
				ListItem item = (ListItem) dlm.get(i);
				oos.writeObject(item);
			}
			oos.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void controllerUpdate(ControllerEvent e)
	{
		if (e instanceof EndOfMediaEvent)
		{
			if (mode == 0)
			{// ˳�򲥷�
				System.out.println("˳�򲥷�");
				next();
			} 
			else if (mode == 1)
			{ // ����ѭ��
				System.out.println("���Ž���");
				player.setMediaTime(new Time(0));
				System.out.println("����ѭ��");
				player.start();
			}
			else if (mode == 2)
			{
				System.out.println("�������");
				rand();
			}
			return;
		}
		// ����ȡý������ݽ���
		if (e instanceof PrefetchCompleteEvent)
		{
			System.out.println("��ʼ����");
			player.start();
			return;
		}
	}

}
