//---------------------------------------------------------------------------------------------------------------//
//faq_ready.js


// 최초 페이지 로딩 시
window.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("faq-result-container");
  if (!container) return;

  const pendingType = localStorage.getItem("pendingFaqType");
  loadFaqType(pendingType || "all");
  localStorage.removeItem("pendingFaqType");
});

function scrollToFaq(event, id) {
    event.preventDefault();
    const el = document.getElementById(id);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth' });
      history.pushState(null, null, '#' + id);
    }
  }

  // 아코디언 열고 닫기
  function toggleAccordion(el) {
    const accordion = el.nextElementSibling;

    if (accordion.style.maxHeight) {
      accordion.style.maxHeight = null;
    } else {
      accordion.style.maxHeight = accordion.scrollHeight + "px";

      const faqId = el.getAttribute("data-id");
      if (faqId) {
        fetch(`/faq/hit?id=${faqId}`, { method: 'POST' })
          .catch(err => console.error("조회수 증가 오류:", err));
      }
    }
  }


// 비동기 FAQ 유형 로딩
function loadFaqType(typeId) {
  const container = document.getElementById("faq-result-container");
  if (!container) return;

  fetch(`/support/faq/faqs/${typeId}`)
    .then(response => response.text())
    .then(data => {
      container.innerHTML = data;

      // 아코디언 바인딩 재설정
      container.querySelectorAll('.child-div[onclick="toggleAccordion(this)"]').forEach(el => {
        el.onclick = function () {
          toggleAccordion(this);
        };
      });

      // openId 처리
      const openId = new URLSearchParams(window.location.search).get("openId");
      if (openId) {
        const checkExist = setInterval(() => {
          const target = document.querySelector(`[data-id="${openId}"]`);
          if (target) {
            toggleAccordion(target);
            target.scrollIntoView({ behavior: "smooth" });
            clearInterval(checkExist);
          }
        }, 100);
      }

    })
    .catch(error => {
      console.error("FAQ 데이터 불러오기 실패:", error);
    });
}

window.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("faq-result-container");
  if (!container) {
    // 해당 페이지는 FAQ 결과 영역이 없으므로 loadFaqType 호출하지 않음
    return;
  }

  const pendingType = localStorage.getItem("pendingFaqType");
  loadFaqType(pendingType || "all");
  localStorage.removeItem("pendingFaqType");

  // 특정 FAQ 자동 펼치기 및 이동
  const openId = new URLSearchParams(window.location.search).get("openId");
  if (openId) {
    const checkExist = setInterval(() => {
      const target = document.querySelector(`[data-id="${openId}"]`);
      if (target) {
        toggleAccordion(target);
        target.scrollIntoView({ behavior: "smooth" });
        clearInterval(checkExist);
      }
    }, 100);
  }
});







function toggleAccordion(el) {
  const accordion = el.nextElementSibling;

  if (accordion.style.maxHeight) {
    accordion.style.maxHeight = null;
  } else {
    accordion.style.maxHeight = accordion.scrollHeight + "px";

    const faqId = el.getAttribute("data-id");
    if (faqId) {
      fetch(`/faq/hit?id=${faqId}`, {
        method: 'POST'
      }).catch(err => {
        console.error("조회수 증가 오류:", err);
      });
    }
  }
}




document.addEventListener("DOMContentLoaded", () => {
  const openId = new URLSearchParams(window.location.search).get("openId");
  if (openId) {
    const checkExist = setInterval(() => {
      const target = document.querySelector(`[data-id="${openId}"]`);
      if (target) {
        toggleAccordion(target);
        scrollToWithOffset(target, 240); // ← 여기!
        clearInterval(checkExist);
      }
    }, 100);
  }
});







function scrollToWithOffset(element, offset = 240) {
  const rect = element.getBoundingClientRect();
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
  window.scrollTo({
    top: rect.top + scrollTop - offset,
    behavior: 'smooth'
  });
}

//---------------------------------------------------------------------------------------------------------------//
//feedback_submit.js



