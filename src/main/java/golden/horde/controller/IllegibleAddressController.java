package golden.horde.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import golden.horde.service.AddressRegistryService;


@RestController
@RequestMapping("/illegible-address/api/v1")
public class IllegibleAddressController {

	@Autowired
	private AddressRegistryService addressRegistryService;

	// http://localhost:2020/illegible-address/api/v1/ping
	@GetMapping(value = "/ping")
	public ResponseEntity<String> ping() {
		return new ResponseEntity<>(LocalDateTime.now() + " PONG!!!", HttpStatus.OK);
	}

	// http://localhost:2020/illegible-address/api/v1/findstreet?substr=москов
	@GetMapping(value = "/findstreet", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String findStreets(@RequestParam(name = "substr") String substr) {
		return addressRegistryService.findMatches(substr);
	}
	
	// http://localhost:2020/illegible-address/api/v1/updatecache
	@GetMapping(value = "/updatecache", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String updateCache() {
		return addressRegistryService.updateCache();
	}
}
