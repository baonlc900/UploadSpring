package com.example.test.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.util.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.test.model.UploadModel;

@RestController
public class UploadController {
	private static String UPLOADED_FOLDER = "D://temp1//";

	private final Logger logger = LoggerFactory.getLogger(UploadController.class);

//	 @GetMapping("/")
//	 public String index() {
//	 return "uploadFile";
//	 }
	//
	// @PostMapping("/upload") // //new annotation since 4.3
	// public String singleFileUpload(@RequestParam("file") MultipartFile file,
	// RedirectAttributes redirectAttributes) {
	//
	// if (file.isEmpty()) {
	// redirectAttributes.addFlashAttribute("message", "Please select a file to
	// upload");
	// return "redirect:uploadStatus";
	// }
	//
	// try {
	//
	// // Get the file and save it somewhere
	// byte[] bytes = file.getBytes();
	// Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
	// Files.write(path, bytes);
	//
	// redirectAttributes.addFlashAttribute("message",
	// "You successfully uploaded '" + file.getOriginalFilename() + "'");
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return "redirect:/uploadStatus";
	// }
	//
	// @GetMapping("/uploadStatus")
	// public String uploadStatus() {
	// return "uploadStatus";
	// }
	// Multipart File

	@PostMapping("/api/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile) {

		logger.debug("Single File Upload");
		if (uploadFile.isEmpty()) {
			return new ResponseEntity<>("Please select a file!", HttpStatus.OK);
		}

		try {
			saveUploadFiles(Arrays.asList(uploadFile));
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Successed Uploaded" + uploadFile.getOriginalFilename(), new HttpHeaders(),
				HttpStatus.OK);

	}
	
//	@PostMapping("/api/upload/multi")
//	public void upload(MultipartHttpServletRequest requestEntity) {
//		 Iterator<String> itrator = requestEntity.getFileNames();
//		 MultipartFile multiFile = requestEntity.getFile(itrator.next());
//		 System.out.println("int"+itrator);
//		 try {
//			System.out.println("File Length:" + multiFile.getBytes().length);
//			System.out.println("File Type:" + multiFile.getContentType());
//	         String fileName=multiFile.getOriginalFilename();
//	         System.out.println("File Name:" +fileName);
////	         String path=requestEntity.getServletContext().getRealPath("/");
//	         String path = Paths.get(UPLOADED_FOLDER + multiFile.getOriginalFilename()).toString();
//	         
//	         byte[] bytes = multiFile.getBytes();
//	         File file = new File(path);
//	         System.out.println("file in "+file);
//	         BufferedOutputStream stream = new BufferedOutputStream(
//	                    new FileOutputStream(file));
//	            stream.write(bytes);
//	            stream.close();
//	         
//	         
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
	@PostMapping("/api/upload/multi")
	public ResponseEntity<?> uploadFileMulti(MultipartHttpServletRequest requestEntity,@RequestParam("files") MultipartFile[] files) {
		logger.info("This is INFO");
//		 Iterator<String> itrator = requestEntity.getFileNames();
		 Map<String, MultipartFile> fileMap = requestEntity.getFileMap();
		 for (MultipartFile multiFile : fileMap.values()) {
		        try {
//		            file.transferTo(new File("/opt/img/" + file.getOriginalFilename()));
//		            System.out.println(file.getOriginalFilename());
		        	System.out.println("File Length:" + multiFile.getBytes().length);
					System.out.println("File Type:" + multiFile.getContentType());
			         
			         
//			         String path=requestEntity.getServletContext().getRealPath("/");
			         String path = Paths.get(UPLOADED_FOLDER + multiFile.getOriginalFilename()).toString();
			         
			         byte[] bytes = multiFile.getBytes();
			         File file = new File(path);
			         System.out.println("file in "+file);
			         BufferedOutputStream stream = new BufferedOutputStream(
			                    new FileOutputStream(file));
			            stream.write(bytes);
			            stream.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		 
//		 MultipartFile multiFile = null;
		 
//		    while (itrator.hasNext()) {
//		    	multiFile = requestEntity.getFile(itrator.next());
		    	
		        
//				 System.out.println("int"+itrator);
//				 try {
//					System.out.println("File Length:" + multiFile.getBytes().length);
//					System.out.println("File Type:" + multiFile.getContentType());
//			         
//			         
////			         String path=requestEntity.getServletContext().getRealPath("/");
//			         String path = Paths.get(UPLOADED_FOLDER + multiFile.getOriginalFilename()).toString();
//			         
//			         byte[] bytes = multiFile.getBytes();
//			         File file = new File(path);
//			         System.out.println("file in "+file);
//			         BufferedOutputStream stream = new BufferedOutputStream(
//			                    new FileOutputStream(file));
//			            stream.write(bytes);
//			            stream.close();
//			         
//			         
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				 
//		    }
//		    String fileName = multiFile.getOriginalFilename();
//		    System.out.println("File Name:" +fileName);
		 
		 return new ResponseEntity<>("Successfully uploaded - " , HttpStatus.OK);
		 
		
//		Long size = multipartFile.getSize();
//        String contentType = multipartFile.getContentType();
////        InputStream stream = multipartFile.getInputStream();
//        String path = Paths.get(UPLOADED_FOLDER + multipartFile.getOriginalFilename()).toString();
//        try {
//			BufferedOutputStream stream =
//			        new BufferedOutputStream(new FileOutputStream(new File(path)));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		
		
//		try {
//		    
//	    }
//		  catch (Exception e) {
//		    System.out.println(e.getMessage());
//		    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		  }
		
	}

	/*
	@PostMapping("/api/upload/multi")
	public ResponseEntity<?> multiFile(@RequestParam("extraField") String extraField,
			MultipartHttpServletRequest request, @RequestParam("image_data") MultipartFile image_data) {
		
		
		String filename = image_data.getOriginalFilename();
		try {
		    // Get the filename and build the local file path (be sure that the 
		    // application have write permissions on such directory)
//			Blob blob = (Blob) image_data.getInputStream();
//			InputStream inBlob = blob.getBinaryStream();
//			byte[] bytes = ((MultipartFile) inBlob).getBytes();
//			InputStream inBlob = ((java.sql.Blob) blob).getBinaryStream();
			
		    String path = Paths.get(UPLOADED_FOLDER + image_data.getOriginalFilename()).toString();
		    
		    System.out.println("pathfile: "+path);
		    // Save the file locally
		    BufferedOutputStream stream =
		        new BufferedOutputStream(new FileOutputStream(new File(path)));
		    stream.write(image_data.getBytes());
		    System.out.println("img: "+image_data.getBytes().length);
		    stream.close();
		  }
		  catch (Exception e) {
		    System.out.println(e.getMessage());
		    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		  }
		  
		return new ResponseEntity<>("Successfully uploaded - " + filename, HttpStatus.OK);
		

		// try {
		// byte[] bytes = multi.getBytes();
		//
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
//		System.out.println("inra" + image_data);
//
//		logger.debug("Multi File Upload");
//		// byte[] bytes = IOUtils.toByteArray(uploadFiles);
//		// Get File Name
//		String uploadedFileName = Arrays.stream(image_data).map(x -> x.getOriginalFilename())
//				.filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
//
//		//Check select file
//		if (StringUtils.isEmpty(uploadedFileName)) {
//			return new ResponseEntity<>("please select a file!", HttpStatus.OK);
//		}
//		try {
//			
//			//Save file
//			saveUploadFiles(Arrays.asList(image_data));
//
//		} catch (IOException e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//
//		return new ResponseEntity<>("Successfully uploaded - " + uploadedFileName, HttpStatus.OK);
	}
*/
	@PostMapping("/api/upload/multi/model")
	public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadModel model) {

		logger.debug("Multiple file upload! With UploadModel");

		try {

			saveUploadFiles(Arrays.asList(model.getFiles()));

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Successfully uploaded!", HttpStatus.OK);

	}

	// Save file
	private void saveUploadFiles(List<MultipartFile> files) throws IOException {
		// SerialBlob serialBlob = new SerialBlob(byte[]);
		System.out.println("in1 +++" + files);
		// BlobDomain blobImage = (BlobDomain)row.getAttribute("Image");
		// InputStream stream = files.get;
		// Blob blob = Hibernate(files.get(0));

		for (MultipartFile file : files) {
			System.out.println("in file" + file);
			System.out.println("Desc:" + file.getSize());
			System.out.println("ContentType:" + file.getContentType());

			if (file.isEmpty()) {
				continue; // next pls
			}
			// Blob blob = null;
			// try {
			// blob = new SerialBlob(bytes);
			// } catch (SerialException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			byte[] bytes = file.getBytes();

			// InputStream inputStream = new ByteArrayInputStream(bytes);

			System.out.println("getByte" + bytes);
			// try {
			// Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
			// } catch (SerialException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			// File file1 = new File(path);
			//
			// BufferedOutputStream stream = new BufferedOutputStream(
			// new FileOutputStream(file));
			// stream.write(bytes);
			// stream.close();

			Files.write(path, bytes);

		}

	}
}
