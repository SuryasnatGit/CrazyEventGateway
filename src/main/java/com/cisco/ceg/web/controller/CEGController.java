package com.cisco.ceg.web.controller;

import static com.cisco.ceg.common.CEGConstants.API_ENDPOINT;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cisco.ceg.model.CEGEvent;
import com.cisco.ceg.service.EventWritingService;

/**
 * Facade controller which exposes the service end point
 *
 */
@Controller
public class CEGController {

	private static final Logger LOG = LoggerFactory.getLogger(CEGController.class);

	@Autowired
	private EventWritingService service;

	@PostMapping(path = API_ENDPOINT)
	public ResponseEntity<Object> captureEvents(@RequestBody List<CEGEvent> events) {

		if (events == null || events.size() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		LOG.debug("received events :" + events);

		service.processEvents(events);

		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
