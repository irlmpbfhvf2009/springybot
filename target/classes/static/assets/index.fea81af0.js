var e=Object.defineProperty,a=Object.getOwnPropertySymbols,l=Object.prototype.hasOwnProperty,t=Object.prototype.propertyIsEnumerable,o=(a,l,t)=>l in a?e(a,l,{enumerable:!0,configurable:!0,writable:!0,value:t}):a[l]=t;import{b as r,a as n,u as i,g as s,d as u}from"./table.23cd6796.js";import{d,r as c,q as m,_ as p,a as f,i as b,o as v,e as h,j as g,b as y,k as D,F as w,f as _,E as x,t as C,l as T,v as V,x as k,L as j,c as S,w as F,g as O,h as q,y as z,m as A,z as N}from"./index.c4c68ae8.js";import{T as U}from"./index.d30c5476.js";function I(e,a,l,t){var o,r=!1,n=0;function i(){o&&clearTimeout(o)}function s(){for(var s=arguments.length,u=new Array(s),d=0;d<s;d++)u[d]=arguments[d];var c=this,m=Date.now()-n;function p(){n=Date.now(),l.apply(c,u)}function f(){o=void 0}r||(t&&!o&&p(),i(),void 0===t&&m>e?p():!0!==a&&(o=setTimeout(t?f:p,void 0===t?e-m:e)))}return"boolean"!=typeof a&&(t=l,l=a,a=void 0),s.cancel=function(){i(),r=!0},s}const E=d({setup(){const e=c(null),a={index:1,size:30,total:0};let l=c(""),t=c([]),o=c(!0),n=c(!0),i=m("active"),s=c(!1);const u=u=>{if(n.value=!0,u||o.value)o.value=!1,a.index=1,e.value&&(e.value.scrollTop=0);else if(a.index*a.size>=a.total)return n.value=!1,void(s.value=!0);let d={page:a.index,pageSize:a.size,keyword:l.value};r(d).then((e=>{1===a.index?(t.value=e.data.list,i.value=t.value[0]):t.value=t.value.concat(e.data.list),a.index+=1,a.total=e.data.pager.total})).catch((e=>{a.index=1,a.total=0,t.value=[]})).finally((()=>{n.value=!1}))},d=(p=300,f=u,void 0===b?I(p,f,!1):I(p,b,!1!==f));var p,f,b;return u(!0),{listDom:e,loading:n,nomore:s,input:l,list:t,active:i,getCategoryData:u,searchData:d,changeActive:e=>{i.value=e}}}}),L={class:"category"},G={class:"header-box"},$=(e=>(V("data-v-7b71b85e"),e=e(),k(),e))((()=>g("h2",null,"分类列表",-1))),P={class:"list system-scrollbar",ref:"listDom","infinite-scroll-immediate":!1,style:{overflow:"auto"}},B=["onClick"],J={key:0,class:"load-tip"},H={key:1,class:"load-tip"};var K=p(E,[["render",function(e,a,l,t,o,r){const n=f("el-input"),i=b("infinite-scroll");return v(),h("div",L,[g("div",G,[$,y(n,{modelValue:e.input,"onUpdate:modelValue":a[0]||(a[0]=a=>e.input=a),placeholder:"请输入内容",onInput:a[1]||(a[1]=a=>e.searchData(!0))},null,8,["modelValue"])]),D((v(),h("ul",P,[(v(!0),h(w,null,_(e.list,(a=>(v(),h("li",{key:a.id,class:x({active:a.id===e.active.id}),onClick:l=>e.changeActive(a)},[g("span",null,C(a.name),1)],10,B)))),128)),e.loading?(v(),h("p",J,"加载中...")):T("",!0),e.nomore?(v(),h("p",H,"数据加载完成")):T("",!0)])),[[i,e.getCategoryData]])])}],["__scopeId","data-v-7b71b85e"]]);const M=[{value:1,label:"运动"},{value:2,label:"健身"},{value:3,label:"跑酷"},{value:4,label:"街舞"}],Q=[{value:1,label:"今天"},{value:2,label:"明天"},{value:3,label:"后天"}],R=d({components:{Layer:j},props:{layer:{type:Object,default:()=>({show:!1,title:"",showButton:!0})}},setup(e,a){const l=c(null);let t=c({name:""});return e.layer.row&&(t.value=JSON.parse(JSON.stringify(e.layer.row))),{form:t,rules:{name:[{required:!0,message:"请输入姓名",trigger:"blur"}],number:[{required:!0,message:"请输入数字",trigger:"blur"}],choose:[{required:!0,message:"请选择",trigger:"blur"}],radio:[{required:!0,message:"请选择",trigger:"blur"}]},layerDom:layerDom,ruleForm:l,selectData:M,radioData:Q}},methods:{submit(){this.ruleForm&&this.ruleForm.validate((e=>{if(!e)return!1;{let e=this.form;this.layer.row?this.updateForm(e):this.addForm(e)}}))},addForm(e){n(e).then((e=>{this.$message({type:"success",message:"新增成功"}),this.$emit("getTableData",!0),this.layerDom&&this.layerDom.close()}))},updateForm(e){i(e).then((e=>{this.$message({type:"success",message:"编辑成功"}),this.$emit("getTableData",!1),this.layerDom&&this.layerDom.close()}))}}});const W=d({components:{Table:U,Layer:p(R,[["render",function(e,a,l,t,o,r){const n=f("el-input"),i=f("el-form-item"),s=f("el-option"),u=f("el-select"),d=f("el-radio"),c=f("el-radio-group"),m=f("el-form"),p=f("Layer",!0);return v(),S(p,{layer:e.layer,onConfirm:e.submit,ref:"layerDom"},{default:F((()=>[y(m,{model:e.form,rules:e.rules,ref:"ruleForm","label-width":"120px",style:{"margin-right":"30px"}},{default:F((()=>[y(i,{label:"名称：",prop:"name"},{default:F((()=>[y(n,{modelValue:e.form.name,"onUpdate:modelValue":a[0]||(a[0]=a=>e.form.name=a),placeholder:"请输入名称"},null,8,["modelValue"])])),_:1}),y(i,{label:"数字：",prop:"number"},{default:F((()=>[y(n,{modelValue:e.form.number,"onUpdate:modelValue":a[1]||(a[1]=a=>e.form.number=a),oninput:"value=value.replace(/[^\\d]/g,'')",placeholder:"只能输入正整数"},null,8,["modelValue"])])),_:1}),y(i,{label:"选择器：",prop:"select"},{default:F((()=>[y(u,{modelValue:e.form.choose,"onUpdate:modelValue":a[2]||(a[2]=a=>e.form.choose=a),placeholder:"请选择",clearable:""},{default:F((()=>[(v(!0),h(w,null,_(e.selectData,(e=>(v(),S(s,{key:e.value,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),y(i,{label:"单选框：",prop:"radio"},{default:F((()=>[y(c,{modelValue:e.form.radio,"onUpdate:modelValue":a[3]||(a[3]=a=>e.form.radio=a)},{default:F((()=>[(v(!0),h(w,null,_(e.radioData,(e=>(v(),S(d,{key:e.value,label:e.value},{default:F((()=>[O(C(e.label),1)])),_:2},1032,["label"])))),128))])),_:1},8,["modelValue"])])),_:1})])),_:1},8,["model","rules"])])),_:1},8,["layer","onConfirm"])}]])},setup(){const e=q({input:""}),r=q({show:!1,title:"新增",showButton:!0}),n=q({index:1,size:20,total:0}),i=m("active"),d=c(!0),p=c([]),f=c([]),b=r=>{d.value=!0,r&&(n.index=1);let u=((e,r)=>{for(var n in r||(r={}))l.call(r,n)&&o(e,n,r[n]);if(a)for(var n of a(r))t.call(r,n)&&o(e,n,r[n]);return e})({category:i.value.id,page:n.index,pageSize:n.size},e);s(u).then((e=>{let a=e.data.list;Array.isArray(a)&&a.forEach((e=>{const a=M.find((a=>a.value===e.choose));e.chooseName=a?a.label:e.choose;const l=Q.find((a=>a.value===e.radio));l?e.radioName=l.label:e.radio})),p.value=e.data.list,n.total=Number(e.data.pager.total)})).catch((e=>{p.value=[],n.index=1,n.total=0})).finally((()=>{d.value=!1}))};return z(i,(e=>{b(!0)})),{query:e,tableData:p,chooseData:f,loading:d,page:n,layer:r,handleSelectionChange:e=>{f.value=e},handleAdd:()=>{r.title="新增数据",r.show=!0,delete r.row},handleEdit:e=>{r.title="编辑数据",r.row=e,r.show=!0},handleDel:e=>{let a={ids:e.map((e=>e.id)).join(",")};u(a).then((e=>{A({type:"success",message:"删除成功"}),b(1===p.value.length)}))},getTableData:b}}}),X={class:"layout-container"},Y={class:"layout-container-form flex space-between"},Z={class:"layout-container-form-handle"},ee={class:"layout-container-form-search"},ae={class:"layout-container-table"};const le=d({name:"categoryTable",components:{Category:K,myTable:p(W,[["render",function(e,a,l,t,o,r){const n=f("el-button"),i=f("el-popconfirm"),s=f("el-input"),u=f("el-table-column"),d=f("Table"),c=f("Layer"),m=b("loading");return v(),h("div",X,[g("div",Y,[g("div",Z,[y(n,{type:"primary",icon:"el-icon-circle-plus-outline",onClick:e.handleAdd},{default:F((()=>[O("新增")])),_:1},8,["onClick"]),y(i,{title:"批量删除",onConfirm:a[0]||(a[0]=a=>e.handleDel(e.chooseData))},{reference:F((()=>[y(n,{type:"danger",icon:"el-icon-delete",disabled:0===e.chooseData.length},{default:F((()=>[O("批量删除")])),_:1},8,["disabled"])])),_:1})]),g("div",ee,[y(s,{modelValue:e.query.input,"onUpdate:modelValue":a[1]||(a[1]=a=>e.query.input=a),placeholder:"请输入关键词进行检索",onChange:a[2]||(a[2]=a=>e.getTableData(!0))},null,8,["modelValue"]),y(n,{type:"primary",icon:"el-icon-search",class:"search-btn",onClick:a[3]||(a[3]=a=>e.getTableData(!0))},{default:F((()=>[O("搜索")])),_:1})])]),g("div",ae,[D((v(),S(d,{ref:"table",page:e.page,"onUpdate:page":a[4]||(a[4]=a=>e.page=a),showIndex:!0,showSelection:!0,data:e.tableData,onGetTableData:e.getTableData,onSelectionChange:e.handleSelectionChange},{default:F((()=>[y(u,{prop:"name",label:"名称",align:"center"}),y(u,{prop:"number",label:"数字",align:"center"}),y(u,{prop:"chooseName",label:"选择器",align:"center"}),y(u,{prop:"radioName",label:"单选框",align:"center"}),y(u,{label:"操作",align:"center",fixed:"right",width:"200"},{default:F((a=>[y(n,{onClick:l=>e.handleEdit(a.row)},{default:F((()=>[O("编辑")])),_:2},1032,["onClick"]),y(i,{title:"删除",onConfirm:l=>e.handleDel([a.row])},{reference:F((()=>[y(n,{type:"danger"},{default:F((()=>[O("删除")])),_:1})])),_:2},1032,["onConfirm"])])),_:1})])),_:1},8,["page","data","onGetTableData","onSelectionChange"])),[[m,e.loading]]),e.layer.show?(v(),S(c,{key:0,layer:e.layer,onGetTableData:e.getTableData},null,8,["layer","onGetTableData"])):T("",!0)])])}],["__scopeId","data-v-4df46550"]])},setup(){let e=c({});N("active",e)}}),te={class:"full"},oe={class:"left"},re={class:"content"};var ne=p(le,[["render",function(e,a,l,t,o,r){const n=f("category"),i=f("my-table");return v(),h("div",te,[g("div",oe,[y(n)]),g("div",re,[y(i)])])}],["__scopeId","data-v-c23cf988"]]);export{ne as default};