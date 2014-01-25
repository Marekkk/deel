var myUI = (function ($) {
	var currentFolder;
	
	
	return  {

		init : function(opts) {
			this.opts = opts;
			jQuery.event.props.push("dataTransfer");
			/* stub */
			currentFolder = sessionStorage.getItem("dir");
			
		}, 
		
		getCurrentFolder: function() {
			return currentFolder;
		},

		setCurrentFolder: function (f) {
			currentFolder = f;
		},
		
		createTable : function (opts, data) {

			
			var t = $("<table></table>");
			
			if (opts["tableClassName"]) 
						t.addClass(opts.tableClassName);

			if ("head" in opts) {
				var thead = $("<thead></thead>");
				var thr = $("<tr></tr>");
				thead.append(thr);
				opts.head.forEach(function (th) {
					thr.append($("<td>"+th+"</td>"));
				});
				t.append(thead);
			}
			
			if("firstRow" in opts)
				t.append(opts.firstRow);
			
			if("data" in opts)
			opts.data.forEach(function(r)  {
				var tr = $("<tr></tr>");
				if (opts.cbRow)
					tr.append(opts.cbRow(r));
				else if (r instanceof Object)
					tr.append(myUI.createDivGeneral(r));
				t.append(tr);
			});
	
			console.log(t);
			return t;
		},
		
		createDivGeneral: function(o) {
			var div = $("<div></div>");
			for (var i in o) {
				var p = $("<span>" + o[i] + "</span>");
				div.append(p);
			}
			return div;
		},
		
		createRowForCompanyAdmin : function(o) {
			var tr = $("<td></td>");
			var div = $("<div></div>");
			div.className = "companyDiv";
			
			var name = $("<span>"+o.name+"<span>");

			var input = $("<input type='text'></input>");
			
			input.val(o.name);
			
			input.keyup(function(e){
				if(e.keyCode == 27) {
					input.hide();
					name.show();
				}
				if (e.keyCode == 13) {
					input.prop("disable", true);
				
					$.ajax({
						url : 'edit',
						type : 'POST',
						data : JSON.stringify({id:o.id, name:input.val()}),
						contentType : "application/json",
						dataType : "json",
						success : function(returndata) {
							if (returndata.status == "success") {
								name.html(input.val());
								name.show();
								input.hide();
							}
						}
					});
				}
			});
			
			div.append(input);
			div.append(name);

			input.hide();

			div.click(function() {
				input.focus();
				name.hide();
				input.show();
			});
			
			tr.append(div);
			var erase = $("<a>X</a>");
			div.append(erase);
			erase.click(function() {
				$.ajax({
					url : 'remove',
					type : 'POST',
					data : JSON.stringify({id:o.id, name:o.name}),
					contentType : "application/json",
					dataType : "json",
					success : function(returndata) {
						if (returndata.status == "success") {
							tr.remove();
						}
					}
				});
			});
			return tr;
		},
		
		createFirstRowForCompany: function () {
			
			var div = $("<div></div>");
			var newC = $("<span> Add... </span>");
			var input = $("<input type='text'></input>");
			
			
			div.append(newC);
			div.append(input.hide());
			
			newC.click(function() {
				input.show();
				input.focus();
				newC.hide();
				
				input.keyup(function(e){
					if(e.keyCode == 27) {
						input.hide();
						newC.show();
					}
					if (e.keyCode == 13) {
						input.prop("disable", true);
					
						$.ajax({
							url : 'new',
							type : 'POST',
							data : JSON.stringify({id: null, name:input.val()}),
							contentType : "application/json",
							dataType : "json",
							success : function(returndata) {
								if (returndata.status == "success") {
									var nr = myUI.createRowForCompanyAdmin(returndata.company);
									input.closest('table').append(nr);
									newC.show();
									input.hide();
								}
							}
						});
					}
				});
				
			});
			var tr = $("<tr></tr>");
			var td = $("<td></td>");
			td.append(div);
			tr.append(td);
			return tr;
			
		},
		
		createUploadDiv: function (opts) {
			if(!opts.uploadCB) {
				return;
			}
			
			var holder = $("<div></div>");
			
			var cssClass = "cssClass" in opts ? opts.cssClass : '';
			var cssClassHover = "cssClassHover" in opts ? opts.cssClassHover : cssClass;
			
			
			/* enable drag and drop */
			/* check that they are files */
			holder.on('dragover', function () { this.className = cssClass; console.log("dragover");return false; });
			holder.on('dragend' , function () { this.className = cssClassHover;console.log("dragend"); return false; });
			
			
			holder.on('drop', function (e) {
				e.preventDefault();
				this.className  = cssClass;
				console.log(e);
				opts.uploadCB(e);
			});
			return holder;
			
		},
		postWithAjax : function (url, cb, data) {
			$.ajax({
				url : url,
				type : 'POST',
				data : JSON.stringify(data),
				contentType : "application/json",
				dataType : "json",
				success : cb(data),
			});
		},
		
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
		
	};
})($);

	
	

