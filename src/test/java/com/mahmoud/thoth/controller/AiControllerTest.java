package com.mahmoud.thoth.controller;

import com.mahmoud.thoth.service.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AiService aiService;

    @InjectMocks
    private OllamaAiController aiController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(aiController).build();
    }

    @Test
    public void testChatEndpoint() throws Exception {
        String testMessage = "What is the capital of France?";
        String expectedResponse = "The capital of France is Paris.";

        when(aiService.chat(testMessage)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/ai/ollama/chat")
                .param("message", testMessage))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(expectedResponse));
    }
}
