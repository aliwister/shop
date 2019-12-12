package com.badals.shop.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the ps_customer database table.
 *
 */
@Entity
@Table(schema="prestashop7",name="ps_customer")
@NamedQuery(name="Customer.findAll", query="SELECT p FROM Customer p")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;


	/*@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
*/
	@Column(name="oc_salt")
	private String salt;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	@Id
	@Column(name="id_customer")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idCustomer;

	private int active;

	private String ape;

	@Temporal(TemporalType.DATE)
	private Date birthday;

	private String company;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_add")
	private Date dateAdd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_upd")
	private Date dateUpd;

	private int deleted;

	private String email;

	private String firstname;

	@Column(name="id_default_group")
	private int idDefaultGroup;

	@Column(name="id_gender")
	private int idGender;

	@Column(name="id_lang")
	private int idLang;

	@Column(name="id_risk")
	private int idRisk;

	@Column(name="id_shop")
	private int idShop;

	@Column(name="id_shop_group")
	private int idShopGroup;

	@Column(name="ip_registration_newsletter")
	private String ipRegistrationNewsletter;

	@Column(name="is_guest")
	private int isGuest;

	@Column(name="last_passwd_gen")
	private Timestamp lastPasswdGen;

	private String lastname;

	@Column(name="max_payment_days")
	private int maxPaymentDays;

	private int newsletter;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="newsletter_date_add")
	private Date newsletterDateAdd;

	@Lob
	private String note;

	private int optin;

	@Column(name="outstanding_allow_amount")
	private BigDecimal outstandingAllowAmount;

	private String passwd;

	@Column(name="secure_key")
	private String secureKey;

	@Column(name="show_public_prices")
	private int showPublicPrices;

	private String siret;

	private String website;

	public Customer() {
	}

	public Long getIdCustomer() {
		return this.idCustomer;
	}

	public void setIdCustomer(Long idCustomer) {
		this.idCustomer = idCustomer;
	}

	public int getActive() {
		return this.active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getApe() {
		return this.ape;
	}

	public void setApe(String ape) {
		this.ape = ape;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getDateAdd() {
		return this.dateAdd;
	}

	public void setDateAdd(Date dateAdd) {
		this.dateAdd = dateAdd;
	}

	public Date getDateUpd() {
		return this.dateUpd;
	}

	public void setDateUpd(Date dateUpd) {
		this.dateUpd = dateUpd;
	}

	public int getDeleted() {
		return this.deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public int getIdDefaultGroup() {
		return this.idDefaultGroup;
	}

	public void setIdDefaultGroup(int idDefaultGroup) {
		this.idDefaultGroup = idDefaultGroup;
	}

	public int getIdGender() {
		return this.idGender;
	}

	public void setIdGender(int idGender) {
		this.idGender = idGender;
	}

	public int getIdLang() {
		return this.idLang;
	}

	public void setIdLang(int idLang) {
		this.idLang = idLang;
	}

	public int getIdRisk() {
		return this.idRisk;
	}

	public void setIdRisk(int idRisk) {
		this.idRisk = idRisk;
	}

	public int getIdShop() {
		return this.idShop;
	}

	public void setIdShop(int idShop) {
		this.idShop = idShop;
	}

	public int getIdShopGroup() {
		return this.idShopGroup;
	}

	public void setIdShopGroup(int idShopGroup) {
		this.idShopGroup = idShopGroup;
	}

	public String getIpRegistrationNewsletter() {
		return this.ipRegistrationNewsletter;
	}

	public void setIpRegistrationNewsletter(String ipRegistrationNewsletter) {
		this.ipRegistrationNewsletter = ipRegistrationNewsletter;
	}

	public int getIsGuest() {
		return this.isGuest;
	}

	public void setIsGuest(int isGuest) {
		this.isGuest = isGuest;
	}

	public Timestamp getLastPasswdGen() {
		return this.lastPasswdGen;
	}

	public void setLastPasswdGen(Timestamp lastPasswdGen) {
		this.lastPasswdGen = lastPasswdGen;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getMaxPaymentDays() {
		return this.maxPaymentDays;
	}

	public void setMaxPaymentDays(int maxPaymentDays) {
		this.maxPaymentDays = maxPaymentDays;
	}

	public int getNewsletter() {
		return this.newsletter;
	}

	public void setNewsletter(int newsletter) {
		this.newsletter = newsletter;
	}

	public Date getNewsletterDateAdd() {
		return this.newsletterDateAdd;
	}

	public void setNewsletterDateAdd(Date newsletterDateAdd) {
		this.newsletterDateAdd = newsletterDateAdd;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getOptin() {
		return this.optin;
	}

	public void setOptin(int optin) {
		this.optin = optin;
	}

	public BigDecimal getOutstandingAllowAmount() {
		return this.outstandingAllowAmount;
	}

	public void setOutstandingAllowAmount(BigDecimal outstandingAllowAmount) {
		this.outstandingAllowAmount = outstandingAllowAmount;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getSecureKey() {
		return this.secureKey;
	}

	public void setSecureKey(String secureKey) {
		this.secureKey = secureKey;
	}

	public int getShowPublicPrices() {
		return this.showPublicPrices;
	}

	public void setShowPublicPrices(int showPublicPrices) {
		this.showPublicPrices = showPublicPrices;
	}

	public String getSiret() {
		return this.siret;
	}

	public void setSiret(String siret) {
		this.siret = siret;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
