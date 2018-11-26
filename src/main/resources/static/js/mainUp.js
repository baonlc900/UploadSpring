$(document).ready(function() {
	$("#fileUploadForm .fileInput").change(function(e) {
        if (e.target.files[0].name.match(/\.(jpg|jpeg|png)$/i)) {
        	var srcProductImage = createObjectURL(e.target.files[0]);
            console.log(srcProductImage);
            readURL(this,srcProductImage);
//            $(this).parent().parent().find('.outputImage').attr("src", srcProductImage);
        } 
        else {
            $(this).val("");
        }
	});
	
	$("#btnSubmit").click(function() {
		
		uploadFileToServer();
			// Resize image
	});
});
//UploadFile To Server
function uploadFileToServer () {
	var imagePosition = 1;
	$("#fileUploadForm .fileInput").each(function(index) { 

	var previewImg = $(this).parent().parent().find('.outputImage')[index];
//	var previewImg = $(this).parent().parent().find('.outputImage').attr("src", show);
	console.log("Preview: ",previewImg);
	var fileToUpload = resizeImage(this.files[0], previewImg); // blob data
	// Get form
	console.log("FileUpload: ",fileToUpload);
	var data = new FormData();
	data.append('image_data', fileToUpload, this.files[0].name);
	data.append("imagePosition", imagePosition);
	console.log("DATA: ",data);

	$.ajax({
		type : "POST",
		//enctype : 'multipart/form-data',
		url : "/api/upload/multi",
		data : data,			
		processData : false, // prevent jQuery from automatically
		// transforming the data into a query string
		contentType : false,
		cache : false,
		async:true,
		success : function(data) {

			$("#result").text(data);
			console.log("SUCCESS : ", data);
			$("#btnSubmit").prop("disabled", false);

		},
		error : function(e) {

			$("#result").text(e.responseText);
			console.log("ERROR : ", e);
			$("#btnSubmit").prop("disabled", false);

		}
	});
	imagePosition = imagePosition + 1;
	});
}

function readURL(input,image) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $(input).next()
                .attr('src', image);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

function resizeImage(file, image) {
    // Resize image
    if (file) {
        var img = document.createElement("img");
        img.src = image.src;

        var canvas = document.createElement("canvas");
        var ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0);

        var MAX_WIDTH = 400;
        var MAX_HEIGHT = 400;
        var width = img.width;
        var height = img.height;

        if (width > height) {
            if (width > MAX_WIDTH) {
                height *= MAX_WIDTH / width;
                width = MAX_WIDTH;
            }
        } 
        else {
            if (height > MAX_HEIGHT) {
                width *= MAX_HEIGHT / height;
                height = MAX_HEIGHT;
            }
        }
        
        canvas.width = width;
        canvas.height = height;
        var ctx = canvas.getContext("2d");

        // Create new image.
        ctx.drawImage(img, 0, 0, width, height); 

        var dataUrl = canvas.toDataURL(file.type);
        //var newFile = common.dataURLToBlob(dataUrl);
        var newFile = dataURItoBlob(dataUrl, file);

        if(newFile.size > file.size)
            return file;
        else
            return newFile;
    }
}

function createObjectURL(file) {
    if (window.webkitURL) {
        return window.webkitURL.createObjectURL(file);
    } 
    else if (window.URL && window.URL.createObjectURL) {
        return window.URL.createObjectURL(file);
    } 
    else {
        return null;
    }
}

function dataURItoBlob(dataURI, file) {
    // convert base64/URLEncoded data component to raw binary data held in a string
    var byteString;

    if (dataURI.split(',')[0].indexOf('base64') >= 0)
        byteString = atob(dataURI.split(',')[1]);
    else
        byteString = unescape(dataURI.split(',')[1]);

    // separate out the mime component
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

    // write the bytes of the string to a typed array
    var ia = new Uint8Array(byteString.length);

    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    ia.lastModified = file.lastModified;
//    ia.lastModifiedDate = file.lastModifiedDate;
    ia.name = file.name;
    ia.webkitRelativePath = "";

    var blob = new Blob([ia], {type:mimeString});
    blob.lastModified = file.lastModified;
//    blob.lastModifiedDate = file.lastModifiedDate;
    blob.name = file.name;
    blob.webkitRelativePath = "";

    return blob;
}

var dataURLToBlob = function(dataURL) {
    var BASE64_MARKER = ';base64,';
    if (dataURL.indexOf(BASE64_MARKER) == -1) {
        var parts = dataURL.split(',');
        var contentType = parts[0].split(':')[1];
        var raw = parts[1];

        return new Blob([raw], {type: contentType});
    }

    var parts = dataURL.split(BASE64_MARKER);
    var contentType = parts[0].split(':')[1];
    var raw = window.atob(parts[1]);
    var rawLength = raw.length;

    var uInt8Array = new Uint8Array(rawLength);

    for (var i = 0; i < rawLength; ++i) {
        uInt8Array[i] = raw.charCodeAt(i);
    }
    console.log(uInt8Array.length);
    return new Blob([uInt8Array], {type: contentType});
}
