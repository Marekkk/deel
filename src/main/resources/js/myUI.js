var myUI = (function($, service) {
	var currentFolder;

	var wrapper;
	var uploadDiv;

	var opsImageUrls = {
		remove : '/deel/resources/img/remove.png',
		revision : '/deel/resources/img/revision.png',
		share : '/deel/resources/img/share.png',
	};

	return {

		init : function() {
			jQuery.event.props.push("dataTransfer");

			uploadDiv = myUI.createUploadDiv({
				cssClass : "upload",
				cssClassHover : "uploadHover",
			});

		},

		setWrapper : function(w) {
			wrapper = w;
		},

		getWrapper : function() {
			return wrapper;
		},

		updateSpace : function() {

			var newWrapper = $('<div id="wrapper"></div>');
			myUI.createUploadDiv( {
				div: newWrapper,
			});
			
			var url = "file/list"
					+ (myUI.getCurrentFolder() ? "?path="
							+ myUI.getCurrentFolder().id : "");
			$.get(url, function(data) {
				myUI.setCurrentFolder(data.me);

				var firstRow = $("<div></div>");
				firstRow.addClass("first");

				var controls = $("<div></div>");
				controls.addClass("controls");

				if (data.me.father) {
					var img = $("<img></img>");

					img.prop('src', '/deel/resources/img/back.png');
					img.click(function() {
						myUI.setCurrentFolder(data.me.father);
						myUI.updateSpace();
					});

					controls.append(img);
				}

				var newFile = $("<img></img>")
				newFile.prop('src', '/deel/resources/img/upload.png');
				newFile.click(function() {
					inputFile.trigger('click');
				});

				var inputFile = $("<input type='file'></input>");
				inputFile.on('change', function(e) {
					service.uploadFilesFromInput(this.files, myUI
							.getCurrentFolder());
				});

				inputFile.hide();

				controls.append(newFile);
				controls.append(inputFile);

				var newFolder = $("<img></img>");
				newFolder.prop('src', '/deel/resources/img/newfolder.png')

				var folderName = $("<input type='text'></input>");
				folderName.hide();

				folderName.keyup(function(e) {
					if (e.keyCode == 27) {
						folderName.val('');
						folderName.hide();
						newFolder.show();
					}
					if (e.keyCode == 13) {
						folderName.prop("disable", true);

						service.newFolder(folderName.val(), myUI
								.getCurrentFolder(), function(returndata) {
							if (returndata.status == "success") {
								newFolder.show();
								folderName.hide();
							}
						});

					}
				});

				newFolder.click(function() {
					newFolder.hide();
					folderName.show();
					folderName.focus();
				});

				controls.append(newFolder);
				controls.append(folderName);

				var undelete = $("<img></img>");
				undelete.prop('src', '/deel/resources/img/undelete.png');
				
				undelete.click(function(){
					$('.hidden').toggle();
				});
				
				controls.append(undelete);

				firstRow.append(controls);
				newWrapper.append(firstRow);

				data.folders.forEach(function(f) {
					newWrapper.append(myUI.makeDivFromFolder(f));
				});

				data.filePaths.forEach(function(f) {
					newWrapper.append(myUI.makeDivFromFilePath(f));
				});

			});

			wrapper.replaceWith(newWrapper);
			wrapper = newWrapper;
		},

		makeDivFromFilePath : function(fp) {
			var now = new Date();
			var div = $("<div></div>");

			if (fp.hidden) {
				div.addClass("hidden");
			}

			var name = $("<div></div>");
			var info = $("<div></div>");
			var ops = $("<div></div>");

			div.addClass("file");
			ops.addClass("ops");

			var icon = $("<img></img>");
			icon.prop('src', "/deel/resources/img/file.png");

			div.append(icon);

			name.html(fp.name);
			name.css("cursor", "pointer");
			name.click(function() {
				service.downloadFile(fp);
			});

			var time = $("<span class='time'></span>");

			var lastModified = new Date(fp.lastModified);
			if (lastModified.getDate() == now.getDate()
					&& lastModified.getMonth() == now.getMonth())
				time.html(lastModified.toLocaleTimeString());
			else
				time.html(lastModified.toLocaleString());

			info.append(time);
			info.append($("<span>" + fp.size + "</span>"));
			info.append($("<span>" + fp.uploadedBy + "</span>"));

			"remove revision share".split(' ').forEach(function(op) {
				var img = $("<img></img>");
				img.prop('src', opsImageUrls[op]);
				img.click(function() {
					service[op](fp);
				});
				ops.append(img);
			});

			div.append(name);
			div.append(info);
			div.append(ops);

			return div;

		},

		makeDivFromFolder : function(f) {

			var div = $("<div></div>");

			if (f.hidden) {
				div.addClass("hidden");
			}

			var name = $("<div></div>");
			var ops = $("<div></div>");

			div.addClass("file");
			ops.addClass("ops");var holder = $("<div></div>");

			name.html(f.name);
			name.css("cursor", "pointer");
			name.click(function() {
				myUI.setCurrentFolder(f);
				myUI.updateSpace();
			});

			var icon = $("<img></img>");
			icon.prop('src', "/deel/resources/img/folder.png");
			div.append(icon);

			var img = $("<img></img>");
			img.prop('src', opsImageUrls["remove"]);
			img.click(function() {
				service.removeFolder(f);
			});
			ops.append(img);

			div.append(name);
			div.append(ops);

			return div;

		},

		getCurrentFolder : function() {
			return currentFolder;
		},

		setCurrentFolder : function(f) {
			currentFolder = f;
		},

		createTable : function(opts, data) {
			debugger;
			var t = $("<table></table>");

			if (opts["tableClassName"])
				t.addClass(opts.tableClassName);

			if ("head" in opts) {
				var thead = $("<thead></thead>");
				var thr = $("<tr></tr>");
				thead.append(thr);
				opts.head.forEach(function(th) {
					thr.append($("<th>" + th + "</th>"));
				});
				t.append(thead);
			}
			var body = $("<tbody></tbody>");
			if ("firstRow" in opts)
				body.append(opts.firstRow);

			if ("data" in opts)
				opts.data.forEach(function(r) {
					var tr = $("<tr></tr>");
					if (opts.cbRow)
						tr.append(opts.cbRow(r));
					else if (r instanceof Object)
						tr.append(myUI.createDivGeneral(r));
					t.append(tr);
				});

			if ("dataCB" in opts) {
			
				opts.dataCB(body);
			}
			
			t.append(body);
			console.log(t);
			return t;
		},

		createDivGeneral : function(o) {
			var div = $("<div></div>");
			for ( var i in o) {
				var p = $("<span>" + o[i] + "</span>");
				div.append(p);
			}
			return div;
		},

		createRowForCompanyAdmin : function(o) {
			var tr = $("<td></td>");
			var div = $("<div></div>");
			div.className = "companyDiv";

			var name = $("<span>" + o.name + "<span>");

			var input = $("<input type='text'></input>");

			input.val(o.name);

			input.keyup(function(e) {
				if (e.keyCode == 27) {
					input.hide();
					name.show();
				}
				if (e.keyCode == 13) {
					input.prop("disable", true);

					$.ajax({
						url : 'edit',
						type : 'POST',
						data : JSON.stringify({
							id : o.id,
							name : input.val()
						}),
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
					data : JSON.stringify({
						id : o.id,
						name : o.name
					}),
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

		createFirstRowForCompany : function() {
			debugger;
			var div = $("<div></div>");
			var newC = $("<span> Add... </span>");
			var input = $("<input type='text'></input>");

			div.append(newC);
			div.append(input.hide());

			newC.click(function() {
						input.show();
						input.focus();
						newC.hide();

						input.keyup(function(e) {
									if (e.keyCode == 27) {
										input.hide();
										newC.show();
									}
									if (e.keyCode == 13) {
										input.prop("disable", true);

									$.ajax({
													url : 'new',
													type : 'POST',
													data : JSON.stringify({
														id : null,
														name : input.val()
													}),
													contentType : "application/json",
													dataType : "json",
													success : function(
															returndata) {
														if (returndata.status == "success") {
															var nr = myUI
																	.createRowForCompanyAdmin(returndata.company);
															input.closest(
																	'table')
																	.append(nr);
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

		createUploadDiv : function(opts) {
			var holder = $("<div></div>");
			if ("div" in opts) 
				holder = opts.div; 
			

			var cssClass = "cssClass" in opts ? opts.cssClass : '';
			var cssClassHover = "cssClassHover" in opts ? opts.cssClassHover
					: cssClass;

			holder.addClass(cssClass);
			/* enable drag and drop */
			/* check that they are files */
			holder.on('dragover', function() {
				this.className = cssClass;
				return false;
			});
			holder.on('dragend', function() {
				this.className = cssClassHover;
				return false;
			});

			holder.on('drop', function(e) {
				e.preventDefault();
				this.className = cssClass;
				service.uploadFiles(e, myUI.getCurrentFolder());
			});
			return holder;

		},

		postWithAjax : function(url, cb, data) {
			$.ajax({
				url : url,
				type : 'POST',
				data : JSON.stringify(data),
				contentType : "application/json",
				dataType : "json",
				success : cb(data),
			});
		},

	// function addingOps(data, tr) {
	// var r = tr;
	// var c = document.createElement("td");
	// c.style = "background-color: transparent";
	// var a = document.createElement("a");
	// var img = document.createElement("img");
	// img.src = "<c:url value="/resources/img/remove.png"/>";
	// img.height = "50";
	// img.width = "75";
	// a.appendChild(img);
	// var id = data.id;
	// var type = data.type;
	//
	// a.type = type;
	// a.id = "opRemove";
	// a.href = "javascript:removeFile(" + id + ", '" + type + "')";
	// a.style = "text-decoration: none";
	// //a.innerHTML = "Remove";
	// c.appendChild(a);
	// r.appendChild(c);
	//
	//
	// if (type == "file") {
	// var cs = document.createElement("td");
	// cs.style = "background-color: transparent";
	// var share = document.createElement("a");
	// var idOpShare = "share_" + id;
	// share.id = idOpShare;
	// share.className = "opShare";
	// share.href = "javascript:sharing(" + id + ")";
	// share.style = "text-decoration: none";
	// //share.innerHTML = "Share";
	// var img = document.createElement("img");
	// img.src = "<c:url value="/resources/img/share.png"/>";
	// img.height = "40";
	// img.width = "40";
	// share.appendChild(img);
	// cs.appendChild(share);
	// r.appendChild(cs);
	//
	// var cr = document.createElement("td");
	// cr.style = "background-color: transparent";
	// var rev = document.createElement("a");
	// rev.id = "revision_" + id;
	// rev.href = "javascript:revision(" + id + ")";
	// rev.style = "text-decoration:none; color: blue";
	// var img = document.createElement("img");
	// img.src = "<c:url value="/resources/img/revision.png"/>";
	// img.height = "35";
	// img.width = "40";
	// rev.appendChild(img);
	// //rev.innerHTML = "Revisions";
	// cr.appendChild(rev);
	// r.appendChild(cr);
	// }
	// }
	// function getFiles() {
	// cleanTable("dataTable");
	// if (sessionStorage.getItem("dir") == null
	// || sessionStorage.getItem("dir") === undefined)
	// var request = "<c:url value="/file/list"/>";
	// else
	// var request = "<c:url value="/file/list?path="/>" +
	// sessionStorage.getItem("dir");
	//
	// makeRequest(request);
	// }
	//		
	// function makeRequest(request) {
	// var req = request;
	// $.get(req, function(data, success) {
	// console.log(success);
	//
	// console.log(data);
	// currentDir = data.currentDir;
	// if (req == "<c:url value="/file/list"/>") {
	// sessionStorage.setItem("root", currentDir.id);
	// sessionStorage.setItem("dir", currentDir.id);
	// } else {
	// sessionStorage.setItem("dir", currentDir.id);
	// }
	// files = data.files;
	// directories = data.directories;
	// filesHidden = data.filesHidden;
	// console.log(currentDir, filesHidden);
	// updateTable("dataTable");
	// });
	// }
	//		
	// function updateTable(tableId) {
	// for ( var i in directories) {
	// var a = document.createElement("a");
	// a.id = i;
	// a.value = i;
	// a.style = "color:red";
	// a.href = "javascript:changeFolder(" + i + ")";
	// a.innerHTML = directories[i];
	// a.type = "folder";
	// addRow(a, i, tableId);
	// }
	//
	// for ( var i in files) {
	// var a = document.createElement("a");
	// a.id = i;
	// a.href = "file/download/" + files[i] + "?id=" + i;
	// a.innerHTML = files[i];
	// a.type = "file";
	// addRow(a, i, tableId);
	// }
	// }

	};
})($, service);
