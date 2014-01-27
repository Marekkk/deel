var service = (function ($) {
	
	var idown;  
	
	function _uploadFiles(files, currentFolder) {
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
			success : function(returndata) { 
					myUI.updateSpace();
			}
		});
	}
	
	return {
		uploadFiles : function (e, currentFolder) {
			var files = e.dataTransfer.files;
			_uploadFiles(files, currentFolder);
			 
		},
		
		uploadFilesFromInput : function (files, currentFolder) {
			return _uploadFiles(files, currentFolder);
		},
		
		downloadFile: function (fp) {
			var url = 'file/download/'+fp.name+'?id='+fp.id;
				if (idown) {
			    idown.attr('src', url);
			  } else {
			    idown = $('<iframe>', { id:'idown', src:url }).hide().appendTo('body');
			  }
				
			},
			
		removeFolder : function (f) {
			$.get('folder/remove', {id:f.id}, function(){myUI.updateSpace();});
		},
		
		remove : function (fp) {
			$.get('file/remove', {id:fp.id}, function(){myUI.updateSpace();});
		},
		revision : function (fp) {
			/* stub */
			$.get("file/revision/list",
					{id:fp.id}, function(returndata) {
					var dates = new Array();
					var idRevs = new Array();
					for ( var i in returndata) {
						// i is the id of current revision
						d = new Date(returndata[i].date);
						var currPos = dates.length;
						dates[currPos] = d;
						idRevs[currPos] = i;
					}
					createRevisionsTable(fp, idRevs, dates);
				});

			$('#revision').dialog("open");	
		},
		share : function (fp) {
			
			var ul = document.getElementById("slist");
			$(ul).remove();
			var sharingDiv = document.getElementById("sharingList");
			var nul = document.createElement("ul");
			nul.id = "slist";
			sharingDiv.appendChild(nul);
			$.ajax({
				url : "user/list",
				type : 'GET',
				success : function(returndata) {
					var users = returndata;
					createShareBox(users.id, users.Username, fp.id);
				}
			});
			var button = document.createElement("input");
			button.id = "sendButton";
			button.type = "button";
			button.value = "Share File!";
			button.innerHTML = "Share File!";
			nul.appendChild(button);
			$('#sendButton').click(function() {
				alert("File Shared!");
				//alert("You' re sharing " + id + " with " + usersWithShare);
				var message = new Object();

				message.users = usersWithShare;
				message.file = fp.id;

				$.ajax({
					url : "file/share",
					type : 'POST',
					data : JSON.stringify(message),
					dataType : "json",
					contentType : "application/json",
					success : function(returndata) {
					}
				});

			});
			$('#sharingList').dialog("open");
		},
		
		newFolder : function (folderName, currentFolder, cb) {
	
			$.get('file/addFolder', 
					{id: (currentFolder.father)?currentFolder.father.id:null, 
				    folderName: folderName}, 
				    cb);
		},
		
	};
	

	
})($);