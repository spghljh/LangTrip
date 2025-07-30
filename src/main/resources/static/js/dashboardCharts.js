/*!
대시보드 통계를 표시하기 위해 직접 넣은 js
 */
// dashboardCharts.js
let studyChartInstance = null;
let quizChartInstance = null;

function drawCharts() {
  const studyCtx = document.getElementById('studyChart');
  if (studyCtx) {
    if (studyChartInstance) studyChartInstance.destroy(); // 🔥 기존 차트 제거
    studyChartInstance = new Chart(studyCtx, {
      type: 'line',
      data: {
        labels: ['07-01', '07-02', '07-03', '07-04'],
        datasets: [{
          label: '학습 시간 (분)',
          data: [30, 60, 45, 75],
          borderColor: '#3f51b5',
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } }
      }
    });
  }

  const quizCtx = document.getElementById('quizChart');
  if (quizCtx) {
    if (quizChartInstance) quizChartInstance.destroy(); // 🔥 기존 차트 제거
    quizChartInstance = new Chart(quizCtx, {
      type: 'doughnut',
      data: {
        labels: ['정답', '오답'],
        datasets: [{
          data: [95.5, 4.5],
          backgroundColor: ['#4caf50', '#f44336']
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'bottom' }
        }
      }
    });
  }
}

window.drawCharts = drawCharts;