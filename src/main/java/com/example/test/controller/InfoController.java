package com.example.test.controller;

import java.io.Console;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.model.Image;
import com.example.test.model.Info;
import com.example.test.repository.InfoRepository;
import com.example.test.util.UtilBase64Image;

@Controller
@EnableSpringDataWebSupport
@CrossOrigin(origins = "http://localhost:4200")
public class InfoController {

	@Autowired
	InfoRepository infoRep;

//	@GetMapping("/")
//	public String showList() {
//		return "welcomePage";
//	}

	private Sort sortId() {
		return new Sort(Sort.Direction.ASC, "id");
	}

	// @RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@GetMapping("infos")

	public ResponseEntity<List<Info>> listAllInfo() {
		List<Info> info = infoRep.findAll(sortId());
		if (info.isEmpty()) {
			return new ResponseEntity<List<Info>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Info>>(info, HttpStatus.OK);
	}

	// @RequestMapping(value = "info", method = RequestMethod.GET)
	@ResponseBody
	@GetMapping("/info/{id}")
	public ResponseEntity<?> getInfoById(@PathVariable("id") final String infoId) {
		int infoId1 = Integer.parseInt(infoId);
		Optional<Info> info = infoRep.findById(infoId1);
		if (info == null) {
			ResponseEntity.notFound().build();
		}

		return new ResponseEntity<Optional<Info>>(info, HttpStatus.OK);
	}

	@PostMapping("/info")
	public ResponseEntity<?> create(@Valid @RequestBody Map<String, String> body) {
		String title = body.get("title");
		String content = body.get("content");

		Info info = infoRep.save(new Info(title, content));

		// HttpHeaders headers = new HttpHeaders();
		HttpHeaders headers = new HttpHeaders();
		// headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		// URI location =
		// ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
		// .buildAndExpand(savedStudent.getId()).toUri();

		return new ResponseEntity<Object>(info, HttpStatus.CREATED);
	}

	@PutMapping("/info/{id}")
	public ResponseEntity<Info> update(@PathVariable String id, @RequestBody Map<String, String> body) {
		int infoId = Integer.parseInt(id);
		Info info = infoRep.getOne(infoId);
		if(info == null) {
			return ResponseEntity.notFound().build();
		}

		info.setTitle(body.get("title"));
		info.setContent(body.get("content"));
		Info info1 = infoRep.save(info);

		return new ResponseEntity<Info>(info1, HttpStatus.OK);
	}

	// @DeleteMapping("/info/{id}")
	// public boolean delete(@PathVariable String id) {
	// int infoId = Integer.parseInt(id);
	//
	// infoRep.deleteById(infoId);
	//
	// return true;
	// }

	@DeleteMapping("/info/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		int infoId = Integer.parseInt(id);

		infoRep.deleteById(infoId);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/info/search")
	public List<Info> findContent(@RequestBody Map<String, String> body) {
		String searchCon = body.get("text");

		return infoRep.findByTitleOrContent(searchCon, searchCon);
	}
	//////////////
//	@RequestMapping(value = "/post", method = RequestMethod.POST)
//	public String post(@RequestBody Image image) {
//		System.out.println("/POST request with " + image.toString());
//		// save Image to C:\\server folder
//		String path = "C:\\server\\" + image.getName();
//		UtilBase64Image.decoder(image.getData(), path);
//		return "/Post Successful!";
//	}
// 
//	@RequestMapping(value = "/get", method = RequestMethod.GET)
//	public Image get(@RequestParam("name") String name) {
//		System.out.println(String.format("/GET info: imageName = %s", name));
//		String imagePath = "C:\\server\\" + name;
//		String imageBase64 = UtilBase64Image.encoder(imagePath);
//		
//		if(imageBase64 != null){
//			Image image = new Image(name, imageBase64);
//			return image;
//		}
//		return null;
//	}

}
