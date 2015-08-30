import com.dropbox.core.*;
import com.dropbox.core.DbxDelta.Entry;
import com.dropbox.core.DbxEntry.File;

import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame{

	public static void main(String[] args) throws IOException, DbxException {

        DropboxFunctions.SetupAuthentication();
		
        BuildGUI.CreateJFrame();
        
	}
	
}
