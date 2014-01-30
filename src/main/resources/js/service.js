var service = (function ($) {
	
	var idown;  
	
	function _uploadFiles(files, currentFolder, cb) {
		var fd = new FormData();
		

		for (var i = 0; i < files.length; i++) 
		      fd.append('files', files[i]);
		
		fd.append('path', currentFolder.id);
		
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
			success : cb
		});
	}
	
	return {
		uploadFiles : function (e, currentFolder, cb) {
			var files = e.dataTransfer.files;
			_uploadFiles(files, currentFolder, cb);
			 
		},
		
		uploadFilesFromInput : function (files, currentFolder, cb) {
			return _uploadFiles(files, currentFolder, cb);
		},
		
		downloadFile: function (fp) {
			var url = 'file/download/'+fp.name+'?id='+fp.id;
				if (idown) {
			    idown.attr('src', url);
			  } else {
			    idown = $('<iframe>', { id:'idown', src:url }).hide().appendTo('body');
			  }
				
			},
			
		removeFolder : function (f, cb) {
			if (f.hidden) {
				$.get('folder/removeFromTrash', {id:f.id}, function(data){
					cb(f, data);}
				);
				return;
			}
			$.get('folder/remove', {id:f.id}, function(data){
				cb(f, data);
				f.hidden = true;
			});
		},
		
		remove : function (fp, cb) {
			if (fp.hidden) {
				$.get('file/removeFromTrash', {id:fp.id}, function(data){
					cb(fp, data);
					});
				return;
			}
			
			$.get('file/remove', {id:fp.id}, function(data){
				cb(fp, data);
				fp.hidden = true;
				});
		},
		revision : function (fp, cb) {
			/* stub */
			$.get("file/revision/list",
					{id:fp.id}, function(returndata) {
						cb(fp, returndata);
					});
		},
		share : function (fp, cb) {
			
			var teams;
			var users;
			
			$.when(
				$.get("team/list"),
				$.get("user/list")
			).done(function(a,b){cb(fp,a,b);});
		},
		
		newFolder : function (folderName, currentFolder, cb) {
	
			$.get('file/addFolder', 
					{id: currentFolder.id, 
				    folderName: folderName}, 
				    cb);
		},
		
	};
	

	
})($);