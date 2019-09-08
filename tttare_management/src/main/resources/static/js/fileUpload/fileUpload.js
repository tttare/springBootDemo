/**
 * @author xinshanlu 2016-6-13
 */
//需要 spark-md5.js jquery.json.min.js
var chunkSize = 2097152;//每个片段2M
/** cms转码服务支持的文件格式 */
var self_exts;
var self_domain = ["res.zjle.cn:21330","yun.dyedu.net:20002","res.sxedu.org:20002","yun.t.zjle.cn:20002","yun.t.zjle.cn:20002","res.yun.zjer.cn:20002"];
var localurl;
var bceurl;
// 计算文件MD5
function calcMD5(fileItem,cons) {
	self_exts = fileItem.ext;
	// 可以去读取用户本地文件的详细内容。
	var fileReader = new FileReader();
	//file的slice方法，注意它的兼容性，在不同浏览器的写法不同
	var blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice
			|| File.prototype.slice;
	var chunks = Math.ceil(fileItem.file.size /chunkSize);
	var currentChunk = 0;
    //依赖spark-md5
	var spark = new SparkMD5();
	//每一段文件处理完毕都会触发fileReader的onload事件
	fileReader.onload = function(e) {
		console.log("读取文件", currentChunk + 1, "/", chunks);
		// 每块交由sparkMD5进行计算 e.target.result就是片段信息
		spark.appendBinary(e.target.result);
		currentChunk++;

		// 如果文件处理完成计算MD5，如果还有分片继续处理
		if (currentChunk < chunks) {
			loadNext();
		} else {
		   fileItem.fileMd5 = spark.end();
		   console.log("md5:"+fileItem.fileMd5);
		   //计算完md5才开始上传
		   //此处判断是上传到cms服务器还是百度云
			if(self_domain.indexOf(cons.domainstr)!=-1||isToCms(fileItem.name)){
				startUpload(fileItem,cons);
			}else{
				bos_startUpload(fileItem,cons);
				//上传完后的设置对应表单值
			}
			var formValue='<input type="hidden" name="title" value="'+fileItem.name+'"/>'
			+'<input type="hidden" name="fileLength" value="'+fileItem.file.size+""+'"/>'
			+'<input type="hidden" name="md5" value="'+fileItem.fileMd5+'"/>'
			+'<input type="hidden" name="resType" value="1"/>'
			+'<input type="hidden" name="fileExt" value="'+getFileExt(fileItem.name)+'"/>';
			$("#"+fileItem.uid).html(formValue);
		}
	};

	function loadNext() {
		var start = currentChunk * chunkSize;
		var end = start + chunkSize >= fileItem.file.size ? fileItem.file.size : start
				+ chunkSize;
		//然后指定file和开始结束的片段，就可以得到切割的文件了。
		fileReader.readAsText(blobSlice.call(fileItem.file, start, end));
	}

	loadNext();
	
}
//百度云上传
function bos_startUpload(fileItem,cons) {
	localurl=cons.localurl;
	bceurl=cons.bceurl;
	
	// 判断能否急速秒传
	var data1 = {
		"userId": fileItem.userId,
		"fileMd5": fileItem.fileMd5,
		"postComplete": "true",
		"filePathLocal": fileItem.name
	};
	
	$.post(localurl+"/rapidUpload.action", data1, function(ret) {
		 ret = $.parseJSON($.toJSON(ret));
		// 接口调用失败
		if (ret.result == "100000") {
    		alert("上传失败!");
			return;
		}
		// 急速秒传
		if (ret.result == "000000") {
			var $process = $("#"+fileItem.uid).siblings(".py_processWrap");
			if ($process.length == 1) {
				$process.show();
				$process.find('.process').css("width", "100%");
				$process.find(".process").next("i").text("100%");
			}
			var fid = '<input type="hidden" name="fid" value="'
					+ ret.data.fid + "" + '"/>';
			$("#" + fileItem.uid).append(fid);
			setTimeout('removeProcess("' + fileItem.uid + '")', 1000);
			return ;
		}
		// 上传到百度云
		var data2 = {"fileMd5":fileItem.fileMd5, "userId":fileItem.userId, "fileName":fileItem.name};
		$.post(localurl+"/getUncompletedUpload.action", data2, function(ret) {
			// 接口调用失败
			if (ret.result == "100000") {
	    		alert("上传失败!");
				return;
			}
			// 断点续传
			if (ret.result == "000000") {
				var postedPercent = percentNum(ret.data.partNumber, ret.data.partCount);
				fileItem.partNumber = ret.data.partNumber;
				fileItem.uploadId = ret.data.uploadId;
				fileItem.postedPercent = postedPercent;
				fileItem.bosKey = ret.data.bosKey;
				fileItem.partSize = ret.data.partSize;
				fileItem.partCount = ret.data.partCount;
				fileItem.bucketName = ret.data.bucketName;
				bos_resumeUpload(fileItem);
				return;
			}
			
			// 新文件上传
			var data = {"fileMd5": fileItem.fileMd5,
				        "userId": fileItem.userId,
				        "fileLength":fileItem.file.size,
				        "fileName": fileItem.name};
			
			$.post(localurl+"/initiateMultipartUpload.action", data, function(ret) {
				if (ret.result == "000000") {
					var postedPercent = percentNum(ret.data.partNumber, ret.data.partCount);
					fileItem.partNumber = ret.data.partNumber;
					fileItem.uploadId = ret.data.uploadId;
					fileItem.postedPercent = postedPercent;
					fileItem.bosKey = ret.data.bosKey;
					fileItem.partSize = ret.data.partSize;
					fileItem.partCount = ret.data.partCount;
					fileItem.bucketName = ret.data.bucketName;
					 bos_resumeUpload(fileItem);
				} else {
    	    		alert("上传失败!");
				}
			}, "json").error(networkError);
		}, "json").error(networkError);
	}, "json").error(networkError);
	
	function networkError() {
		alert("网络异常！");
	}
}