document.addEventListener("DOMContentLoaded", function () {
  const realnameRadio = document.getElementById("realnameRadio");
  const anonymousRadio = document.getElementById("anonymousRadio");
  const userIdSection = document.getElementById("userIdSection");

  function toggleUserIdSection() {
    if (anonymousRadio.checked) {
      userIdSection.style.display = "none";
    } else {
      userIdSection.style.display = "flex"; // 또는 원래 스타일대로 "block"
    }
  }

  // 처음 로드 시 체크된 상태 반영
  toggleUserIdSection();

  // 라디오 변경 시 감지해서 토글
  realnameRadio.addEventListener("change", toggleUserIdSection);
  anonymousRadio.addEventListener("change", toggleUserIdSection);
});

document.addEventListener('DOMContentLoaded', () => {
	const form = document.querySelector('form[th\\:action="@{/support/feedback/submit}"]') || document.querySelector('form');

	form.addEventListener('submit', function(event) {

		const email = form.email.value.trim();
		const title = form.feedback_title.value.trim();
		const content = form.feedback_content.value.trim();

		if (!email) {
			alert('이메일을 입력해주세요.');
			form.email.focus();
			event.preventDefault();
			return false;
		}
		if (!title) {
			alert('제목을 입력해주세요.');
			form.feedback_title.focus();
			event.preventDefault();
			return false;
		}
		if (!content) {
			alert('내용을 입력해주세요.');
			form.feedback_content.focus();
			event.preventDefault();
			return false;
		}
	});
});
//---------------------------------------------------------------------------------------------------------------//
//index_autoSlider



// static/js/autoSlider.js

function moveToFaq(typeId) {
	localStorage.setItem("pendingFaqType", typeId); // ✅ 저장
	// 					    alert("typeId: "+typeId);
	location.href = "/support/faq/faqs";            // ✅ URL 고정
}


function autoSlider() {
	const track = document.querySelector('.slide-track');
	if (!track) return;

	let items = Array.from(track.children);
	const itemWidth = 180;

	function updateFocus() {
		items.forEach((card, i) => {
			card.classList.toggle('active', i === 2); // 가운데 강조
		});
	}

	function slideOnce() {
		track.style.transform = `translateX(-${itemWidth}px)`;

		setTimeout(() => {
			track.appendChild(items[0]);
			track.style.transition = 'none';
			track.style.transform = 'translateX(0)';

			setTimeout(() => {
				track.style.transition = 'transform 0.3s ease-in-out';
			}, 50);

			items = Array.from(track.children);
			updateFocus();
		}, 300);
	}

	updateFocus();
	setInterval(slideOnce, 2000);
}//autoSlider





//---------------------------------------------------------------------------------------------------------------//
//index_ready.js

$(function() {
	searchSuggest();
	autoSlider();
});//ready

//---------------------------------------------------------------------------------------------------------------//
//index_searchSuggest.js

let selectedIndex = -1;

$('#suggestBox').removeClass('suggestBoxCss').empty();

