package kr.co.sist.e_learning.usercourse;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Alias("UserCourseDTO")
@ToString
public class UserCourseDTO {
	private int enrollSeq;
	private String userId;
	private String courseSeq;
	private Date enrollDate;
	private String status;
	private int progress;
	private String courseTitle;
	private String introduction;
	private Date uploadDate;
	private String isPublic;
	private String flag;
	private String category;
	private String difficulty;
	private String thumbnailPath;
	private String thumbnailName;
}
