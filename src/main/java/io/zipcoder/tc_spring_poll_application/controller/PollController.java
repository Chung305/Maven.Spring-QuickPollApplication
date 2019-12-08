package io.zipcoder.tc_spring_poll_application.controller;

import io.zipcoder.tc_spring_poll_application.domain.Poll;
import io.zipcoder.tc_spring_poll_application.exception.ResourceNotFoundException;
import io.zipcoder.tc_spring_poll_application.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class PollController {
     private PollRepository pollRepository;

     @Autowired
     public PollController(PollRepository pollRepository){
         this.pollRepository = pollRepository;
     }
    //GET all polls
    @RequestMapping(value = "/polls", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Poll>> getAllPolls(){
         Iterable<Poll> allPolls = pollRepository.findAll();
         return new ResponseEntity<>(allPolls, HttpStatus.OK);
     }

     //POST creates a poll
    @RequestMapping(value = "/polls", method = RequestMethod.POST)
    public ResponseEntity<?> createPoll(@Valid @RequestBody Poll poll){
         poll = pollRepository.save(poll);
         URI newPollUri = ServletUriComponentsBuilder
                 .fromCurrentRequest()
                 .path("/{id}")
                 .buildAndExpand(poll.getId())
                 .toUri();
         HttpHeaders newHeader = new HttpHeaders();
         newHeader.setLocation(newPollUri);
         return new ResponseEntity<>(newHeader, HttpStatus.CREATED);
    }

    //GET poll by id
    @RequestMapping(value = "/polls/{pollId}", method = RequestMethod.GET)
    public ResponseEntity<?> getPoll(@PathVariable Long pollId){
         verifyPoll(pollId);
         Poll p = pollRepository.findOne(pollId);
         return new ResponseEntity<>(p, HttpStatus.OK);
    }

    //PUT updates a poll
    @RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId){
         verifyPoll(pollId);
         Poll p = pollRepository.save(poll);
         return new ResponseEntity<>(p, HttpStatus.OK);
    }

    //DELETE a poll
    @RequestMapping(value="/polls/{pollId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
         verifyPoll(pollId);
         pollRepository.delete(pollId);
         return new ResponseEntity<>(HttpStatus.OK);
    }

    public void verifyPoll(@PathVariable Long pollId){
         if(!pollRepository.exists(pollId)){
             throw new ResourceNotFoundException("Poll id : " + pollId + " does not exist");
         }
    }
}
