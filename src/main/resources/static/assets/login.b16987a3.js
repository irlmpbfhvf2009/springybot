import{_ as s,d as e,I as a,G as n,J as r,h as o,r as t,K as l,a as d,o as p,e as m,j as i,t as u,b as c,w,E as y,g,m as f,M as h,v,x}from"./index.c4c68ae8.js";const T=e({setup(){const s=a(),e=n(),d=r(),p=o({username:"admin",password:"123456"}),m=t("password");return{systemTitle:l,form:p,passwordType:m,passwordTypeChange:()=>{""===m.value?m.value="password":m.value=""},submit:()=>{new Promise(((s,e)=>{""!==p.name?""!==p.password?s(!0):f.warning({message:"密碼不能為空",type:"warning"}):f.warning({message:"用戶名不能為空",type:"warning"})})).then((()=>{let a={username:p.username,password:p.password};s.dispatch("user/login",a).then((()=>{f.success({message:"登入成功",type:"success",showClose:!0,duration:1e3}),h(),e.push(d.query.redirect||"/")}))}))}}}}),C=s=>(v("data-v-703f7dc2"),s=s(),x(),s),_={class:"container"},V={class:"box"},b=C((()=>i("i",{class:"sfont system-xingmingyonghumingnicheng"},null,-1))),j=C((()=>i("i",{class:"sfont system-mima"},null,-1)));var k=s(T,[["render",function(s,e,a,n,r,o){const t=d("el-input"),l=d("el-button"),f=d("el-form");return p(),m("div",_,[i("div",V,[i("h1",null,u(s.systemTitle),1),c(f,{class:"form"},{default:w((()=>[c(t,{size:"large",modelValue:s.form.username,"onUpdate:modelValue":e[0]||(e[0]=e=>s.form.username=e),placeholder:"用户名",type:"text",maxlength:"50"},{prepend:w((()=>[b])),_:1},8,["modelValue"]),c(t,{size:"large",ref:"password",modelValue:s.form.password,"onUpdate:modelValue":e[2]||(e[2]=e=>s.form.password=e),type:s.passwordType,placeholder:"密码",name:"password",maxlength:"50"},{prepend:w((()=>[j])),append:w((()=>[i("i",{class:y(["sfont password-icon",s.passwordType?"system-yanjing-guan":"system-yanjing"]),onClick:e[1]||(e[1]=(...e)=>s.passwordTypeChange&&s.passwordTypeChange(...e))},null,2)])),_:1},8,["modelValue","type"]),c(l,{type:"primary",onClick:s.submit,style:{width:"100%"},size:"medium"},{default:w((()=>[g("登录")])),_:1},8,["onClick"])])),_:1})])])}],["__scopeId","data-v-703f7dc2"]]);export{k as default};
