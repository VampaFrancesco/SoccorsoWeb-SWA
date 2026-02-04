package it.univaq.swa.soccorsoweb.service;

import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibModel.*;
import sibApi.TransactionalEmailsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${app.mail.from}")
    private String fromEmail;

    public void inviaEmailConvalida(String toEmail, String nomeSegnalante, String linkConvalida) {
        // Configura API Key
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(brevoApiKey);

        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

        // Mittente
        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail(fromEmail);
        sender.setName("SoccorsoWeb");
        sendSmtpEmail.setSender(sender);

        // Destinatario
        SendSmtpEmailTo to = new SendSmtpEmailTo();
        to.setEmail(toEmail);
        to.setName(nomeSegnalante);
        sendSmtpEmail.setTo(List.of(to));

        // Contenuto
        sendSmtpEmail.setSubject("⚠️ Conferma la tua richiesta di soccorso");

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
                            <p>Per attivarla, clicca sul pulsante qui sotto:</p>

                            <div style="text-align: center;">
                                <a href="%s" class="button">✅ CONFERMA RICHIESTA</a>
                            </div>

                            <p><strong>Oppure</strong> copia questo link:</p>
                            <div class="link-box">%s</div>

                            <p style="margin-top: 30px; color: #856404; background-color: #fff3cd; padding: 10px; border-radius: 5px;">
                                ⚠️ <strong>Importante:</strong> Se non hai effettuato questa richiesta, ignora questa email.
                            </p>
                        </div>
                        <div class="footer">
                            <p>Questa è un'email automatica.</p>
                            <p>&copy; 2026 SoccorsoWeb</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(nomeSegnalante, linkConvalida, linkConvalida);

        sendSmtpEmail.setHtmlContent(htmlContent);

        try {
            CreateSmtpEmail result = apiInstance.sendTransacEmail(sendSmtpEmail);
            log.info("✅ Email Brevo/Sendinblue inviata a: {} (Msg ID: {})", toEmail, result.getMessageId());
        } catch (ApiException e) {
            log.error("❌ Errore API Brevo: {} - {}", e.getCode(), e.getResponseBody());
            log.error("Eccezione completa:", e);
        }
    }
}
