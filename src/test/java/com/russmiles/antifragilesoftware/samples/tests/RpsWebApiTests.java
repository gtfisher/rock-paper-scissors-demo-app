package com.russmiles.antifragilesoftware.samples.tests;

import com.russmiles.antifragilesoftware.samples.RockPaperScissorsApplication;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by gtarrant-fisher on 13/05/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RockPaperScissorsApplication.class)
@WebAppConfiguration
//@IntegrationTest({"server.port=0", "spring.cloud.bus.enabled=false"})
public class RpsWebApiTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .build();

    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void rpsHome() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/rps/home").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                //.andExpect(content().contentType("application/json"))
                .andReturn();
        String resultStr = result.getResponse().getContentAsString();

        assertThat (resultStr, containsString("RpsCommandsController-home"));
    }

    @Test
    public void rpsCreateGame() throws Exception {
        ResultActions result = mockMvc.perform(post("/api/v1/rps/")
                .header("SimpleIdentity","grant@test.com"))
                .andExpect(status().isCreated());
        System.out.println("Result->" + result.toString());
    }

    @Test
   // @Ignore // ** TBC
    public void rpsCreateGameAndMove() throws Exception {
        MvcResult r = mockMvc.perform(post("/api/v1/rps/")
                .header("SimpleIdentity","grant@test.com"))
                .andExpect(status().isCreated())
                .andReturn();

        String content = r.getResponse().getContentAsString();

        //ResultActions result = mockMvc.perform(post("/api/v1/rps/")
        //        .header("SimpleIdentity","grant@test.com"))
        //        .andExpect(status().isOk());
        System.out.println("Result->" + content);
        ResultActions result2 = mockMvc.perform(put("/api/v1/rps/move/" + content)
                .header("SimpleIdentity","grant@test.com"))
                .andExpect(status().isOk());

    }



}