function searchSuggest() {
  $('#keywordInput').on('input', function () {
    const query = $(this).val().trim();
	
	
    if (query.length === 0) {
//      $('#suggestBox').empty();
		$('#suggestBox').removeClass('suggestBoxCss').empty();
      $('#overlay').hide();
      return;
    }
	$('#keywordInput').on('focus click', function () {
	  const query = $(this).val().trim();

	  // 🔁 입력값이 있고 추천이 꺼져 있을 때만 다시 실행
	  if (query.length > 0 && !$('#overlay').is(':visible')) {
	    $(this).trigger('input');
	  }
	});


    $('#overlay').show();

    $.ajax({
      url: '/support/search-all',
      method: 'GET',
      data: { keyword: query },
      success: function (data) {
        const box = $('#suggestBox');
        box.empty();
		box.addClass('suggestBoxCss');

        const notices = data.notices || [];
        const faqs = data.faqs || [];

        if (notices.length === 0 && faqs.length === 0) {
          box.append('<div style="padding-top:30px; padding-bottom:30px; color: #898989; text-align : center; font-size:36px;">관련 키워드가 없습니다.</div>');
          box.append(`
            <div class="suggestBottom">
              <div style="display: flex;" onclick="location.href='/support/feedback/send'" class="suggestBottomIn">
                <div style="flex: 1;"><img src="/images/symbol_feedback.png" style="width:40px; height:40px;" /></div>
                <div style="flex: 4; text-align: center;">피드백 작성하기</div>
                <div style="flex: 1;"></div>
              </div>
            </div>
          `);
          return;
        }

        // 🔔 공지사항 표시
        notices.forEach(notice => {
          const item = createSuggestItem({
            title: notice.notice_title,
            label: '공지사항',
            icon: '/images/symbol_notice.png',
            link: `/support/notice/${notice.notice_id}`,
            hits: notice.notice_hits
          });
          box.append(item);
        });

        // ❓ FAQ 표시
        faqs.forEach(faq => {
          const item = createSuggestItem({
            title: faq.faq_title,
            label: 'FAQ',
            icon: '/images/symbol_faq.png',
//          link: `/support/faq/${faq.faq_id}`,
			link: `/support/faq?openId=${faq.faq_id}`,

            hits: faq.faq_hits
          });
          box.append(item);
        });

        box.append(`
          <div class="suggestBottom">
            <div style="display: flex;" onclick="location.href='/support/feedback/send'" class="suggestBottomIn">
              <div style="flex: 1;"><img src="/images/symbol_feedback.png" style="width:40px; height:40px;" /></div>
              <div style="flex: 4; text-align: center; cursor: pointer">피드백 작성하기</div>
              <div style="flex: 1;"></div>
            </div>
          </div>
        `);

        highlightKeywordInCells(query);
      },
      error: function () {
        $('#suggestBox').html('<div style="padding:10px;">요청 실패</div>');
      }
    });
  });
}

// 🔧 공지/FAQ 공통 아이템 생성 함수
function createSuggestItem({ title, label, icon, link, hits }) {
  const item = $('<div class="suggestItem"></div>')
    .css({
      padding: '10px',
      cursor: 'pointer',
      borderBottom: '1px solid #eee',
      display: 'flex',
      alignItems: 'center',
      gap: '10px',
      transition: 'background-color 0.3s, color 0.3s'
    })
    .on('click', function () {
      $('#keywordInput').val(title);
      location.href = link;
      $('#suggestBox').empty();
    })
    .hover(
      function () {
        $(this).css({
          backgroundColor: 'rgba(87,1,208, 0.2)',
          color: 'rgb(87,1,208)'
        });
      },
      function () {
        $(this).css({
          backgroundColor: '',
          color: ''
        });
      }
    );

  // 아이콘
  const imgDiv = $('<div></div>').css({
    flex: '0 0 40px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
	paddingLeft: '18px'
  });
  const img = $('<img>')
    .attr('src', icon)
    .css({
      width: '36px',
      height: '36px',
      objectFit: 'contain'
    });
  imgDiv.append(img);

  // 라벨 (공지사항 / FAQ)
  const labelDiv = $('<div></div>')
    .text(label)
    .css({
      flex: '0 0 64px',
      color: '#5701d0',
      fontWeight: 'bold',
//    fontSize: '16px',
	  fontSize: label === 'FAQ' ? '22px' : '16px',
      textAlign: 'center',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      transition: 'color 0.3s'
    });

  // 제목
  const titleDiv = $('<div></div>')
    .addClass('suggest-title')
    .text(title)
    .css({
      flex: '1',
      overflow: 'hidden',
      whiteSpace: 'nowrap',
      textOverflow: 'ellipsis',
	  paddingLeft: '40px'
    });

  // 조회수 라벨
  const hitsLabelDiv = $('<div></div>')
    .text('조회수')
    .css({
      flex: '0 0 60px',
      fontSize: '15px',
      color: '#aaa',
      textAlign: 'right',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'flex-end'
    });

  // 조회수 값
  const hitsValueDiv = $('<div></div>')
    .text(hits)
    .css({
      flex: '0 0 40px',
      fontSize: '20px',
      fontWeight: 'bold',
//      color: hits > 100 ? '#5701d0' : '#999',
      color: "#9f5cfe",
      textAlign: 'left',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'flex-start'
    });

  return item
    .append(imgDiv)
    .append(labelDiv)
    .append(titleDiv)
    .append(hitsLabelDiv)
    .append(hitsValueDiv);
}

