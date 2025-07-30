package kr.co.sist.e_learning.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SupportDAOImpl implements SupportDAO {

	private static SupportDAOImpl sDAO;

	private SupportDAOImpl() {
	}// SupportDAOImpl

	public static SupportDAOImpl getInstance() {
		if (sDAO == null) {
			sDAO = new SupportDAOImpl();
		} // end if
		return sDAO;

	}// getInstance

	public static Connection getConnection() throws SQLException {
//		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
//		return DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl", "c##under", "dog");
//		return DriverManager.getConnection("jdbc:oracle:thin:@211.63.89.162:1521:orcl", "c##under", "dog");
		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "c##under", "dog");
	}// getConnection

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean insertNoticeDTO(NoticeDTO notice) {
		String sql = "SELECT NVL(MAX(TO_NUMBER(notice_id)), 999) FROM notice WHERE REGEXP_LIKE(notice_id, '^[0-9]+$')";

		try (Connection conn = getConnection()) {
			String newId = null;
			try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
				int id = 1000;
				if (rs.next()) {
					id = rs.getInt(1) + 1;
				} // end if
				newId = String.valueOf(id);
				notice.setNotice_id(newId);
			} // end try

			// INSERT 실행
			try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO notice (notice_id) VALUES (?)")) {
				insertStmt.setString(1, newId);
				int result = insertStmt.executeUpdate();
//				System.out.println("[NoticeDTO] " + result + "건의 insert 시행.");
				return result > 0;
			} // end try

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} // end catch
	}// insertNoticeDTO

	@Override
	public List<NoticeDTO> selectAllNoticeDTO() {
		List<NoticeDTO> resultList = new ArrayList<>();
//		String sql = "SELECT * FROM notice ORDER BY notice_created_date DESC";
//		String sql = "SELECT * FROM notice ORDER BY notice_fix_flag ASC, notice_created_date DESC";
		String sql = "SELECT * FROM notice WHERE notice_status = 'A' ORDER BY notice_fix_flag ASC, notice_created_date DESC";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setNotice_id(rs.getString("notice_id"));
				dto.setNotice_title(rs.getString("notice_title"));
				dto.setNotice_content(rs.getString("notice_content"));
				dto.setNotice_created_date(rs.getDate("notice_created_date"));
				dto.setNotice_fix_flag(rs.getString("notice_fix_flag"));
				dto.setNotice_writer(rs.getString("notice_writer"));
				dto.setNotice_hits(rs.getInt("notice_hits"));
				dto.setNotice_last_modified_date(rs.getDate("notice_last_modified_date"));
				dto.setNotice_status(rs.getString("notice_status"));
				resultList.add(dto);
			} // end while

			System.out.println("[공지사항 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return resultList;
	}// selectAllNoticeDTO

	@Override
	public List<NoticeDTO> selectAllNoticeDTOAdmin() {
		List<NoticeDTO> resultList = new ArrayList<>();
//		String sql = "SELECT * FROM notice ORDER BY notice_created_date DESC";
//		String sql = "SELECT * FROM notice ORDER BY notice_fix_flag ASC, notice_created_date DESC";
		String sql = "SELECT * FROM notice WHERE notice_status = 'A' ORDER BY notice_id DESC ";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setNotice_id(rs.getString("notice_id"));
				dto.setNotice_title(rs.getString("notice_title"));
				dto.setNotice_content(rs.getString("notice_content"));
				dto.setNotice_created_date(rs.getDate("notice_created_date"));
				dto.setNotice_fix_flag(rs.getString("notice_fix_flag"));
				dto.setNotice_writer(rs.getString("notice_writer"));
				dto.setNotice_hits(rs.getInt("notice_hits"));
				dto.setNotice_last_modified_date(rs.getDate("notice_last_modified_date"));
				dto.setNotice_status(rs.getString("notice_status"));
				resultList.add(dto);
			} // end while

			System.out.println("[공지사항 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return resultList;
	}// searchAllNoticeAdmin

	@Override
	public NoticeDTO selectOneNoticeById(String id) {
		NoticeDTO notice = null;
		String sql = "SELECT * FROM notice WHERE notice_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						notice = new NoticeDTO();
						notice.setNotice_id(rs.getString("notice_id"));
						notice.setNotice_title(rs.getString("notice_title"));
						notice.setNotice_content(rs.getString("notice_content"));
						notice.setNotice_created_date(rs.getDate("notice_created_date"));
						notice.setNotice_fix_flag(rs.getString("notice_fix_flag"));
						notice.setNotice_writer(rs.getString("notice_writer"));
						notice.setNotice_hits(rs.getInt("notice_hits"));
						notice.setNotice_last_modified_date(rs.getDate("notice_last_modified_date"));
						notice.setNotice_status(rs.getString("notice_status"));
					} // end if
				} // end try
			} // end try
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return notice;
	}// selectOneNoticeById

	@Override
	public int updateNoticeById(NoticeDTO notice) {
		int result = 0;
		String sql = "UPDATE notice " + "SET notice_title = ?, " + "    notice_content = ?, "
				+ "    notice_writer = ?, " + "    notice_last_modified_date = SYSDATE, " + "    notice_fix_flag = ?, "
				+ "    notice_status = ? " + "WHERE notice_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, notice.getNotice_title());
				stmt.setString(2, notice.getNotice_content());
				stmt.setString(3, notice.getNotice_writer());
				stmt.setString(4, notice.getNotice_fix_flag());
				stmt.setString(5, notice.getNotice_status());
				stmt.setString(6, notice.getNotice_id());

				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean updateNoticeTitle(String id, String newTitle) {
		return false;
	}

	@Override
	public boolean updateNoticeContent(String id, String newContent) {
		return false;
	}

	@Override
	public int updateNoticeStatusInactive(String notice_id) {
		int result = 0;
//		String sql = "UPDATE notice SET notice_status = 'I', notice_last_modified_date = SYSDATE WHERE notice_id = ?";
		String sql = "UPDATE notice SET notice_status = 'I' WHERE notice_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, notice_id);
				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public int updateFaqStatusInactive(String notice_id) {
		int result = 0;
//		String sql = "UPDATE notice SET notice_status = 'I', notice_last_modified_date = SYSDATE WHERE notice_id = ?";
		String sql = "UPDATE faq SET faq_status = 'I' WHERE faq_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, notice_id);
				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public int updateNoticeStatusActive(String notice_id) {
		int result = 0;
//		String sql = "UPDATE notice SET notice_status = 'A', notice_last_modified_date = SYSDATE WHERE notice_id = ?";
		String sql = "UPDATE notice SET notice_status = 'A' WHERE notice_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, notice_id);
				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<NoticeDTO> selectNoticeTitlesByKeyword(String keyword) {
		List<NoticeDTO> resultList = new ArrayList<>();
		String sql = "SELECT notice_title FROM notice WHERE notice_title LIKE ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, keyword + "%");

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					NoticeDTO dto = new NoticeDTO();
					dto.setNotice_title(rs.getString("notice_title"));
					resultList.add(dto);
				} // end while
			}

			System.out.println("[SupportDAOImpl] " + resultList.size() + "건의 추천 키워드 조회.");

		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return resultList;
	}// selectNoticeTitlesByKeyword

	@Override
	public NoticeDTO selectOneNoticeByTitle(String title) {
		NoticeDTO notice = null;
		String sql = "SELECT * FROM notice WHERE notice_title = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, title);

				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						notice = new NoticeDTO();
						notice.setNotice_id(rs.getString("notice_id"));
						notice.setNotice_title(rs.getString("notice_title"));
						notice.setNotice_content(rs.getString("notice_content"));
						notice.setNotice_created_date(rs.getDate("notice_created_date"));
						notice.setNotice_fix_flag(rs.getString("notice_fix_flag"));
						notice.setNotice_writer(rs.getString("notice_writer"));
						notice.setNotice_hits(rs.getInt("notice_hits"));
						notice.setNotice_last_modified_date(rs.getDate("notice_last_modified_date"));
						notice.setNotice_status(rs.getString("notice_status"));
					} // end if
				} // end try
			} // end try
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return notice;
	}// selectOneNoticeByTitle

	@Override
	public List<NoticeDTO> selectNoticesByKeyword(String keyword) {
		List<NoticeDTO> list = new ArrayList<>();
		String sql = "SELECT notice_id, notice_title, notice_hits FROM notice WHERE notice_title LIKE ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//				stmt.setString(1, keyword + "%");
				stmt.setString(1, "%" + keyword + "%");

				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						NoticeDTO dto = new NoticeDTO();
						dto.setNotice_id(rs.getString("notice_id"));
						dto.setNotice_title(rs.getString("notice_title"));
						dto.setNotice_hits(rs.getInt("notice_hits"));
						list.add(dto);
					} // end while
				} // end try
			} // end try
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return list;
	}// selectNoticesByKeyword

	@Override
	public boolean increaseNoticeHits(String id) {
		boolean flag = true;
		String sql = "UPDATE notice SET notice_hits = notice_hits + 1 WHERE notice_id = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // 혹은 로깅 처리
			return false;
		}
		return flag;
	}

	@Override
	public void updateNoticeFixFlag(String noticeId, String fixFlag) {
		String sql = "UPDATE notice SET notice_fix_flag = ? WHERE notice_id = ?";

		try (Connection conn = getConnection(); // 또는 dataSource.getConnection()
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, fixFlag);
			stmt.setString(2, noticeId);

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("공지 고정 상태 업데이트 실패", e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean insertFaqDTO(FaqDTO faq) {
		String sql = "SELECT NVL(MAX(TO_NUMBER(faq_id)), 1999) FROM faq WHERE REGEXP_LIKE(faq_id, '^[0-9]+$')";

		try (Connection conn = getConnection()) {
			String newId = null;
			try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
				int id = 2000;
				if (rs.next()) {
					id = rs.getInt(1) + 1;
				} // end if
				newId = String.valueOf(id);
				faq.setFaq_id(newId);
			} // end try

			try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO faq (faq_id) VALUES (?)")) {
				insertStmt.setString(1, newId);
				int result = insertStmt.executeUpdate();
//				System.out.println("[FaqDTO] " + result + "건의 insert 시행.");
				return result > 0;
			} // end try

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} // end catch
	}// insertFaqDTO

	@Override
	public List<FaqDTO> selectAllFaqDTO() {
		List<FaqDTO> resultList = new ArrayList<>();

//		faqtype_name 참조된 동일한 쿼리(가독성)

//	    String sql = "SELECT " +
//	                 "f.faq_id, " +
//	                 "f.faqtype_id, " +
//	                 "NVL(f.faqtype_name, t.faqtype_name) AS faqtype_name, " +
//	                 "f.faq_title, " +
//	                 "f.faq_content, " +
//	                 "f.faq_created_date, " +
//	                 "f.faq_hits, " +
//	                 "f.faq_last_modified_date, " +
//	                 "f.faq_status " +
//	                 "FROM faq f " +
//	                 "LEFT JOIN faqtype t ON f.faqtype_id = t.faqtype_id " +
//	                 "ORDER BY f.faq_id ASC";

		String sql = "SELECT " + "f.faq_id, " + "f.faqtype_id, "
				+ "NVL(f.faqtype_name, t.faqtype_name) AS faqtype_name, " + "f.faq_title, " + "f.faq_content, "
				+ "f.faq_created_date, " + "f.faq_hits, " + "f.faq_last_modified_date, " + "f.faq_status "
				+ "FROM faq f " + "LEFT JOIN faqtype t ON f.faqtype_id = t.faqtype_id " + "WHERE faq_status = 'A' "
				+ "ORDER BY f.faq_id ASC";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				FaqDTO dto = new FaqDTO();
				dto.setFaq_id(rs.getString("faq_id"));
				dto.setFaqtype_id(rs.getString("faqtype_id"));
				dto.setFaqtype_name(rs.getString("faqtype_name")); // NVL 적용된 컬럼값
				dto.setFaq_title(rs.getString("faq_title"));
				dto.setFaq_content(rs.getString("faq_content"));
				dto.setFaq_created_date(rs.getDate("faq_created_date"));
				dto.setFaq_hits(rs.getInt("faq_hits"));
				dto.setFaq_last_modified_date(rs.getDate("faq_last_modified_date"));
				dto.setFaq_status(rs.getString("faq_status"));

				resultList.add(dto);
			}

			System.out.println("[FAQ 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	@Override
	public FaqDTO selectOneFaqById(String id) {
		FaqDTO faq = null;
		String sql = "SELECT * FROM faq WHERE faq_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						faq = new FaqDTO();
						faq.setFaq_id(rs.getString("faq_id"));
						faq.setFaqtype_id(rs.getString("faqtype_id"));
						faq.setFaq_title(rs.getString("faq_title"));
						faq.setFaq_content(rs.getString("faq_content"));
						faq.setFaq_created_date(rs.getDate("faq_created_date"));
						faq.setFaq_hits(rs.getInt("faq_hits"));
						faq.setFaq_last_modified_date(rs.getDate("faq_last_modified_date"));
						faq.setFaq_status(rs.getString("faq_status"));
					} // end if
				} // end try
			} // end try
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return faq;
	}// selectOneFaqById

	@Override
	public int updateFaqById(FaqDTO faq) {
		int result = 0;
		String sql = "UPDATE faq SET faq_title = ?, faq_content = ? WHERE faq_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, faq.getFaq_title());
				stmt.setString(2, faq.getFaq_content());
				stmt.setString(3, faq.getFaq_id());

				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	} // updateFaqById

	@Override
	public boolean updateFaqTitle(String id, String newTitle) {
		return false;
	}

	@Override
	public boolean updateFaqContent(String id, String newContent) {
		return false;
	}

	@Override
	public boolean updateFaqtype(String id, String faqtype) {
		return false;
	}

	@Override
	public List<FaqDTO> selectFaqsByType(String typeId) {
		List<FaqDTO> faqList = new ArrayList<>();
		String sql = "SELECT * FROM faq WHERE faqtype_id = ? AND faq_status = 'A'";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, typeId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					FaqDTO faq = new FaqDTO();
					faq.setFaq_id(rs.getString("faq_id"));
					faq.setFaqtype_id(rs.getString("faqtype_id"));
					faq.setFaq_title(rs.getString("faq_title"));
					faq.setFaq_content(rs.getString("faq_content"));
					faq.setFaq_created_date(rs.getDate("faq_created_date"));
					faq.setFaq_hits(rs.getInt("faq_hits"));
					faq.setFaq_last_modified_date(rs.getDate("faq_last_modified_date"));
					faq.setFaq_status(rs.getString("faq_status"));
					faqList.add(faq);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return faqList;
	}

	@Override
	public int updateFaqStatus(String faq_id) {
		int result = 0;
		String sql = "UPDATE faq SET faq_status = 'I', faq_last_modified_date = SYSDATE WHERE faq_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, faq_id);
				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<FaqDTO> selectFaqByType(String typeId) {
		List<FaqDTO> faqList = new ArrayList<>();

		String sql = "SELECT * FROM faq WHERE faqtype_id = ? ORDER BY faq_id DESC";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, typeId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					FaqDTO dto = new FaqDTO();
					dto.setFaq_id(rs.getString("faq_id"));
					dto.setFaqtype_id(rs.getString("faqtype_id"));
					dto.setFaq_title(rs.getString("faq_title"));
					dto.setFaq_content(rs.getString("faq_content"));
					dto.setFaq_created_date(rs.getDate("faq_created_date"));
					dto.setFaq_hits(rs.getInt("faq_hits"));
					dto.setFaq_last_modified_date(rs.getDate("faq_last_modified_date"));
					dto.setFaq_status(rs.getString("faq_status"));
					faqList.add(dto);
				}
			}

			System.out.println("[SupportDAO] " + faqList.size() + "건의 faqtype_id(" + typeId + ") 반환.");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return faqList;
	}// selectFaqByType

	@Override
	public List<FaqtypeDTO> selectAllFaqtype() {
		List<FaqtypeDTO> resultList = new ArrayList<>();
		String sql = "SELECT * FROM faqtype WHERE faqtype_status = 'A' ORDER BY faqtype_id ASC";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				FaqtypeDTO dto = new FaqtypeDTO();
				dto.setFaqtype_id(rs.getString("faqtype_id"));
				dto.setFaqtype_name(rs.getString("faqtype_name"));
				dto.setFaqtype_status(rs.getString("faqtype_status"));

				resultList.add(dto);
			}

			System.out.println("[FAQ 유형 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public List<FeedbackDTO> selectAllFeedbackDTO() {
		List<FeedbackDTO> resultList = new ArrayList<>();
		String sql = "SELECT * FROM feedback ORDER BY feedback_created_date DESC";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				FeedbackDTO dto = new FeedbackDTO();
				dto.setFeedback_id(rs.getString("feedback_id"));
				dto.setUser_id(rs.getString("user_id"));
				dto.setEmail(rs.getString("email"));
				dto.setFeedback_title(rs.getString("feedback_title"));
				dto.setFeedback_content(rs.getString("feedback_content"));
				dto.setFeedback_created_date(rs.getDate("feedback_created_date"));
				dto.setFeedback_status(rs.getString("feedback_status"));
				dto.setFeedback_step(rs.getString("feedback_step"));
				dto.setFeedback_style(rs.getString("feedback_style"));
				dto.setFeedback_type(rs.getString("feedback_type"));
				resultList.add(dto);
			} // end while

			System.out.println("[피드백 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return resultList;
	}// selectAllFeedbackDTO

	@Override
	public List<FeedbackDTO> selectTotalFeedbackDTO() {
		List<FeedbackDTO> resultList = new ArrayList<>();
		String sql = "SELECT * FROM feedback ORDER BY feedback_created_date DESC";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				FeedbackDTO dto = new FeedbackDTO();
				dto.setFeedback_id(rs.getString("feedback_id"));
				dto.setUser_id(rs.getString("user_id"));
				dto.setEmail(rs.getString("email"));
				dto.setFeedback_title(rs.getString("feedback_title"));
				dto.setFeedback_content(rs.getString("feedback_content"));
				dto.setFeedback_created_date(rs.getDate("feedback_created_date"));
				dto.setFeedback_status(rs.getString("feedback_status"));
				dto.setFeedback_step(rs.getString("feedback_step"));
				dto.setFeedback_style(rs.getString("feedback_style"));
				dto.setFeedback_type(rs.getString("feedback_type"));
				resultList.add(dto);
			} // end while

			System.out.println("[피드백 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return resultList;
	}// selectAllFeedbackDTO

	// ------------------------------

	@Override
	public boolean insertFeedbackDTO(FeedbackDTO feedback) {
		String idSql = "SELECT NVL(MAX(TO_NUMBER(feedback_id)), 2999) FROM feedback WHERE REGEXP_LIKE(feedback_id, '^[0-9]+$')";

		try (Connection conn = getConnection()) {
			String newId = null;
			try (PreparedStatement stmt = conn.prepareStatement(idSql); ResultSet rs = stmt.executeQuery()) {
				int id = 3000;

				if (rs.next()) {
					id = rs.getInt(1) + 1;
				}
				newId = String.valueOf(id);
				feedback.setFeedback_id(newId);
			}

			String insertSql = "INSERT INTO feedback (feedback_id, user_id, email, feedback_title, feedback_content, feedback_created_date, feedback_status, feedback_step, feedback_style, feedback_type) "
					+ "VALUES (?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?)";

			try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
				insertStmt.setString(1, feedback.getFeedback_id());
				insertStmt.setString(2, feedback.getUser_id());
				insertStmt.setString(3, feedback.getEmail());
				insertStmt.setString(4, feedback.getFeedback_title());

				insertStmt.setString(5, feedback.getFeedback_content());
//				insertStmt.setDate(6, feedback.getFeedback_created_date());
				insertStmt.setString(6, "A");// 활성A, 비활성I
				insertStmt.setString(7, "1");// 접수된 피드백 = 초기상태 1 = 접수완료
				insertStmt.setString(8, feedback.getFeedback_style()); // 실명1, 익명0
				insertStmt.setString(9, feedback.getFeedback_type());

				System.out.println("feedback.getFeedback_style(): " + feedback.getFeedback_style());
				int result = insertStmt.executeUpdate();
//				System.out.println("[FeedbackDTO] " + result + "건의 전체 insert 시행.");
				return result > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<FeedbackDTO> selectAllNewFeedbackDTO() {
		List<FeedbackDTO> resultList1 = new ArrayList<>();
		String sql1 = "SELECT * FROM feedback  WHERE feedback_step = '1'  ORDER BY feedback_id ASC";

		try (Connection conn1 = getConnection();
				PreparedStatement stmt1 = conn1.prepareStatement(sql1);
				ResultSet rs1 = stmt1.executeQuery()) {

			System.out.println();
//			boolean selectFlag1 = rs1.next();
			while (rs1.next()) {
				FeedbackDTO dto1 = new FeedbackDTO();
				dto1.setFeedback_id(rs1.getString("feedback_id"));
				dto1.setUser_id(rs1.getString("user_id"));
				dto1.setEmail(rs1.getString("email"));
				dto1.setFeedback_title(rs1.getString("feedback_title"));
				dto1.setFeedback_content(rs1.getString("feedback_content"));
				dto1.setFeedback_created_date(rs1.getDate("feedback_created_date"));
				dto1.setFeedback_status(rs1.getString("feedback_status"));
				dto1.setFeedback_step(rs1.getString("feedback_step"));
				dto1.setFeedback_style(rs1.getString("feedback_style"));
				dto1.setFeedback_type(rs1.getString("feedback_type"));
				resultList1.add(dto1);
			} // end while
			System.out.println("resultList1: " + resultList1);
			System.out.println("[접수된 피드백 목록]");
			resultList1.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return resultList1;
	}

	@Override
	public List<FeedbackDTO> selectAllNiceFeedbackDTO() {
		List<FeedbackDTO> resultList2 = new ArrayList<>();
		String sql2 = "SELECT * FROM feedback WHERE feedback_step = '2' ORDER BY feedback_id ASC";

		try (Connection conn2 = getConnection();
				PreparedStatement stmt2 = conn2.prepareStatement(sql2);
				ResultSet rs2 = stmt2.executeQuery()) {

			System.out.println();
//			boolean selectFlag2 = rs2.next();
			while (rs2.next()) {
				FeedbackDTO dto2 = new FeedbackDTO();
				dto2.setFeedback_id(rs2.getString("feedback_id"));
				dto2.setUser_id(rs2.getString("user_id"));
				dto2.setEmail(rs2.getString("email"));
				dto2.setFeedback_title(rs2.getString("feedback_title"));
				dto2.setFeedback_content(rs2.getString("feedback_content"));
				dto2.setFeedback_created_date(rs2.getDate("feedback_created_date"));
				dto2.setFeedback_status(rs2.getString("feedback_status"));
				dto2.setFeedback_step(rs2.getString("feedback_step"));
				dto2.setFeedback_style(rs2.getString("feedback_style"));
				dto2.setFeedback_type(rs2.getString("feedback_type"));
				resultList2.add(dto2);
			} // end while
			System.out.println("resultList2: " + resultList2);
			System.out.println("[진행중인 피드백 목록]");
			resultList2.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return resultList2;
	}

	// ------------------------------

//	@Override
//	public List<FeedbackDTO> selectAllNiceFeedbackDTO() {
//		List<FeedbackDTO> resultList2 = new ArrayList<>();
//		String sql = "SELECT * FROM feedback ORDER BY feedback_created_date DESC";
//		String sql = "SELECT * FROM feedback  WHERE feedback_step = '2'  ORDER BY feedback_created_date DESC";
//		String sql2 = "SELECT * FROM feedback  WHERE feedback_step = '2'  ORDER BY feedback_id ASC";
//
//		try (Connection conn2 = getConnection();
//				PreparedStatement stmt2 = conn2.prepareStatement(sql2);
//				ResultSet rs2 = stmt2.executeQuery()) {
//
//			System.out.println();
//			System.out.println("conn2: " + conn2);
//			System.out.println("rs2.next(): " + rs2.next());
//
//			while (rs2.next()) {
//				FeedbackDTO dto2 = new FeedbackDTO();
//				dto2.setFeedback_id(rs2.getString("feedback_id"));
//				dto2.setUser_id(rs2.getString("user_id"));
//				dto2.setEmail(rs2.getString("email"));
//				dto2.setFeedback_title(rs2.getString("feedback_title"));
//				dto2.setFeedback_content(rs2.getString("feedback_content"));
//				dto2.setFeedback_created_date(rs2.getDate("feedback_created_date"));
//				dto2.setFeedback_status(rs2.getString("feedback_status"));
//				dto2.setFeedback_step(rs2.getString("feedback_step"));
//				dto2.setFeedback_style(rs2.getString("feedback_style"));
//				resultList2.add(dto2);
//			} // end while
//
//			System.out.println("resultList2: " + resultList2);
//			System.out.println("[진행중인 피드백 목록]");
//			resultList2.forEach(System.out::println);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} // end catch
//
//		return resultList2;
//	}

	@Override
	public FeedbackDTO selectOneFeedbackById(String id) {
		FeedbackDTO feedback = null;
		String sql = "SELECT * FROM feedback WHERE feedback_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						feedback = new FeedbackDTO();
						feedback.setFeedback_id(rs.getString("feedback_id"));
						feedback.setUser_id(rs.getString("user_id"));
						feedback.setEmail(rs.getString("email"));
						feedback.setFeedback_title(rs.getString("feedback_title"));
						feedback.setFeedback_content(rs.getString("feedback_content"));
						feedback.setFeedback_created_date(rs.getDate("feedback_created_date"));
						feedback.setFeedback_status(rs.getString("feedback_status"));
						feedback.setFeedback_step(rs.getString("feedback_step"));
						feedback.setFeedback_step(rs.getString("feedback_style"));
						feedback.setFeedback_type(rs.getString("feedback_type"));

					} // end if
				} // end try
			} // end try
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return feedback;
	}// selectOneFeedbackById

	public boolean updateFeedbackStatus(String feedback_id) {
		boolean flag = false;
		String sql = "UPDATE feedback SET feedback_status = 'I', feedback_created_date = SYSDATE WHERE feedback_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, feedback_id);
				if (stmt.executeUpdate() > 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;
	}

	@Override
	public boolean updateFeedbackStep(String feedback_id) {
		boolean flag = false;
		String sql = "UPDATE feedback SET feedback_step = '2' WHERE feedback_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, feedback_id);
				if (stmt.executeUpdate() > 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("feedback_status: " + flag);
		return flag;

	}

	@Override
	public boolean updateFeedbackStepRefuse(String feedback_id) {
		boolean flag = false;
		String sql = "UPDATE feedback SET feedback_step = '0' WHERE feedback_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, feedback_id);
				if (stmt.executeUpdate() > 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("feedback_status: " + flag);
		return flag;

	}

	@Override
	public boolean updateFeedbackStepFinished(String feedback_id) {
		boolean flag = false;
		String sql = "UPDATE feedback SET feedback_step = '3' WHERE feedback_id = ?";

		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, feedback_id);
				if (stmt.executeUpdate() > 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;

	}

	@Override
	public boolean increaseFaqHits(String id) {
		boolean flag = true;
		String sql = "UPDATE faq SET faq_hits = faq_hits + 1 WHERE faq_id = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return flag;
	}

	@Override
	public List<FaqtypeDTO> selectActiveFaqTypes() {
		List<FaqtypeDTO> list = new ArrayList<>();
		String sql = "SELECT faqtype_id, faqtype_name FROM faqtype WHERE faqtype_status = 'A'";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				FaqtypeDTO dto = new FaqtypeDTO();
				dto.setFaqtype_id(rs.getString("faqtype_id"));
				dto.setFaqtype_name(rs.getString("faqtype_name"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<FaqDTO> selectTop3FaqDTO() {
		List<FaqDTO> resultList = new ArrayList<>();
//		String sql = "SELECT * FROM faq ORDER BY faq_hits DESC FETCH FIRST 3 ROWS ONLY";
		String sql = "SELECT * FROM ( SELECT * FROM faq ORDER BY faq_hits DESC, faq_id ASC ) WHERE ROWNUM <= 10";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				FaqDTO dto = new FaqDTO();
				dto.setFaq_id(rs.getString("faq_id"));
				dto.setFaqtype_id(rs.getString("faqtype_id"));
				dto.setFaq_title(rs.getString("faq_title"));
				dto.setFaq_content(rs.getString("faq_content"));
				dto.setFaq_created_date(rs.getDate("faq_created_date"));
				dto.setFaq_hits(rs.getInt("faq_hits"));
				dto.setFaq_last_modified_date(rs.getDate("faq_last_modified_date"));
				dto.setFaq_status(rs.getString("faq_status"));
				resultList.add(dto);
			}

			System.out.println("[FAQ 상위 3개 조회순]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	@Override
	public List<FaqDTO> selectFaqByTypeId(String typeId) {
		List<FaqDTO> faqList = new ArrayList<>();
		String sql = "SELECT * FROM faq WHERE faqtype_id = ? ORDER BY faq_created_date DESC";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, typeId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					FaqDTO faq = new FaqDTO();
					faq.setFaq_id(rs.getString("faq_id"));
					faq.setFaqtype_id(rs.getString("faqtype_id"));
					faq.setFaq_title(rs.getString("faq_title"));
					faq.setFaq_content(rs.getString("faq_content"));
					faq.setFaq_created_date(rs.getDate("faq_created_date"));
					faq.setFaq_hits(rs.getInt("faq_hits"));
					faq.setFaq_last_modified_date(rs.getDate("faq_last_modified_date"));
					faq.setFaq_status(rs.getString("faq_status"));

					faqList.add(faq);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return faqList;
	}

	@Override
	public int selectMaxFaqHits() {
		String sql = "SELECT MAX(faq_hits) FROM faq";
		int maxHits = 0;

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			if (rs.next()) {
				maxHits = rs.getInt(1); // 첫 번째 컬럼 (MAX값) 가져오기
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxHits;
	}

	@Override
	public int selectMaxFeedbackSends() {
		String sql = "SELECT COUNT(*) AS cnt FROM feedback GROUP BY feedback_type ORDER BY cnt DESC FETCH FIRST 1 ROWS ONLY";
		int maxSends = 0;

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			if (rs.next()) {
				maxSends = rs.getInt(1); // 첫 번째 컬럼 (MAX값) 가져오기
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxSends;
	}

	@Override
	public List<FaqDTO> selectFaqsByKeyword(String keyword) {
		List<FaqDTO> list = new ArrayList<>();
		String sql = "SELECT faq_id, faq_title, faq_hits FROM faq WHERE faq_title LIKE ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, "%" + keyword + "%");

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					FaqDTO dto = new FaqDTO();
					dto.setFaq_id(rs.getString("faq_id"));
					dto.setFaq_title(rs.getString("faq_title"));
					dto.setFaq_hits(rs.getInt("faq_hits"));
					list.add(dto);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

}
