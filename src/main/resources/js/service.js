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
			success : cb,
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
			$.get('folder/remove', {id:f.id}, function(data){
				cb(f, data);}
			);
		},
		
		remove : function (fp, cb) {
			$.get('file/remove', {id:fp.id}, function(data){
				cb(fp, data);
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
			).done(function (teamXHR, userXHR) {
				teams = teamXHR[0].teams;
				users = userXHR[0].users;
				
				var sharing = $("#sharingList");
				var insertUser = $("<input></input>");
				insertUser.attr("id", "inputSharing");
				var sharingWith = $("<div></div");
				sharing.append(sharingWith);
				sharingWith.attr("id", "sharingWith");
				sharingWith.before(insertUser);
				var sender = $("<button>Share!</button>");
				sender.attr("id", "sender");
				sharingWith.append(sender);
				var usersSharing = new Array();
				var teamsSharing = new Array();
				
				users.forEach(function (u) {
					u.label = u.username;
					u.type = "user";
				});
				
				teams.forEach(function (t) {
					t.label = t.name;
					t.type = "team";
				});
				
				insertUser.autocomplete({
					source : users.concat(teams),
					select : function(e, ui) {
						if (ui.item.type == "user")
							usersSharing.push(ui.item.id);
						else if (ui.item.type == "team")
							teamsSharing.push(ui.item.id);
			
						var sharingWith = $("#sharingWith");
						var p = $("<p></p>");
						p.html(ui.item.label);
						sharingWith.append(p);
						$("#inputSharing").val("");
						return false;
					}
				});
					
				sender.click(function() {
					
					var message = new Object();

					message.users = usersSharing;
					message.teams = teamsSharing;
					message.file = fp.id;
					
					console.log(message);

					$.ajax({
						url : "file/share",
						type : 'POST',
						data : JSON.stringify(message),
						dataType : "json",
						contentType : "application/json",
						success : function(returndata) {
							alert("File Shared!");
						}
					});

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