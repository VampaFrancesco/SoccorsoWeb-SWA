package it.univaq.swa.soccorsoweb.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@soccorsoweb.it}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void inviaEmailConvalida(String toEmail, String nomeSegnalante, String linkConvalida) throws MessagingException {

            log.info("📧 Invio email di convalida a: {}", toEmail);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("⚠️ Conferma la tua richiesta di soccorso");

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #dc3545; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                        .content { background-color: #f8f9fa; padding: 30px; border-radius: 0 0 5px 5px; }
                        .button { display: inline-block; padding: 15px 30px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; font-weight: bold; }
                        .footer { margin-top: 20px; font-size: 12px; color: #666; text-align: center; }
                        .link-box { background-color: #e9ecef; padding: 10px; border-radius: 5px; word-break: break-all; font-size: 12px; margin-top: 10px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🚨 SoccorsoWeb</h1>
                        </div>
                        <div class="content">
                            <h2>Ciao %s,</h2>
                            <p>Abbiamo ricevuto la tua <strong>richiesta di soccorso</strong>.</p>
                            <p>Per attivarla e permettere ai nostri operatori di gestirla, devi <strong>confermare</strong> cliccando sul pulsante qui sotto:</p>
                           \s
                            <div style="text-align: center;">
                                <a href="%s" class="button">✅ CONFERMA RICHIESTA</a>
                            </div>
                           \s
                            <p><strong>Oppure</strong> copia e incolla questo link nel browser:</p>
                            <div class="link-box">%s</div>
                           \s
                            <p style="margin-top: 30px; color: #856404; background-color: #fff3cd; padding: 10px; border-radius: 5px;">
                                ⚠️ <strong>Importante:</strong> Se non hai effettuato questa richiesta, ignora questa email.
                            </p>
                        </div>
                        <div class="footer">
                            <p>Questa è un'email automatica, non rispondere.</p>
                            <p>&copy; 2025 SoccorsoWeb - Sistema di gestione emergenze</p>
                        </div>
                    </div>
                </body>
                </html>
               \s""".formatted(nomeSegnalante, linkConvalida, linkConvalida);

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("✅ Email inviata con successo a: {}", toEmail);
    }


}