//断点续传
function bos_resumeUpload(fileItem) {
	if (fileItem.partNumber == fileItem.partCount) {
		completeMultipartUpload(fileItem);
		return;
	}
	var xhr = new XMLHttpRequest();
	fileItem.xhr = xhr;
	xhr.onabort = function() {
		// 已暂停
	}
	xhr.onerror = function(e) {
		alert("网络异常");
	}
	xhr.onload = function () {
		if (xhr.status == "200") { // 上传成功，继续上传下一分片
			$.ajax({
				url: localurl+"/uploadPart.action?uploadId=" + fileItem.uploadId,
				type: "PUT",
				dataType: "json",
				success: function(ret, status, xhr) {
					if (ret.result == "000000") {
						fileItem.partNumber = ret.data.partNumber;
						if (fileItem.partNumber == fileItem.partCount) {
							completeMultipartUpload(fileItem);
						} else {
							uploadPart(fileItem);
						}
					} else {
	    	    		alert("上传失败!");
					}
				},
				error: function(xhr, error, exception) {
    	    		alert("网络异常!");
				}
			});
		} else { // 传失败了，重传该分片
			uploadPart(fileItem);
		}
	}

	xhr.upload.onprogress = function(e) {
		// 选用 如果文件分块大小较大 可以通过该方法判断单片文件具体的上传进度
		// e.loaded  该片文件上传了多少
		// e.totalSize 该片文件的总共大小
		 //console.log(e.loaded + "/" + e.totalSize);
		var loaded = e.loaded + (fileItem.partSize * fileItem.partNumber);
		var postedPercent = percentNum(loaded,fileItem.file.size);
		fileItem.postedPercent = postedPercent;
		var $process = $("#"+fileItem.uid).siblings(".py_processWrap");
		if ($process.length == 1) {
			$process.show();
			$process.find('.process').css("width", fileItem.postedPercent);
			$process.find(".process").next("i").text(fileItem.postedPercent);
		}
	}
	
	uploadPart(fileItem);
	
	function uploadPart(fileItem) {
		var start = fileItem.partNumber * fileItem.partSize;
		var end = start + fileItem.partSize >=fileItem.file.size ?fileItem.file.size : start + fileItem.partSize;
		var blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice;
		var part = blobSlice.call(fileItem.file, start, end);
		var url1 = bceurl+ "/" + fileItem.bucketName + "/" + fileItem.bosKey + "?partNumber=" + (fileItem.partNumber + 1) + "&uploadId=" + fileItem.uploadId;
		var data3={
				"contentLength":part.size,
				"bucketName":fileItem.bucketName,
				"bosKey":fileItem.bosKey,
				"partNumber": (fileItem.partNumber + 1) + "",
				"uploadId": fileItem.uploadId
		       };
		$.ajax({
			url: localurl+"/genAuthorization.action",
			data: data3,
			dataType: "json",
			type: "POST",
			success: function(ret, status, xhr) {
				if (ret.result == "000000") {
					var authorization = ret.authorization;
					fileItem.xhr.open('PUT', url1, true);
					fileItem.xhr.setRequestHeader("Authorization", authorization);
					fileItem.xhr.setRequestHeader("Content-Type","application/octet-stream");
					fileItem.xhr.setRequestHeader("x-bce-date", ret.date);
					fileItem.xhr.send(part);
				} else {
    	    		alert("上传失败!");
				}
			},
			error: function(xhr, error, exception) {
	    		alert("网络异常!");
			}
		});
	}
}

