package com.russmiles.antifragilesoftware.samples.rest.controllers;

import com.russmiles.antifragilesoftware.samples.es.store.EventStream;
import com.russmiles.antifragilesoftware.samples.rps.Move;
import com.russmiles.antifragilesoftware.samples.rps.command.CreateGameCommand;
import com.russmiles.antifragilesoftware.samples.rps.game.Game;
import com.russmiles.antifragilesoftware.samples.es.store.memory.InMemoryEventStore;
import com.russmiles.antifragilesoftware.samples.es.impl.ApplicationService;
import com.russmiles.antifragilesoftware.samples.rps.command.MakeMoveCommand;
import com.russmiles.antifragilesoftware.samples.services.api.objects.GameDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(value = "/api/v1/rps")
public class RpsCommandsController {

	//@Autowired

    private InMemoryEventStore eventStore = new InMemoryEventStore();
	private ApplicationService applicationService =  new ApplicationService(eventStore,Game.class) ;

    @RequestMapping("/home")
    public ResponseEntity<String> home() {
        return new ResponseEntity<String>("RpsCommandsController-home", HttpStatus.OK);
    }

    @RequestMapping("/events/{gameId}")
    public ResponseEntity<String> events(@PathVariable("gameId") UUID gameId) {

        System.out.println ("Getting event list for game:" + gameId);

        EventStream<Long> eventStream = eventStore.loadEventStream(gameId);

        StringBuffer result = new StringBuffer();

        eventStream.forEach(item->result.append(item + "\n"));

        return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestHeader(value="SimpleIdentity") String email) {

        UUID gameId = UUID.randomUUID();
        System.out.println("going to create Game:" + gameId );
        try {
            applicationService.handle(new CreateGameCommand(gameId, email));

            System.out.println("created Game:" + gameId);
            //Response resp = Response.created(UriBuilder.fromMethod(RpsResource.class, "game").build(gameId.toString())).build();
            //System.out.println("got response:" + resp.toString() );
            //return resp;
            return new ResponseEntity<String>(gameId.toString(), HttpStatus.CREATED);
        }
        catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
	}

    @RequestMapping(method = RequestMethod.PUT, value="/move/{gameId}/{move}")
    public ResponseEntity<String> makeMove(@PathVariable("gameId") UUID gameId, @PathVariable("move") Move move, @RequestHeader(value="SimpleIdentity") String email ) {
    //public ResponseEntity<String> makeMove(@RequestBody GameDto gameDto) {

        try {
            System.out.println("going to make a move for game id:" + gameId );
            System.out.println("with move:" + move );

            applicationService.handle(new MakeMoveCommand(gameId,email, move));

            //applicationService.handle(new MakeMoveCommand(gameDto.getGameId(),gameDto.getPlayerEmail(),gameDto.getMove()));

            return new ResponseEntity<String>("Move accepted", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
