<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { getEmpJobData, getEmpGenderData } from '@/api/report'

const jobChartRef = ref()
const genderChartRef = ref()
let jobChart = null
let genderChart = null

const renderJob = (jobList, dataList) => {
  jobChart = echarts.init(jobChartRef.value)
  jobChart.setOption({
    title: { text: '员工岗位分布', left: 'center', textStyle: { fontSize: 16 } },
    tooltip: {},
    grid: { left: 40, right: 30, top: 50, bottom: 40 },
    xAxis: { type: 'category', data: jobList, axisLabel: { interval: 0 } },
    yAxis: { type: 'value', minInterval: 1 },
    series: [{
      type: 'bar',
      data: dataList,
      barWidth: 40,
      itemStyle: { color: '#409EFF', borderRadius: [4, 4, 0, 0] },
      label: { show: true, position: 'top' }
    }]
  })
}

const renderGender = (data) => {
  genderChart = echarts.init(genderChartRef.value)
  genderChart.setOption({
    title: { text: '员工性别分布', left: 'center', textStyle: { fontSize: 16 } },
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: '60%',
      data,
      label: { formatter: '{b}: {c} 人 ({d}%)' }
    }]
  })
}

const handleResize = () => {
  jobChart && jobChart.resize()
  genderChart && genderChart.resize()
}

onMounted(async () => {
  window.addEventListener('resize', handleResize)
  const [jobRes, genderRes] = await Promise.all([getEmpJobData(), getEmpGenderData()])
  if (jobRes.code === 1) {
    renderJob(jobRes.data.jobList, jobRes.data.dataList)
  }
  if (genderRes.code === 1) {
    renderGender(genderRes.data.map(x => ({ name: x.gender, value: x.num })))
  }
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  jobChart && jobChart.dispose()
  genderChart && genderChart.dispose()
})
</script>

<template>
  <h1>员工信息统计</h1>
  <el-row :gutter="20">
    <el-col :span="14">
      <div class="chart-box">
        <div ref="jobChartRef" class="chart"></div>
      </div>
    </el-col>
    <el-col :span="10">
      <div class="chart-box">
        <div ref="genderChartRef" class="chart"></div>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped>
.chart-box {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 20px;
}
.chart {
  width: 100%;
  height: 360px;
}
</style>