function completeMultipartUpload(fileItem) {
	$.ajax({
		url: localurl+"/completeMultipartUpload.action",
		type: "POST",
		data: {"uploadId":fileItem.uploadId},
		dataType: "JSON",
		success: function(ret) {
			if (ret.result == "000000") { // 上传完成
				var $process = $("#"+fileItem.uid).siblings(".py_processWrap");
				if ($process.length == 1) {
					$process.show();
					$process.find('.process').css("width", "100%");
					$prfileItem.extocess.find(".process").next("i").text("100%");
				}
				var fid = '<input type="hidden" name="fid" value="'
						+ ret.data.fid + "" + '"/>';
				$("#" + fileItem.uid).append(fid);
				setTimeout('removeProcess("' + fileItem.uid + '")', 1000);
			} else {
	    		alert("上传失败!");
				fileItem.status = "6";
			}
		},
		error: function() {
    		alert("网络异常!");
		}
	});
}

//CMS上传--------------------------------------------------------------开始
//开始上传
function startUpload(fileItem,cons) {
	localurl=cons.localurl;
	domainstr=cons.domainstr;
	
	// 判断能否急速秒传
	var data1 = {
		"userId": fileItem.userId,
		"fileMd5": fileItem.fileMd5,
		"postComplete": "true",
		"filePathLocal": fileItem.name
	};
	$.post(localurl+"/rapidUpload.action", data1, function(ret) {
		// 接口调用失败
		if (ret.result == "100000") {
			alert("上传失败!");
			fileItem.status = "6";
			return;
		}
		
		// 急速秒传
		if (ret.result == "000000") {
			var $process = $("#"+fileItem.uid).siblings(".py_processWrap");
			var fid;
			if ($process.length == 1) {
				$process.show();
				$process.find('.process').css("width", "100%");
				$process.find(".process").next("i").text("100%");
			}
			fid = '<input type="hidden" name="fid" value="' + ret.data.fid
					+ "" + '"/>';
			$("#"+fileItem.uid).append(fid);
			setTimeout('removeProcess("'+fileItem.uid+'")',1000);
			return;
		}

		// 上传到天喻云
		var data2 = {"fileMd5":fileItem.fileMd5, "userId":fileItem.userId, "postComplete":"false"};
		$.post("http://"+domainstr+"/cms-ft/getFileInfo", data2, function(ret) {
			// 接口调用失败
			if (ret.result == "100000") {
				alert("上传失败!");
				fileItem.status = "6";
				return;
			}
			
			// 断点续传
			if (ret.result == "000000") {
				fileItem.fid = ret.data.fid;
				fileItem.filePos = ret.data.filePos;
				fileItem.currentChunk = fileItem.filePos / chunkSize;
				fileItem.postedPercent = ret.data.postedPercent;
				fileItem.fileNameLocal=fileItem.name;
				//setFileName(ret.data.fileNameLocal);
				resumeUpload(fileItem,domainstr);
				return;
			}
			
			// 新文件上传
			var data = {
				"fileMd5": fileItem.fileMd5,
				"userId": fileItem.userId,
				"fileLength": fileItem.file.size,
				"filePathLocal": fileItem.name
			};
			
			$.post("http://"+domainstr+"/cms-ft/createFid", data, function(ret) {
				if (ret.result == "000000") {
					//$(".file_progress").text(ret.data.postedPercent);
				    fileItem.currentChunk = 0;
				    fileItem.fid = ret.data.fid;
				    fileItem.filePos = ret.data.filePos;
				    fileItem.postedPercent = ret.data.postedPercent;
				    fileItem.fileNameLocal=fileItem.name;
				    //setFileName(ret.data.fileNameLocal);
				    resumeUpload(fileItem,domainstr);
				} else {
					alert("上传失败!");
					fileItem.status = "6";
				}
			}, "json").error(networkError);
		}, "json").error(networkError);
	}, "json").error(networkError);
	
	function networkError() {
		alert("网络异常：");
		fileItem.status = "6";
	}
}

