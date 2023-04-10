var e=Object.defineProperty,a=Object.getOwnPropertySymbols,l=Object.prototype.hasOwnProperty,t=Object.prototype.propertyIsEnumerable,o=(a,l,t)=>l in a?e(a,l,{enumerable:!0,configurable:!0,writable:!0,value:t}):a[l]=t;import{d as r,r as n,q as s,_ as d,u as i,a as u,o as c,e as m,j as p,b,v as h,x as f,L as g,c as y,w as v,F as D,f as _,g as w,t as C,h as x,y as T,m as k,i as V,k as j,l as N,z as F}from"./index.c4c68ae8.js";import{g as O}from"./job.268bb027.js";import{T as S}from"./index.d30c5476.js";import{a as q,u as U,g as I,d as L}from"./table.23cd6796.js";const P=r({setup(){let e=n([]);const a=n(null),l=s("active");return O({}).then((t=>{console.log(t.data[0]),e.value=t.data,l.value=t.data[0],i((()=>{a.value&&a.value.setCurrentKey(l.value.id)}))})),{data:e,tree:a,defaultProps:{children:"children",label:"label"},handleNodeClick:e=>{console.log(e),l.value=e}}}}),z={class:"category"},A=(e=>(h("data-v-1d2c2718"),e=e(),f(),e))((()=>p("div",{class:"header-box"},[p("h2",null,"组织管理")],-1))),E={class:"list system-scrollbar"};var G=d(P,[["render",function(e,a,l,t,o,r){const n=u("el-tree");return c(),m("div",z,[A,p("div",E,[b(n,{ref:"tree",class:"my-tree",data:e.data,props:e.defaultProps,"expand-on-click-node":!1,"node-key":"id","highlight-current":"","default-expand-all":"",onNodeClick:e.handleNodeClick},null,8,["data","props","onNodeClick"])])])}],["__scopeId","data-v-1d2c2718"]]);const $=[{value:1,label:"运动"},{value:2,label:"健身"},{value:3,label:"跑酷"},{value:4,label:"街舞"}],B=[{value:1,label:"今天"},{value:2,label:"明天"},{value:3,label:"后天"}],J=r({components:{Layer:g},props:{layer:{type:Object,default:()=>({show:!1,title:"",showButton:!0})}},setup(e,a){const l=n(null),t=n(null);let o=n({name:""});return e.layer.row&&(o.value=JSON.parse(JSON.stringify(e.layer.row))),{form:o,rules:{name:[{required:!0,message:"请输入姓名",trigger:"blur"}],number:[{required:!0,message:"请输入数字",trigger:"blur"}],choose:[{required:!0,message:"请选择",trigger:"blur"}],radio:[{required:!0,message:"请选择",trigger:"blur"}]},layerDom:t,ruleForm:l,selectData:$,radioData:B}},methods:{submit(){this.ruleForm&&this.ruleForm.validate((e=>{if(!e)return!1;{let e=this.form;this.layer.row?this.updateForm(e):this.addForm(e)}}))},addForm(e){q(e).then((e=>{this.$message({type:"success",message:"新增成功"}),this.$emit("getTableData",!0),this.layerDom&&this.layerDom.close()}))},updateForm(e){U(e).then((e=>{this.$message({type:"success",message:"编辑成功"}),this.$emit("getTableData",!1),this.layerDom&&this.layerDom.close()}))}}});const K=r({components:{Table:S,Layer:d(J,[["render",function(e,a,l,t,o,r){const n=u("el-input"),s=u("el-form-item"),d=u("el-option"),i=u("el-select"),p=u("el-radio"),h=u("el-radio-group"),f=u("el-form"),g=u("Layer",!0);return c(),y(g,{layer:e.layer,onConfirm:e.submit,ref:"layerDom"},{default:v((()=>[b(f,{model:e.form,rules:e.rules,ref:"ruleForm","label-width":"120px",style:{"margin-right":"30px"}},{default:v((()=>[b(s,{label:"名称：",prop:"name"},{default:v((()=>[b(n,{modelValue:e.form.name,"onUpdate:modelValue":a[0]||(a[0]=a=>e.form.name=a),placeholder:"请输入名称"},null,8,["modelValue"])])),_:1}),b(s,{label:"数字：",prop:"number"},{default:v((()=>[b(n,{modelValue:e.form.number,"onUpdate:modelValue":a[1]||(a[1]=a=>e.form.number=a),oninput:"value=value.replace(/[^\\d]/g,'')",placeholder:"只能输入正整数"},null,8,["modelValue"])])),_:1}),b(s,{label:"选择器：",prop:"select"},{default:v((()=>[b(i,{modelValue:e.form.choose,"onUpdate:modelValue":a[2]||(a[2]=a=>e.form.choose=a),placeholder:"请选择",clearable:""},{default:v((()=>[(c(!0),m(D,null,_(e.selectData,(e=>(c(),y(d,{key:e.value,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),b(s,{label:"单选框：",prop:"radio"},{default:v((()=>[b(h,{modelValue:e.form.radio,"onUpdate:modelValue":a[3]||(a[3]=a=>e.form.radio=a)},{default:v((()=>[(c(!0),m(D,null,_(e.radioData,(e=>(c(),y(p,{key:e.value,label:e.value},{default:v((()=>[w(C(e.label),1)])),_:2},1032,["label"])))),128))])),_:1},8,["modelValue"])])),_:1})])),_:1},8,["model","rules"])])),_:1},8,["layer","onConfirm"])}]])},setup(){const e=x({input:""}),r=x({show:!1,title:"新增",showButton:!0}),d=x({index:1,size:20,total:0}),i=s("active"),u=n(!0),c=n([]),m=n([]),p=r=>{u.value=!0,r&&(d.index=1);let n=((e,r)=>{for(var n in r||(r={}))l.call(r,n)&&o(e,n,r[n]);if(a)for(var n of a(r))t.call(r,n)&&o(e,n,r[n]);return e})({category:i.value.id,page:d.index,pageSize:d.size},e);I(n).then((e=>{let a=e.data.list;Array.isArray(a)&&a.forEach((e=>{const a=$.find((a=>a.value===e.choose));e.chooseName=a?a.label:e.choose;const l=B.find((a=>a.value===e.radio));l?e.radioName=l.label:e.radio})),c.value=e.data.list,d.total=Number(e.data.pager.total)})).catch((e=>{c.value=[],d.index=1,d.total=0})).finally((()=>{u.value=!1}))};return T(i,(e=>{p(!0)})),{query:e,tableData:c,chooseData:m,loading:u,page:d,layer:r,handleSelectionChange:e=>{m.value=e},handleAdd:()=>{r.title="新增数据",r.show=!0,delete r.row},handleEdit:e=>{r.title="编辑数据",r.row=e,r.show=!0},handleDel:e=>{let a={ids:e.map((e=>e.id)).join(",")};L(a).then((e=>{k({type:"success",message:"删除成功"}),p(1===c.value.length)}))},getTableData:p}}}),H={class:"layout-container"},M={class:"layout-container-form flex space-between"},Q={class:"layout-container-form-handle"},R={class:"layout-container-form-search"},W={class:"layout-container-table"};const X=r({name:"treeTable",components:{Category:G,myTable:d(K,[["render",function(e,a,l,t,o,r){const n=u("el-button"),s=u("el-popconfirm"),d=u("el-input"),i=u("el-table-column"),h=u("Table"),f=u("Layer"),g=V("loading");return c(),m("div",H,[p("div",M,[p("div",Q,[b(n,{type:"primary",icon:"el-icon-circle-plus-outline",onClick:e.handleAdd},{default:v((()=>[w("新增")])),_:1},8,["onClick"]),b(s,{title:"批量删除",onConfirm:a[0]||(a[0]=a=>e.handleDel(e.chooseData))},{reference:v((()=>[b(n,{type:"danger",icon:"el-icon-delete",disabled:0===e.chooseData.length},{default:v((()=>[w("批量删除")])),_:1},8,["disabled"])])),_:1})]),p("div",R,[b(d,{modelValue:e.query.input,"onUpdate:modelValue":a[1]||(a[1]=a=>e.query.input=a),placeholder:"请输入关键词进行检索",onChange:a[2]||(a[2]=a=>e.getTableData(!0))},null,8,["modelValue"]),b(n,{type:"primary",icon:"el-icon-search",class:"search-btn",onClick:a[3]||(a[3]=a=>e.getTableData(!0))},{default:v((()=>[w("搜索")])),_:1})])]),p("div",W,[j((c(),y(h,{ref:"table",page:e.page,"onUpdate:page":a[4]||(a[4]=a=>e.page=a),showIndex:!0,showSelection:!0,data:e.tableData,onGetTableData:e.getTableData,onSelectionChange:e.handleSelectionChange},{default:v((()=>[b(i,{prop:"name",label:"名称",align:"center"}),b(i,{prop:"number",label:"数字",align:"center"}),b(i,{prop:"chooseName",label:"选择器",align:"center"}),b(i,{prop:"radioName",label:"单选框",align:"center"}),b(i,{label:"操作",align:"center",fixed:"right",width:"200"},{default:v((a=>[b(n,{onClick:l=>e.handleEdit(a.row)},{default:v((()=>[w("编辑")])),_:2},1032,["onClick"]),b(s,{title:"删除",onConfirm:l=>e.handleDel([a.row])},{reference:v((()=>[b(n,{type:"danger"},{default:v((()=>[w("删除")])),_:1})])),_:2},1032,["onConfirm"])])),_:1})])),_:1},8,["page","data","onGetTableData","onSelectionChange"])),[[g,e.loading]]),e.layer.show?(c(),y(f,{key:0,layer:e.layer,onGetTableData:e.getTableData},null,8,["layer","onGetTableData"])):N("",!0)])])}],["__scopeId","data-v-b22ba51a"]])},setup(){let e=n({});F("active",e)}}),Y={class:"full"},Z={class:"left"},ee={class:"content"};var ae=d(X,[["render",function(e,a,l,t,o,r){const n=u("category"),s=u("my-table");return c(),m("div",Y,[p("div",Z,[b(n)]),p("div",ee,[b(s)])])}],["__scopeId","data-v-01773b8c"]]);export{ae as default};