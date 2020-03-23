package com.cisco.ceg;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cisco.ceg.common.CEGConstants;
import com.cisco.ceg.model.CEGEvent;
import com.cisco.ceg.service.EventWritingService;
import com.cisco.ceg.web.controller.CEGController;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = CEGController.class)
class CEGControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EventWritingService service;

	@Test
	void testPostEventsSuccess() throws IOException, Exception {
		List<CEGEvent> list = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			CEGEvent event = new CEGEvent(1, i);
			list.add(event);
		}

		doNothing().when(service).processEvents(list);

		assertTrue(mvc.perform(post(CEGConstants.API_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(list))).andReturn()
				.getResponse().getStatus() == HttpStatus.ACCEPTED.value());
	}

	@Test
	void testPostEventsFailure() throws IOException, Exception {
		List<CEGEvent> list = new ArrayList<>();
		assertTrue(mvc.perform(post(CEGConstants.API_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(list))).andReturn()
				.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value());
	}

	private byte[] toJson(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
}
