<script setup>
  import { ref, onMounted } from 'vue'
  import { queryDeptAll, addDept, updateDept, queryDeptById,deleteDept } from '@/api/dept'
  import { ElMessage ,ElMessageBox  } from 'element-plus'
  const deptList = ref([])
  const dialogFormVisible = ref(false)
  const formTitle = ref()
  const dept = ref({
    name: ''
  })
  const formLabelWidth = '120px'
  // 部门列表查询
  const search =async()=>{
    const result=await queryDeptAll();
    if(result.code){
      deptList.value=result.data;
      
    }
  }
  // 部门添加
  const add = ()=>{
    dialogFormVisible.value=true;
    formTitle.value='添加部门';
    dept.value ={name: ''};
    if(deptFormRef.value){
      deptFormRef.value.resetFields();
    }
  }
  //确认保存
  const save = async()=>{
    //表单的校验
    if(!deptFormRef.value) return;
    deptFormRef.value.validate( async (valid) =>{
      if(valid){
        let result;
        if(dept.value.id){
          //编辑
           result=await updateDept(dept.value);
        }
        else{
          //添加
          result=await addDept(dept.value);
        }
        if(result.code){
          ElMessage.success(dept.value.id?'修改成功':'添加成功');
          dialogFormVisible.value=false;
          search();
        }
        else{
          dialogFormVisible.value=false;
          ElMessage.error(result.msg);
        }
      }
      else{
        ElMessage.error('请填写完整信息');
      }
    })  
  }
  //编辑
  const edit = async(id)=>{
    const result=await queryDeptById(id);
    formTitle.value='编辑部门';
    if(deptFormRef.value){
      deptFormRef.value.resetFields();
    }
    if(result.code){
      dept.value=result.data;
      dialogFormVisible.value=true;
    }
  }
  //删除
  const deleteById =async (id)=>{
     ElMessageBox.confirm(
    '',
    '您确定删除该部门吗？',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
      center: true,
    }
  )
    .then(async () => {
      const result =await deleteDept(id);
      if(result.code){
        ElMessage.success('删除成功');
        search();
      }
      else{
        ElMessage.error(result.msg);
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '删除已取消',
      })
    })
}
  
  onMounted(()=>{
    search();
  })
  //部门名称规则
  const rules = ref({
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 7, message: '部门名称长度必须在2-7个字符之间', trigger: 'blur' },
  ]
})
const deptFormRef = ref();
</script>

<template>
  <!-- 部门列表 -->
  <div>
    <h1>部门管理</h1>
     <div class="button"><el-button type="primary" @click="add">+添加部门</el-button></div>
    <div>
      <el-table :data="deptList"  style="width: 100%" border align="center">
        <el-table-column type="index" label="序号" width="100" align="center" />
        <el-table-column prop="name" label="部门名称" width="180" align="center" />
        <el-table-column prop="updateTime" label="最后操作时间" width="360" align="center" />
        <el-table-column  label="操作" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="edit(scope.row.id)"><el-icon><EditPen /></el-icon> 编辑</el-button>
            <el-button type="danger" size="small" @click="deleteById(scope.row.id)"><el-icon><Delete /></el-icon> 删除</el-button>  
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <!-- 对话框 -->
   
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500">
    <el-form :model="dept" :rules="rules" ref="deptFormRef">
      <el-form-item prop="name" label="部门名称" :label-width="formLabelWidth">
        <el-input v-model="dept.name" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
 .button{
  margin-bottom: 10px;
 }

</style>