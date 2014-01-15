package org.deel.controllers;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FileForm {

 
	    private List<MultipartFile> files;
	    private String path;
	    
		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public List<MultipartFile> getFiles() {
			return files;
		}

		public void setFiles(List<MultipartFile> files) {
			this.files = files;
		}
	     
	    
	
}
