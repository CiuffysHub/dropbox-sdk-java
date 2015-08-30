import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;


public class DropboxFunctions {

	public static String authorizeUrl, currentdir;
	public static DbxWebAuthNoRedirect webAuth;
	public static DbxClient client;
	public static DbxRequestConfig config;
	
	public static List<String> CleanArrayGetPaths(String arraytostring)
	{
		List<String> passing=new ArrayList<String>();
		while(arraytostring.contains("("))
		{
			int pos = arraytostring.indexOf("("), y=0;
			char c=' ';
			while (c!='"')
			{
				y++;
				c=arraytostring.charAt(pos+2+y);
			}
			passing.add(arraytostring.substring(pos+2,pos+2+y));
			arraytostring = arraytostring.substring(pos+2+y, arraytostring.length());
		}
		return passing;
	}
	
	public static List<String>listDropboxFolders(String folderPath, DbxClient client, boolean includeDeleted) throws DbxException {
		
		List<String> array= new ArrayList<String>();
		
		DbxEntry.WithChildren listing = client.getMetadataWithChildren(folderPath, includeDeleted);
		
		for (DbxEntry child : listing.children) {
			array.add("	" + child.name + ": " + child.toString());
		}
		
		currentdir = folderPath;
		
		return array;
	}
	
	public static void SetupAuthentication()
	{
		final String APP_KEY = "YOURAPPKEY";
        final String APP_SECRET = "YOURAPPSECRET";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        config = new DbxRequestConfig(
        "ROCkSafe", Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        
        authorizeUrl = webAuth.start();
	}
	
	public static void Authenticate(String code) throws DbxException
	{       
	        DbxAuthFinish authFinish = webAuth.finish(code);
	        String accessToken = authFinish.accessToken;
	        
	        client = new DbxClient(config, accessToken);
	        BuildGUI.link.setText("Linked account: " + client.getAccountInfo().displayName);
	}
	
	public static List<String> getRevisionsArray(String listostring, List<String> revs)
	{

		revs.clear();
		
		while(listostring.contains("rev"))
		{
			int pos = listostring.indexOf("rev"), y=0;
			char c=' ';
			while (c!='"')
			{
				y++;
				c=listostring.charAt(pos+5+y);
			}
			revs.add(listostring.substring(pos+5,pos+5+y));
			listostring = listostring.substring(pos+3, listostring.length());
		}
		//revs=["PIU' NUOVA", "PIU' VECCHIA"], ultima che leggi, prima versione "0"
		return revs;
		
	}
	
	public static void Upload(String filetoupload, String where) throws DbxException, IOException
	{
		File inputFile = new File(filetoupload);
		FileInputStream inputStream = new FileInputStream(inputFile);
		try {
				while (filetoupload.contains("/"))
				{
					filetoupload= filetoupload.substring(1, filetoupload.length());
				}
				if (where!="/")
				{
					where=where+"/";
				}
		    DbxEntry.File uploadedFile = client.uploadFile(where+filetoupload,
		        DbxWriteMode.add(), inputFile.length(), inputStream);
		} finally {
		    inputStream.close();
		}
	}
	
	public static void Download(String filetodownload, String where) throws DbxException, IOException
	{
		String filetodownloadstripped=filetodownload;
		while (filetodownloadstripped.contains("/"))
		{
			filetodownloadstripped= filetodownloadstripped.substring(1, filetodownloadstripped.length());
		}
		File inputFile = new File(where+filetodownloadstripped);
		FileOutputStream outputStream = new FileOutputStream(inputFile);
		try {
		    DbxEntry.File downloadFile = client.getFile(filetodownload, null, outputStream);
		} finally {
		    outputStream.close();
		}
	}
	
}
