<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { getStudentsEducation } from '@/api/report'

const chartRef = ref()
let chart = null

onMounted(async () => {
  const result = await getStudentsEducation()
  if (result.code === 1) {
    const data = result.data.map(x => ({ name: x.education, value: x.num }))
    chart = echarts.init(chartRef.value)
    chart.setOption({
      title: { text: '学员学历分布', left: 'center', textStyle: { fontSize: 16 } },
      tooltip: { trigger: 'item' },
      legend: { bottom: 0 },
      series: [{
        type: 'pie',
        radius: ['35%', '65%'],
        data,
        label: { formatter: '{b}: {c} 人 ({d}%)' }
      }]
    })
  }
  window.addEventListener('resize', () => chart && chart.resize())
})
onBeforeUnmount(() => {
  chart && chart.dispose()
})
</script>

<template>
  <h1>学员信息统计</h1>
  <div class="chart-box">
    <div ref="chartRef" class="chart"></div>
  </div>
</template>

<style scoped>
.chart-box {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 12px;
}
.chart {
  width: 100%;
  height: 460px;
}
</style>
