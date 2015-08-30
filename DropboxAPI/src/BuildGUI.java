import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;


public class BuildGUI {
	
	public static JScrollPane listScrollPane = new JScrollPane();
	public static JButton Search, Auth, list, Selected, Upload, Download;
	public static JCheckBox include_deleted;
	public static JList listbox = new JList();
	public static JTextField link, code, directory, rev;
	public static JPanel panel;
	
	public static void CreateJFrame()
	{
		
		JFrame frame = new JFrame("Dropbox API File Browser - Deleted Files Support");
		frame.setSize(1000,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(panel = new JPanel());
		panel.setLayout(null);
		
		D.SetGrid(frame.getSize(),100, 100);
		
		panel.add(link = new JTextField(DropboxFunctions.authorizeUrl));
		D.SetGivenSquaresFixedBounds(link, 5, 8, 60, 10);
	    D.SetFontbyHeight(link, Font.PLAIN);
		
	    panel.add(code = new JTextField("Paste code here..."));
		D.SetGivenSquaresFixedBounds(code, 5, 23, 40, 10);
	    D.SetFontbyHeight(code, Font.PLAIN);
	    
	    panel.add(include_deleted = new JCheckBox("Include Deleted"));
	    D.SetGivenSquaresFixedBounds(include_deleted, 50, 23, 20, 10);
	    
	    panel.add(rev = new JTextField("Rev"));
		D.SetGivenSquaresFixedBounds(rev, 70, 83, 7, 10);
		D.SetFontbyHeight(rev, Font.PLAIN);
	    
	    panel.add(directory = new JTextField("/Type/Directory..."));
		D.SetGivenSquaresFixedBounds(directory, 5, 38, 60, 10);
		D.SetFontbyHeight(directory, Font.PLAIN);
		
		panel.add(Download = new JButton("Download"));
		D.SetGivenSquaresFixedBounds(Download, 70, 53, 25, 10);
		Download.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try {
				DropboxFunctions.Download(listbox.getSelectedValue().toString(), directory.getText());
			} catch (DbxException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  }
		});
		
		panel.add(Auth = new JButton("Authenticate"));
		D.SetGivenSquaresFixedBounds(Auth, 70, 8, 25, 10);
		Auth.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try {
				DropboxFunctions.Authenticate(code.getText());
				if (include_deleted.isSelected())
	            	ListFolders("/", true);
	            	else
	            	ListFolders("/", false);	
				   //DropboxFunctions.Upload();
			} catch (DbxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  }
		});
		
		panel.add(list = new JButton("List Elements in Folder"));
		D.SetGivenSquaresFixedBounds(list, 70, 23, 25, 10);
		list.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
				if (include_deleted.isSelected())
	            	ListFolders(code.getText(), true);
	            	else
	            	ListFolders(code.getText(), false);	
		  }
		});
	    
		panel.add(Search = new JButton("Find Matching Files"));
		D.SetGivenSquaresFixedBounds(Search, 70, 38, 25, 10);
		Search.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try {
				List<DbxEntry> lista = DropboxFunctions.client.searchFileAndFolderNames("/", directory.getText());
				List<String> array=DropboxFunctions.CleanArrayGetPaths(lista.toString());
				DefaultListModel listModel = new DefaultListModel();
				//listModel.removeAllElements();
				for (int i=0; i<array.size(); i++)
				{
					listModel.addElement(array.toArray()[i]);
				}
				listbox.setModel(listModel);
			} catch (DbxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  }
		});

		panel.add(Selected = new JButton("Recover"));
		D.SetGivenSquaresFixedBounds(Selected, 80, 83, 15, 10);
		Selected.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  List<String> array = listbox.getSelectedValuesList();
			  for (int i = 0; i<array.size(); i++)
			  {
				  String element=array.get(i);
				  //array.set(i,element.substring(0,element.length()-10));
				  List<String> revisions = new ArrayList<String>();
				  try{
				  DropboxFunctions.getRevisionsArray(DropboxFunctions.client.getRevisions(array.get(i)).toString(), revisions);
				  DropboxFunctions.client.restoreFile(array.get(i), revisions.get(Integer.parseInt(rev.getText())));
				} catch (DbxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			  }
		  }
		});

		panel.add(Upload = new JButton("Upload In Browsed Folder"));
		D.SetGivenSquaresFixedBounds(Upload, 70, 68, 25, 10);
		Upload.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try {
				DropboxFunctions.Upload(directory.getText(), DropboxFunctions.currentdir);
			} catch (DbxException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  }
		});
		
		listbox.setBorder(BorderFactory.createLineBorder(Color.black));
		listbox.addKeyListener(new KeyAdapter() {
	         public void keyReleased(KeyEvent ke) {
	            if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
	            	if (include_deleted.isSelected())
	            	ListFolders(listbox.getSelectedValue().toString(), true);
	            	else
	            	ListFolders(listbox.getSelectedValue().toString(), false);	
	            }
	         }
	      });
	    listScrollPane.setViewportView(listbox);
		D.SetGivenSquaresFixedBounds(listScrollPane, 5, 53, 60, 40);
		panel.add(listScrollPane);
		
	    frame.setVisible(true);
	}
	
	public static void ListFolders(String dir, boolean includeDeleted)
	{
		try {
			List<String> array=DropboxFunctions.listDropboxFolders(dir,DropboxFunctions.client, includeDeleted);
			array=DropboxFunctions.CleanArrayGetPaths(array.toString());
			DefaultListModel listModel = new DefaultListModel();
			//listModel.removeAllElements();
			for (int i=0; i<array.size(); i++)
			{
				listModel.addElement(array.toArray()[i]);
			}
			listbox.setModel(listModel);
			code.setText(DropboxFunctions.currentdir);
		} catch (DbxException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
	}
	
}
