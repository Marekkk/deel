package org.deel.controllers;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FileForm {

 
	    private List<MultipartFile> files;

	    private long path;
	    

		public long getPath() {
			return path;
		}

		public void setPath(long path) {
			this.path = path;
		}

		public List<MultipartFile> getFiles() {
			return files;
		}

		public void setFiles(List<MultipartFile> files) {
			this.files = files;
		}
	     
	    
	
}
