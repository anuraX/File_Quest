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
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.compress;

import org.anurag.file.quest.R;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.adarshr.raroscope.RAREntry;


/**
 * 
 * @author Anurag
 *
 */
public class RarObj {
	
	String rarName;
	String rarPath;
	Drawable icon;
	RAREntry ent;
	boolean isFile;
	String fileType;
	
	
	public RarObj(RAREntry entry , String name , String path , Context ctx) {
		// TODO Auto-generated constructor stub
		this.ent = entry;
		this.rarName = name;
		this.rarPath = path;
		this.isFile = checkForFile();
		fileType = getType(rarName, ctx);
	}

	/**
	 * 
	 * @return
	 */
	public String getFileName(){
		return this.rarName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPath(){
		return this.rarPath;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFile(){
		return this.isFile;
	}
	
	/**
	 * 
	 * @return
	 */
	public Drawable getIcon(){
		return this.icon;
	}
	
	/**
	 * 
	 * @return
	 */
	public RAREntry getRAREntry(){
		return this.ent;
	}
	
	/**
	 * 
	 */
	public String getFileType(){
		return this.fileType;
	}
	
	private boolean checkForFile(){
		String str = ent.getName().substring(rarPath.length(), ent.getName().length());
		if(str.startsWith("/"))
			str = str.substring(1, str.length());
		if(str.contains("/"))
			return false;
		return true;
	}
	
	/**
	 * 
	 * @param Name
	 * @return
	 */
	private String getType(String Name , Context ctx){
		
		/*
		 * ITS DIRECTORY CHECK......
		 */
		if(!isFile())
			return ctx.getString(R.string.directory);
		
		/*
		 * NOW FILE CHECK FOR THEIR TYPES....
		 */
		
		if(Name.endsWith(".jpg")||Name.endsWith(".JPG")|| Name.endsWith(".png") || Name.endsWith(".PNG") || Name.endsWith(".gif") || Name.endsWith(".GIF")
				|| Name.endsWith(".JPEG") || Name.endsWith(".jpeg") ||Name.endsWith(".bmp") ||Name.endsWith(".BMP")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_images);
			return ctx.getString(R.string.image);
		}	
		else if(Name.endsWith(".zip") || Name.endsWith(".ZIP")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_zip_it);
			return ctx.getString(R.string.zip);
		}	
		else if( Name.endsWith("mhtml")||Name.endsWith(".MHTML")||  Name.endsWith(".HTM") || Name.endsWith(".htm") 
				||Name.endsWith(".html") || Name.endsWith(".HTML")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_web_pages);
			return ctx.getString(R.string.web);
		}			
		else if(Name.endsWith(".tar") || Name.endsWith(".TAR") || Name.endsWith(".rar") 
				|| Name.endsWith("RAR") || Name.endsWith(".7z") || Name.endsWith(".7Z")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_rar);
			return ctx.getString(R.string.compr);
		}	
		else if(Name.endsWith(".apk") || Name.endsWith(".APK")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_apk);
			return ctx.getString(R.string.application);
		}	
		else if(Name.endsWith(".mp3") || Name.endsWith(".MP3") || Name.endsWith(".amr") || Name.endsWith(".AMR")
				|| Name.endsWith(".ogg") || Name.endsWith(".OGG")||Name.endsWith(".m4a")||Name.endsWith(".M4A")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_music);
			return ctx.getString(R.string.music);
		}	
		else if(Name.endsWith(".doc") ||Name.endsWith(".DOC")
				|| Name.endsWith(".DOCX") || Name.endsWith(".docx") || Name.endsWith(".ppt") || Name.endsWith(".PPT")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_ppt);
			return ctx.getString(R.string.document);
		}	
		else if(Name.endsWith(".txt") || Name.endsWith(".TXT") || Name.endsWith(".inf") || Name.endsWith(".INF")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_text);
			return ctx.getString(R.string.text);
		}	
		else if(Name.endsWith(".mp4") || Name.endsWith(".MP4") || Name.endsWith(".avi") ||Name.endsWith(".AVI")
				|| Name.endsWith(".FLV") || Name.endsWith(".flv") || Name.endsWith(".3GP") || Name.endsWith(".3gp")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_video);
			return ctx.getString(R.string.vids);		
		}	
		else if(Name.endsWith(".default")||Name.endsWith(".prop")||Name.endsWith(".rc")||Name.endsWith(".sh")||Name.endsWith("init")){
			this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_sh);
			return ctx.getString(R.string.script);
		}	
		this.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_unknown);
		return ctx.getString(R.string.unknown);
	}
	
}