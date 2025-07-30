package kr.co.sist.e_learning.support;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class SupportServiceAbstract implements SupportService {

	public List<NoticeDTO> getPaginatedNotices(String keyword, int page, int pageSize) {
		List<NoticeDTO> all = searchAllNotice();

		if (keyword != null && !keyword.trim().isEmpty()) {
			String[] words = keyword.trim().toLowerCase().split("\\s+");
			all = all.stream().filter(n -> {
				String title = Optional.ofNullable(n.getNotice_title()).orElse("").toLowerCase();
				String content = Optional.ofNullable(n.getNotice_content()).orElse("").toLowerCase();
				return Arrays.stream(words).anyMatch(w -> title.contains(w) || content.contains(w));
			}).collect(Collectors.toList());
		}

		int start = (page - 1) * pageSize;
		int end = Math.min(start + pageSize, all.size());
		return all.subList(start, end);
	}

	public int getFilteredNoticeCount(String keyword) {
		List<NoticeDTO> all = searchAllNotice();

		if (keyword != null && !keyword.trim().isEmpty()) {
			String[] words = keyword.trim().toLowerCase().split("\\s+");
			return (int) all.stream().filter(n -> {
				String title = Optional.ofNullable(n.getNotice_title()).orElse("").toLowerCase();
				String content = Optional.ofNullable(n.getNotice_content()).orElse("").toLowerCase();
				return Arrays.stream(words).anyMatch(w -> title.contains(w) || content.contains(w));
			}).count();
		}

		return all.size();
	}

}