//断点续传
function resumeUpload(fileItem,domainstr) {
	fileItem.status = "2";
	var $process = $("#"+fileItem.uid).siblings(".py_processWrap");
	if ($process.length == 1) {
		$process.show();
		$process.find('.process').css("width", "0%");
		$process.find(".process").next("i").text("0%");
	}
	var blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice;
	var xhr = new XMLHttpRequest();
	fileItem.xhr = xhr;
	var url = "http://"+domainstr+"/cms-ft/fileResume";
	xhr.onabort = function() {
		// 已暂停
		if (fileItem.status == "3") {
			//$(".bficon").show();
    		//$(".zticon").hide();
		}
	};
	xhr.onerror = function() {
		//$(".bficon").show();
		//$(".zticon").hide();
		alert("网络异常：");
		fileItem.status = "6";
	};
	xhr.onload = function () {
		var json = eval("(" + xhr.responseText + ")");
		
		if (json.result == "100000") { // 传失败了，重传该分片
			sendPart();
		} else if (json.result == "000000") { // 上传成功，继续上传下一分片
			//$(".file_progress").text(json.data.postedPercent);
    		//$(".progress_path").css("width", json.data.postedPercent);
			fileItem.filePos = json.data.filePos;
			fileItem.postedPercent = json.data.postedPercent;
			var $process = $("#"+fileItem.uid).siblings(".py_processWrap");
			if ($process.length == 1) {
				$process.show();
				$process.find('.process').css("width", fileItem.postedPercent);
				$process.find(".process").next("i").text(fileItem.postedPercent);
			}
			// 继续传
			if (json.data.postComplete == 'false') {
				fileItem.currentChunk++;
				sendPart();
			} else { //上传完成
				//$(".bficon").hide();
	    		//$(".zticon").hide();
	    		//alert("上传完成：");
	    		//$("#attachFileName").val(json.data.fileNameLocal);
				//$("#attachFileId").val(json.data.fid);
				var $process = $("#"+fileItem.uid).siblings(".py_processWrap");
				if ($process.length == 1) {
					$process.show();
					$process.find('.process').css("width", "100%");
					$process.find(".process").next("i").text("100%");
				}
				var fid='<input type="hidden" name="fid" value="'+fileItem.fid+""+'"/>';
				$("#"+fileItem.uid).append(fid);
				setTimeout('removeProcess("'+fileItem.uid+'")',1000);
			}
		}
	};
	xhr.upload.onprogress = function(e) {
	     // 选用 如果文件分块大小较大 可以通过该方法判断单片文件具体的上传进度
	     // e.loaded  该片文件上传了多少
	     // e.totalSize 该片文件的总共大小
	     // console.log(e.loaded + "/" + e.totalSize);
		/*var loaded = e.loaded + (fileItem.partSize * fileItem.partNumber);
		var postedPercent = percentNum(loaded,fileItem.file.size);
		fileItem.postedPercent = postedPercent;
		$("#"+fileItem.uid).find("span.process").find("em").css("width",postedPercent);
		$("#"+fileItem.uid).find("span.process").next("i").text(postedPercent);*/
	};

	function sendPart() {
		var start = fileItem.currentChunk * chunkSize;
		var end = start + chunkSize >= fileItem.file.size ? fileItem.file.size : start + chunkSize;
		var part = blobSlice.call(fileItem.file, start, end);
		var formData = new FormData();
		formData.append("fid", fileItem.fid);
		formData.append("userId",fileItem.userId);
		formData.append("fileLength", fileItem.file.size);
		formData.append("fileMd5", fileItem.fileMd5);
		formData.append("fileNameLocal", fileItem.name);
		formData.append("filePos", fileItem.filePos);
		formData.append("part", part);
		xhr.open('POST', url, true);
		xhr.send(formData);
	}
	sendPart();
}

//暂停上传
function stopUpload() {
	fileItem.status = "3";
	if (fileItem.xhr) {
		fileItem.xhr.abort();
	}
	$("#status").text("已暂停：");
}

// 删除上传
function delUpload() {
	if (fileItem.xhr) {
		fileItem.xhr.abort();
	}
	location.reload();
	var title = parent.document.getElementById(titleFieldId);
	if (title != undefined) {
		title.value = '';
	}
}
//CMS上传--------------------------------------------------------------结束
//计算百分比
function percentNum(num, num2) {
	return (Math.round(num / num2 * 100) + "%"); //小数点后两位百分比
}
//获取后缀
function getFileExt(fileName) {
	var idx = fileName.lastIndexOf(".");
	if (idx == -1) {
		return "";
	}
	return fileName.substring(idx + 1).toLowerCase();
}
//单位转换
function bytesToSize(bytes) {
    if (bytes === 0) return '0 B';
    var k = 1024, 
        sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
   return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}

function isToCms(fileName) {
	var fileExt = getFileExt(fileName);
	if (contains(self_exts, fileExt.toLowerCase())) {
		return true;
	} else {
		return false
	}
}
function contains(list, value) {
	for (var i = 0; i < list.length; i++) {
		if (list[i] == value) {
			return true;
		}
	}
	return false;
}
function removeProcess(uid){
	try{
		saveUploadFile($("#"+uid));
		if ($("#" + uid).siblings(".py_processWrap").length == 1) {
			$("#" + uid).siblings(".py_processWrap").remove();
		}
	}catch(e){
		
	}
	
	try{
		var date = formData(uid);		 
		 resList(date);
	}catch(e){
		
	}
	
 
}
