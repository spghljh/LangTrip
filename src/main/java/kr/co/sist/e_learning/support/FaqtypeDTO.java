package kr.co.sist.e_learning.support;

import org.springframework.stereotype.Component;

@Component
public class FaqtypeDTO {
	private String faqtype_id;
	private String faqtype_name;
	private String faqtype_status;

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

	public String getFaqtype_status() {
		return faqtype_status;
	}

	public void setFaqtype_status(String faqtype_status) {
		this.faqtype_status = faqtype_status;
	}

	@Override
	public String toString() {
		return "FaqtypeDTO [faqtype_id=" + faqtype_id + ", faqtype_name=" + faqtype_name + ", faqtype_status="
				+ faqtype_status + "]";
	}

}// class
