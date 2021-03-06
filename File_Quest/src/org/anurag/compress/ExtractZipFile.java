/**
 * Copyright(c) 2014 DRAWNZER.ORG PROJECTS -> ANURAG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *                             
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.anurag.compress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.anurag.dialogs.BluetoothChooser;
import org.anurag.dialogs.OpenFileDialog;
import org.anurag.file.quest.AppBackup;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

/**
 * This class is for extracting the files from zip archive....
 * 
 * @author Anurag
 *
 */
public class ExtractZipFile {
	
	private boolean running;
	private byte data[] = new byte[Constants.BUFFER];
	private long prog ;
	private int read ;
	private String DEST;
	private String name;
	private String size;
	private long max;
	private boolean errors;
	private Enumeration<? extends ZipEntry> zList;
	private String dest;	
	private Thread thread;
	
	/**
	 * 
	 * @param ctx
	 * @param zFile item to be extracted
	 * @param extractDir path where to extract
	 * @param file main zip file from which extraction will be done
	 * @param openafterExtract true then open file after extraction
	 * @param share if true then open window to share the file after extraction
	 */
	public ExtractZipFile(final Context ctx ,final Item zFile , final String extractDir ,
			final File file ,final boolean openafterExtract , final boolean share) {
		// TODO Auto-generated constructor stub
		running = false;
		errors = false;
		prog = 0;
		read = 0;
		final Handler handle;final
		
		LayoutInflater INF = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = INF.inflate(R.layout.extract_file, null , false);
		final ProgressBar progress = (ProgressBar)view.findViewById(R.id.zipProgressBar);
		
		MaterialDialog.Builder build = new MaterialDialog.Builder(ctx);
		
		build.title(R.string.extract)
		.customView(view, true)
		.autoDismiss(false)
		.callback(new ButtonCallback() {

			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				if(!running){
					progress.setVisibility(View.VISIBLE);
					thread.start();
				}
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				running = false;
				dialog.dismiss();
			}

			@Override
			public void onNeutral(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNeutral(dialog);
			}			
		})
		.positiveText(R.string.extract)
		.negativeText(R.string.dismiss);
		
		final MaterialDialog dialog = build.show();
		
		
		DEST = extractDir;
		final TextView to = (TextView)view.findViewById(R.id.zipFileName);
		final TextView from = (TextView)view.findViewById(R.id.zipLoc);
		final TextView cfile = (TextView)view.findViewById(R.id.zipSize );
		final TextView zsize = (TextView)view.findViewById(R.id.zipNoOfFiles);
		final TextView status = (TextView)view.findViewById(R.id.zipFileLocation);
		
		
		if(extractDir==null)
			to.setText(ctx.getString(R.string.extractingto)+" Cache directory");
		else
			to.setText(ctx.getString(R.string.extractingto)+" "+DEST);
		
		from.setText(ctx.getString(R.string.extractingfrom)+" "+file.getName());
		
				
		try {
			zList = new ZipFile(file).entries();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			zList = null;
		}
		
	/*	try {
			zis = new ZipInputStream(new FileInputStream(file));
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			zis = null;
		}*/
		
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 0:
						
							progress.setProgress(0);
							cfile.setText(ctx.getString(R.string.extractingfile)+" "+name);
							break;
							
					case 1:
							status.setText(name);
							progress.setProgress((int)prog);
							break;
					case 2:
							if(running){
								dialog.dismiss();
							    if(openafterExtract){
							    	//after extracting file ,it has to be opened....
							    	new OpenFileDialog(ctx, Uri.parse(dest));
							    }
							    
							    if(share){
							    	//FILE HAS TO BE SHARED....
							    	new BluetoothChooser(ctx, new File(dest).getAbsolutePath(), null);
							    }
							    
							    if(errors){
							    	Toast.makeText(ctx, R.string.errorinext, Toast.LENGTH_SHORT).show();
							    }
							    Toast.makeText(ctx, ctx.getString(R.string.fileextracted),Toast.LENGTH_SHORT).show();
							    
							}						    
						    break;
					case 3:
							zsize.setText(size);
							progress.setMax((int)max);
							break;
					case 4:
							status.setText(ctx.getString(R.string.preparing));
							break;
					case 5:
							running = false;
							Toast.makeText(ctx, ctx.getString(R.string.extaborted),Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(running){
					if(DEST==null){
						DEST = Environment.getExternalStorageDirectory()+"/Android/data/org.anurag.file.quest";
						new File(DEST).mkdirs();
					}
					
					
					ZipEntry ze;
					while(zList.hasMoreElements()){
					//	++count;
						ze = zList.nextElement();
						handle.sendEmptyMessage(4);
						if(!zFile.isDirectory()){
							//EXTRACTING A SINGLE FILE FROM AN ARCHIVE....
							if(ze.getName().equalsIgnoreCase(zFile.getEntry())){
								try {
								    
									//SENDING CURRENT FILE NAME....
									try{
										name = zFile.getName();
									}catch(Exception e){
										name = zFile.getEntry();
									}
									handle.sendEmptyMessage(0);
									dest = DEST;
									dest = dest + "/"+name;
									FileOutputStream out = new FileOutputStream((dest));
									max = ze.getSize();
									size =AppBackup.size(max, ctx);
									handle.sendEmptyMessage(3);
								//	for(;i<count;++i)
								//		zis.getNextEntry();
									InputStream fin = (new ZipFile(file).getInputStream(ze));
									while((read=fin.read(data))!=-1&&running){
										out.write(data, 0, read);
										prog+=read;
										name = AppBackup.status(prog, ctx);
										handle.sendEmptyMessage(1);
									}										
									out.flush();
									out.close();
									fin.close();
									break;
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									//errors = true;
								} catch(IOException e){
									errors = true;
								}
							}
						}else{
							//EXTRACTING A DIRECTORY FROM ZIP ARCHIVE....
							String p = zFile.getPath();
							if(p.startsWith("/"))
								p = p.substring(1, p.length());
							if(ze.getName().startsWith(p)){
								prog = 0;
								dest = DEST;
								name = ze.getName();
								String path = name;
								name = name.substring(name.lastIndexOf("/")+1, name.length());
								handle.sendEmptyMessage(0);
								
								
								String foname = zFile.getPath();
								if(!foname.startsWith("/"))
									foname = "/"+foname;
								
								if(!path.startsWith("/"))
									path = "/"+path;
								path = path.substring(foname.lastIndexOf("/"), path.lastIndexOf("/"));
								if(!path.startsWith("/"))
									path = "/"+path;
								dest = dest+path;
								new File(dest).mkdirs();
								dest = dest+"/"+name;
								
								FileOutputStream out;
								try {
									max = ze.getSize();
									out = new FileOutputStream((dest));
									size =AppBackup.size(max, ctx);
									handle.sendEmptyMessage(3);
								
									InputStream fin = (new ZipFile(file).getInputStream(ze));
									while((read=fin.read(data))!=-1&&running){
										out.write(data, 0, read);
										prog+=read;
										name = AppBackup.status(prog, ctx);
										handle.sendEmptyMessage(1);
									}										
									out.flush();
									out.close();
									fin.close();
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								//	errors = true;
								}catch(IOException e){
									errors = true;
								}catch(Exception e){
									//errors = true;
								}								
							}
						}
					}				
					
				/*	try {
						zis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					handle.sendEmptyMessage(2);
				}
			}
		});
		
		if(extractDir == null){
			running = true;
			thread.start();
			progress.setVisibility(View.VISIBLE);
		}		
	}
}