$(document).ready(function () {
  $('#overlay').on('click', function () {
//    $('#suggestBox').empty();
	$('#suggestBox').removeClass('suggestBoxCss').empty();
    $('#overlay').hide();
//    $('#keywordInput').val('');//찾던 키워드가 초기화된다!
  });
});

// 셀 내 키워드 하이라이트 (자동완성 결과용)
function highlightKeywordInCells(keyword) {
  if (!keyword.trim()) return;

  const escapedKeyword = keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  const regex = new RegExp(`(${escapedKeyword})`, 'gi');

  const titleCells = document.querySelectorAll('#suggestBox .suggest-title');
  titleCells.forEach(cell => {
    const originalText = cell.textContent;
    const highlightedText = originalText.replace(regex, '<span style="background-color: #9f5cfe; color: white; font-weight: bold;">$1</span>');
    cell.innerHTML = highlightedText;
  });
}

$(document).on('keydown', function (e) {
//  if (!$('#keywordInput').is(':focus')) return;
	if (!$('#keywordInput').is(':focus') || !$('#overlay').is(':visible')) return;

  const items = $('.suggestItem');
  if (!items.length) return;

  if (e.key === 'ArrowDown') {
    selectedIndex = (selectedIndex + 1) % items.length;
  } else if (e.key === 'ArrowUp') {
    selectedIndex = (selectedIndex - 1 + items.length) % items.length;
  } else if (e.key === 'Enter' && selectedIndex >= 0) {
    items.eq(selectedIndex).click();
    return;
  }

  items.removeClass('hovered');
  items.eq(selectedIndex).addClass('hovered');
  items.eq(selectedIndex)[0].scrollIntoView({ behavior: 'smooth', block: 'nearest' });
});




//---------------------------------------------------------------------------------------------------------------//
//notices_search.js



  // URL에서 keyword 파라미터 추출
  function getKeywordFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get('keyword') || '';
  }

  // 정규식 특수문자 이스케이프 처리
  function escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  }
  
//    document.addEventListener('DOMContentLoaded', () => {
//      const keyword = getKeywordFromURL();
//    });
	
	document.addEventListener('DOMContentLoaded', () => {
	  const keyword = getKeywordFromURL();
	  highlightKeywordInCells(keyword);
	});


//  // 키워드 하이라이트 함수 (notice_title 컬럼만)
//  function highlightKeywordInCells(keyword) {
//    if (!keyword.trim()) return;
//
//    const safeKeyword = escapeRegExp(keyword);
//    const regex = new RegExp(safeKeyword, 'gi');
//
//    const cells = document.querySelectorAll('.notice-row .notice-cell');
//
//    cells.forEach((cell, index) => {
//      // notice_title 컬럼만 (기존 index % 9 === 1)
//      if (index % 9 === 1) {
//        const originalHTML = cell.innerHTML;
//
//        if (originalHTML.includes('highlighted')) return;
//
//        const highlightedHTML = originalHTML.replace(regex, match => {
//          return `<span class="highlighted">${match}</span>`;
//        });
//
//        cell.innerHTML = highlightedHTML;
//      }
//    });
//  }

// 셀 내 키워드 하이라이트
function highlightKeywordInCells(keyword) {
  if (!keyword.trim()) return;

  const escapedKeyword = escapeRegExp(keyword);
  const regex = new RegExp(`(${escapedKeyword})`, 'gi');

  // 제목 셀 선택
  const titleCells = document.querySelectorAll('.notice-row .notice-cell:nth-child(2)');
  titleCells.forEach(cell => {
    const originalText = cell.textContent;
    const highlightedText = originalText.replace(regex, '<span style="background-color: #9a43fe; color: white; font-weight: bold">$1</span>');
    cell.innerHTML = highlightedText;
  });
}








//---------------------------------------------------------------------------------------------------------------//
//support_ready.js


const supportLink = document.querySelector('.supportActive');

supportLink.style.fontWeight = 'bold';
supportLink.style.fontSize = '18px';
supportLink.style.color = '#5701d0';

//---------------------------------------------------------------------------------------------------------------//








