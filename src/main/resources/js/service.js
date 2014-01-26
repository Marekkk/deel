var service = (function ($) {
	
	
	
	return {
		uploadFiles : function (e) {
			var files = e.dataTransfer.files;
			var fd = new FormData();
			console.log(files);
			for (var i = 0; i < files.length; i++) 
			      fd.append('files', files[i]);
			
			fd.append('path', currentFolder);
			
			$.ajax({
				/* TODO set correct URL */
				//url : "<c:url value="/file/upload"/>",
				url : "file/upload",
				type : 'POST',
				data : fd,
				cache : false,
				contentType : false,
				async : false,
				processData : false,
				success : function(returndata) {
					getFiles();
				}
			});
			 
			

		},
		
		downloadFile: function (id) {
			console.log("donwloading file with id " + id);
		},
		removeFolder : function (id) {
			console.log("removing folder with id " + id);
		},
		
		changeDir: function(id) {
			console.log("changing to dir with id " + id);
		},
		remove : function (id) {
			console.log("removing id " +id);
		},
		revision : function (id) {
			console.log("revision id " +id);	
		},
		share : function (id) {
			console.log("sharing id" + id);
		},
		
	};
	

	
})($);