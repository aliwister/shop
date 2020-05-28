package com.badals.shop.service;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.User;

import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import com.badals.shop.service.dto.PricingRequestDTO;
import io.github.jhipster.config.JHipsterProperties;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import static com.badals.shop.domain.checkout.CheckoutCart_.ITEMS;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String REASON = "reason";
    private static final String PRICINGREQUESTS = "pricingrequests";
    private static final String ORDER = "order";
    private static final String ITEMS = "items";
    private static final String PAYMENT = "payment";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom(), "Badals.com");
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmailFromTemplate(Customer user, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag("en");//user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmailFromTemplate(Customer user, List<PricingRequestDTO> dtos, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag("en");//user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(PRICINGREQUESTS, dtos);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmailFromTemplate(CustomerDTO user, OrderDTO order, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag("en");//user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(ORDER, order);
        context.setVariable(ITEMS, order.getOrderItems());
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        final String[] params = new String[]{order.getReference()};
        String subject = messageSource.getMessage(titleKey, params, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmailFromTemplate(CustomerDTO user, OrderDTO order, String reason, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag("en");//user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(ORDER, order);
        context.setVariable(REASON, reason);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        final String[] params = new String[]{order.getReference()};
        String subject = messageSource.getMessage(titleKey, params, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmailFromTemplate(CustomerDTO user, PaymentDTO payment, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag("en");//user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(PAYMENT, payment);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        final String[] params = new String[]{payment.getOrderReference()};
        String subject = messageSource.getMessage(titleKey, params, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(Customer user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(Customer user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(Customer user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendOrderCreationMail(CustomerDTO user, OrderDTO order) {
        log.debug("Sending order creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, order,"mail/orderCreationEmail", "email.order.title");
    }

    @Async
    public void sendPaymentAddedMail(CustomerDTO user, PaymentDTO order) {
        log.debug("Sending order creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, order,"mail/paymentCreationEmail", "email.payment.title");
    }

    @Async
    public void sendPricingMail(Customer user, List<PricingRequestDTO> dtos) {
        log.debug("Sending order creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, dtos,"mail/pricingRequestEmail", "email.pricing.title");
    }

    @Async
    public void sendVoltageMail(CustomerDTO user, OrderDTO order) {
        log.debug("Sending order creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, order,"mail/edit/voltageEmail", "email.voltage.title");
    }
    @Async
    public void sendCancelMail(CustomerDTO user, OrderDTO order, String reason) {
        sendEmailFromTemplate(user, order, reason,"mail/cancelEmail", "email.cancel.title");
    }
    @Async
    public void sendEditMail(CustomerDTO user, OrderDTO order, String reason) {
        sendEmailFromTemplate(user, order, reason,"mail/editEmail", "email.voltage.title");
    }
}
