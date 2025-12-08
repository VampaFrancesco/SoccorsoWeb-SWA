package it.univaq.swa.soccorsoweb.swa.open;

import it.univaq.swa.soccorsoweb.model.dto.response.RichiestaSoccorsoResponse;
import it.univaq.swa.soccorsoweb.service.FeedbackService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/swa/open/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Endpoint che serve la pagina HTML di feedback  il path viene formato dall'EmailService quando clicco il pulsante nella mail una volta chiusa la richiesta
    @GetMapping(value = "/{idSoccorso}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> mostraFormFeedback(@PathVariable Long idSoccorso) throws IOException {
        Resource resource = new ClassPathResource("static/feedback.html");
        String html = new String(Files.readAllBytes(resource.getFile().toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    // Endpoint API per salvare il feedback
    @GetMapping("/{idSoccorso}/{livello}/invia-feedback")  // viene chiamato dal form HTML
    public ResponseEntity<RichiestaSoccorsoResponse> inviaFeedback(@PathVariable Long idSoccorso, @PathVariable Integer livello){
        return ResponseEntity.ok().body(feedbackService.inviaFeedback(idSoccorso, livello));
    }


}
