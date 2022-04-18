package com.albo.util;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void sendEmail(Mail mail, String template) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = templateEngine.process("email/" + template, context);
			System.out.println("HTML" + html);

			helper.setTo(mail.getTo());
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom(mail.getFrom());
//			helper.addAttachment("QR_Ingreso_ALBO.png", mail.getAdjunto());

			ByteArrayDataSource byteArrayDataSource = null;
			if (mail.getAdjunto().length > 10) {
				System.out.println("IF MAIL ADJUNTO AQUIIIIIIIIII");
				byteArrayDataSource = new ByteArrayDataSource(mail.getAdjunto(), "image/jpeg");
				helper.addAttachment("QR_Ingreso_ALBO.jpeg", byteArrayDataSource);

			} else {
				byteArrayDataSource = new ByteArrayDataSource(mail.getAdjunto(), "application/pdf");
				helper.addAttachment(byteArrayDataSource.getName() + ".pdf", byteArrayDataSource);

			}

			System.out.println("LLEGO HASTA SEMIFINAL");
			emailSender.send(message);
			System.out.println("PASO EL ENVIO");
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException(e);
		}
	}

}