// 페이지가 로드될 때 메시지 기본적으로 보이기
document.addEventListener('DOMContentLoaded', () => {
  const noDonationsMessage = document.getElementById('noDonationsMessage');
  noDonationsMessage.style.display = 'block';  // '후원 내역이 없습니다' 메시지 표시

  // 초기 데이터를 로드하고 메시지 처리
  filterDonations();
});

function filterDonations() {
  const donationType = document.querySelector('.donation-filter').value;

  fetch(`/api/donation/donations?type=${donationType}`)
    .then(response => response.json())
    .then(data => {
      const tbody = document.querySelector('.donation-table tbody');
      const noDonationsMessage = document.getElementById('noDonationsMessage');
      tbody.innerHTML = ''; // 기존 데이터 비우기

      // 데이터가 없을 경우 메시지 표시
      if (data.length === 0) {
        noDonationsMessage.style.display = 'block'; // 메시지 보이기
      } else {
        noDonationsMessage.style.display = 'none'; // 메시지 숨기기

        // 데이터가 있을 경우 테이블에 추가
        data.forEach(donation => {
          const tr = document.createElement('tr');

          // 메시지 처리 (null일 경우 기본 값)
          const message = donation.message ? donation.message : '';

          // 날짜 처리 (null일 경우 기본 값으로 '날짜 없음' 처리)
          let date = donation.createdAt ? new Date(donation.createdAt) : null;
          date = date ? date.toISOString().split('T')[0] : '날짜 없음'; // 날짜를 'YYYY-MM-DD' 형식으로 변환

          tr.innerHTML = `
            <td>${donation.courseTitle}</td>
            <td>${donation.donationAmount} M</td>
            <td>${message}</td>
            <td>${date}</td>
          `;
          tbody.appendChild(tr);
        });
      }
    })
    .catch(error => {
      console.error('Error fetching donations:', error);
      const noDonationsMessage = document.getElementById('noDonationsMessage');
      noDonationsMessage.style.display = 'block'; // 오류 발생 시 메시지 표시
    });
}
