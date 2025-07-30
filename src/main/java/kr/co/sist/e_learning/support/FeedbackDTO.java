package kr.co.sist.e_learning.support;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class FeedbackDTO {
	private String feedback_id;
	private String user_id;
	private String email;
	private String feedback_title;
	private String feedback_content;
	private Date feedback_created_date;
	private String feedback_status;
	private String feedback_step;
	private String feedback_style;
	private String feedback_type;

	public String getFeedback_id() {
		return feedback_id;
	}

	public void setFeedback_id(String feedback_id) {
		this.feedback_id = feedback_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFeedback_title() {
		return feedback_title;
	}

	public void setFeedback_title(String feedback_title) {
		this.feedback_title = feedback_title;
	}

	public String getFeedback_content() {
		return feedback_content;
	}

	public void setFeedback_content(String feedback_content) {
		this.feedback_content = feedback_content;
	}

	public Date getFeedback_created_date() {
		return feedback_created_date;
	}

	public void setFeedback_created_date(Date feedback_created_date) {
		this.feedback_created_date = feedback_created_date;
	}

	public String getFeedback_status() {
		return feedback_status;
	}

	public void setFeedback_status(String feedback_status) {
		this.feedback_status = feedback_status;
	}

	public String getFeedback_step() {
		return feedback_step;
	}

	public void setFeedback_step(String feedback_step) {
		this.feedback_step = feedback_step;
	}

	public String getFeedback_style() {
		return feedback_style;
	}

	public void setFeedback_style(String feedback_style) {
		this.feedback_style = feedback_style;
	}

	public String getFeedback_type() {
		return feedback_type;
	}

	public void setFeedback_type(String feedback_type) {
		this.feedback_type = feedback_type;
	}

	@Override
	public String toString() {
		return "FeedbackDTO [feedback_id=" + feedback_id + ", user_id=" + user_id + ", email=" + email
				+ ", feedback_title=" + feedback_title + ", feedback_content=" + feedback_content
				+ ", feedback_created_date=" + feedback_created_date + ", feedback_status=" + feedback_status
				+ ", feedback_step=" + feedback_step + ", feedback_style=" + feedback_style + ", feedback_type="
				+ feedback_type + "]";
	}

}// class
