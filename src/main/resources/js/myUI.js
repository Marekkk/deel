var myUI = (function($, service) {
	var currentFolder;

	var wrapper;
	var uploadDiv;
	var progress;

	var showHidden = false;
	
	var opsImageUrls = {
		remove : '/deel/resources/img/remove.png',
		revision : '/deel/resources/img/revision.png',
		share : '/deel/resources/img/share.png',
	};

	function _bytesToSize(bytes) {
	    var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
	    if (bytes == 0) return 'n/a';
	    var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
	    if (i == 0) return bytes + ' ' + sizes[i];
	    return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
	};
	
	return {

		init : function() {
			jQuery.event.props.push("dataTransfer");

			uploadDiv = myUI.createUploadDiv({
				cssClass : "upload",
				cssClassHover : "uploadHover",
			});

		},

		progressStart : function() {
			progress.show();
		},

		progressStop : function() {
			progress.hide();
		},

		setWrapper : function(w) {
			wrapper = w;
		},

		getWrapper : function() {
			return wrapper;
		},
		onNewFile : function(returndata) {

			myUI.progressStop();
			if (returndata.status != "success") {
				alert("error uploading");
				return;
			}
			returndata.files.forEach(function(fp) {
				var oldDiv = $('#FP_' + fp.id);
				var newDiv = myUI.makeDivFromFilePath(fp);

				if (oldDiv.length > 0) {
					oldDiv.replaceWith(newDiv);
				} else {
					$('#wrapper').append(newDiv);
				}
			});

		},
		
		share : function (fp, teamXHR, userXHR) {
				
			myUI.progressStop();
			teams = teamXHR[0].teams;
			users = userXHR[0].users;
			
			var sharing = $("#sharingList");
			var insertUser = $("<input></input>");
			insertUser.attr("id", "insertUser");
			var sharingWith = $("<div></div>");
			sharingWith.attr("id", "sharingWith");
			sharing.append(sharingWith);
			$("#sharingWith").before(insertUser);
			var sender = $("<button>Share!</button>");
			sender.attr("id", "sender");
			sharing.append(sender);
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
				}
			});
				
			sender.click(function() {
				
				var message = new Object();

				message.users = usersSharing;
				message.teams = teamsSharing;
				message.file = fp.id;
			

				$.ajax({
					url : "file/share",
					type : 'POST',
					data : JSON.stringify(message),
					dataType : "json",
					contentType : "application/json",
					success : function(returndata) {
						alert("File Shared!");
						$("#insertUser").attr("value", "");
					}
				});

			});
			$('#sharingList').dialog("open");
		},
			
		updateSpace : function() {

			var newWrapper = $('<div id="wrapper"></div>');
			myUI.createUploadDiv({
				div : newWrapper,
			});

			var url = "file/list"
					+ (myUI.getCurrentFolder() ? "?path="
							+ myUI.getCurrentFolder().id : "");
			$.get(url, function(data) {
				myUI.setCurrentFolder(data.me);

				var firstRow = $("<div></div>");
				firstRow.addClass("first");
				firstRow.attr('id', 'firstRow');

				var controls = $("<div></div>");
				controls.addClass("controls");
				controls.attr("id", "controls");

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
					myUI.progressStart();
					service.uploadFilesFromInput(this.files, myUI
							.getCurrentFolder(), myUI.onNewFile);
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
						myUI.progressStart();
						service.newFolder(folderName.val(), myUI
								.getCurrentFolder(), function(returndata) {
							myUI.progressStop();
							newFolder.show();
							folderName.hide();
							if (returndata.status == "success") {
								myUI.onNewFolder(returndata);

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
				
				showHidden = false;
				undelete.click(function() {
					showHidden = !showHidden;
					if (showHidden) {
						$('.hidden').show();
					}
					else {
						$('.hidden').hide();
					}
				});

				controls.append(undelete);

				firstRow.append(controls);

				progress = $("<img></img>");
				progress.prop('src', '/deel/resources/img/progress.gif');
				progress.css("float", "right");
				progress.hide();
				firstRow.append(progress);

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

			div.attr("id", "FP_" + fp.id);

			if (fp.hidden) {
				div.addClass("hidden");
			}

			var name = $("<div></div>");
			var info = $("<div></div>");
			var ops = $("<div></div>");

			div.addClass("file");
			ops.addClass("ops");

			var icon = $("<img></img>");
			icon.addClass("icon");
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
			

			var size = _bytesToSize(fp.size);
			info.append($("<span>" + size + "</span>"));
			info.append($("<span>" + fp.uploadedBy + "</span>"));

			"remove revision share".split(' ').forEach(function(op) {
				var img = $("<img></img>");
				img.prop('src', opsImageUrls[op]);
				img.click(function() {
					myUI.progressStart();
					service[op](fp, myUI[op]);
				});
				ops.append(img);
			});

			div.append(name);
			div.append(info);
			div.append(ops);

			return div;

		},

		remove : function(fp, data) {
			if (fp.hidden) {
				myUI.progressStop();
				$("#FP_" + fp.id).remove();
				return;
			}
			myUI.progressStop();
			$('#FP_' + fp.id).addClass("hidden");
			if (showHidden)
				$('#FP_' + fp.id).show();
		},

		removeFolder : function(f, data) {
			if (f.hidden) {
				myUI.progressStop();
				$("#F_" + f.id).remove();
				return;
			}
			myUI.progressStop();
			$('#F_' + f.id).addClass("hidden");
			if (showHidden)
				$('#F_' + f.id).show();

		},

		
		revision : function(fp, returndata) {
			myUI.progressStop();
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
			$('#revision').dialog("open");
		},
		makeDivFromFolder : function(f) {

			var div = $("<div></div>");

			div.attr("id", "F_" + f.id);

			if (f.hidden) {
				div.addClass("hidden");
			}

			var name = $("<div></div>");
			var ops = $("<div></div>");

			div.addClass("file");
			ops.addClass("ops");
			var holder = $("<div></div>");

			name.html(f.name);
			name.css("cursor", "pointer");
			name.click(function() {
				myUI.setCurrentFolder(f);
				myUI.updateSpace();
			});

			var icon = $("<img></img>");
			icon.prop('src', "/deel/resources/img/folder.png");
			icon.addClass("icon");
			div.append(icon);

			var img = $("<img></img>");
			img.prop('src', opsImageUrls["remove"]);
			img.click(function() {
				myUI.progressStart();
				service.removeFolder(f, myUI.removeFolder);
			});
			ops.append(img);

			div.append(name);
			div.append(ops);

			return div;

		},

		onNewFolder : function(returndata) {
			myUI.progressStop();
			if ("status" in returndata && returndata.status == "success") {
				var div = myUI.makeDivFromFolder(returndata.folder);
				$('#firstRow').after(div);
			}
		},

		getCurrentFolder : function() {
			return currentFolder;
		},

		setCurrentFolder : function(f) {
			currentFolder = f;
		},

		createTable : function(opts, data) {
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
					body.append(tr);
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
			var erase = $("<button>remove</button>");
			erase.css('margin-left', '10px');
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
			var div = $("<div></div>");
			var newC = $("<button> Add </button>");
			var input = $("<input type='text'></input>");
			var button = $("<button>Add</button>");

			div.append(newC);
			div.append(input.hide());
			div.append(button.hide());

			input.prop("disable", true);

			button
					.click(function() {

						$
								.ajax({
									url : 'new',
									type : 'POST',
									data : JSON.stringify({
										id : null,
										name : input.val()
									}),
									contentType : "application/json",
									dataType : "json",
									success : function(returndata) {
										if (returndata.status == "success") {
											var tr = $("<tr></tr>");
											var nr = myUI
													.createRowForCompanyAdmin(returndata.company);
											tr.append(nr);
											input.closest('tbody').append(tr);
											newC.show();
											button.hide();
											input.hide();
										}
									}
								});

					});

			newC.click(function() {
				input.show();
				button.show();
				input.focus();
				newC.hide();

				input.keyup(function(e) {
					if (e.keyCode == 27) {
						input.hide();
						button.hide();
						newC.show();
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

			holder.on('drop',
					function(e) {
						e.preventDefault();
						this.className = cssClass;
						myUI.progressStart();
						service.uploadFiles(e, myUI.getCurrentFolder(),
								myUI.onNewFile);
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
	};
})($, service);
