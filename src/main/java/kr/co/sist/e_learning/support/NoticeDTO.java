package kr.co.sist.e_learning.support;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class NoticeDTO {
	private String notice_id;
	private String notice_title;
	private String notice_content;
	private Date notice_created_date;
	private String notice_fix_flag;
	private String notice_writer;
	private int notice_hits;
	private Date notice_last_modified_date;
	private String notice_status;

	public String getNotice_id() {
		return notice_id;
	}

	public String getNotice_title() {
		return notice_title;
	}

	public String getNotice_content() {
		return notice_content;
	}

	public Date getNotice_created_date() {
		return notice_created_date;
	}

	public String getNotice_fix_flag() {
		return notice_fix_flag;
	}

	public String getNotice_writer() {
		return notice_writer;
	}

	public int getNotice_hits() {
		return notice_hits;
	}

	public Date getNotice_last_modified_date() {
		return notice_last_modified_date;
	}

	public String getNotice_status() {
		return notice_status;
	}

	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}

	public void setNotice_created_date(Date notice_created_date) {
		this.notice_created_date = notice_created_date;
	}

	public void setNotice_fix_flag(String notice_fix_flag) {
		this.notice_fix_flag = notice_fix_flag;
	}

	public void setNotice_writer(String notice_writer) {
		this.notice_writer = notice_writer;
	}

	public void setNotice_hits(int notice_hits) {
		this.notice_hits = notice_hits;
	}

	public void setNotice_last_modified_date(Date notice_last_modified_date) {
		this.notice_last_modified_date = notice_last_modified_date;
	}

	public void setNotice_status(String notice_status) {
		this.notice_status = notice_status;
	}

	@Override
	public String toString() {
		return "NoticeDTO [notice_id=" + notice_id + ", notice_title=" + notice_title + ", notice_content="
				+ notice_content + ", notice_created_date=" + notice_created_date + ", notice_fix_flag="
				+ notice_fix_flag + ", notice_writer=" + notice_writer + ", notice_hits=" + notice_hits
				+ ", notice_last_modified_date=" + notice_last_modified_date + ", notice_status=" + notice_status + "]";
	}

}// class
