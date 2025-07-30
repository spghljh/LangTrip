package kr.co.sist.e_learning.support;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class FaqDTO {
	private String faq_id;
	private String faqtype_id;
	private String faqtype_name;
	private String faq_title;
	private String faq_content;
	private Date faq_created_date;
	private int faq_hits;
	private Date faq_last_modified_date;
	private String faq_status;

	public String getFaq_id() {
		return faq_id;
	}

	public void setFaq_id(String faq_id) {
		this.faq_id = faq_id;
	}

	public String getFaqtype_id() {
		return faqtype_id;
	}

	public void setFaqtype_id(String faqtype_id) {
		this.faqtype_id = faqtype_id;
	}

	public String getFaqtype_name() {
		return faqtype_name;
	}

	public void setFaqtype_name(String faqtype_name) {
		this.faqtype_name = faqtype_name;
	}

	public String getFaq_title() {
		return faq_title;
	}

	public void setFaq_title(String faq_title) {
		this.faq_title = faq_title;
	}

	public String getFaq_content() {
		return faq_content;
	}

	public void setFaq_content(String faq_content) {
		this.faq_content = faq_content;
	}

	public Date getFaq_created_date() {
		return faq_created_date;
	}

	public void setFaq_created_date(Date faq_created_date) {
		this.faq_created_date = faq_created_date;
	}

	public int getFaq_hits() {
		return faq_hits;
	}

	public void setFaq_hits(int faq_hits) {
		this.faq_hits = faq_hits;
	}

	public Date getFaq_last_modified_date() {
		return faq_last_modified_date;
	}

	public void setFaq_last_modified_date(Date faq_last_modified_date) {
		this.faq_last_modified_date = faq_last_modified_date;
	}

	public String getFaq_status() {
		return faq_status;
	}

	public void setFaq_status(String faq_status) {
		this.faq_status = faq_status;
	}

	@Override
	public String toString() {
		return "FaqDTO [faq_id=" + faq_id + ", faqtype_id=" + faqtype_id + ", faqtype_name=" + faqtype_name
				+ ", faq_title=" + faq_title + ", faq_content=" + faq_content + ", faq_created_date=" + faq_created_date
				+ ", faq_hits=" + faq_hits + ", faq_last_modified_date=" + faq_last_modified_date + ", faq_status="
				+ faq_status + "]";
	}

}