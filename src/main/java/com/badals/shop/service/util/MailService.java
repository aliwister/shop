package com.badals.shop.service.util;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Customer;

import com.badals.shop.domain.UserBase;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.service.TenantLayoutService;
import com.badals.shop.service.dto.*;
import io.github.jhipster.config.JHipsterProperties;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private static final String LOGO_WIDTH = "logo_width" ;
    private static final String LOGO_HEIGHT = "logo_height";
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String REASON = "reason";
    private static final String PREHEADER = "preheader";
    private static final String PRICINGREQUESTS = "pricingrequests";
    private static final String ORDER = "order";
    private static final String LOGO = "logo";
    private static final String TENANT = "tenant";
    private static final String ITEMS = "items";
    private static final String PAYMENT = "payment";
    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;
    private final TenantLayoutService tenantLayoutService;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
                       MessageSource messageSource, SpringTemplateEngine templateEngine, TenantLayoutService tenantLayoutService) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.tenantLayoutService = tenantLayoutService;
    }

    @Async
    public void sendEmail(String from, String fromEmail, String to, String replyTo, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(fromEmail, from);
            message.setReplyTo(replyTo);
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

    private String buildProfileBaseUrl(TenantDTO tenant) {
        return tenant.getIsSubdomain()?"https://"+tenant.getSubdomain()+".profile.shop":"https://www."+tenant.getCustomDomain();
    }

    @Async
    public void sendEmailFromTemplate(String email, String viewName, String titleKey, Map variables, String[] params) {
        TenantDTO tenant = tenantLayoutService.getTenant();
        String tenantName = tenant.getName();
        Locale locale = LocaleContextHolder.getLocale(); //user.getLangKey());
        Context context = new Context(locale);
        context.setVariables(variables);
        context.setVariable(BASE_URL, buildProfileBaseUrl(tenant));
        context.setVariable(LOGO, tenant.getLogo());
        context.setVariable(LOGO_WIDTH, parseWidth(tenant.getLogo()));
        context.setVariable(LOGO_HEIGHT, parseHeight(tenant.getLogo()));
        context.setVariable(TENANT, tenantName);
        context.setVariable("view", viewName);
        if(tenant.getSocialList() != null)
            for (Attribute attribute: tenant.getSocialList()) {
                context.setVariable(attribute.getName(), attribute.getValue());
            }
        String content = templateEngine.process("mail/template", context);
        String[] subjectParams = params != null && params.length > 0? ArrayUtils.addAll(new String[] {tenantName}, params):new String[] {tenantName};
        String subject = messageSource.getMessage(titleKey, subjectParams, locale);
        String tenantEmail = buildProfileEmail(tenant);
        sendEmail(tenantName, tenantEmail, email, tenant.getReplyToEmail(), subject, content, false, true);
    }

    private String buildProfileEmail(TenantDTO tenant) {
        return "care@" + tenant.getSubdomain()+".profile.shop";
    }

    private static String parseWidth(String logo) {
        String [] x = logo.split("[x.-]");
        return x[x.length-3];
    }
    private static String parseHeight(String logo) {
        String [] x = logo.split("[x.-]");
        return x[x.length-2];
    }
    @Async
    public void sendActivationEmail(UserBase user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(),"mail/views/activate.html", "email.activation.title",
                new HashMap<String, Object>() {{
                    put(USER, user);
                    put(PREHEADER, "Your account requires activation");

                }}, null);
    }
    @Async
    public void sendPasswordResetMail(UserBase user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(),"mail/views/password_reset.html", "email.reset.title",
            new HashMap<String, Object>() {{
                put(USER, user);
                put(PREHEADER, "A reset email has been requested");

            }}, null);
    }
    @Async
    public void sendOrderCreationMail(UserBase user, OrderDTO order) {
        log.debug("Sending order creation email to '{}'", user.getEmail());
        TenantDTO tenant = tenantLayoutService.getTenant();
        sendEmailFromTemplate(user.getEmail(), "mail/views/new_order.html", "email.order.title",
            new HashMap<String, Object>() {{
                put(ORDER, order);
                put(ITEMS, order.getItems());
                put(USER, user);
                put(PREHEADER, "Thank you for using "+buildProfileBaseUrl(tenant));

            }}, new String[]{order.getReference()});
    }
    @Async
    public void sendPaymentAddedMail(UserBase user, PaymentDTO payment) {
        log.debug("Sending order creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(),"mail/views/new_payment.html", "email.payment.title",
            new HashMap<String, Object>() {{
                put(USER, user);
                put(PAYMENT, payment);
                put(PREHEADER, "We have received your payment of " + payment.getAmount() );
        }}, new String[]{payment.getOrderReference()});
    }

    @Async
    public void sendPricingMail(UserBase user, List<PricingRequestDTO> dtos) {
        log.debug("Sending order creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(),"mail/views/price_request.html", "email.pricing.title",
                new HashMap<String, Object>() {{
                    put(PRICINGREQUESTS, dtos);
                    put(USER, user);
        }}, null);
    }

    @Async
    public void sendVoltageMail(UserBase user, OrderDTO order) {
        log.debug("Sending order creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(),"mail/views/edit/voltage.html", "email.voltage.title",
        new HashMap<String, Object>() {{
            put(USER, user);
            put(ORDER, order);
            put(PREHEADER, "Approval Required" );
        }}, new String[]{order.getReference()}
    );
    }
    @Async
    public void sendCancelMail(UserBase user, OrderDTO order, String reason) {
        sendEmailFromTemplate(user.getEmail(),"mail/views/cancel.html", "email.cancel.title",
            new HashMap<String, Object>() {{
                put(USER, user);
                put(ORDER, order);
                put(REASON, reason);
                put(PREHEADER, "Your order has been cancelled" );

            }}, new String[]{order.getReference()}
        );
    }
    @Async
    public void sendEditCancelMail(UserBase user, OrderDTO order, String reason) {
        sendEmailFromTemplate(user.getEmail(),"mail/views/edit_cancel.html", "email.edit.cancel.title",
            new HashMap<String, Object>() {{
                put(USER, user);
                put(ORDER, order);
                put(ITEMS, order.getItems());
                put(REASON, reason);
                put(PREHEADER, "Part of your order has been cancelled" );

            }}, new String[]{order.getReference()}
        );
    }
    @Async
    public void sendEditMail(UserBase user, OrderDTO order, String reason) {
        sendEmailFromTemplate(user.getEmail(),"mail/views/edit.html", "email.edit.title",
            new HashMap<String, Object>() {{
                put(USER, user);
                put(ORDER, order);
                put(REASON, reason);
                put(ITEMS, order.getItems());
                put(PREHEADER, "Your order has been modified" );

            }}, new String[]{order.getReference()}
        );
    }
}
